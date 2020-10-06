const board = {
    init() {
        const self = this
        $("#btn-save").on("click", e => {
            self.savePost();
        });

        $("#btn-reply").on("click", e => {
            self.saveReply();
        })

        $("#btn-update").on("click", e => {
            self.update();
        });

        $("#btn-delete").on("click", e => {
            self.delete();
        });

        $('#txt-content').summernote({
            height: 300,
            lang: "ko-KR",
            placeholder: '최대 2048자까지 쓸 수 있습니다.',
            callbacks: {
                onImageUpload: function (files) {
                    for (let file of files) {
                        board.sendFile(file, this, 50);
                    }
                }
            }
        });

        $('#view-content').summernote('disable');
    },
    sendFile(file, el, size) {
        const form_data = new FormData();
        form_data.append('file', file);
        $.ajax({
            type: "POST",
            url: '/images',
            contentType: false,
            data: form_data,
            cache: false,
            enctype: 'multipart/form-data',
            processData: false,
            headers: {'X-CSRF-TOKEN': this.token()}
        }).done((url) => {
            $(el).summernote('insertImage', url, function ($image) {
                $image.css('width', size+"%");
            });
        }).fail(err => {
            console.log(err);
        });
    },

    token() {
        console.log($("input[name='_csrf']").val())
        return $("input[name='_csrf']").val();
    },

    savePost() {
        const data = {
            subject: $("#input-subject").val(),
            contents: $('#txt-content').summernote('code')
        }

        $.ajax({
            type: "POST",
            url: '/api/v1/boards',
            dataType: "text",
            contentType: "application/json;",
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': board.token()}
        }).done(() => {
            alert("게시글이 등록되었습니다.")
            window.location.href = "/boards";
        }).fail(err => {
            console.log(JSON.stringify(err))
        })
    },

    saveReply() {
        const id = $("#hidden-id").val();
        const thread = $("#hidden-thread").val();
        const depth = $("#hidden-depth").val();
        const data = {
            subject: $("#input-subject").val(),
            userId: $("#hidden-email").val(),
            contents: $('#txt-content').summernote('code'),
            thread: parseInt(thread) - 1,
            depth: parseInt(depth) + 1
        }

        console.log(data)
        $.ajax({
            type: "POST",
            url: '/api/v1/boards/' + id + '/reply',
            dataType: "text",
            contentType: "application/json;",
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': board.token()}
        }).done(() => {
            alert("답글이 등록되었습니다.")
            window.location.href = "/boards";
        }).fail(err => {
            console.log(JSON.stringify(err))
        })
    },

    update() {
        const id = $("#input-id").val()
        const data = {
            id: id,
            subject: $("#input-subject").val(),
            contents: $('#txt-content').summernote('code'),
        }

        $.ajax({
            type: "PATCH",
            url: "/api/v1/boards/" + id,
            data: JSON.stringify(data),
            dataType: "text",
            contentType: "application/json;",
            headers: {'X-CSRF-TOKEN': this.token()}
        }).done(() => {
            alert("게시글이 수정되었습니다.")
            window.location.href = "/boards";
        }).fail(err => {
            console.log(JSON.stringify(err))
        })
    },
    delete() {
        const id = $("#input-id").val()
        if (!confirm("이 게시글을 삭제하시겠습니까?")) {
            return false
        }

        $.ajax({
            type: "DELETE",
            url: "/api/v1/boards/" + id,
            dataType: "text",
            contentType: "application/json;",
            headers: {'X-CSRF-TOKEN': this.token()}
        }).done(() => {
            alert("게시글이 삭제되었습니다.")
            window.location.href = "/boards";
        }).fail(err => {
            console.log(JSON.stringify(err))
        })
    }
}

board.init()
