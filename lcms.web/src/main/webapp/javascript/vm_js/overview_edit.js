$(document).ready(function(){
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_0");
	APP.ACCORDION_MOVEABLE();
	APP.BODY_COLLAPSES("CLOSE");

	$("#frm_overview_edit").trackChanges();
	$("#currentFormName").val("frm_overview_edit");

	$.validator.addMethod("valueNotEquals", function(value, element, arg){
		return arg != value;
	}, "Select A Category.");
});

$(function() {
	// Setup form validation on the #register-form element
	var cType = getParameterByName('cType');
	var bRequired = false;

	if(cType != null && (cType == WLCMS_CONSTANTS_COURSE_TYPE.CLASSROOM_COURSE || cType == WLCMS_CONSTANTS_COURSE_TYPE.WEBINAR_COURSE)){
		bRequired = true;
	}

	// KeyWords Validation Empty Field
	$(".bootstrap-tagsinput input").keyup(function(){
		$(".bootstrap-tagsinput input").removeAttr("placeholder");
		var errors = false;
		var tagchecker = $("#keywords").parent().find(".bootstrap-tagsinput > span");
		if(tagchecker.length == 0)
		{
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
			$(".bootstrap-tagsinput").siblings("label").remove();
		}
		return errors;
	});

	$(".bootstrap-tagsinput input").keyup(function(){

		var errors = false;
		var tagchecker = $("#keywords").parent().find(".bootstrap-tagsinput > span");
		if(tagchecker.length == 0)
		{
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


	$("#frm_overview_edit").validate({

		// Specify the validation rules
		rules: {
			name: "required",
			description: "required",
			language_id: "required",
			businessunitName: { valueNotEquals: "Select A Category" },
			keywords:{
				required:bRequired
			}

		},
		// Specify the validation error messages
		messages: {
			name: "Please enter your title here",
			description: "Please enter your description here",
			language_id: "Please select your language",
			businessunitName: "Please select a category",
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
			elementFadeOut("#msgdiv");
		}
	});

	var courseType = getUrlParameter ("cType");
	$("#courseType").val(courseType);

});

$( document ).ready(function() {
	course_id = getUrlParameter ("id");
	$('#id').val (course_id);
	$('input[name="id"]').val(course_id);
	var cType = getParameterByName('cType');
	var myvalpublish ;
	var myvalcourse;
	var myvalcoursestatusa;

	//Online/classroom/webinar courses
	if(cType != null) {
		myvalpublish = $("#publish_status").text().toLowerCase().trim();
		myvalcourse = $("#course_rating").text().toLowerCase().trim();
		myvalcoursestatusa = $("#course_status").text().toLowerCase().trim();

		if ( myvalcourse == "pending" || myvalcourse === undefined || myvalcourse === "" ){
			$("#course_ratingI").html('<i class="icon-exclamation" style="color: #ffb848"></i>');
		} else {
			$("#course_ratingI").html('<i class="icon-ok green-text"></i>');
		}

		if (myvalpublish == "published") {
			$("#publish_statusI").html('<i class="icon-ok green-text"></i>');
		} else if (myvalpublish == "not published" || myvalpublish == "changes not published" || myvalpublish === undefined || myvalpublish === "" ){
			$("#publish_statusI").html('<i class="icon-exclamation" style="color: #ffb848"></i>');
		}

		if (myvalcoursestatusa == "retired"){
			$("#course_statusI").html('<i class="icon-exclamation" style="color: #ffb848"></i>');
		} else if (myvalcoursestatusa == "active"){
			$("#course_statusI").html('<i class="icon-ok green-text"></i>');
		}
	}
});

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

function gotoEdit (courseid)
{
	window.location = "editCourseOverview?id=" + courseid.value;
}

function EditUrl()
{
	var orignalurl = window.location.href;
	var updatedurl = orignalurl.replace("&msg=success","");
	window.location.href = updatedurl ;
}

//Yasin
function elementFadeOut(id)
{
	setTimeout(function(){
		$(id).html('');
	},9000);
}