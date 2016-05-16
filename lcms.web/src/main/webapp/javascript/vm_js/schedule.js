function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
	for ( var i = 0; i < sURLVariables.length; i++) {
		var sParameterName = sURLVariables[i].split('=');
    	if (sParameterName[0] === sParam) {
			return sParameterName[1];
		}
	}
}

$(function() {

	APP.PLACEHOLDER_FIX();
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.BODY_COLLAPSES();

	APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");
	if ($('#varisPublished').val() !== "Published") {
		APP.DATE_PICKER("#enrollDate");
		APP.DATE_PICKER("#startDate");

	} else {

		$("#timezome").attr('disabled', 'disabled');
		$("#start_time").attr('disabled', 'disabled');
		$("#end_time").attr('disabled', 'disabled');
		$("#btnSave").attr('disabled', 'disabled');

	}

	if ($('#synccoursetype').val() === "Classroom Course") {
		APP.LEFT_NAV.init("OPEN", "nav_accordion_1c");
	} else if ($('#synccoursetype').val() === "Webinar Course") {
		APP.LEFT_NAV.init("OPEN", "nav_accordion_1w");
	}
	$('#lnkSchedule a').addClass('active');
    $("#frm_schedule").trackChanges();
	$("#currentFormName").val("frm_schedule");

	// Setup form validation on the #register-form element
	var startDateisbeforeEnrollDate = function(startDateStr, endDateStr) {

    	var enrollDate = $('#enroll_date').val();
		var startDate = $('#start_date').val();
		var D1 = new Date(enrollDate.replace(/(\d{2})-(\d{2})-(\d{4})/,
				"$2/$1/$3")); // Year, Month, Date
		var D2 = new Date(startDate.replace(/(\d{2})-(\d{2})-(\d{4})/,
				"$2/$1/$3"));

		if (D1 <= D2) {
			return true;
		} else if (D1 > D2) {
			return false;
		}

	};

	var startDateisBeforeCurrentDate = function(enrolldate, startdate) {

		var courseStartDate = $('#start_date').val();
		var today = new Date();

		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);

		if (Date.parse(today) <= Date.parse(courseStartDate)) {

			return true;
		} else if (Date.parse(today) > Date.parse(courseStartDate)) {

			return false;
		}

	};

	var enrollDateisBeforeCurrentDate = function(enrolldate, startdate) {

		var courseEnrollDate = $('#enroll_date').val();
		var today = new Date();

		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);

		if (Date.parse(today) <= Date.parse(courseEnrollDate)) {

			return true;
		} else if (Date.parse(today) > Date.parse(courseEnrollDate)) {

			return false;
		}

	};

	var starttimeisBeforeCurrenttime = function(enrolldate, startdate) {

		var strcourseStartDate = $('#start_date').val();
		var strcourseStarttime = $('#start_time').val();
		var index = strcourseStarttime.indexOf(":");
		var hours, minutes, inthours;
		var currentDatetime = new Date();
		var courseStartDate = new Date(strcourseStartDate);
		var currentDate = new Date();

		currentDatetime.setHours(0);
		currentDatetime.setMinutes(0);
		currentDatetime.setSeconds(0);

		courseStartDate.setHours(0);
		courseStartDate.setMinutes(0);
		courseStartDate.setSeconds(0);

		if (index > 0) {
			if (strcourseStarttime.indexOf("AM") > 0) {
				hours = strcourseStarttime.substr(0, index);
				inthours = parseInt(hours);
				minutes = strcourseStarttime.substr(strcourseStarttime
						.indexOf(":") + 1, 2);
				intminutes = parseInt(minutes);
			} else if (strcourseStarttime.indexOf("PM") > 0) {
				hours = strcourseStarttime.substr(0, index);
				inthours = parseInt(hours) + 12;
				minutes = strcourseStarttime.substr(strcourseStarttime
						.indexOf(":") + 1, 2);
				intminutes = parseInt(minutes);
			}
		}

		courseStartDate.setHours(inthours);
		courseStartDate.setMinutes(intminutes);
		courseStartDate.setSeconds(0);

		if (Date.parse(currentDate) > Date.parse(courseStartDate)) {
			return false;
		} else if (Date.parse(currentDate) < Date.parse(courseStartDate)) {
			return true;
		}

	};

	var starttimeisBeforeEndtime = function(enrolldate, startdate) {

		var strcourseStartDate = $('#start_date').val();
		var strcourseStarttime = $('#start_time').val();
		var strcourseEndtime = $('#end_time').val();
		var courseStarttimeindex = strcourseStarttime.indexOf(":");
		var courseEndtimeindex = strcourseEndtime.indexOf(":");
		var courseStarthours, courseStartminutes, intcourseStarthours, intcourseStartminutes;
		var courseEndhours, courseEndminutes, intcourseEndhours, intcourseEndminutes;
		var courseStartDate = new Date(strcourseStartDate);
		var courseEndDate = new Date(strcourseStartDate);

		if (courseStarttimeindex > 0) {
			var strcourseStartsplit = convertTimeformat(strcourseStarttime)
					.split(":");
			intcourseStarthours = strcourseStartsplit[0];
			intcourseStartminutes = strcourseStartsplit[1];
		}

		courseStartDate.setHours(intcourseStarthours);
		courseStartDate.setMinutes(intcourseStartminutes);
		courseStartDate.setSeconds(0);

		if (courseEndtimeindex > 0) {
			var strcourseEndsplit = convertTimeformat(strcourseEndtime).split(
					":");
			intcourseEndhours = strcourseEndsplit[0];
			intcourseEndminutes = strcourseEndsplit[1];
		}

		courseEndDate.setHours(intcourseEndhours);
		courseEndDate.setMinutes(intcourseEndminutes);
		courseEndDate.setSeconds(0);

		if (Date.parse(courseStartDate) > Date.parse(courseEndDate)) {
			return false;
		} else if (Date.parse(courseStartDate) < Date.parse(courseEndDate)) {
			return true;
		}
	};

	$('#enrollDate').datepicker({
		autoclose : true,
		weekStart : 1
	}).on('changeDate', function(ev) {
		$('#enrollDate').datepicker('hide');
		if ($('#enroll_date').valid()) {
			$('#enroll_date').removeClass('invalid').addClass('success');
			enrollDateisBeforeCurrentDate();
		}
	});

	$('#startDate').datepicker({
		autoclose : true,
		weekStart : 1
	}).on('changeDate', function(ev) {
		$('#startDate').datepicker('hide');
		if ($('#start_date').valid()) {
			$('#start_date').removeClass('invalid').addClass('success');
			startDateisbeforeEnrollDate();
			startDateisBeforeCurrentDate();
		}
	});

	jQuery.validator.addMethod("startDateisBeforeCurrentDate", function(value,
			element) {

		return startDateisBeforeCurrentDate($('#enroll_date').val(), value);
	}, "Course start date cannot be set to a past date.");

	jQuery.validator.addMethod("startDateisbeforeEnrollDate", function(value,
			element) {

		return startDateisbeforeEnrollDate($('#start_date').val(), value);
	}, "End date should be after start date");

	jQuery.validator.addMethod("enrollDateisBeforeCurrentDate", function(value,
			element) {

		return enrollDateisBeforeCurrentDate($('#start_date').val(), value);
	}, "Enrollment close date cannot be set to a past date.");

	jQuery.validator.addMethod("starttimeisBeforeCurrenttime", function(value,
			element) {

		return starttimeisBeforeCurrenttime($('#start_date').val(), value);
	}, "Enrollment close date cannot be set to a past date.");

	jQuery.validator.addMethod("starttimeisBeforeEndtime", function(value,
			element) {

		return starttimeisBeforeEndtime($('#start_date').val(), value);
	}, "Enrollment close date cannot be set to a past date.");

	$("#frm_schedule")
			.validate(
					{
						/* ignore: ':hidden:not(#enroll_date)', */
						// Specify the validation rules
						rules : {
							timezome : {
								required : true
							},
							enroll_date : {
								required : true,
								enrollDateisBeforeCurrentDate : true
							},
							start_date : {
								required : true,
								startDateisBeforeCurrentDate : true,
								startDateisbeforeEnrollDate : true
							},
							start_time : {
								required : true,
								starttimeisBeforeCurrenttime : true
							},
							end_time : {
								required : true,
								starttimeisBeforeEndtime : true
							}
						},
						// Specify the validation error Placement
						errorPlacement : function(error, element) {
							if (element.attr("name") === "enroll_date") {

								$('#enrollDate').parent().append(
										"<label for='enroll_date' generated='true' class='error'>"
												+ error[0].innerHTML
												+ "</label>");

							} else if (element.attr("name") === "start_date") {
								$('#startDate').parent().append(
										"<label for='start_date' generated='true' class='error'>"
												+ error[0].innerHTML
												+ "</label>");

							} else {
								error.insertAfter(element);

							}
						},
						// Specify the validation error messages
						messages : {
							timezome : {
								required : "Please enter the time zone."
							},
							enroll_date : {
								required : "Please enter the enrollment close date.",
								enrollDateisBeforeCurrentDate : "Enrollment close date cannot be set to a past date."
							},
							start_date : {
								required : "Please enter the course start date.",
								startDateisbeforeEnrollDate : "Start date cannot be earlier than Enrollment close date.",
								startDateisBeforeCurrentDate : "Course start date cannot be set to a past date."
							},
							start_time : {
								required : "Please enter the course start time.",
								starttimeisBeforeCurrenttime : "Course start time must be a future time."
							},
							end_time : {
								required : "Please enter the course end time.",
								starttimeisBeforeEndtime : "Course end time must be later than Course start time."
							}
						// end_time: {required : "Please enter the course end
						},

						submitHandler : function(form) {
							form.submit();
						},

						invalidHandler : function(event, validator) {
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


function gotoEdit(courseid) {
	window.location = "editCourseOverview?id=" + courseid.value;
}

function elementFadeOut(id) {
	setTimeout(function() {
		$(id).html('');
	}, 9000);
}

function convertTimeformat(str) {
	var hours = Number(str.match(/^(\d+)/)[1]);
	var minutes = Number(str.match(/:(\d+)/)[1]);
	var AMPM = str.match(/\s(.*)$/)[1];
	if (AMPM === "PM" && hours < 12) {
		hours = hours + 12;
	}

	if (AMPM === "AM" && hours === 12) {
		hours = hours - 12;
	}

	var sHours = hours.toString();
	var sMinutes = minutes.toString();
	if (hours < 10) {
		sHours = "0" + sHours;
	}

	if (minutes < 10) {
		sMinutes = "0" + sMinutes;
	}
	var convertTime = sHours + ":" + sMinutes;
	return convertTime;
}
