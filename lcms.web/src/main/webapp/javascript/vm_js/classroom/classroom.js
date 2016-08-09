function Enable_Disable_Delete_Btn(trg){

    if(trg.checked) {
		$(".del-session").removeClass("disabled");
	}
	else {
		$(".del-session").addClass("disabled");
	}


}
function SaveClassSetup (trg, event){
	$trg = $(trg).parent().parent().parent ().parent ();
	$trg1 = $(trg);
	if ($trg1.attr('class') === 'accordion-toggle'){
		$trg1 = $trg.find('.portlet .form').closest('.form');
    	$trg1 = $trg1.find('form');
		if (!$trg1.valid()){
			 event.preventDefault ();
			 event.stopPropagation ();
			 return false;
		}
		$trg = $trg.find('.portlet .form').closest('.form').find ('input.blue');
    	$trg.trigger( "click" );
	}
}

function addClassroom (){
	if(!$('#addClassform').valid()){
		$('#submitClass').removeAttr('data-dismiss', 'modal');
		return;
	}
	course_id 				= getUrlParameter ('id');
	var objClass 			= null;
	var varClassTitle 		= $("#classTitle").val();
	var rdoClassroomlimit 	= $("input[name=rdoClassroomlimit]:checked").val();
	var varClassSize 		= $("#classSize").val();
	var varTimezome 		= $("#classTimezome").val();
	var varEnrolCloseDate 	= $("#enrollmentCloseDate").val();
	var varLocation 		= $("#classLocation").val();
	var varClassInstructor 	= $("#classInstructor").val();
	APP.AJAX({
		url: 'saveClass',
		dataType: "text",
		type: "POST",
		cache: false,
		data: 'varCourseId=' + course_id + '&classInstructor=' + varClassInstructor+'&classTitle=' + varClassTitle + '&rdoClassroomlimit=' + rdoClassroomlimit + '&classSize=' + varClassSize + '&classTimezome=' + varTimezome + '&enrollmentCloseDate=' + varEnrolCloseDate + '&classLocation='+ varLocation,
	    async: false,
		success: function(response) {
			 objClass = $.parseJSON(response);
			 b_setup = true;
	    },
	    error : function (data) {
			console.log ("error" + eval (data));
		}
	});
	if (b_setup){
		var $divClone = $('div#__new__').clone();
		$divClone.html($divClone.html().replace(/__new__/g,objClass.syncClassId));
		$divClone.attr('id', objClass.syncClassId);
		$divClone.show ();
		$('div#classes_accordion').append($divClone);
		$('#submitClass').attr('data-dismiss', 'modal');
		// Class label setting on Class Bar
		$divClone.find("#class_panel_title_").text(' ' + varClassTitle);
		$divClone.find("#linkClassSchedule").click(function (event)
		{
			getSessions(objClass.syncClassId);
	    });
		$divClone.find("#linkClassSetup").attr("href", "#class_setup_" + objClass.syncClassId);
		$divClone.find("#linkClassSchedule").attr("href", "#class_schedule_" + objClass.syncClassId);
		var model = {
			"className": varClassTitle,
			"id":objClass.syncClassId,
			"maximumClassSize":varClassSize,
			"timeZoneId":varTimezome,
			"enrollmentCloseDate": varEnrolCloseDate,
			"locId": varLocation,
            "classInsId":varClassInstructor
		};
		$divClone.modelToDom360(model);
		if(varClassSize === "" || varClassSize === "0") {
			$divClone.find("#rdoUnlimited").attr('checked', 'checked');
			$divClone.find("#rdoUnlimited").click();
		} else {
			$divClone.find("#rdoLimited").attr('checked', 'checked');
			$divClone.find("#rdoLimited").click();
		}
		APP.DATE_PICKER("#closeDateDiv_" + objClass.syncClassId);
		$divClone.find("#linkClassSchedule").click();
		TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
	}
}

function resetManualSessionModal(){
	$("#add_manual_sessionId").val('');
	$('#manual_start_date_modal').datepicker('setValue', '');
	$('#manual_end_date_modal').datepicker('setValue', '');
	$("#addManuallyModal").modal('show');
	$("label.error").hide();
    $(".error").removeClass("error");
	$('#add_manual_sdate').val('');
	$('#add_manual_edate').val('');
	$("#add_manual_startTime").val($("#add_manual_startTime option:first").val());
	$("#add_manual_endTime").val($("#add_manual_endTime option:first").val());
}

