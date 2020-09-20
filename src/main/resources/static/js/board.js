const main = {
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
    save() {
        const data = {
            subject: $("#input-subject").val(),
            userName: "밍굴이",
            userId: 1,
            contents: $("#txt-content").val()
        }

        $.ajax({
            type: "POST",
            url: "/board/save",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data)
        }).done(() => {
            alert("게시글이 등록되었습니다.")
            window.location.href = "/board";
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
            type: "PUT",
            url: "/board/edit/" + id,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data)
        }).done(() => {
            alert("게시글이 수정되었습니다.")
            window.location.href = "/board";
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
            url: "/board/" + id,
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        }).always(() => {
            alert("게시글이 삭제되었습니다.")
            window.location.href = "/board";
        })
    }
}

main.init()
