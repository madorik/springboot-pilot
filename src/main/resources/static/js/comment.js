const comment = {
    init() {
        const self = this;
        let page = 1;
        $("#btn-comment").on("click", () => {
            self.saveComment();
        });

        setTimeout(function () {
            self.initSummernote();
            self.getCommentAll(page);
        }, 1);


        $(window).scroll(function () {
            if (($(window).scrollTop() + 20) >= ($(document).height() - $(window).height())) {
                page++;
                comment.getCommentAll(page);
            }
        });
    },

    initSummernote() {
        const saveReplyBtn = function () {
            const button = $.summernote.ui.button({
                contents: '등록',
                tooltip: "등록",
                click: function () {
                    comment.saveComment();
                },
            });
            return button.render();
        };

        const saveCommentBtn = function () {
            const button = $.summernote.ui.button({
                contents: '등록',
                tooltip: "등록",
                click: function () {
                    comment.saveReplyByComment();
                },
            });
            return button.render();
        };

        const cancelCommentBtn = function () {
            const button = $.summernote.ui.button({
                contents: '<i class="fa fa-pencil"/>취소',
                tooltip: '취소',
                click: function () {
                    comment.hiveCommentDiv();
                },
            });
            return button.render();
        };

        const options = {
            height: 60,
            lang: 'ko-KR',
            placeholder: '댓글은 최대 200자까지 쓸 수 있습니다.',
            callbacks: {
                onImageUpload: function (files) {
                    for (let file of files) {
                        board.sendFile(file, this, 50);
                    }
                },
                onMediaDelete: function (files) {
                    const imageUrl = $(files[0]).attr('src');
                    board.removeFile(imageUrl);
                }
            },
            toolbar: [
                ['insert', ['picture']],
                ['customButton', ['saveReplyBtn']]
            ]
        }

        $.summernote.options.buttons.saveReplyBtn = saveReplyBtn;
        $.summernote.options.buttons.saveCommentBtn = saveCommentBtn;
        $.summernote.options.buttons.cancelCommentBtn = cancelCommentBtn;
        $.summernote.options.toolbar.push(['customButton', ['saveReplyBtn', 'saveCommentBtn', 'cancelCommentBtn']]);

        $('#txt-comment').summernote(options);

        options.toolbar.splice(options.toolbar.indexOf('customButton', 1));
        options.toolbar.push(['customButton', ['saveCommentBtn', 'cancelCommentBtn']]);
        $('#txt-reComment').summernote(options);
    },

    showCommentDiv(boardId, thread, depth) {
        const commentDiv = document.getElementById("comment" + boardId);
        const dialog = document.getElementById("comment-dialog");
        dialog.style.display = '';
        $("#hidden-thread").val(thread);
        $("#hidden-depth").val(depth);
        $('#txt-reComment').summernote('reset');
        commentDiv.appendChild(dialog);
    },

    hiveCommentDiv() {
        const div = document.getElementById('comment-dialog');
        div.style.display = 'none';
        document.body.appendChild(div);
    },

    getCommentAll(page) {
        const boardId = $("#hidden-board-id").val();
        if (!boardId) return;
        $.ajax({
            url: '/api/v1/boards/' + boardId + '/comments?page=' + page,
            type: "GET",
            dataType: "text",
            contentType: "application/json;",
        }).done((data) => {
            comment.gridCommentList(data, boardId);
        }).fail(err => {
            console.log(JSON.stringify(err))
        });
    },

    gridCommentList(data, boardId) {
        const userEmail = $('#hidden-email').val();
        let htmlStr = '';
        $.each(JSON.parse(data), function (key, value) {
            htmlStr = `<div id="comment${value.id}" style="border-bottom:1px solid darkgray; margin-bottom: 15px; padding-left:${value.depth * 15}px;"><p>`;
            if (value.depth > 0) {
                htmlStr += `<img src="/img/arrow2.png" width="15" alt="arrow"/>`;
            }
            htmlStr += `<b>${value.userEntity.userName}</b>  ${value.createdDate}
            <div style="float: right;">`;
            if (value.deleteYn !== 'Y') {
                htmlStr += `<i class="far fa-comment-dots" style="padding-right: 10px" onclick="comment.showCommentDiv(${value.id}, ${value.thread}, ${value.depth})"></i>`;
                if (userEmail === value.userEntity.email) {
                    htmlStr += `<i class="far fa-edit" style="padding-right: 10px" onclick="comment.editReplyByComment(${value.id});"></i>
                                <i class="far fa-trash-alt" style="padding-right: 10px" onclick="comment.deleteComment(${boardId}, ${value.id})"></i>`;
                }
            }
            htmlStr += `</div></p><p><div id="reply${value.id}" style="padding-bottom: 5px; padding-left: ${value.depth * 15}px;">`;
            if (value.deleteYn === 'Y') {
                htmlStr += `삭제된 댓글입니다.`;
            } else {
                htmlStr += value.contents;
            }
            htmlStr += `</div></p></p>`;
            $("#commentList").append(htmlStr);
        });
    },

    saveComment() {
        const id = $("#input-id").val();
        const contentObj = $('#txt-comment');
        const data = {
            boardId: id,
            userId: $("#hidden-email").val(),
            contents: contentObj.summernote('code')
        }

        const isEmpty = contentObj.summernote('isEmpty');
        if (isEmpty) {
            alert("내용은 필수 입력 항목입니다.");
            return;
        }

        $.ajax({
            type: "POST",
            url: '/api/v1/boards/' + id + '/comments',
            dataType: "text",
            contentType: "application/json;",
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': user.token()}
        }).done(() => {
            alert("댓글이 등록되었습니다.");
            contentObj.summernote('reset');
            location.reload();
        }).fail(err => {
            console.log(JSON.stringify(err))
        })
    },

    saveReplyByComment() {
        const id = $("#hidden-board-id").val();
        const thread = $("#hidden-thread").val();
        const depth = $("#hidden-depth").val();
        const commentId = $("#hidden-comment-id").val();
        const commentApi = '/api/v1/boards/' + id + '/comments/reply';
        const method = thread ? 'POST' : 'PUT'
        const url = thread ? commentApi : commentApi + '/' + commentId;
        const contentObj = $('#txt-reComment');
        const data = {
            userId: $("#hidden-email").val(),
            contents: contentObj.summernote('code'),
            thread: parseInt(thread) - 1,
            depth: parseInt(depth) + 1
        }

        const isEmpty = contentObj.summernote('isEmpty');
        if (isEmpty) {
            alert("내용은 필수 입력 항목입니다.");
            return;
        }

        $.ajax({
            type: method,
            url: url,
            dataType: "text",
            contentType: "application/json;",
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': user.token()}
        }).done(() => {
            alert("댓글이 등록되었습니다.")
            contentObj.summernote('reset');
            location.reload();
        }).fail(err => {
            console.log(JSON.stringify(err))
        })
    },

    editReplyByComment(commentId) {
        const replyDiv = $("#reply" + commentId);
        const commentDiv = document.getElementById("comment" + commentId);
        const dialog = document.getElementById("comment-dialog");
        $('#txt-reComment').summernote('code', replyDiv.html());
        replyDiv.html('');
        dialog.style.display = '';
        $("#hidden-comment-id").val(commentId);
        commentDiv.appendChild(dialog);
    },

    deleteComment(boardId, commentId) {
        if (!confirm("이 게시글을 삭제하시겠습니까?")) {
            return false;
        }

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/boards/' + boardId + '/comments/' + commentId,
            dataType: "text",
            contentType: "application/json;",
            headers: {'X-CSRF-TOKEN': user.token()}
        }).done(() => {
            alert("댓글이 삭제되었습니다.");
            location.reload();
        }).fail(err => {
            console.log(JSON.stringify(err))
        })
    }
}

comment.init()