function resetClassModal(){
	$("label.error").hide();
    $(".error").removeClass("error");
	$('#classTitle').val('');
	$('#classInstructor').val($("#classInstructor option:first").val());
	$('#enrollmentCloseDate').val('');
	$('#classSize').val('0');
	$("#classTimezome").val($("#classTimezome option:first").val());
	$("#classLocation").val($("#classTimezome option:first").val());
	$('input:radio[name=rdoClassroomlimit]')[0].checked = true;
	$('input:radio[name=rdoClassroomlimit]').closest('.form-group').next().removeClass('hide');
}

function resetSessionModal(){
	$('input:radio[name=add_sess_recurrence_d]')[0].checked = true;
	$('#start_by_date').datepicker('setValue', '');
	$('#end_by_date').datepicker('setValue', '');
	$('#add_sess_manual_daily_edate_days').val('0');
	$("label.error").hide();
    $(".error").removeClass("error");
	$('#add_sess_manual_daily_sdate').val('');
	$('#add_sess_manual_daily_edate').val('');
	$('#add_sess_number_days').val('0');
	$('#add_sess_number_week_days').val('1');
	$("#add_sess_start_time").val($("#add_sess_start_time option:first").val());
	$("#add_sess_end_time").val($("#add_sess_end_time option:first").val());
	$('input:radio[name=add_sess_rPattern]')[0].checked = true;
	$('#chkdSunday').attr('checked', false);
	$('#chkdMonday').attr('checked', false);
	$('#chkdTuesday').attr('checked', false);
	$('#chkdWednesday').attr('checked', false);
	$('#chkdThursday').attr('checked', false);
	$('#chkdFriday').attr('checked', false);
	$('#chkdSaturday').attr('checked', false);
	conditionalField(this,'manual');
}

function getSessions(classId){
	$('#schedule_table_'+classId).find('tbody').html('');
	$("#hidSelectedClassId").val(classId);
	$(".del-session").addClass("disabled");
	var objSession = null;
	getSessionsUrl = "/lcms/getSessions";
	APP.AJAX({
		  url: getSessionsUrl,
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'varClassId='+classId,
    	  async: false,
		  success: function(response) {
				 objSession = $.parseJSON(response);
		}
	});
	$table = $('#schedule_table_'+classId).find('tbody');
	if(objSession.length===0){
		$('#schdl_tbl_'+classId).addClass('hide');
		$('#addSessionLnk_'+classId).removeClass('hide');
		return;
    }else{
		$('#addSessionLnk_'+classId).addClass('hide');
		$('#schdl_tbl_'+classId).removeClass('hide');
    	for ( var i = 0; i < objSession.length;  i++){
		var strHTML =	" <tr id='tr_" + objSession[i].id + "'> "+
						"	<td><input class='checks' name='session_checkboxes' value='"+ objSession[i].id +"' type='checkbox' onclick='APP.CHECKBOX(this,false);;Enable_Disable_Delete_Btn(this);' ></td>" +
						"	<td><a href='javascript:;' onclick='manageSession(\"edit\",this)' class='anchor'><i class='icon-pencil'></i> Edit</a></td>" +
    					"	<td style='display:none' class='row-data' data-id = '" + objSession[i].id +
								"'  data-sessionKey = '" + objSession[i].sessionKey +
								"'  data-startDate = '" + objSession[i].startDate +
    							"'  data-endDate = '" + objSession[i].endDate +
    							"'  data-startTime = '" + objSession[i].startTime +
    							"'  data-endTime = '" + objSession[i].endTime +
								"' ></td>" +
						"	<td> " + objSession[i].sessionKey + " </td>"+
						"	<td> " + objSession[i].startDateDisplay + " </td>"+
						"	<td> " + objSession[i].endDateDisplay + " </td>"+
						"	<td> " + objSession[i].startTime + " </td>"+
						"	<td> " + objSession[i].endTime + " </td>"+
						" </tr> ";
			$table.append(strHTML);
		} // for end
	}// else end
}

