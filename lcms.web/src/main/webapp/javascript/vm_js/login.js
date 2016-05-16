$(function() {

    // Setup form validation on the #register-form element
    $("#frm_login").validate({

        // Specify the validation rules
        rules: {
        	username: "required",
            password:  {
                     required: true
                    // maxlenght:50
                 }
        },

        // Specify the validation error messages
        messages: {
        	username: "Please enter your login id",

            password: {
                required: "Please provide a password"
                //maxlenght: "Your password maximum 50 characters long"
            }

        },

        submitHandler: function(form)
        {
            form.submit();
        }
    });

  });

 $( document ).ready(function() {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
	var error_msg  ;
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == 'error_message')
        {
           $('#login_error').show ();
        }
    }
});
