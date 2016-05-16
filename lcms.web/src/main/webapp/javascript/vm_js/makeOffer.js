function click_makeOffer()
{


	$trgModal = $("#confirmationModal");

	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="icon-exclamation-sign"></i> Publish Course?';

	var msg = '<p>The offer on this course may be sent to 360training only on course publish. Do you want to publish the course now?</p>';

	var btns = '<button type="button" class="btn blue" onclick="makeOfferAccepted()" data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default" data-dismiss="modal">NO</button>';
	//	END TITLE, MESSAGE AND BUTTONS

	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');

}



function makeOfferAccepted(){

	$("div#divBtnMakeOfferbtn").hide();
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
				}
	});

	// displaying success message
	$('#successMsg').html("");


	$('#successMsg').append("<div  class='messages'>"+
		"<div role='alert' class='alert alert-success alert-dismissible fade in'>"+
		"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
		"<strong>Success!</strong> Your offer has been submitted.</div></div>");
	elementFadeOut('.messages');

	document.getElementById("divPending").setAttribute("style", "display:block");
}