function isValidForm(formElement){
    trimAllInputs(formElement);
    var notAnsweredFieldCount = 0;
    removePreviousErrorsInfo(formElement);
    formElement.find('.required').each(function(){
        if(!isAnswered(this)) {
            notAnsweredFieldCount++;
            appendErrorBlock(this);
        }
    });
    return notAnsweredFieldCount == 0;
}

function isAnswered(field) {
    if($(field).find('select').length != 0) {
        return $(field).find('option:selected').length > 0;
    }

    if($(field).find('input').length == 1) {
        return($(field).find('input:first').val().trim() !== "");
    }

    if($(field).find('input').length > 1) {
        return $(field).find('input:checked').length > 0;
    }
}

function appendErrorBlock(field) {
    $(field).addClass('has-error');
    $(field).addClass('error');
    $(field).append('<span class="help-block error">Field is required.</span>');
}

$(document).on('change', '.has-error input', function(event){
    var parentDiv = $(event.target).closest('.has-error');
    $(parentDiv).removeClass('has-error');
    $(parentDiv).removeClass('error');
    $(parentDiv).find('.help-block.error').remove();
});

function trimAllInputs(element) {
    element.find('input').each(function(){
        this.value = $(this).val().trim();
    })
}