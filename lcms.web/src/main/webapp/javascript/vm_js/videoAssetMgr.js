$(document).ready(function(){

	//	BEGIN APP **************************************************************
	APP.PLACEHOLDER_FIX();
	//APP.MODE_INIT("#atr_mode");
	addSearchArea();
	oTb = APP.SEARCHGRID.init('#search_table', false, true, {pageSize:20});

	checked = "<h4 class='green-text icon-ok'></h4>";
	unchecked = "<h4 class='red-text icon-remove'></h4>";

	$('.dataTables_filter #seachText').keydown(function(event) {
        if (event.keyCode == 13) {
        	searchContentOwners();
        }
    });

	$("#updateVideoAssetModal").on('show.bs.modal', function (e) {
		showVideoPopup(this,e.relatedTarget);
	});


	disableSearchButton();
	updateFmsSpaceUsagePanel();
	 searchContentOwners();
	//	END APP ****************************************************************
});
function searchContentOwners(){

	var searchText = $(".dataTables_filter #seachText").filter(':visible').val();
	showProgressLoader("<div id='loader-label'></div>");
	APP.AJAX({
		url: 'searchVideoAssets',
		dataType: "text",
		type: "GET",
		cache: false,
		data: 'searchText=' + searchText,
		async: true,
		success: function(response) {
			if(response){
				var rows = $.parseJSON(response);
				oTb.fnClearTable();
				if(rows.length > 0){
					for(i = 0; i < rows.length; i++) {
						var row = rows[i];
						row[4] = "<span style='display:none'>" + row[4].padLeft(12) + "</span>" + plupload.formatSize(row[4]);
					}
					oTb.fnAddData(rows);
				}
			}else{
				$("#search_table tbody").html('');
			}
			hideProgressLoader();
		},
		error : function(response) {
			hideProgressLoader();
			TopMessageBar.displayMessageTopBar({
				vType : 2,
				vMsg : WLCMS_LOCALIZED.FAILED_MESSAGE,
				bFadeOut : true
			});
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
function showVideoPopup(modal,invoker){
	var uploader = $(modal).find('.upload-360')[0];
	var dataElement = invoker;
	APP.CACHE = {row:$(dataElement).closest('tr')[0]};
	$(uploader).uploader360().reset();

	//show player
	$(uploader).find('#btnRemoveVideo').removeAttr('disabled');
	$(uploader).find("#addVideoSlidPreview").removeClass("hide");
	$(uploader).find('#dropPreview').addClass('hide');
	$(uploader).find('.file-size').html(plupload.formatSize($(dataElement).attrData360("sizeInBytes")));
	$(uploader).find('.file-name').html($(dataElement).attrData360("videoLocation").fileName());
	$(uploader).find('.file-preview,.file-progress,.file-panel').removeClass('hide');
	$(uploader).find('td .btn-cancel').closest('td').addClass("hide");
	$(uploader).find('td .progress-bar:not(.show-always)').closest('td').addClass("hide");

	if(jwplayer) {
		var jwpAddVideoSlidePreview = jwplayer($(uploader).find("#addVideoSlidPreview")[0]);
		var folderPath = $(uploader).attrData360("fmsPreviewLoc");
		jwpAddVideoSlidePreview.setup({
			file: folderPath + $(dataElement).attrData360("videoLocation"),
			width :"100%"
		});
	}
}

function cancelVideoAssetUpdate(invoker){
	var form = $(invoker).closest('form');
	var uploader = $(form).find('.upload-360')[0];
	$(uploader).uploader360().reset();

	if(jwplayer) {
		var jwpAddVideoSlidePreview = jwplayer($(uploader).find("#addVideoSlidPreview")[0]);
		jwpAddVideoSlidePreview.remove();
	}

}

function updateVideoAsset(invoker) {
	var form = $(invoker).closest('form');
	if (!$(form).valid()) {
		return false;
	}

	var dataElement = $(APP.CACHE.row).find('.row-data')[0];
	$(form).find("#assetId").val($(dataElement).attrData360('assetId'));

	// grab all form data
	var formData = $(form).serialize();

	showProgressLoader("<div id='loader-label'></div>");
	APP.AJAX({
		url : 'videoAssetMgrPage/updateAsset',
		dataType : "text",
		type : "POST",
		cache : false,
		data : formData,
		success : function(response) {
			hideProgressLoader();
			response = $.parseJSON(response);
			$360.showMessage(response);
			if (!response.error) {
				$(form).closest('.modal').modal('hide');

				gFmsUsedSpace +=  response.data.sizeInBytes - parseFloat($(dataElement).attrData360('sizeInBytes'));
				updateFmsSpaceUsagePanel();

				var grid = $("#search_table").dataTable();
				grid.fnUpdate( 'Now', $(dataElement).attrData360("index")-1, 2,false);
				grid.fnUpdate("<span style='display:none'>" + String(response.data.sizeInBytes).padLeft(12) + "</span>" + plupload.formatSize(response.data.sizeInBytes), $(dataElement).attrData360("index")-1, 4,false);

				$(dataElement).attr('data-videoLocation',response.data.videoLocation);
				$(dataElement).attr('data-sizeInBytes',response.data.sizeInBytes);
				grid.fnUpdate( $('<div>').append($(dataElement).clone()).html(), $(dataElement).attrData360("index")-1, 1,false);
			}
		},
		error : function(response) {
			hideProgressLoader();
			TopMessageBar.displayMessageTopBar({
				vType : 2,
				vMsg : WLCMS_LOCALIZED.FAILED_MESSAGE,
				bFadeOut : true
			});
		}

	});
}


function deleteVideoAsset(invoker) {

	var dataElement = $(invoker).closest('tr').find('.row-data')[0];

	var action = function() {
		var formData = 'assetId=' + $(dataElement).attrData360('assetId');
		showProgressLoader("<div id='loader-label'></div>");
		APP.AJAX({
			url : 'videoAssetMgrPage/deleteAsset',
			dataType : "text",
			type : "POST",
			cache : false,
			data : formData,
			success : function(response) {
				hideProgressLoader();
				response = $.parseJSON(response);
				$360.showMessage(response);
				if (!response.error) {

					gFmsUsedSpace -=  parseFloat($(dataElement).attrData360('sizeInBytes'));
					updateFmsSpaceUsagePanel();

					var grid = $("#search_table").DataTable();
					var row = $(dataElement).closest('tr')[0];
					grid.row(row).remove().draw( false );
				}
			},
			error : function(response) {
				hideProgressLoader();
				TopMessageBar.displayMessageTopBar({
					vType : 2,
					vMsg : WLCMS_LOCALIZED.FAILED_MESSAGE,
					bFadeOut : true
				});
			}

		});
	};

	var trgModal = $("#confirmationModal")[0];
	var delelteModalContent = $("#deleteAssetConfirmationModalContents")[0];

	$(trgModal).find(".modal-title").html($(delelteModalContent).find(".modal-title").html());
	$(trgModal).find(".modal-body").html($(delelteModalContent).find(".modal-body").html());
	$(trgModal).find(".modal-footer").html($(delelteModalContent).find(".modal-footer").html());
	$(trgModal).find('#btnYes').click(action);
	$(trgModal).find('.video-name').html($(dataElement).attrData360('assetName'));
	$(trgModal).modal('show');

}


function addSearchArea(){
	TableManaged.tool_html = $("#searchPanelContents").html();
}

function disableSearchButton(){
	var searhButton = $('.dataTables_filter #searchButton')[0];
	$(searhButton).attr('disabled',true);
    $('.dataTables_filter #seachText').keyup(function(){
        if($(this).val().length !=0)
            $(searhButton).attr('disabled', false);
        else
            $(searhButton).attr('disabled',true);
    });
}

function updateFmsSpaceUsagePanel() {
	var usedSpacePercent = Math.round(gFmsUsedSpace/gFmsQuota*100);
	var availSpacePercent = 100 - usedSpacePercent;

	var panel = $("#fmsSpaceUsagePanel")[0];
	$(panel).find(".used-space-text").html(usedSpacePercent + "%");
	$(panel).find(".avail-space-text").html(availSpacePercent + "%");

	var usedSpaceBar = $(panel).find(".used-space-bar")[0];
	$(usedSpaceBar).css('width',usedSpacePercent+"%")
	$(usedSpaceBar).removeClass('progress-bar-success progress-bar-danger progress-bar-info progress-bar-warning');
	var colorClass = 'progress-bar-info';

	if(usedSpacePercent <= 15) {
		colorClass = 'progress-bar-success'
	} else if(usedSpacePercent > 90) {
		colorClass = 'progress-bar-danger'
	} else if(usedSpacePercent > 50) {
		colorClass = 'progress-bar-warning'
	}

	$(usedSpaceBar).addClass(colorClass);


}