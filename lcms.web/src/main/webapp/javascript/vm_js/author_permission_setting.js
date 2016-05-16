$(document).ready(function(){

	//	BEGIN APP **************************************************************
	APP.PLACEHOLDER_FIX();

	//APP.MODE_INIT("#atr_mode");
	addSearchArea();
	oTb = APP.SEARCHGRID.init('#search_table', false, true);

	checked = "<h4 class='green-text icon-ok'></h4>";
	unchecked = "<h4 class='red-text icon-remove'></h4>";

	$('#seachText').keydown(function(event) {
        if (event.keyCode == 13) {
        	searchContentOwners();
        }
    });

	$("#furtherDetailsModal").on('show.bs.modal', function (e) {
		var settings = APP.CACHE;

		if(settings.userPerm == 1){
			$("#userPermissionId").prop("checked", true);
		}else{
			$("#userPermissionId").prop("checked", false);
		}

		if(settings.npsRatingPerm == 1){
			$("#npsRatingPermId").prop("checked", true);
		}else{
			$("#npsRatingPermId").prop("checked", false);
		}

		if(settings.royaltySettingPerm == 1){
			$("#royaltySettingPermId").prop("checked", true);
		}else{
			$("#royaltySettingPermId").prop("checked", false);
		}

		if(settings.bulkUpload == 1){
			$("#bulkUploadId").prop("checked", true);
		}else{
			$("#bulkUploadId").prop("checked", false);
		}

		if(settings.viewWLCMSReport == 1){
			$("#viewReportPermId").prop("checked", true);
		}else{
			$("#viewReportPermId").prop("checked", false);
		}

		$("#authorGrpId").val(settings.authorGrpId);
		$("p.form-control-static").text(settings.firstName + " " + settings.lastName);
		$("#usernameModel").text(settings.username);
	});
	disableSearchButton();
	//	END APP ****************************************************************
});
function searchContentOwners(){

	var searchText = $("#seachText").val();
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	APP.AJAX({
		url: 'getAuthorsPerm',
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
function showProgressLoader(message){
	$("#loader-overlay").html(message);
	$("#bg-loader").show();
	$("#loader-overlay").show();
}
function hideProgressLoader(){
	$("#loader-overlay").fadeOut();
	$("#bg-loader").fadeOut();
}
function showDetail(_username, _npsRatingPerm, _royaltySettingPerm, _userPerm, _bulkUpload, _viewWLCMSReport, _authorGrpId, _firstName, _lastName, _rId){
	APP.CACHE = {username:_username, npsRatingPerm: _npsRatingPerm, royaltySettingPerm: _royaltySettingPerm, userPerm:_userPerm, bulkUpload: _bulkUpload, viewWLCMSReport: _viewWLCMSReport, authorGrpId: _authorGrpId, firstName: _firstName, lastName: _lastName, rId: _rId};
	$("#furtherDetailsModal").modal("show");
}
function addSearchArea(){
	TableManaged.tool_html = '<div class="dataTables_filter">' +
		'<button id="searchButton" name="searchButton" class="btn blue pull-right" onclick="searchContentOwners();"><i class="search-icon-white"></i> <span class="hide-md">' + WLCMS_LOCALIZED.ROYALTY_SETTINGS_LABEL_FIND_AUTHOR + '<span></button>'+
		'<input id="seachText" name="seachText" type="text" class="pull-right" maxlength="255" placeholder="' + WLCMS_LOCALIZED.ROYALTY_SETTINGS_LABEL_FIRST_OR_LAST_NAME + '">'+
		'</div>';
}
function saveSettings(elem){


	var npsRatingReviewPermValue = 2;
	var royaltySettingPermValue = 2;
	var userPermValue = 2;
	var bulkUploadPermValue = 2;
	var viewReportPermValue = 2;

	if($("#npsRatingPermId").is(':checked')){
		npsRatingReviewPermValue = 1;
	}

	if($("#royaltySettingPermId").is(':checked')){
		royaltySettingPermValue = 1;
	}

	if($("#userPermissionId").is(':checked')){
		userPermValue = 1;
	}

	if($("#bulkUploadId").is(':checked')){
		bulkUploadPermValue = 1;
	}

	if($("#viewReportPermId").is(':checked')){
		viewReportPermValue = 1;
	}

	var authorGrpId = $("#authorGrpId").val();
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	APP.AJAX({
		url: 'savePermissionSettings',
		dataType: "text",
		type: "POST",
		cache: false,
		data: 'authorGrpId=' + authorGrpId + '&userPermValue=' + userPermValue + '&npsRatingReviewPermValue=' + npsRatingReviewPermValue + '&royaltySettingPermValue=' + royaltySettingPermValue + '&bulkUploadPermValue=' + bulkUploadPermValue + '&viewReportPermValue=' + viewReportPermValue,
		async: true,
		success: function(response) {

			if(response){
				console.log("Successfully Saved");
				var settings = APP.CACHE;
				var hyp = "<a class='anchor' onclick=\"showDetail('"+settings.username+"',"+npsRatingReviewPermValue+","+royaltySettingPermValue+","+userPermValue+","+bulkUploadPermValue+"," +viewReportPermValue+"," +authorGrpId+", '"+settings.firstName+"', '"+settings.lastName+"', "+settings.rId+")\" href='javascript:;' data-toggle='modal' data-target='#furtherDetailsModal'>"+settings.firstName+" "+settings.lastName+"</a>";
				oTb.fnUpdate([settings.rId+1,settings.username, hyp, (userPermValue == 1) ? checked : unchecked, (bulkUploadPermValue == 1) ? checked : unchecked, (npsRatingReviewPermValue == 1) ? checked : unchecked, (royaltySettingPermValue == 1) ? checked : unchecked , (viewReportPermValue == 1) ? checked : unchecked], settings.rId);

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
		if($(this).val().length !=0)
	            $('#searchButton').attr('disabled', false);
	        else
	            $('#searchButton').attr('disabled',true);
	    })
}