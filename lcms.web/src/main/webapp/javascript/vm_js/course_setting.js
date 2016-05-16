$(function() {
     $("#frm_courseSettings").validate({
        rules: {
			specifiedText:  {
				required: "#agreeWithSpecifiedText:checked"
			}
		},
        // Specify the validation error messages
        messages: {
			specifiedText :{
				required: 'Please enter the disclaimer or acknowledgment text here.'
			}
		},
		submitHandler: function(form){
			form.submit();
        },
		invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();
			if (errors) {
				TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST_COURSE});
			} else {
				$("#msgdiv").html('');
			}
			elementFadeOut('.messages');
			elementFadeOut("#msgdiv");
		}
    });
	$("#frm_courseSettings").trackChanges();
	$("#currentFormName").val("frm_courseSettings");
});



function elementFadeOut(id){
	setTimeout(function(){
		$(id).html('');
    },3000);
}