function saveSession()
{
	if(!$('#frm_session').valid()) {
		$('#submitSession').removeAttr("data-dissmiss");
		return false;
	}
	course_id = getUrlParameter ('id');
	var objClass 			= null;
	var recurringPattren 	= $("input[name=add_sess_rPattern]:checked").val(); //$("#add_sess_rPattern").val();
	var vardailysession 	= $("input[name=add_sess_recurrence_d]:checked").val();
	var vardailysessiondays = $("#add_sess_number_days").val();
	var varsDate 		    = $("#add_sess_manual_daily_sdate").val();
	var varEndDate 	        = $("#add_sess_manual_daily_edate").val();
	var varEndDateDays 		= $("#add_sess_manual_daily_edate_days").val();
	var varStime 		    = $("#add_sess_start_time").val();
	var varEtime 	        = $("#add_sess_end_time").val();
	var varchkdSunday = $('#chkdSunday').attr('checked')?true:false;
	var varchkdMonday = $('#chkdMonday').attr('checked')?true:false;
	var varchkdTuesday = $('#chkdTuesday').attr('checked')?true:false;
	var varchkdWednesday = $('#chkdWednesday').attr('checked')?true:false;
	var varchkdThursday = $('#chkdThursday').attr('checked')?true:false;
	var varchkdFriday = $('#chkdFriday').attr('checked')?true:false;
	var varchkdSaturday = $('#chkdSaturday').attr('checked')?true:false;
 	var add_sess_number_week_days = $("#add_sess_number_week_days").val();
	var varselectedClassId	= $("#hidSelectedClassId").val();
	APP.AJAX({
		url: 'saveSession',
		dataType: "text",
		type: "POST",
		cache: false,
		data: 'varCourseId=' + course_id + '&classId=' + varselectedClassId + '&recurringPattren=' + recurringPattren + '&dailysession=' + vardailysession + '&dailysessiondays=' + vardailysessiondays + '&sDate=' + varsDate + '&endDate=' + varEndDate + '&endDatedays='+ varEndDateDays + '&stime=' + varStime + '&etime=' + varEtime + '&chkdSunday=' + varchkdSunday + '&chkdMonday=' + varchkdMonday + '&chkdTuesday=' + varchkdTuesday + '&chkdWednesday=' + varchkdWednesday +  '&chkdThursday=' + varchkdThursday +  '&chkdFriday=' + varchkdFriday + '&chkdSaturday=' + varchkdSaturday+ '&add_sess_number_week_days=' + add_sess_number_week_days ,
    	async: false,
		success: function(response) {
			 objClass = response;
			 if(objClass==="daterangeerror")
			 {
				TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.CLASSROOM_DATE_OUT_RANGE_ERROR});
				b_setup = false;
				return;
			 }else{
				b_setup = true;
			 }
    	},
    	error : function (data) {
    		console.log ("error" + eval (data));
		}
	});
	if (b_setup){
		$('#addSessionModal').modal('hide');
		getSessions(varselectedClassId);
		TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
	}
}



function saveManualSession()
{
	if(!$('#frm_manual_session').valid()) {
		return false;
	}
	course_id = getUrlParameter ('id');
	var varselectedClassId	= $("#hidSelectedClassId").val();
	var objSession 			= null;
	var varsDate 		    = $("#add_manual_sdate").val();
	var varEndDate 	        = $("#add_manual_edate").val();
	var varStime 		    = $("#add_manual_startTime").val();
	var varEtime 	        = $("#add_manual_endTime").val();
	var sessionId 	        = $("#add_manual_sessionId").val();
	var sessionKey 	        = $("#add_manual_sessionKey").val();
	APP.AJAX({
		url: 'saveManualSession',
		dataType: "text",
		type: "POST",
		cache: false,
		data: 'sessionId=' + sessionId + '&classId=' + varselectedClassId + '&sDate=' + varsDate + '&endDate=' + varEndDate + '&stime=' + varStime + '&etime=' + varEtime + '&sessionKey=' + sessionKey ,
    	async: false,
		success: function(response) {
			 objSession = $.parseJSON(response);
			 b_setup = true;
    	},
    	error : function (data) {
    		console.log ("error" + eval (data));
		}
	});
	if (b_setup){
		$('#addManuallyModal').modal('hide');
		getSessions(varselectedClassId);
		TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
	}
}


function manageSession(cnd,target){
	if(cnd === "add"){
                resetSessionModal();
		$("#addSessionModal").modal('show');
	}else if(cnd === "delete"){
		confirmDeleteSession();
	}else if(cnd === "edit"){
		resetManualSessionModal();
		var data = $(target).closest('tr').find('.row-data');
		$('#add_manual_sessionId').val(data.attrData360("id"));
		$('#manual_start_date_modal').datepicker('setValue', data.attrData360("startDate"));
		$('#manual_end_date_modal').datepicker('setValue', data.attrData360("endDate"));
		$("#add_manual_startTime").val(data.attrData360("startTime").toLowerCase());
		$("#add_manual_endTime").val(data.attrData360("endTime").toLowerCase());
		$("#add_manual_sessionKey").val(data.attrData360("sessionKey").toLowerCase());
	}
}


function confirmDeleteSession() {
	$trgModal = $("#confirmationModal");
	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';
    var msg = '<p>Are you sure you want to remove the class session(s)?</p>';
	var btns = '<button type="button" class="btn blue" onclick="delete_session(); " data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default"  data-dismiss="modal">NO</button>';
	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);
	$trgModal.modal('show');
}

