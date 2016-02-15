function appendErrors(element, errors) {
    removePreviousErrorsInfo(element);
    var errorsJson = errors.responseJSON;
    if (errorsJson.hasOwnProperty("global")) {
        appendGlobalError(element, errorsJson.global);
    }
    appendNonGlobalErrors(element, errorsJson);
}

function removePreviousErrorsInfo(element) {
    element.find('.help-block.error').remove();
    element.find('.has-error').removeClass('has-error');
    element.find('.error').removeClass('error');
    element.find('.global-error-div').addClass('hidden');
    element.find('.alert-span').empty();
}

function appendGlobalError(element, globalErrors) {
    var globalErrorDiv = element.find('.global-error-div');
    globalErrorDiv.removeClass('hidden');
    for (var error in globalErrors) {
        globalErrorDiv.find('.alert-span').append(globalErrors[error]);
    }
}

function appendNonGlobalErrors(element, errors) {
    for (var key in errors) {
        var value = errors[key];
        var fieldElement = element.find("[id='" + key + "_field']");
        if (!fieldElement.length && key !== "global") {
            var globalErrorDiv = element.find('.global-error-div');
            globalErrorDiv.removeClass('hidden');
            globalErrorDiv.find('.alert-span').append(value.join());
        } else {
            var formGroup = fieldElement.parent();
            formGroup.addClass('has-error');
            formGroup.addClass('error');
            formGroup.append('<span class="help-block error">' + value.join() + '</span>');
        }
    }
}

$(document).on('click', '.global-error-div .close', function(event) {
    event.preventDefault();
    $(event.target).parent().addClass('hidden');
});