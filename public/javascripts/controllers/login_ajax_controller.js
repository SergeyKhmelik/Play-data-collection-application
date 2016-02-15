$('#loginForm').submit(function(event) {
    event.preventDefault();

    if(!isValidForm($(event.target))) {
        return false;
    }

    var formData = new FormData($(event.target)[0]);
    jsRoutes.controllers.UserController.login().ajax({
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response_text) {
            location.reload(true);
        },
        error: function(errors) {
            appendErrors($('#loginForm'), errors);
            $('#password_field').find('input').val("");
        }
    });
});