function delete_session()
{
		var ids = $('input[name="session_checkboxes"]:checked');
		if(ids.length<=0){
			TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_ON_DELETE_SELECT});
			return false;
		}
		var commaseparateids="";
		for(var i=0; i<ids.length; i++){
			commaseparateids+=ids[i].value+",";
		}
		commaseparateids = commaseparateids.substr(0,commaseparateids.length-1);
		APP.AJAX({
		url: 'deleteSessions',
		type: "POST",
		cache: false,
		data:{ sessionIds: commaseparateids },
    	async: false,
		success: function(response) {
			for(var i=0;i<response.length; i++){
				$("#tr_"+response[i]).remove();
			}
			$('#successMsg').html("");
    		$('#successMsg').append("<div  class='messages'>"+
			"<div role='alert' class='alert alert-success alert-dismissible fade in'>"+
			"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
			WLCMS_LOCALIZED.SAVE_MESSAGE+"</div></div>");
			elementFadeOut("#successMsg");
			var varClassId = $("#hidSelectedClassId").val();
			$('.checker').prop('checked',false);
			$('.del-session').addClass("disabled");
			if ($("#schedule_table_" + varClassId + " > tbody > tr").length < 1) {
				$('#schdl_tbl_'+varClassId).addClass('hide');
				$('#addSessionLnk_'+varClassId).removeClass('hide');
			}
		},error: function(data){
			alert('Error');
		}
   });
}
function confimration_delete(id)
{

	$trgModal = $("#confirmationModal");
		//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';
    var msg = '<p>Are you sure you want to remove this class?</p>';
	var btns = "<button type='button' class='btn blue' onclick='delete_classroom("+id+");' data-dismiss='modal'>YES</button>"+
				"<button type='button' class='btn btn-default'  data-dismiss='modal'>NO</button>";

	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);
	$trgModal.modal('show');
}


function add_location_from_classroom()
{

	if(!$('#addMyLocationform').valid()){
		return;
	}
	APP.AJAX({
		url: 'addLocation',
		type: "POST",
		cache: false,
		data:{locationName: $("#locationname").val(), city: $("#locationcity").val(),
			zip: $("#locationzip").val(),country: $("#country").val(),
			state: $("#state").val(),phone: $("#locationphone").val(),
			desc: $("#locationdesc").val(),address: $("textarea[name='locationaddress']").val()},
    	async: false,
		success: function(response) {
			if(response.error) {
				$360.showMessage(response);
				$('#submitLocation').removeAttr("data-dismiss");
			} else {
				$('[name=classLocation]')
					.append($("<option></option>")
						.attr("value", response.id)
						.text(response.locationname));
				$('#addClassform').find('#classLocation').val(response.id);
				$('#submitLocation').attr('data-dismiss', 'modal');
				TopMessageBar.displayMessageTopBar({vType: 1, vMsg: WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut: true});
			}
		}
   });
}

function delete_classroom(id)
{
	APP.AJAX({
	url: 'deleteClassroom',
	type: "POST",
	cache: false,
	data:{ class_id: id },
    async: false,
	success: function(response) {
		$("#"+id).remove();
    	TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
	},error: function(data){
		alert('Error');
	}
});
}


