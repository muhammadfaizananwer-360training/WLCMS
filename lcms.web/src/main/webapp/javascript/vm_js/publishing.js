$(document).ready(function() {

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

	});

	$("#frm_publish")
		.validate(
		{
			rules: {
				mSRP: {
					required: true,
					min: 0.00
				},
				lowestSalePrice: {
					required: true,
					min: 0.00
				}
			},
			messages: {
				mSRP: {
					required: "Please select MSRP / list price.",
					min: "Price cannot be a negative value."
				},
				lowestSalePrice: {
					required: "Please select Lowest on-sale price.",
					min: "Price cannot be a negative value."
				}
			}
		});
	$("#frm_webinar_publish")
		.validate(
		{
			rules: {
				mSRP: {
					required: true,
					min: 0.00
				},
				lowestSalePrice: {
					required: true,
					min: 0.00
				}
			},
			messages: {
				mSRP: {
					required: "Please select MSRP / list price.",
					min: "Price cannot be a negative value."
				},
				lowestSalePrice: {
					required: "Please select Lowest on-sale price.",
					min: "Price cannot be a negative value."
				}
			}
		});
});

function ProceedtoPublish(formName) {
	$("#frm_publish").valid();
}

function ProceedtoPublishWebinarClassRoom(formName,checkValidation) {
	if(checkValidation)
	{
		$("#"+formName).valid();
	}
	$('#' +formName).submit();
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
	ProceedtoPublish(formName);
}

function elementFadeOut(id) {
	setTimeout(function() {
		$(id).html('');
	}, 10000);
}

function callValidator(){
	var cType = getParameterByName('cType');
	if(cType != null && cType == WLCMS_CONSTANTS_COURSE_TYPE.ONLINE_COURSE )
		$("#frm_publish").valid();
	else
		$("#frm_webinar_publish").valid();
}