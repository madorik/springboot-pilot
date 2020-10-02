const comment = {
    init() {
        const self = this
        $("#btn-comment").on("click", e => {
            self.saveComment();
        });
        self.getCommentAll();


        const buttons = $.summernote.options.buttons;
        $.summernote.options.toolbar.push(["customButton", ["saveReply"]]);

        const saveReply = function (context) {
            const ui = $.summernote.ui;
            const button = ui.button({
                contents: '<i class="fa fa-pencil"/>등록',
                tooltip: "등록",
                click: function () {
                    self.saveComment();
                },
            });
            return button.render();
        };
        buttons.saveReply = saveReply;

        $('#txt-comment').summernote({
            height: 60,
            lang: "ko-KR",
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
                ['customButton', ['saveReply']]
            ]
        });
    },

    getCommentAll() {
        const boardId = $("#input-id").val();
        $.ajax({
            url: '/api/v1/comments/' + boardId,
            type: "GET",
            dataType: "text",
            contentType: "application/json; charset=utf-8",
        }).done((data) => {
            let htmlStr = '';

            $.each(JSON.parse(data), function (key, value) {
                htmlStr += '<div style="border-bottom:1px solid darkgray; margin-bottom: 15px;">';
                htmlStr += '<div>작성자 : ' + value.userName;
                htmlStr += '<a href="#"> 수정</a>';
                htmlStr += '<a href="#"> 삭제</a></div>';
                htmlStr += '<div><p> 내용 : ' + value.contents + ' </p>';
                htmlStr += '</div></div>';
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
            comment.getCommentAll();
        }).fail(err => {
            alert(JSON.stringify(err))
        })
    }
}

comment.init()
