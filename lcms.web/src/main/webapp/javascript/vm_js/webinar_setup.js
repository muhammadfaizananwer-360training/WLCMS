

$("#varCourseId").val(getUrlParameter ('id'));

function getUrlParameter(sParam)
{
	var sPageURL = window.location.search.substring(1);
	var sURLVariables = sPageURL.split('&');
	for (var i = 0; i < sURLVariables.length; i++)
	{
		var sParameterName = sURLVariables[i].split('=');
		if (sParameterName[0] === sParam)
		{
			return sParameterName[1];
		}
	}
}

$(function() {

	APP.PLACEHOLDER_FIX();
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_1w");
	APP.BODY_COLLAPSES("CLOSE");
	APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");
	$('#lnkWebinarSetup a').addClass('active');
	$("#frm_webinarsetup").trackChanges();
	$("#currentFormName").val("frm_webinarsetup");
	var courseStartDate = $('#courseStartDate').val();
	var presenterEmail = $('#presenterEmail').val();
	if((courseStartDate == null  || courseStartDate.length <= 0) || (presenterEmail == null  || presenterEmail.length <= 0 )){
		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_WEBINAR_SETUP_REQUIRED_INSTRUCTOR_SCEHDULE_W, bFadeOut:false});
		$("#WEBINAR_SERVICE_PROVIDER").attr('disabled','disabled');
		$('.form-group').each(function(i, item){
			if ($(item).find('.control-label').text ().indexOf('Webinar Service Provider') < 0 ){
				$(item).hide ();
			}
		});
		$("#saveBtn").hide ();
	}
	$("#frm_webinarsetup").validate({
		/*ignore: ':hidden:not(#enroll_date)',*/
		// Specify the validation rules
		rules: {
			MEETINGURL: {
				required: true,
				url: true
			},
			WEBINAR_SERVICE_PROVIDER: {
				required:true
			}

		},
		// Specify the validation error Placement
		// Specify the validation error messages
		messages: {
			MEETINGURL: {
				required : "Please enter Meeting URL.",
				url      :	"Invalid URL format. Please correct the URL before proceeding."
			},
			WEBINAR_SERVICE_PROVIDER: {
				required : "Please select Webinar Service Provider."
			}
		},
		submitHandler: function(form)
		{
			if(this.currentForm['WEBINAR_SERVICE_PROVIDER'].value === "360training"  && form.shouldSubmbitForm.value === "false")
			{
				$trgModal = $("#confirmationModal");
				//	BEGIN TITLE, MESSAGE AND BUTTONS
				var title = '<i class="glyphicon glyphicon-info-sign"></i> Please Confirm';
				/*var msg = ' <p> IMPORTANT:  If you wish to use the “360training” webinar provider, your course must be offered for distribution on the 360training marketplace.  When you publish this webinar, you must select “Yes” for the option “Publish to 360training marketplace.</p> <p>If you do not wish to offer this webinar for distribution, please select "Others" as your webinar provider, and fill in the information of your own webinar provider. Note that you can still make an offer on a webinar using a non-360training webinar provider.</p></br><p>Continue with Save?</p>';*/
				var msg = ' <p> IMPORTANT:  If you wish to use the “360training” webinar provider, your course must be offered for distribution on the 360training marketplace.  When you publish this webinar, you must select “Yes” for the option “Publish to 360training marketplace.” </p> <p>If you do not wish to offer this webinar for distribution, please select “Others” as your webinar provider, and fill in the information of your own webinar provider.  Note that you can still offer this course for distribution using a non-360training webinar provider.</p></br><p>Continue with Save?</p>';
				var btns = '<button type="button" class="btn blue" data-dismiss="modal" onclick="sendEmail();">YES</button>'+
					'<button type="button" class="btn btn-default" data-dismiss="modal">NO</button>';
				//	END TITLE, MESSAGE AND BUTTONS
				$trgModal.find(".modal-title").html(title);
				$trgModal.find(".modal-body").html(msg);
				$trgModal.find(".modal-footer").html(btns);
				$trgModal.modal('show');
			}
			else
			{
				form.submit();
			}
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
	TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_WEBINAR_SETUP_REQUIRED_INSTRUCTOR_SCEHDULE, bFadeOut:false});
});



function gotoEdit (courseid)
{
	window.location = "editCourseOverview?id=" + courseid.value;
}

function sendEmail ()
{
	var form = $("#frm_webinarsetup");
	$("#shouldSubmbitForm").val("true");
	form.submit();
}

function elementFadeOut(id)
{
	setTimeout(function(){ $(id).html('');  },9000);
}
