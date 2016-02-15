#Add response
$('#response_form').on 'submit', (event) ->
  event.preventDefault()
  $('.help-block .error').remove()
  formData = new FormData($(event.target)[0])

  $.ajax({
    method: 'POST'
    data: formData
    processData: false,
    contentType: false,
    success: (response_text) ->
      socket.send(JSON.stringify(response_text))
      $('.container').empty()
      $('.container').append('<h3 class="page-center text-center">Thank you for submitting your data</h3>')
    error: (error) ->
      console.log(error)
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
          console.log($("[id='" + key + "_field']").length)
          $('#global-error-div').removeClass('hidden')
          $('#global-error-div .alert-span').append(value.join())
        else
          console.log($("[id='" + key + "_field']").length)
          formGroup = $("[id='" + key + "_field']").parent()
          formGroup.addClass('has-error')
          formGroup.addClass('error')
          formGroup.append('<span class="help-block error">' + value.join() + '</span>')
  })