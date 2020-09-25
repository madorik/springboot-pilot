const board = {
    init() {
        const self = this
        $("#btn-save").on("click", e => {
            self.save();
        })

        $("#btn-update").on("click", e => {
            self.update();
        })

        $("#btn-delete").on("click", e => {
            self.delete();
        })

        if ($("#div-content").length != 0) {
            const content = $("#div-content").text()
            const expUrl = /(((http(s)?:\/\/)\S+(\.[^(\n|\t|\s,)]+)+)|((http(s)?:\/\/)?(([a-zA-z\-_]+[0-9]*)|([0-9]*[a-zA-z\-_]+)){2,}(\.[^(\n|\t|\s,)]+)+))+/gi
            const changedContent = content.replace(expUrl, "<a href='$&' target='_blank'>$&</a>")
            $("#div-content").html(changedContent)
        }
    },
    token() {
        return $("input[name='_csrf']").val();
    },

    save() {
        const pid = $("#hidden-pid").val();
        const orderNo = $("#hidden-orderNo").val();
        const depth = $("#hidden-depth").val();

        const data = {
            subject: $("#input-subject").val(),
            userId : $("#hidden-email").val(),
            userName: $("#input-author").val(),
            contents: $("#txt-content").val(),
            pid: pid ? pid : 0,
            orderNo: orderNo ? (parseInt(orderNo)+1) : 0,
            depth: depth ? (parseInt(depth)+1) : 0
        }

        $.ajax({
            type: "POST",
            url: "/api/v1/boards" + (pid ? '/' + orderNo +'/reply' : ''),
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': this.token()}
        }).done(() => {
            alert("게시글이 등록되었습니다.")
            window.location.href = "/boards";
        }).fail(err => {
            alert(JSON.stringify(err))
        })
    },
    update() {
        const id = $("#input-id").val()
        const data = {
            id: id,
            subject: $("#input-subject").val(),
            contents: $("#txt-content").val()
        }

        $.ajax({
            type: "PATCH",
            url: "/api/v1/boards/" + id,
            data: JSON.stringify(data),
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            headers: {'X-CSRF-TOKEN': this.token()}
        }).done(() => {
            alert("게시글이 수정되었습니다.")
            window.location.href = "/boards";
        }).fail(err => {
            alert(JSON.stringify(err))
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
            contentType: "application/json; charset=utf-8",
            headers: {'X-CSRF-TOKEN': this.token()}
        }).done(() => {
            alert("게시글이 삭제되었습니다.")
            window.location.href = "/boards";
        }).fail(err => {
            alert(JSON.stringify(err))
        })
    }
}

board.init()
