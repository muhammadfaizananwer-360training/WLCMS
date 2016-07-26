$(document).ready(function(){

	//	BEGIN APP **************************************************************
	APP.PLACEHOLDER_FIX();
	//APP.MODE_INIT("#atr_mode");
	addSearchArea();
	oTb = APP.SEARCHGRID.init('#search_table', false, true);

	$('#seachText').keydown(function(event) {
        if (event.keyCode == 13) {
        	searchContentOwners();
        }
    });

	$("#furtherDetailsModal").on('show.bs.modal', function (e) {
		var settings = APP.CACHE;
		$("#online_amt").val(settings.onlinePerc);
		$("#classroom_amt").val(settings.classroomPerc);
		$("#webinar_amt").val(settings.webinarPerc);
		$("#cId").val(settings.cId);
		$("p.form-control-static").text(settings.firstName + " " + settings.lastName);
	});
	disableSearchButton();
	//	END APP ****************************************************************
	$.validator.addMethod("checkNegativeNumbers", function(value, element) {
		if(value < 0){
			return false;
		}else{
			return true;
		}
	},
	WLCMS_LOCALIZED.ROYALTY_MSG_POSITIVE_NUMBERS);

	initializeForm();
});
function on_type_click(trg, id) {
	$trg = $(trg);
	enable_or_disable($trg.prop("checked"), id);
}

$('#furtherDetailsModal').on('shown.bs.modal', function() {
	enable_or_disable(false, 1);
	enable_or_disable(false, 2);
	enable_or_disable(false, 3);
	uncheckAllCheckBox();
});

function searchContentOwners(){

	var searchText = $("#seachText").val();
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	APP.AJAX({
		url: 'getAllContentOwner',
		dataType: "text",
		type: "GET",
		cache: false,
		data: 'searchText=' + searchText,
		async: true,
		success: function(response) {
			if(response){
				var object = $.parseJSON(response);
				oTb.fnClearTable();
				if(object.length > 0){
					oTb.fnAddData(object);
				}
			}else{
				$("#search_table tbody").html('');
			}
			hideProgressLoader();
		}
   });
}

function enable_or_disable(cond, id) {
	if (cond == false) {
		var $elm;
		switch (id) {
		case 1:
			$elm = $("#online_amt");
			break;

		case 2:
			$elm = $("#classroom_amt");
			break;

		case 3:
			$elm = $("#webinar_amt");
			break;
		}
		$elm.attr("disabled", "disabled");
		$elm.parent().find("span.addon2").addClass("bg-gray-2 gray-text");
	} else {
		var $elm;
		switch (id) {
		case 1:
			$elm = $("#online_amt");
			break;

		case 2:
			$elm = $("#classroom_amt");
			break;

		case 3:
			$elm = $("#webinar_amt");
			break;
		}
		$elm.removeAttr("disabled", "disabled");
		$elm.parent().find("span.addon2").removeClass("bg-gray-2 gray-text");
	}
}
function showProgressLoader(message){
	$("#loader-overlay").html(message);
	$("#bg-loader").show();
	$("#loader-overlay").show();
}
function hideProgressLoader(){
	$("#loader-overlay").fadeOut();
	$("#bg-loader").fadeOut();
}
function showDetail(_username, _onlinePerc, _classroomPerc, _webinarPerc, _cId, _firstName, _lastName, _rId){
	APP.CACHE = {username:_username, onlinePerc:_onlinePerc, classroomPerc: _classroomPerc, webinarPerc:_webinarPerc, cId: _cId, firstName: _firstName, lastName: _lastName, rId: _rId};
	$("#furtherDetailsModal").modal("show");
}
function addSearchArea(){
	TableManaged.tool_html = '<div class="dataTables_filter">' +
		'<button id="searchButton" name="searchButton" class="btn blue pull-right" onclick="searchContentOwners();"><i class="search-icon-white"></i> <span class="hide-md">' + WLCMS_LOCALIZED.ROYALTY_SETTINGS_LABEL_FIND_AUTHOR + '<span></button>'+
		'<input id="seachText" name="seachText" type="text" class="pull-right" maxlength="255" placeholder="' + WLCMS_LOCALIZED.ROYALTY_SETTINGS_LABEL_FIRST_OR_LAST_NAME + '">'+
		'</div>';
}
function saveSettings(elem){

	initializeForm();
	var bValidOnline = $("#detailForm").validate().element("#online_amt");
	var bValidClassroom = $("#detailForm").validate().element("#classroom_amt");
	var bValidWebinar = $("#detailForm").validate().element("#webinar_amt");

	if(!bValidOnline || !bValidClassroom || !bValidWebinar){
		return false;
	}

	var cId = $("#cId").val();
	var onlinePerc = $("#online_amt").val();
	var classroomPerc = $("#classroom_amt").val();
	var webinarPerc = $("#webinar_amt").val();
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	APP.AJAX({
		url: 'saveSettings',
		dataType: "text",
		type: "POST",
		cache: false,
		data: 'cId=' + cId + '&onlinePerc=' + onlinePerc + '&classroomPerc=' + classroomPerc + '&webinarPerc=' + webinarPerc,
		async: true,
		success: function(response) {

			if(response){
				console.log("Successfully Saved");
				var settings = APP.CACHE;
				var hyp = "<a class='anchor' onclick=\"showDetail('"+settings.username+"',"+onlinePerc+","+classroomPerc+","+webinarPerc+","+settings.cId+", '"+settings.firstName+"', '"+settings.lastName+"', "+settings.rId+")\" href='javascript:;' data-toggle='modal' data-target='#furtherDetailsModal'>"+settings.firstName+" "+settings.lastName+"</a>";
				oTb.fnUpdate([settings.rId+1,settings.username, hyp, onlinePerc+"%", classroomPerc+"%", webinarPerc+"%"], settings.rId);

				TopMessageBar.displayMessageTopBar({});
			}else{
				console.log("Failure in Saving");
			}
			$("#furtherDetailsModal").modal("hide");
			hideProgressLoader();
		}
   });
}
function disableSearchButton(){

	 $('#searchButton').attr('disabled',true);
	    $('#seachText').keyup(function(){
	        if($(this).val().length !=0) {
				$('#searchButton').attr('disabled', false);
			} else {
	            $('#searchButton').attr('disabled',true);
			}
	    });
}
function uncheckAllCheckBox(){
	$( "#detailForm" ).validate().resetForm();

	$("#online_amt").removeData("previousValue");
	$("#online_amt").valid();

	$("#classroom_amt").removeData("previousValue");
	$("#classroom_amt").valid();

	$("#webinar_amt").removeData("previousValue");
	$("#webinar_amt").valid();

	$('#detailForm').find('input[type="checkbox"]:checked').removeAttr('checked');
}
function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode;
   if (charCode > 31 && (charCode < 48 || charCode > 57)) {
       return false;
   }
   return true;
}

