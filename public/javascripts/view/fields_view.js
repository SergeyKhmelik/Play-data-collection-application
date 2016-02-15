function getOptionsFromForm(formData) {
    if (!$('#options-div').hasClass('hidden')) {
        var iterator = 0;
        $('#options-list .input-group input').each(function (index) {
            if ($(this).val() != "") {
                formData.append('options[' + iterator + ']', $(this).val());
                iterator++;
            }
        });
    }
}

function removeDeletedField(data, idField) {
    $('#fields_table tr[id=' + idField + ']').remove();
    $('#confirm-delete').modal('hide');
}

function appendFieldDeleteError(error) {
    $('#fields_table_div').after(
        '<br/>' +
        '<div class="alert alert-warning fade in">' +
        '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
        '<strong>Warning!</strong> This field has been already deleted. <p>Please refresh your page.</p>' +
        '</div>');
    $('#confirm-delete').modal('hide');

}


// Show/hide options inputs depending on fieldType selected
$(document).on('change', '#fieldType', function () {
    var multiOptionalFields = ["radio button", "check box", "combo box"];
    var fieldValue = $('#fieldType').val();
    if ($.inArray(fieldValue, multiOptionalFields) != -1) {
        $('#options-div').removeClass('hidden');
    } else {
        $('#options-div').addClass('hidden');
    }
});

//Add new option to field options
$(document).on('focus', '#add-new-option input', function () {
    $('#add-new-option').before(
        '<div class="input-group">' +
        '<input type="text" class="form-control" placeholder="Write option text here...">' +
        '<span class="input-group-btn">' +
        '<button class="btn btn-default btn-delete-option" type="button">X</button>' +
        '</span>' +
        '</div>');
    $('#add-new-option').prev().find('input').select();
});

//Remove option from field options
$(document).on('click', '.btn-delete-option', function () {
    $(this).parent().parent().remove()
});

//Delete confirmation-modal show action
$(document).on('show.bs.modal', '#confirm-delete', function (modalTriggerEvent) {
    var idField = parseInt($(modalTriggerEvent.relatedTarget).parent().parent().attr('id'));
    $('#confirmDeleteBtn').attr('data-id-field', idField);
});