/**
 * Save/update field query
 */
$('#fieldCreationForm').on('submit', function (event) {
    event.preventDefault();

    if(!isValidForm($(event.target))) {
        return false;
    }

    var formData = new FormData($(event.target)[0]);
    getOptionsFromForm(formData);

    $.ajax({
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            window.location.replace("/fields")
        },
        error: function (errors) {
            appendErrors($('#fieldCreationForm'), errors);
        }
    });
});


/**
 * Delete field query
 */
$('#confirmDeleteBtn').on('click', function(event) {
    var idField = $(event.target).attr('data-id-field');
    jsRoutes.controllers.FieldController.deleteField(idField).ajax({
        success: function(data) {
            removeDeletedField(data, idField);
        },
        error: function(error) {
            appendFieldDeleteError(error);
        }
    });
});
