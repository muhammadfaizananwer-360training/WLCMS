
$(document).ready(function(){
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_0");
	APP.ACCORDION_MOVEABLE();
	APP.BODY_COLLAPSES("CLOSE");

	$("#frm_signupAuthor").trackChanges();
	$("#currentFormName").val("frm_signupAuthor");
	var urlSource = location.search.replace('?', '');
	$("#src").val(urlSource);
});

$(function() {
	 $.validator.addMethod("noSpecialChars", function(value, element) {
		return this.optional(element) || /^[A-Za-z\d][A-Za-z\d@.\-_]+$/i.test(value);
	}, "Username must contain letters, numbers or special characters \".@_-\" only");

	$.validator.addMethod("noStartWithSpecialChars", function(value, element) {
		return this.optional(element) || /^[@.\-_]$/i.test(value);
	}, "Username must start with only letters, numbers");

	var asValue = function(valOrFunc) {
	return $.isFunction(valOrFunc) ? valOrFunc() : valOrFunc;
	};

	$.validator.addMethod("checkUserExistance", function(value, element, params) {
		var useMyEmailTF = asValue(params.useMyEmailTF);
		var email = asValue(params.email);

		if (!useMyEmailTF || useMyEmailTF == 0) { // should probably be a little more careful than this

			return "reenterEmail"; // this is a special value for the plugin
		}

		return $.validator.methods.remote.call(this, value, element, {
			url: "/lcms/signUpAuthorExists",
			type: "post",
			data: {
				useMyEmailTF: useMyEmailTF,
				email:email
			}
		});
	});
    $("#frm_signupAuthor").validate({

        rules: {
        	firstName: {
				required: true
			},
			lastName: {
				required: true
			},
			email: {
				required: true,
				email: true,
				"remote": {
					url: "/lcms/signUpAuthorExists",
					type: "post",
					data: {
						email: function() {
								return $("#email").val();
							},
						useMyEmailTF: function() {
							return $('input[name=useMyEmailTF]:checked', '#frm_signupAuthor').val();
						}
					}
				}
			},
			reenterEmail: {
				required: true,
				email: true,
				equalTo: "#email"
			},
			passwrod: {
				required: true,
				minlength:8
			},
			reenterPasswrod: {
					required: true,
					minlength:8,
					equalTo: "#passwrod"
			},
			useOtherEmail: {
				required: "#useMyEmailTF2:checked",
				noSpecialChars: true,
				minlength:2,
				maxlength:50,
				"remote": {
					url: "/lcms/signUpAuthorExists",
					type: "post",
					data: {
					email: function() {
							return $("#useOtherEmail").val();
						}
					}
				}
			},
			eulaFT:  {
				required: true
			}
		},
        // Specify the validation error messages
        messages: {
        	firstName: "Please enter your first name here",
			lastName: "Please enter your last name here",
			email: {
				required: "Please enter your email address here",
				email: "Your email address has an invalid email address format. Please try  again",
				remote: "To sign up to use the course creation software, please create an account specific to this purpose.<br> The username/email you specified is already in use."
			},
			reenterEmail: {
					required: "Please re-enter your email address here",
					equalTo: "Your email entries do not match, please re-enter your email address.",
					//remote: "It looks like this may be your regular account for buying courses.<br> To sign up to use the course creation software, you need to create a new account. <br>That way the courses you make can be shared with others or even sold through your very own storefront. <br> You can use your regular email, but please choose a different username. <br>should be shown if \"Normal\" account already exists in the system"
					remote: "To sign up to use the course creation software, please create an account specific to this purpose.<br> The username/email you specified is already in use."
			},
			passwrod: {
				required: "Please enter your password here",
				minlength: "Your password must contain letters and numbers and be at least 8 characters long. Please try again."
			},
			reenterPasswrod: {
				required: "Please re-enter your password here",
				minlength: "Your password must contain letters and numbers and be at least 8 characters long. Please try again.",
				equalTo: "Your password entries do not match, please check carefully and try again."
			},
			useOtherEmail: {
				required: "Please enter your username",
				//remote: "It looks like this may be your regular account for buying courses.<br> To sign up to use the course creation software, you need to create a new account. <br>That way the courses you make can be shared with others or even sold through your very own storefront. <br> You can use your regular email, but please choose a different username. <br>should be shown if \"Normal\" account already exists in the system"
				remote: "To sign up to use the course creation software, please create an account specific to this purpose.<br> The username/email you specified is already in use."
			},
			eulaFT :{
				required: "Please accept the terms of agreement"

			}

		},

		errorPlacement:
		 function(error, element) {

			if(element.attr("name") == "eulaFT") {
				error.insertAfter(".accept-chk");
			  } else {
				error.insertAfter(element);
			  }

        },

        submitHandler: function(form){
        	showProgressLoader("");
			form.submit();
        },

		invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();
			if (errors) {
				TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
			} else {
				$("#msgdiv").html('');
			}
			elementFadeOut('.messages');
			elementFadeOut("#msgdiv");
		}

    });

	 $("#reset").click(function () {
	            var validator = $("#frm_signupAuthor").validate();
                validator.resetForm();
            });

  });








function elementFadeOut(id)
{
	setTimeout(function()
	{
        $(id).html('');
    },3000);
}

$(document).ready(function (){
	$('input:radio').change(function() {
		if(this.id == 'useMyEmailTF2'){
			$('#useOtherEmail').prop('value', '');
			$('#useOtherEmail').prop('disabled', false);
			$('#useMyEmailTF2').checked = true
			$('#useMyEmailTF1').checked = false

			$('#useMyEmailTF2').checked = false
			$('#useMyEmailTF1').checked = true

			//removing error msg for reenterEmail
			$("#email").removeData("previousValue");
			// then revalidate to make sure other possible errors remain intact
			$("#email").valid();

		}
		else if(this.id == 'useMyEmailTF1'){
			$('#useOtherEmail').prop('value', '');
			$('#useOtherEmail').prop('disabled', true);

			$('#useMyEmailTF2').checked = true
			$('#useMyEmailTF1').checked = false

			$('#useOtherEmail').removeClass('error');
			$('label[for=useOtherEmail]').remove();

			//removing error msg for reenterEmail
			$("#email").removeData("previousValue");
			// then revalidate to make sure other possible errors remain intact
			$("#email").valid();
		}

    });
})

function showProgressLoader(message){
	$("#loader-overlay").html(message);
	$("#bg-loader").show();
	$("#loader-overlay").show();
}
function hideProgressLoader(){
	$("#loader-overlay").fadeOut();
	$("#bg-loader").fadeOut();
}