//----------------Validation of Manual Session -------------------------
//------------------------start-----------------------------------------
$(document).ready(function (){
		jQuery.validator.addMethod("manual_endDateisbeforestartDate", function(value, element) {
                       return manual_endDateisbeforestartDate(value, element);
	        }, "End Date must be after Start Date.");
			 jQuery.validator.addMethod("manual_sessionStarttimeBeforeCurrenttime", function(value, element) {
                       return manual_sessionStarttimeBeforeCurrenttime(value, element);
	        }, "Session start time must be a future time.");
			jQuery.validator.addMethod("manual_sessionstarttimeisBeforeEndtime", function(value, element) {
                       return manual_sessionstarttimeisBeforeEndtime(value, element);
	        }, "Session end time must be later than session start time.");
			 var validator2 =  $("#frm_manual_session").validate({
             rules: {
               add_manual_sdate : {
								required : true,
								isFutureDate : true
						  },
               add_manual_edate : {
								required : true,
								manual_endDateisbeforestartDate : true
						  },
               add_manual_startTime : {
								required : true,
								manual_sessionStarttimeBeforeCurrenttime : true
						  },
               add_manual_endTime : {
								required : true,
								manual_sessionstarttimeisBeforeEndtime : true
						  }
				},
			messages: {
				  add_manual_sdate : {
								required : "Please enter the session start date."
							 },
				  add_manual_edate : {
								required : "Please enter the session end date."
							 },
				  add_manual_startTime : {
								required : "Please enter the session start time."
							 },
				  add_manual_endTime : {
								required : "Please enter the session end time."
    						 }
				}
			})
});
	var manual_endDateisbeforestartDate = function(value, element) {

		var startDate = $('#add_manual_sdate').val();
		var endDate = $('#add_manual_edate').val();
		if(typeof(endDate) !== "undefined")
		{
			var D1 = new Date(startDate.replace(/(\d{2})-(\d{2})-(\d{4})/,
					"$2/$1/$3")); // Year, Month, Date
			var D2 = new Date(endDate.replace(/(\d{2})-(\d{2})-(\d{4})/,
					"$2/$1/$3"));

			if (D1 <= D2) {
				return true;
			} else if (D1 > D2) {
				return false;
			}
		}

	};
	var manual_sessionStarttimeBeforeCurrenttime = function(value, element) {
		var strcourseStartDate = $('#add_manual_sdate').val();
		var strcourseStarttime = $('#add_manual_startTime').val();
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
			if (strcourseStarttime.indexOf("am") > 0) {
				hours = strcourseStarttime.substr(0, index);
				inthours = parseInt(hours);
				minutes = strcourseStarttime.substr(strcourseStarttime
						.indexOf(":") + 1, 2);
				intminutes = parseInt(minutes);
			} else if (strcourseStarttime.indexOf("pm") > 0) {
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
	var manual_sessionstarttimeisBeforeEndtime = function(enrolldate, startdate) {

		var strcourseStartDate = $('#add_manual_sdate').val();
		var strcourseStarttime = $('#add_manual_startTime').val();
		var strcourseEndDate = $('#add_manual_edate').val();
		var strcourseEndtime = $('#add_manual_endTime').val();
		var index = strcourseStarttime.indexOf(":");
		var endDateindex = strcourseEndtime.indexOf(":");
		var hours, minutes, inthours,endhours, endminutes, endinthours;
		var courseEndDate = new Date(strcourseEndDate);
		var courseStartDate = new Date(strcourseStartDate);
		var currentDate = new Date();
		var courseFixedEndDate = new Date(strcourseStartDate);

		// alert( strcourseStarttime.substr(
		courseEndDate.setHours(0);
		courseEndDate.setMinutes(0);
		courseEndDate.setSeconds(0);

		courseStartDate.setHours(0);
		courseStartDate.setMinutes(0);
		courseStartDate.setSeconds(0);
		if (index > 0) {
			if (strcourseStarttime.indexOf("am") > 0) {
				hours = strcourseStarttime.substr(0, index);
				inthours = parseInt(hours);
				if(inthours === 12) {
					inthours = 0;
				}
				minutes = strcourseStarttime.substr(strcourseStarttime.indexOf(":") + 1, 2);
				intminutes = parseInt(minutes);
			} else if (strcourseStarttime.indexOf("pm") > 0) {
				hours = strcourseStarttime.substr(0, index);
				inthours = parseInt(hours) + 12;
				minutes = strcourseStarttime.substr(strcourseStarttime.indexOf(":") + 1, 2);
				intminutes = parseInt(minutes);
			}
		}
		courseStartDate.setHours(inthours);
		courseStartDate.setMinutes(intminutes);
		courseStartDate.setSeconds(0);

		if (endDateindex > 0) {
			if (strcourseEndtime.indexOf("am") > 0) {
				endhours = strcourseEndtime.substr(0, endDateindex);
				endinthours = parseInt(endhours);
				endminutes = strcourseEndtime.substr(strcourseEndtime.indexOf(":") + 1, 2);
				endintminutes = parseInt(endminutes);
			} else if (strcourseEndtime.indexOf("pm") > 0) {
				endhours = strcourseEndtime.substr(0, endDateindex);
				endinthours = parseInt(endhours) + 12;
				endminutes = strcourseEndtime.substr(strcourseEndtime.indexOf(":") + 1, 2);
				endintminutes = parseInt(endminutes);
			}
		}
		courseEndDate.setHours(endinthours);
		courseEndDate.setMinutes(endintminutes);
		courseEndDate.setSeconds(0);
		if (Date.parse(courseEndDate) < Date.parse(courseStartDate)) {
			return false;
		} else if (Date.parse(courseEndDate) > Date.parse(courseStartDate)) {
			return true;
		}

	};
//----------------Validation of Manual Session -------------------------
