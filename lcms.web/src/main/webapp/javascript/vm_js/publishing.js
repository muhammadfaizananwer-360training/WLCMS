$(document).ready(function() {
	/*APP.PLACEHOLDER_FIX();
	 APP.EDIT_OR_VIEW_TOGGLE();
	 APP.BODY_COLLAPSES();
	 APP.LEFT_NAV.init("OPEN", "nav_accordion_3");

	 $('#lnkPublishing a').addClass('active');
	 $("#frm_publish").trackChanges();
	 $("#currentFormName").val("frm_publish");
	 */
	$.validator.addMethod('onecheck', function(value, ele) {
		var chk = false;
		if ($("input:checked").length >= 1) {
			chk = true;
		}
		return chk;
	}, '')



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
$(function() {
	$(':checkbox').change(function() {

       // callValidator();
	});

	$("#frm_publish")
		.validate(
		{
			rules : {
				mSRP :{ required:true},
				lowestSalePrice : { required:true}
			},
			messages : {
				mSRP :"Please select MSRP / list price.",
				lowestSalePrice:"Please select Lowest on-sale price."

			}
		});
	$("#frm_webinar_publish")
		.validate(
		{
			rules : {
				mSRP :{ required:true},
				lowestSalePrice : { required:true}
			},
			messages : {
				mSRP :"Please select MSRP / list price.",
				lowestSalePrice:"Please select Lowest on-sale price."

			}
		});

	/*$("#frm_publish")
	 .validate(
	 {
	 rules : {
	 publishLMS : {
	 onecheck : true
	 },
	 updateLMS : {
	 onecheck : true
	 },
	 updateSF : {
	 onecheck : true
	 }
	 },
	 messages : {
	 publishLMS : {
	 onecheck : "Please select one method to publish."
	 },
	 updateLMS : {
	 onecheck : "Please select one method to publish."
	 }
	 },
	 submitHandler : function(form) {
	 if ($("#errorExist").val()=="1")
	 return false;
	 else
	 form.submit();
	 },
	 errorPlacement : function(error, element) {
	 error.appendTo('#err');
	 }

	 });*/


	/*$("#frm_webinar_publish")
	 .validate(
	 {
	 rules : {
	 publishLMS : {
	 onecheck : true
	 },
	 updateLMS : {
	 onecheck : true
	 },
	 updateSF : {
	 onecheck : true
	 }
	 },
	 messages : {
	 publishLMS : {
	 onecheck : "Please select one method to publish."
	 },
	 updateLMS : {
	 onecheck : "Please select one method to publish."
	 }
	 },
	 errorPlacement : function(error, element) {
	 error.appendTo('#err');
	 },
	 submitHandler : function(form) {
	 form.submit();
	 }
	 });*/

});


function ProceedtoPublish(formName) {
	$("#frm_publish").valid();
	//$('#' +formName).submit();
	//makeOfferAccepted();
}

function ProceedtoPublishWebinarClassRoom(formName,checkValidation) {
	if(checkValidation)
	{
		$("#"+formName).valid();
	}
	$('#' +formName).submit();
	//makeOfferAccepted();
}


function makeOfferAccepted(){

	var courseId = getUrlParameter ('id');
	var cType = getUrlParameter ('cType');

	targetUrl = "makeOffer";
	APP.AJAX({
		url: targetUrl,
		dataType: "text",
		type: "POST",
		cache: false,
		data:'hidCourseId='+courseId+'&cType='+cType,
		async: false,
		success: function(response) {
			obj = $.parseJSON(response);
			if(obj=="SUCCESS") {
				thisCourse.offerState = 1;
			}
			else{
				thisCourse.offerState=2;
			}
		}
	});

	// displaying success message


}


function requestToCancelOffer(){

	var courseId = getUrlParameter ('id');
	var cType = getUrlParameter ('cType');
	var success = false;
	targetUrl = "cancelOffer";
	APP.AJAX({
		url: targetUrl,
		dataType: "text",
		type: "POST",
		cache: false,
		data:'hidCourseId='+courseId+'&cType='+cType,
		async: false,
		success: function(response) {
			obj = $.parseJSON(response);
			if(obj.status=="SUCCESS") {
				success = true;
				$360.showMessage({})
			} else {
				$360.showMessage({error:"error"})
			}
		},
		error:function () {
			$360.showMessage({error:"error"})
		}
	});

	return success;
	// displaying success message


}

function showPublishAlert(formName) {
	$("#errorExist").val("0");
	/*var bValidLMSOption = $("#updateLMS").attr('checked')=='checked';
	 var bValidSFOption = $("#updateSF").attr('checked')=='checked' ;
	 var bValidLMSOptionFirstTime = $("#publishLMS").attr('checked')=='checked';
	 var bValidSFOptionFirstTime = $("#publishSF").attr('checked')=='checked' ;
	 var bValidupdateCouseContent = $("#updateCouseContent").attr('checked')=='checked' ;

	 if (bValidLMSOption == false &&  bValidSFOption == false && bValidLMSOptionFirstTime == false &&  bValidSFOptionFirstTime == false && bValidupdateCouseContent== false){
	 TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST_PUBLISHING});
	 var cType = getParameterByName('cType');
	 if(cType != null && cType == WLCMS_CONSTANTS_COURSE_TYPE.ONLINE_COURSE ){
	 $("#errorExist").val("1");
	 }
	 callValidator();
	 return false;
	 }*/

	ProceedtoPublish(formName);
}

function elementFadeOut(id) {
	setTimeout(function() {
		$(id).html('');
	}, 10000);
}

function callValidator(){
	var cType = getParameterByName('cType');
    alert('sffffdf')

	if(cType != null && cType == WLCMS_CONSTANTS_COURSE_TYPE.ONLINE_COURSE )
		$("#frm_publish").valid();
	else
		$("#frm_webinar_publish").valid();
}