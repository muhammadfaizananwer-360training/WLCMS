
$(document).ready(function(){
	//APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_0");
	APP.ACCORDION_MOVEABLE();
	APP.BODY_COLLAPSES("CLOSE");

	$("#frm_passwordReterieval").trackChanges();
	$("#currentFormName").val("frm_passwordReterieval");


	$("#frm_passwordReterieval").validate({
	rules: {
		userName: {
			required: "#radioUserName:checked"
		},
		userEmailAddress: {
			required: "#radioEmailAddress:checked"
		},
		userFirstName: {
			required: "#radioEmailAddress:checked"
		},
		userLastName: {
			required: "#radioEmailAddress:checked"
		}
	},
	messages: {
		userName: "Please enter your username.",
		userEmailAddress: "Please enter your email address.",
		userFirstName: "Please enter the first name of the account holder.",
		userLastName: "Please enter the last name of the account holder."
	},

	submitHandler: function(form) {
		form.submit();
	}

	});

});



