var maxFileSize = 0;

$(document).ready(function(){

	APP.AJAX({
		url: 'maxfilesize',
		dataType: "text",
		type: "GET",
		cache: false,
		success: function(response) {
			maxFileSize = response;
		}
	});


	$.validator.addMethod('filesize', function(value, element, param) {
		input = document.getElementById('importPPTFile');
		file = input.files[0];
		return file.size <= maxFileSize;
		//return this.optional(element) || (element.files[0].size <= param)
	});

	$("#frmPPTImport").validate({
	        // Specify the validation rules
	        rules: {
	        	txtimportPPTFileUpload:  {
					required: true,
					extension: "ppt|PPT|pptx|PPTX",
					filesize :	maxFileSize
				}
	        },
	        // Specify the validation error messages
	        messages: {
	        	txtimportPPTFileUpload: {
	        		required:  "Please provide a file with valid ppt/pptx type.",
					extension: "Please provide a file with valid ppt/pptx type.",
					filesize : "Please provide a PPT/PPTX with file size less than 20 MB."
	        	}
	        }
        });

});


function imprtPPTDialog (trg) {
	$('#pptUploader').uploader360().reset();
	APP.CACHE = trg;
}


function uploadPPT (form) {

	if (!$(form).valid()) {
		return false;
	}

	// Submit the form...
trg = APP.CACHE;
	$trg = $(trg);
	APP.CACHE = '';
	contentObjectId    = $trg.data('lesson-id');
	$('#frmPPTImport').find("#content_id").val( contentObjectId ) ;
	$('#frmPPTImport').find("#id").val( getParameterByName ('id')) ;	showProgressLoader("<div id='loader-label'>Uploading...</div>");


	// Create the iframe...
    var iframe = document.createElement("iframe");
    iframe.setAttribute("id", "upload_iframe");
    iframe.setAttribute("name", "upload_iframe");
    iframe.setAttribute("width", "0");
    iframe.setAttribute("height", "0");
    iframe.setAttribute("border", "0");
    iframe.setAttribute("style", "width: 0; height: 0; border: none;");

	// Add to document...
    form.parentNode.appendChild(iframe);
    window.frames['upload_iframe'].name = "upload_iframe";

	iframeId = document.getElementById("upload_iframe");
	var eventHandler = function () {
            if (iframeId.detachEvent) iframeId.detachEvent("onload", eventHandler);
            else iframeId.removeEventListener("load", eventHandler, false);
             // Message from server...
            if (iframeId.contentDocument) {
                content = iframeId.contentDocument.body.innerHTML;
            } else if (iframeId.contentWindow) {
                content = iframeId.contentWindow.document.body.innerHTML;
            } else if (iframeId.document) {
                content = iframeId.document.body.innerHTML;
            }
			hideProgressLoader();
            //document.getElementById("upload").innerHTML = content;
            // Del the iframe...
            setTimeout('iframeId.parentNode.removeChild(iframeId)', 250);

			// Success message
            TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
			$('#successMsg').show ();
			// Call function to list the asset
			getSlidesByContentAjax (contentObjectId);
    }

    if (iframeId.addEventListener) iframeId.addEventListener("load", eventHandler, true);
    if (iframeId.attachEvent) iframeId.attachEvent("onload", eventHandler);

    // Set properties of form...
    form.setAttribute("target", "upload_iframe");
    form.setAttribute("action", "uploadPPTFile");
    form.setAttribute("method", "post");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("encoding", "multipart/form-data");
    // Submit the form...
	showProgressLoader("<div id='loader-label'>Uploading...</div>");
    form.submit();

	var validator = $(form).validate();
	validator.resetForm();
	$('#btAcceptPPTFile').attr("data-dismiss", "modal");
}


function CancelUploadPPT (form) {
	var validator = $( "#frmPPTImport" ).validate();
	validator.resetForm();

	$('#importPPTFile').val ('');
	$('#txtimportPPTFileUpload').val ('');

	$('#lblPPTPreview').val('');
	$('#importPPTModal').find('.fileupload').fileupload ('clear');
}