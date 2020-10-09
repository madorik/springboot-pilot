const INVALID_COLOR = "#FF0000";
const VALID_COLOR = "#5CD1E5";

const user = {
    init() {
        const self = this
        $("#sign-email").on("input", () => {
            self.checkInvalidEmail();
        });

        $("#password").on("input", () => {
            self.checkInvalidPassword();
        });

        $("#confirm-password").on("input", () => {
            self.checkInvalidPassword();
        });
    },

    checkInvalidEmail() {
        const token = $("input[name='_csrf']").val();
        const email = $('#sign-email').val();
        const data = {
            email: email
        };

        $.ajax({
            type: "POST",
            url: "/user/check/mail",
            dataType: "json",
            contentType: "application/json;",
            data: JSON.stringify(data),
            headers: {'X-CSRF-TOKEN': token}
        }).done((count) => {
            if (count === 1) {
                $("#btn-signup").prop("disabled", true);
                $("#sign-email").css("border-color", INVALID_COLOR);
            } else {
                $("#btn-signup").prop("disabled", false);
                $("#sign-email").css("border-color", VALID_COLOR);
            }
        }).fail(err => {
            console.log(err)
        })
    },
    checkInvalidPassword() {
        const regExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,15}$/;
        const password = $('#password').val();
        const confirmPassword = $('#confirm-password').val();

        if (regExp.test(password)) {
            $("#password").css("border-color", VALID_COLOR);
        } else {
            $("#password").css("border-color", INVALID_COLOR);
        }

        if (password === confirmPassword) {
            $("#confirm-password").css("border-color", VALID_COLOR);
        } else {
            $("#confirm-password").css("border-color", INVALID_COLOR);
        }
    }
}

user.init()
