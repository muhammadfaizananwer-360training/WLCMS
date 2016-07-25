$(function() {
    var maxFileSize = 0;
    APP.AJAX({
		url: 'getFileSizeLimit?supportMaterialFileLimit=import.file.supportMaterial.size',
		dataType: "text",
		type: "GET",
		cache: false,
		success: function(response) {
			maxFileSize = response;
		}
	});

	$.validator.addMethod('validateFileSizeLimitSM', function(value, element, param) {
		input = document.getElementById('smFileUpload');
		file = input.files[0];
		return file.size <= maxFileSize;
	});


	$.validator.addMethod("validateExtension",
		function(value, element) {
			var ext = value.split('.').pop().toLowerCase();

			if($.inArray(ext, ['doc','docx','pdf','ppt','pptx','xls','xlsx','png','jpeg','jpg','gif']) != -1) {
				return true;
			} else {
				return false;
			}
		},
	"Please provide a file with a valid file type.");


    $("#frmsmUploadAsset").validate({

        // Specify the validation rules
        rules: {
        	audioAssetName: "required",
        	txtAreasmFileUpload:{
									required: true,
									validateExtension : true,
									validateFileSizeLimitSM : true
								}
        },
        // Specify the validation error messages
        messages: {
        	audioAssetName: "Please enter your asset name here.",

			txtAreasmFileUpload: {
	        		required:  "Please provide a file.",
					extension: "Please provide a file with a valid file type.",
					validateFileSizeLimitSM : "Please select a file that is less than 20 MB in size."
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

//--------------  Start --------------Audio asset form script------------------------------------

function addSupportMaterial(form){


	TEMPLATE.GARBAGE_CLEANER_LOCKED = true;
	varAudioAssetName = $(form).find("#audioAssetName").val();
	aValid = $(form).validate().element("#audioAssetName");
	bValid =  true;//$(form).validate().element("#txtAreasmFileUpload");

	if ( (aValid == false && varAudioAssetName=='') || bValid == false)
	{
		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
		elementFadeOut("#msgdiv");
		$('#btnSubmitAudioAssetForm').removeAttr("data-dismiss");
		return false;
	}

	$(form).find('input[name="lessonIdSupportMaterial"]').val($("#hidLessonId").val());
	$(form).find('input[name="courseIdForAudioAsset"]').val(getUrlParameter ('id'));

	// Create the iframe...
    var iframe = document.createElement("iframe");
    iframe.setAttribute("id", "upload_iframe");
    iframe.setAttribute("name", "upload_iframe");
    iframe.setAttribute("width", "0");
    iframe.setAttribute("height", "0");
    iframe.setAttribute("border", "0");
    iframe.setAttribute("style", "width: 0; height: 0; border: none;");

	// Add to document...
	if($(form).find('#IsCustomTemplate').val() == 'true') {
		form.parentNode.parentNode.parentNode.appendChild(iframe);
	} else {
		form.parentNode.appendChild(iframe);
	}
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

            // Delete the iframe...
            setTimeout('iframeId.parentNode.removeChild(iframeId)', 250);

			// Success message
            TopMessageBar.displayMessageTopBar({vType:1, vMsg: WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
			// display newly added Support material bar
			getAddSupportMaterialBar(varAudioAssetName);
			setArrowVsbltySpprtMtrl($("#hidLessonId").val());
        }

    if (iframeId.addEventListener) {
		iframeId.addEventListener("load", eventHandler, true);
	} else if (iframeId.attachEvent) {
		iframeId.attachEvent("onload", eventHandler);
	}

    // Set properties of form...
    form.setAttribute("target", "upload_iframe");
    form.setAttribute("action", "uploadAudioAsset");
    form.setAttribute("method", "post");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("encoding", "multipart/form-data");

	showProgressLoader("<div id='loader-label'>Uploading...</div>");
    form.submit();
	cancelsmUploadForm();
}

function getAddSupportMaterialBar(parAssetName){
	supprtMtrIdMax = 0;
	var supprtMtrIdMaxURL = "/lcms/getSpprtMtrIdMax";
	APP.AJAX({
		 url:  supprtMtrIdMaxURL,
		 dataType: "text",
		 type: "POST",
		 cache: false,
		 data:'LessonId='+ $("#hidLessonId").val(),
		 async: false,
		 success: function(response) {
			supprtMtrIdMax = $.parseJSON(response);
		}
	});

	var $divClone = $("div#supportMaterialStrip").clone();
	var arrSupportMaterialid = supprtMtrIdMax.status;
	var contentObjectId = $("#hidLessonId").val();

	$divClone.find("#divSupportMaterial_Setup").attr("id", "divSupportMaterial_Setup_"+arrSupportMaterialid);
	$divClone.find("#linkSupportMaerial").attr("onclick", 'getSupportMaterialInfo('+ arrSupportMaterialid +','+ contentObjectId + ')' );
	$divClone.attr("id", "supportMaterialStrip_"+contentObjectId+"_"+arrSupportMaterialid );
	$divClone.find('#linkSupportMaerial').attr('href', "#divSupportMaterial_Setup_"+arrSupportMaterialid);
	$divClone.find('#linkSupportMaerial').attr('data-parent', "#supportMaterial_"+contentObjectId);
	$divClone.find('#supportMaterial_title').text( parAssetName );
	$divClone.find ("#supMaterialDelButton").attr("onclick", 'remove_panel(this, 7 , '+ arrSupportMaterialid +')' );
	$("div#supportMaterial_"+contentObjectId).append($divClone);
	$divClone.show();
}

function showProgressLoader(message){
	$("#loader-overlay").html(message);
	$("#bg-loader").show();
	$("#loader-overlay").show();
}

function cancelsmUploadForm() {
	$("#frmsmUploadAsset").get(0).reset();
	$('#btnSubmitAudioAssetForm').attr("data-dismiss", "modal");

	$("#audioAssetKeywords").parent().find('span').remove()
	$('#msgdiv').html ('');
	return false;
}


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

function initSuppMatAttAsset(){
	$("#addSMModal").css("display", "none");
	$("#addSMModal").attr('aria-hidden', 'true');
	$("#addSMModal").attr('class', 'modal fade');
}

function deleteSupportMaterial(supMaterialId){

	deleteSupportMaterialUrl = "deleteSupportMaterial";
	var course_id = getParameterByName("id");
	APP.AJAX({
				url: deleteSupportMaterialUrl,
				dataType: "text",
				type: "POST",
				cache: false,
				data:'varSupMaterialId='+supMaterialId + '&courseId='+ getUrlParameter ("id")+'&courseId='+course_id,
				async: false,
				success: function(response) {
					 obj = $.parseJSON(response);
				}
	});
}