function initializeForm(){
	$("#detailForm").validate({
	       rules: {
	    	   		online_amt: {
	    	   				checkNegativeNumbers: true,
	    	   				required:true,
	    	   				min: onlineRoyaltyDefaultValue,
		    	   			max:100

	    	   			},
	    	   		classroom_amt: {
	    	   				checkNegativeNumbers: true,
	    	   				required: true,
	    	   				min:classroomRoyaltyDefaultValue,
	    	   				max:100

	    	   			},
	    	   		webinar_amt: {
	    	   				checkNegativeNumbers: true,
	    	   				required: true,
	    	   				min:webinarRoyaltyDefaultValue,
	    	   				max:100
	    	   			}
		    },
		    messages: {
		    	 online_amt: {
				    		 min: WLCMS_LOCALIZED.ROYALTY_MSG_ONLINE_ROYALTY_NOT_LESS_DEFUALT_NUMBERS + onlineRoyaltyDefaultValue + '%.',
				    		 max: WLCMS_LOCALIZED.ROYALTY_MSG_ROYALTY_MORE_THAN_HUNDRED,
				    		 required:  WLCMS_LOCALIZED.ONLINE_ROYALTY_MISSING
		    			 },
		    	 classroom_amt: {
				    		 min: WLCMS_LOCALIZED.ROYALTY_MSG_CLASSROOM_ROYALTY_NOT_LESS_DEFUALT_NUMBERS + classroomRoyaltyDefaultValue + '%.',
				    		 max:WLCMS_LOCALIZED.ROYALTY_MSG_ROYALTY_MORE_THAN_HUNDRED,
				    		 required: WLCMS_LOCALIZED.CLASSROOM_ROYALTY_MISSING
						 },
		    	 webinar_amt: {
					    	 min: WLCMS_LOCALIZED.ROYALTY_MSG_WEBINAR_ROYALTY_NOT_LESS_DEFUALT_NUMBERS + webinarRoyaltyDefaultValue + '%.',
					    	 max:WLCMS_LOCALIZED.ROYALTY_MSG_ROYALTY_MORE_THAN_HUNDRED,
					    	 required: WLCMS_LOCALIZED.WEBINAR_ROYALTY_MISSING
						 }
		    },

			invalidHandler: function(event, validator) {
			},
			submitHandler: function(form)
		    {
		    },
			errorClass: 'error',
		    unhighlight: function (element, errorClass, validClass) {
		        $(element).removeClass(errorClass).addClass(validClass);
		    },
		    highlight: function (element, errorClass, validClass) {
		        $(element).addClass(errorClass).removeClass(validClass);
		    },
		    errorPlacement: function(error, element) {
		    	 error.insertBefore(element.parent("div"));
		    }
		});
}