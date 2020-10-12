const board = {
    init() {
        const self = this
        $("#btn-save").on("click", () => {
            self.savePost();
        });

        $("#btn-reply").on("click", () => {
            self.saveReply();
        });

        $("#btn-update").on("click", () => {
            self.update();
        });

        $("#btn-delete").on("click", () => {
            self.delete();
        });

        $('#txt-content').summernote({
            height: 300,
            lang: "ko-KR",
            placeholder: '최대 2048자까지 쓸 수 있습니다.',
            toolbar: [
                ['style', ['style']],
                ['font', ['bold', 'italic', 'underline', 'clear']],
                ['fontname', ['fontname']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']],
                ['table', ['table']],
                ['insert', ['link', 'picture', 'video']],
                ['view', ['fullscreen']],
                ['help', ['help']]
            ],
            callbacks: {
                onImageUpload: function (files) {
                    if (files.length > 3) {
                        alert("업로드 파일은 한 번에 최대 3개까지만 선택 가능합니다.");
                        return;
                    }
                    for (let file of files) {
                        board.sendFile(file, this, 50);
                    }
                },
                onMediaDelete: function (files) {
                    const imageUrl = $(files[0]).attr('src');
                    board.removeFile(imageUrl);
                }
            }
        });

        $.summernote.options.toolbar.push(['view', ['fullscreen']]);

        $('#view-content').summernote('disable');
    },

    removeFile(url) {
        $.ajax({
            type: 'DELETE',
            url: url,
            dataType: "text",
            contentType: "application/json;",
            headers: {'X-CSRF-TOKEN': board.token()}
        }).done(() => {
            console.log("remove image")
        }).fail(err => {
            console.log(JSON.stringify(err))
        })
    },

    sendFile(file, el, size) {
        const data = new FormData();
        data.append('file', file);
        if (file.size > 1048576) {
            alert("파일 사이즈는 1MB를 초과할 수 없습니다.");
            return;
        }

        $.ajax({
            type: "POST",
            url: '/api/v1/images',
            contentType: false,
            data: data,
            cache: false,
            enctype: 'multipart/form-data',
            processData: false,
            headers: {'X-CSRF-TOKEN': this.token()}
        }).done((id) => {
            $(el).summernote('insertImage', "/api/v1/images/" + id, function ($image) {
                $image.css('width', size + "%");
            });
        }).fail(err => {
            console.log(err);
        });
    },

    token() {
        return $("input[name='_csrf']").val();
    },

    savePost() {
        const valid = $("#form-post-save")[0].checkValidity();
        if (!valid) {
            alert("제목과 내용은 필수 입력 항목입니다.");
            return;
        }
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
        });
    },

    saveReply() {
        const valid = $("#form-post-reply")[0].checkValidity();
        if (!valid) {
            alert("제목과 내용은 필수 입력 항목입니다.");
            return;
        }
        const id = $("#hidden-board-id").val();
        const thread = $("#hidden-thread").val();
        const depth = $("#hidden-depth").val();
        const data = {
            subject: $("#input-subject").val(),
            userId: $("#hidden-email").val(),
            contents: $('#txt-content').summernote('code'),
            thread: parseInt(thread) - 1,
            depth: parseInt(depth) + 1
        }

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
        });
    },

    update() {
        const id = $("#input-id").val()
        const data = {
            id: id,
            subject: $("#input-subject").val(),
            contents: $('#txt-content').summernote('code'),
        }
        const isEmpty = $('#txt-content').summernote('isEmpty');
        if (data.subject === '' || isEmpty) {
            alert("제목과 내용은 필수 입력 항목입니다.");
            return;
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
        });
    },

    delete() {
        const id = $("#input-id").val();
        if (!confirm("이 게시글을 삭제하시겠습니까?")) {
            return false;
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
