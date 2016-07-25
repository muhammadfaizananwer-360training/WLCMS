//$("#frm_overview").trackChanges();
$("#currentFormName").val("frm_overview");



$(function() {
	var cType = getParameterByName('cType');
	var bRequired = false;

	if(cType != null && (cType == WLCMS_CONSTANTS_COURSE_TYPE.CLASSROOM_COURSE || cType == WLCMS_CONSTANTS_COURSE_TYPE.WEBINAR_COURSE)){
		bRequired = true;
	}

	switch (cType){
	case WLCMS_CONSTANTS_COURSE_TYPE.CLASSROOM_COURSE:
			APP.LEFT_NAV.init("OPEN","nav_accordion_0_cr");
			break;
	case WLCMS_CONSTANTS_COURSE_TYPE.WEBINAR_COURSE:
			APP.LEFT_NAV.init("OPEN","nav_accordion_0_wb");
			break;
	default:
			APP.LEFT_NAV.init("OPEN","nav_accordion_0");
	}

	APP.ACCORDION_MOVEABLE();
	APP.BODY_COLLAPSES("CLOSE");
	APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");

	$('.carousel').carousel('pause');
	var uname =  $("#username").val();
	var showTutorial = readCookie('showOverviewTutorial_' + uname);
	if (showTutorial!="false"){
		$('#chkShowTutorial').prop('checked', true);
		$('#orientationModal').modal('show');
		}
	else{
		$('#chkShowTutorial').prop('checked', false);
	}


	$.validator.addMethod("valueNotEquals", function(value, element, arg){
		  return arg != value;
		 }, "Value must not equal arg.");

	$("#btnSave").removeAttr('disabled');
	// KeyWords Validation Empty Field
	$(".bootstrap-tagsinput input").keyup(function(){
	$(".bootstrap-tagsinput input").removeAttr("placeholder");
	var errors = false;
		var tagchecker = $("#keywords").parent().find(".bootstrap-tagsinput > input");
		if(tagchecker.length == 0)
		{
			$(".bootstrap-tagsinput input").attr("placeholder","Enter keywords.");
			errors = true;
			if(!$("#keywords").parent().find("label").hasClass("error"))
			{
			$("#keywords").parent().append("<label for='keywords' generated='true' class='error'>Please enter at least one keyword. Separate keywords by using a comma ( , ).</label>");
			}
			$("#keywords").parent().find(".bootstrap-tagsinput").addClass("error");
		}
		else
		{
			errors = false;
			$("#keywords").parent().find("lable.error").remove();
			$("#keywords").parent().find(".bootstrap-tagsinput").removeClass("error");
			$(".bootstrap-tagsinput").siblings("label.error").remove();
		}

		return errors;
	});

	// KeyWords Validation Field
	function tagchecker()
	{
		var errors = false;
		var tagchecker = $("#keywords").parent().find(".bootstrap-tagsinput > span");
		if(tagchecker.length == 0)
		{
			errors = true;
			$("#keywords").parent().append("<label for='keywords' generated='true' class='error'>Please enter at least one keyword. Separate keywords by using a comma ( , ).</label>");
			$("#keywords").parent().find(".bootstrap-tagsinput").addClass("error");
		}
		else
		{
			errors = false;
			$("#keywords").parent().find("lable.error").remove();
			$("#keywords").parent().find(".bootstrap-tagsinput").removeClass("error");
			$(".bootstrap-tagsinput").siblings("label.error").remove();
		}

		return errors;
	}


    // Setup form validation on the #register-form element
    $("#frm_overview").validate({

        // Specify the validation rules
        rules: {
        	name: "required",
        	description: "required",
			industry: { valueNotEquals: "Select A Category" },
        	language_id: "required",
        	keywords:{
        		required:bRequired
        		}
        },
        // Specify the validation error messages
        messages: {
        	name: "Please enter your title here",
        	description: "Please enter your description here",
        	industry: "Please select a category",
        	language_id: "Please select your language",
        	keywords: "Please enter at least one keyword. Separate keywords by using a comma(,)."

        },

        submitHandler: function(form)
        {
			if(!tagchecker())
			{
				form.submit();
			}
        },

		invalidHandler: function(event, validator) {

			var errors = validator.numberOfInvalids();

			errors = tagchecker();

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
			TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_COURSE_SAVE_REQUIRED});
		  });

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

  function createCookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else {
		var expires = "";
	}
    document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') {
			c = c.substring(1, c.length);
		}
        if (c.indexOf(nameEQ) == 0) {
			return c.substring(nameEQ.length, c.length);
		}
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name,"",-1);
}

function setTutorialPreference(){
	var checked = $('#chkShowTutorial').is(":checked");
	var uname =  $("#username").val();
	createCookie('showOverviewTutorial_' + uname, checked, 365 );
}
