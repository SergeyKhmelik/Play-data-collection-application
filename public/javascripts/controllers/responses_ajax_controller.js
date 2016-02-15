/**
 * Sends questioner answers data
 */
$('#response_form').on('submit', function(event) {
    event.preventDefault();

    if(!isValidForm($(event.target))) {
        return false;
    }

    var formData = new FormData($(event.target)[0]);

    $.ajax({
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (response_text) {
            socket.send(JSON.stringify(response_text));
            showCongrats();
        },
        error: function (errors) {
            appendErrors($('#response_form'), errors);
        }
    });
});