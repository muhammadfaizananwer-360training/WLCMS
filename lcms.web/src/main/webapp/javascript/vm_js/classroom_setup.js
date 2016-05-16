function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam)
        {
            return sParameterName[1];
        }
    }
}

$(function() {

	APP.PLACEHOLDER_FIX();
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_1c");



	if($('#Coursestatus').val() == 'Published')
	{
	    $("#rdoClassroomlimit").attr('disabled','disabled');
	    $("#maximumClassSize").attr('disabled','disabled');
		$("#location_name").attr('disabled','disabled');
		$("#location_address").attr('disabled','disabled');
		$("#location_city").attr('disabled','disabled');
		$("#location_postcode").attr('disabled','disabled');
		$("#country").attr('disabled','disabled');
		$("#state").attr('disabled','disabled');
		$("#location_phone").attr('disabled','disabled');
		$("#location_description").attr('disabled','disabled');

		$("#saveBtn").attr('disabled','disabled');
	}

	 if($('#varSyncClass').val() > 0)
		{

		}
		else
		{
			TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_WEBINAR_SETUP_REQUIRED_INSTRUCTOR_SCEHDULE});
			$("#rdoClassroomlimit").attr('disabled','disabled');
			$("#maximumClassSize").attr('disabled','disabled');
			$("#location_name").attr('disabled','disabled');
			$("#location_address").attr('disabled','disabled');
			$("#location_city").attr('disabled','disabled');
			$("#location_postcode").attr('disabled','disabled');
			$("#country").attr('disabled','disabled');
			$("#state").attr('disabled','disabled');
			$("#location_phone").attr('disabled','disabled');
			$("#location_description").attr('disabled','disabled');

			$("#saveBtn").attr('disabled','disabled');
		}

	 $('#lnkclassroomsetup a').addClass('active');

    // Setup form validation on the #register-form element

	 $.validator.addMethod('minStrict', function (value, el, param) {
		    return value > param;
	});

	$.validator.addMethod("numberOnlyforLimittedClass",
		function(value, element) {
			return /^[0-9]+$/i.test(value);
		},
		"Please enter the class size"
	);

    $("#frm_classroomsetup").validate({

    	// Specify the validation rules


    	rules: {
        	location_name: { required: true },
        	location_address: { required: true},
        	location_city: { required: true },
        	location_postcode: {
        		required: true,
				maxlength: 5,
				number: true
			},
        	country: { required: true },
        	state: { required: true },
        	location_phone: {
        		required: true,
				number: true
			},
			maximumClassSize:  {
				numberOnlyforLimittedClass: true,
				maxlength: 3,
				minStrict: 0
 			}


        },
        // Specify the validation error Placement

      	 // Specify the validation error messages
        messages: {
        	location_name: {required : "Please enter the Location"},
        	location_address: {required : "Please enter the Location Address"},
        	location_city: {required : "Please enter the Location City"},
        	location_postcode: {required : "Please enter the Location Post Code",
        		maxlength: "Please enter zip code no more than 5 digits"},
        	country: {required : "Please enter the Location Country"},
        	state: {required : "Please enter the Location State"},
        	location_phone: {required : "Please enter the Location Phone Number"},
        	maximumClassSize: {digits : "Please enter a number greater than zero",
        		maxlength:"Please enter size not more than 3 digits",
        		minStrict:"Please enter a number greater than zero."}
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
			elementFadeOut('.messages');
			elementFadeOut("#msgdiv");
		}

    });
  });


$('.btn-sub-group').click(function()
		  {
	TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_WEBINAR_SETUP_REQUIRED_INSTRUCTOR_SCEHDULE});
		  });

function enableLimitField(isRemove)
{
	if(!isRemove)
	{
		$("#_limit").addClass("hide");
	}
	else
	{
		$("#_limit").removeClass("hide");
	}
}

function gotoEdit (courseid)
{
	window.location = "editCourseOverview?id=" + courseid.value;
}

function elementFadeOut(id)
{
	setTimeout(function()
	{
      $(id).html('');
  },9000);
}
