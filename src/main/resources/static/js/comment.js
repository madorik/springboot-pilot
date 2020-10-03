const comment = {
    init() {
        const self = this
        $("#btn-comment").on("click", e => {
            self.saveComment();
        });

        setTimeout(function () {
            self.initSummernote();
            self.getCommentAll();
        }, 1);
    },

    initSummernote() {
        const saveReplyBtn = function (c) {
            const button = $.summernote.ui.button({
                contents: '등록',
                tooltip: "등록",
                click: function (e) {
                    console.log(c);
                    comment.saveComment();
                },
            });
            return button.render();
        };

        const saveCommentBtn = function (c) {
            const button = $.summernote.ui.button({
                contents: '등록',
                tooltip: "등록",
                click: function (e) {
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
                    //comment.saveComment();
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
                        board.sendFile(file, this, 15);
                    }
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
        const reply = document.getElementById("reply" + boardId);
        const replyDia = document.getElementById("replyDialog");
        replyDia.style.display = '';
        $("#hidden-thread").val(thread);
        $("#hidden-depth").val(depth);
        $('#txt-reComment').summernote('reset');
        reply.appendChild(replyDia);
    },

    hiveCommentDiv() {
        var div = document.getElementById(id);
        div.style.display = "none";
        document.body.appendChild(div);

    },

    getCommentAll() {
        const id = $("#hidden-id").val();
        if (!id) return;
        $.ajax({
            url: '/api/v1/comments/' + id,
            type: "GET",
            dataType: "text",
            contentType: "application/json; charset=utf-8",
        }).done((data) => {
            let htmlStr = '';
            $.each(JSON.parse(data), function (key, value) {
                htmlStr += '<div id="reply' + value.id + '" style="border-bottom:1px solid darkgray; margin-bottom: 15px; padding-left: ' + (value.depth * 15) + 'px;">';
                htmlStr += '<p>';
                if (value.depth > 0) {
                    htmlStr += '<img src="/img/arrow2.png" width="15"/>';
                }
                htmlStr += '<b>' + value.userName + '</b> ' + value.createdDate;
                htmlStr += '<div style="float: right;">';
                htmlStr += '<i class="far fa-comment-dots" style="padding-right: 10px" onclick="comment.showCommentDiv(' + value.id + ',' + value.thread + ',' + value.depth + ')"></i>';
                htmlStr += '<i class="far fa-edit" style="padding-right: 10px"></i>';
                htmlStr += '<i class="far fa-trash-alt" style="padding-right: 10px"></i>';
                htmlStr += '</div></p>';
                htmlStr += '<p style="padding-left: ' + (value.depth * 15) + 'px;">' + value.contents + '</p>';
                htmlStr += '</div>';
            });
            $("#commentList").html(htmlStr);
        }).fail(err => {
            alert(JSON.stringify(err))
        });
    },

    saveComment() {
        const id = $("#input-id").val();
        const data = {
            boardId: id,
            userId: $("#hidden-email").val(),
            userName: $("#input-author").val(),
            contents: $('#txt-comment').summernote('code')
        }

        $.ajax({
            type: "POST",
            url: '/api/v1/comments/' + id,
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': board.token()}
        }).done(() => {
            alert("댓글이 등록되었습니다.");
            $('#txt-comment').summernote('reset');
            comment.getCommentAll();
        }).fail(err => {
            alert(JSON.stringify(err))
        })
    },

    saveReplyByComment() {
        const id = $("#hidden-id").val();
        const thread = $("#hidden-thread").val();
        const depth = $("#hidden-depth").val();
        const data = {
            userId: 'tester',
            userName: 'tester',
            contents: $('#txt-reComment').summernote('code'),
            thread: parseInt(thread) - 1,
            depth: parseInt(depth) + 1
        }

        $.ajax({
            type: "POST",
            url: '/api/v1/comments/' + id + '/reply',
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': board.token()}
        }).done(() => {
            alert("게시글이 등록되었습니다.")
            $('#txt-reComment').summernote('reset');
            comment.getCommentAll();
        }).fail(err => {
            alert(JSON.stringify(err))
        })
    }
}

comment.init()
