$(document).ready(function(){
	APP.PLACEHOLDER_FIX();
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_3");
	APP.DATE_PICKER("#expDate");
	APP.BODY_COLLAPSES("OPEN");
	$('#lnkAvailability a').addClass('active');
	$("#frm_availability").trackChanges();
	$("#currentFormName").val("frm_availability");


	var nowTemp = new Date();
	var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);


	var checkin = $('#courseExpiration').datepicker({
	  onRender: function(date) {
		return date.valueOf() <= now.valueOf() ? 'disabled' : '';
	  }
	}).on('changeDate', function(ev) {
	  checkin.hide();
	}).data('datepicker');

	$.validator.addMethod('onecheck', function(value, ele) {
		var chk = false;
		if($("input:checked").length >= 2){
			chk=true;
		}
		return chk;
	}, 'Please select one method to publish.');


});


$(function() {

	var nowTemp = new Date();
	var future = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);

	$.validator.addMethod('futuredate', function(value, ele) {

		if(value == null || value == '' || value == 'undefined'){
			return true;
		}
		var dsplit = value.split("-");
		var valueDateobj = new Date( dsplit[2],dsplit[0]-1,dsplit[1],0, 0, 0, 0);
		var validDate = false;
		if ( valueDateobj > future )
			validDate = true;

		return validDate;
	}, 'Course expiration must be a future date.');

	$("#frm_availability").validate({
		rules: {
			learnerAccessToCourse:  {
				digits: true,
				min:1
			},
			courseExpiration : {
				required: false
			}
		},
		messages: {
			learnerAccessToCourse :{
				digits: "Please enter a positive integer",
				min:"Please enter a positive integer"
			}
		},
		errorPlacement: function(error, element) {
			//error.appendTo('#err');
			error.insertAfter(element);
		},
		submitHandler: function(form){
			form.submit();
		},
		invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();
			if (errors) {
			$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Not so fast! Required fields are missing. Complete the fields, then click <strong>Save</strong>.</div>');
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

function validateForm () {
	$("#frm_availability").valid ();
}

function elementFadeOut(id){
	setTimeout(function(){$(id).html('');},3000);
}