$(function() {

	APP.BOOTSTRAP_POPOVER(".help-anchor");

	var bSuccess = getParameterByName("success");
	if(bSuccess == 1){
		TopMessageBar.displayMessageTopBar({vType:1, vMsg:'Your profile has been updated successfully!'});
	}

	$.validator.addMethod('mypassword', function(value, element) {
        return this.optional(element) || (value.match(/[a-zA-Z]/) && value.match(/[0-9]/));
    },
    'Your password must be at least 8 characters long and contain at least one letter and one number.');


	$("#userProfileForm").validate({
	        rules: {
	        	firstName: {
					required: true
				},
				lastName: {
					required: true
				},
				email: {
					required: true,
					email: true
				},
				password: {
					required: true,
					minlength:8,
					mypassword:true				},
				cpassword: {
						required: true,
						equalTo: "#password"
				}
			},
	        // Specify the validation error messages
	        messages: {
	        	firstName: {
					required: "Please enter your first name."
				},
				lastName: {
					required: "Please enter your last name."
				},
				email: {
					required: "Please enter a valid email address.",
					email: "Your email address has an invalid email address format. Please try again."
				},
				password: {
					required: "Please enter a password.",
					minlength: "Your password must be at least 8 characters long and contain at least one letter and one number.",
					mypassword:"Your password must be at least 8 characters long and contain at least one letter and one number."
				},
				cpassword: {
					required: "Please re-enter your password.",
					equalTo: "Your passwords do not match."
				}
			},

			errorPlacement:
			 function(error, element) {
					error.insertAfter(element);
	        },

	        submitHandler: function(form){
				form.submit();
	        },

			invalidHandler: function(event, validator) {
				var errors = validator.numberOfInvalids();
				if (errors) {
					TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
				} else {
					$("#TopMsgBarDiv").html('');
				}
				elementFadeOut('.messages');
				elementFadeOut("#msgdiv");
			}
 	    });

	$.validator.addMethod("validateImageExtension",
			function(value, element) {
				var ext = value.split('.').pop().toLowerCase();
				if( $.inArray(ext, ['gif','png','jpg','jpeg','bmp']) != -1) {
					return true;
				} else {
					return false;
				}
			},
		"Please provide a file with these types gif, png, jpg, jpeg, bmp.");

	$("#formUploadProfilePicture").validate({
        rules: {
        	txtAreaProfilePictureUpload:  {
				required: true,
				extension : "gif|png|jpg|jpeg|bmp"
			}
        },
        messages: {
        	txtAreaProfilePictureUpload: {
				required: "Please provide a file with a valid file type.",
				extension: "Please provide a file with these types gif, png, jpg, jpeg, bmp."
			}
        },
        submitHandler: function(form)
        {
			form.submit();
        },
		invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();
			if (errors) {
				TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
			} else {
				$("#msgdiv").html('');
			}
			elementFadeOut("#msgdiv");
		}
    });
});

function elementFadeOut(id)
{
	setTimeout(function()
	{
        $(id).html('');
    },3000);
}

function saveProfile(element) {

	var bValidate =  $("#userProfileForm").valid();

	if(bValidate){
		element.form.submit();
	}
}

function AddProfilePicture(form){

	// Create the iframe...
    var iframe = document.createElement("iframe");
    iframe.setAttribute("id", "upload_iframe");
    iframe.setAttribute("name", "upload_iframe");
    iframe.setAttribute("width", "0");
    iframe.setAttribute("height", "0");
    iframe.setAttribute("border", "0");
    iframe.setAttribute("style", "width: 0; height: 0; border: none;");

	form.parentNode.appendChild(iframe);

    window.frames['upload_iframe'].name = "upload_iframe";

	iframeId = document.getElementById("upload_iframe");



	var eventHandler = function () {

            if (iframeId.detachEvent) {
				iframeId.detachEvent("onload", eventHandler);
			} else {
				iframeId.removeEventListener("load", eventHandler, false);
			}

            // Message from server...
            if (iframeId.contentDocument) {
                content = iframeId.contentDocument.body.innerHTML;
            } else if (iframeId.contentWindow) {
                content = iframeId.contentWindow.document.body.innerHTML;
            } else if (iframeId.document) {
                content = iframeId.document.body.innerHTML;
            }
			hideProgressLoader();
			content = $.parseJSON(content);
            setTimeout('iframeId.parentNode.removeChild(iframeId)', 250);
            $("#assetId").val(content.assetId);
            $("#profileImage").attr("src", content.assetUrl);
        }

    if (iframeId.addEventListener) iframeId.addEventListener("load", eventHandler, true);
    if (iframeId.attachEvent) iframeId.attachEvent("onload", eventHandler);

    // Set properties of form...
    form.setAttribute("target", "upload_iframe");
    form.setAttribute("action", "uploadProfileImage");
    form.setAttribute("method", "post");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("encoding", "multipart/form-data");

	showProgressLoader("<div id='loader-label'>Uploading...</div>");
  // Submit the form...

    form.submit();

	$('#txtAreaProfilePictureUpload').val ('');

	$('#btaddProfileImageUpload').attr("data-dismiss", "modal");
}
function showProgressLoader(message){
	$("#loader-overlay").html(message);
	$("#bg-loader").show();
	$("#loader-overlay").show();
}
function hideProgressLoader(){
	$("#loader-overlay").fadeOut();
	$("#bg-loader").fadeOut();
}
function cancelProfilePicturetUploadForm() {
	var form = $("#formUploadProfilePicture")[0];
	$(form).find('.upload-360').uploader360().reset();
	$("#formUploadProfilePicture").get(0).reset();
	$('#msgdiv').html ('');
	var validator = $("#formUploadProfilePicture").validate();
	validator.resetForm();
	$("#txtAreaProfilePictureUpload").removeClass("error");
	return false;
}

function cancelProfileEdit(url) {

	window.location = url;
}