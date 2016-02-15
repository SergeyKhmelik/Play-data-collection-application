#Save/update field query
$('#fieldCreationForm').on 'submit', (event) ->
  event.preventDefault()
  formData = new FormData($(event.target)[0])
  if(!$('#options-div').hasClass('hidden'))
    iterator = 0
    $('#options-list .input-group input').each((index) ->
      if($(this).val() != "")
        formData.append('options[' + iterator + ']', $(this).val())
        iterator += 1
      )
  $.ajax({
    method: 'POST'
    data: formData
    processData: false,
    contentType: false,
    success: (response) ->
      window.location.replace("/fields")
    error: (error) ->
      errors = error.responseJSON
      $('.help-block.error').remove()
      $('.has-error').removeClass('has-error')
      $('.error').removeClass('error')
      $('.alert-span').empty()

      if errors.hasOwnProperty("global")
        $('#global-error-div').removeClass('hidden')
        for error of errors.global
        	$('#global-error-div .alert-span').append(errors.global)
      
      for key, value of errors
      	if(!$("[id='" + key + "_field']").length)
          $('#global-error-div').removeClass('hidden')
          $('#global-error-div .alert-span').append(value.join())
        else
          formGroup = $("[id='" + key + "_field']").parent()
          formGroup.addClass('has-error')
          formGroup.addClass('error')
          formGroup.append('<span class="help-block error">' + value.join() + '</span>')
  })

#Delete confirmation-modal show action
$('#confirm-delete').on 'show.bs.modal', (modalTriggerEvent) ->
  idField = parseInt($(modalTriggerEvent.relatedTarget).parent().parent().attr('id'))
  $('#confirmDeleteBtn').attr('data-id-field', idField)

#Delete confirmed action
$('#confirmDeleteBtn').on 'click', (event) ->
  idField = $(event.target).attr('data-id-field')
  jsRoutes.controllers.FieldController.deleteField(idField).ajax({
    error: (error) ->
      $('#fields_table_div').after(
        '<br/>' +
          '<div class="alert alert-warning fade in">' +
          '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
          '<strong>Warning!</strong> This field has been already deleted. Please refresh your page.' +
          '</div>')
      $('#confirm-delete').modal('hide')
    success: (data) ->
      $('#fields_table tr[id=' + idField + ']').remove()
      $('#confirm-delete').modal('hide')
  })


#Option field
$(document).on 'change', '#fieldType', ->
  if $('#fieldType').val() in ["radio button", "check box", "combo box"]
    $('#options-div').removeClass('hidden')
  else
    $('#options-div').addClass('hidden')

#Add new options to field
$(document).on 'focus', '#add-new-option', ->
  $(this).before(
    '<div class="input-group">' +
      '<input type="text" class="form-control" placeholder="Write option text here...">' +
      '<span class="input-group-btn">' +
      '<button class="btn btn-default btn-delete-option" type="button">X</button>' +
      '</span>' +
      '</div>');
  $(this).prev().find('input').select()

#Delete option from field
$(document).on 'click', '.btn-delete-option', (event) ->
  $(event.target).parent().parent().remove()