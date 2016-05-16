
$(document).ready(function(){
	//APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_0");
	APP.ACCORDION_MOVEABLE();
	APP.BODY_COLLAPSES("CLOSE");

	$("#frm_userNameReterieval").trackChanges();
	$("#currentFormName").val("frm_userNameReterieval");


	$("#frm_userNameReterieval").validate({
	rules: {
		userEmailAddress: {
			required: true
		},
		userFirstName: {
			required: true
		},
		userLastName: {
			required: true
		}
	},
	messages: {
		userEmailAddress: "Please enter your email address.",
		userFirstName: "Please enter the first name of the account holder.",
		userLastName: "Please enter the last name of the account holder."
	},

	submitHandler: function(form) {
		form.submit();
	}

	});

});



