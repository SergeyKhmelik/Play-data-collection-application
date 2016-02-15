$(document).on 'submit', '#loginForm', (event) ->
  event.preventDefault();
  formData = new FormData($(event.target)[0])
  jsRoutes.controllers.UserController.login().ajax({
    method: 'POST'
    data: formData
    processData: false,
    contentType: false,
    success: (response_text) ->
      $('#login-nav-btn').addClass("hidden")
      $('#logout-nav-btn').removeClass("hidden")
      $('#login-modal').modal('hide')
      location.reload(true)
    error: (error) ->
      console.log(error)
      errors = error.responseJSON
      $('.help-block.error').remove()
      $('.has-error').removeClass('has-error')
      $('.error').removeClass('error')
      $('.alert-span').empty()
      $('#password_field input').val("")

      if errors.hasOwnProperty("global")
        $('#login-global-error-div').removeClass('hidden')
        for error of errors.global
          $('#login-global-error-div .alert-span').append(errors.global)
      for key, value of errors
        if(errors.hasOwnProperty(key))
          formGroup = $("[id='" + key + "_field'").parent()
          formGroup.addClass('has-error')
          formGroup.addClass('error')
          formGroup.append('<span class="help-block error">' + value.join() + '</span>')
  })