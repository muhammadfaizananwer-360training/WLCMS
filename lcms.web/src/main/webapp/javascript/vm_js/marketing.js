var CourseAssetTypeEnum = {
		AUTHORIMAGE: "AuthorImage",
		COURSEIMAGE: "CourseImage",
		VIDEODEMO: "VIDEODEMO",
		COURSETHUMBNAILIMAGE : "CourseThumbnailImage"
};
var onChangeURL = false;
var curr_instance='';
var playerInstance;

$(document).ready(function(){
	APP.PLACEHOLDER_FIX();
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_3");

	$('#lnkMarketing a').addClass('active');

	$("#currentFormName").val("frm_marketing");



	APP.BODY_COLLAPSES("CLOSE");
	dnd('inst_info_img','0','image');
	dnd('course_cover_img','1','image');
	dnd('course_thumbnail_img','2','image');
	dnd('course_promo_video','3','video');
	jQuery('#video_demo').css('display','block');
	$( "#collapse_course_author_id" ).removeClass( "expand" ).addClass( "collapse" );
	$( "#collapse_course_author_portlet_body_id").removeAttr("style");

	jQuery.validator.addMethod("atleastOneRow", function(value, element) {
        var fileNameLength = $('#icc_list_0 tr:eq(0) > td:eq(0)').text().length;
		return (fileNameLength > 0);

    }, "There should be at least one row.");
	$.validator.addMethod('DecimalWith2Precision', function(value, element) {
		return this.optional(element) || /^\d+(\.\d{0,2})?$/.test(value);
		}, "Please enter a correct number");
  $("#frm_marketing").validate({
        // Specify the validation rules
        rules: {
        	AuthorBackground: "required",
			authorImageTableField: { atleastOneRow: true },
			duration: "DecimalWith2Precision",
			classInstructor: "required"
        },
        // Specify the validation error messages
        messages: {
        	AuthorBackground: "Please add background information for the author or presenter of this course. This information will display on the store website to potential customers.<br>",
			authorImageTableField: "Please upload an 86px x 86px image of the author or presenter of this course. The image will display along with the background information, on the store website.",
			duration: "Please enter a numeric value and only 2 digits after decimal is allowed",
			classInstructor: "Please select an instructor"
        },
        submitHandler: function(form)
        {
			form.submit();
        },
	errorPlacement: function(error, element) {

		error.insertAfter(element);
    },

		errorClass: 'error',
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass(errorClass).addClass(validClass);

        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass);
        }
    });

	$("#frmAuthorImage").validate({
        // Specify the validation rules
        rules: {
        	AuthorImageName: "required"
        },
        // Specify the validation error messages
        messages: {
        	AuthorImageName: "Please enter your asset name here"
        },
        submitHandler: function(form) {
			form.submit();
        },
		invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();
		},errorPlacement: function (error, element) {
		    var name = $(element).attr("name");
		    error.appendTo($("#" + name ).parent());
        }
    });

	$.validator.addMethod("validateExtensionForVideoAsset",
			function(value, element) {
				var ext = value.split('.').pop().toLowerCase();

				if($("#cboAssetTypeVisual").val()=="MovieClip" && $.inArray(ext, ['mp4']) != -1)
					return true;
				else
					return false;
			},
		"Please provide a file with a valid file type");


	    $("#frmUploadVideoDemoAsset").validate({

	        // Specify the validation rules
	        rules: {
	        	videoDemoAssetName: "required"

	        },
	        // Specify the validation error messages
	        messages: {
	        	videoDemoAssetName: "Please enter asset name here."

	       },

	        submitHandler: function(form)
	        {
				form.submit();
	        },

			invalidHandler: function(event, validator) {
				var errors = validator.numberOfInvalids();
				if (errors) {
					TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
				} else {
					$("#msgdiv").html('');
				}
				elementFadeOut("#msgdiv");
			},

	        ignore:[]
	    });
	EnableDisable  ();
});

var checkChangesEnabled = true;
var destination;

window.onbeforeunload = function (e) {
    var e = e || window.event;
    var isDirty = false;
    for (var i in CKEDITOR.instances) {
        if(CKEDITOR.instances[i].checkDirty()) {
            isDirty = true;
        }
    }

    if (isDirty == true && checkChangesEnabled) {



















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



function SaveMarketingOption () {
	isDirty = false;

	for (var i in CKEDITOR.instances) {
		if(CKEDITOR.instances[i].checkDirty()) {
			isDirty = true;
		}
	}
	onChangeURL  = true;
	if (isDirty) {
		var title = '<i class="icon-exclamation-sign"></i> Please Confirm';
		msg = '<p>It looks like you have unsaved work on this page. Would you like to save the work before continuing? </p>';
		var btns = "<button type='button' class='btn blue' onclick='SaveMarketing ()' data-dismiss='modal'>Yes</button>"+
		"<button type='button' class='btn btn-default' onclick='exitWithoutSaving ()' data-dismiss='modal'>No</button>";

		//	END TITLE, MESSAGE AND BUTTONS
		$trgModal = $("#confirmationModal");
		$trgModal.find(".modal-title").html(title);
		$trgModal.find(".modal-body").html(msg);
		$trgModal.find(".modal-footer").html(btns);

		$trgModal.modal('show');
	}else {
		link = APP.CACHE;
		APP.CACHE = '';
		onChangeURL = false;
		window.location.assign(link);
	}
}

function exitWithoutSaving () {

	link = APP.CACHE;
	APP.CACHE ='';
	window.location.assign(decodeURIComponent(link));
}

function cancelImageUpload () {

	$("#AuthorImageName").val('');
	$("#AuthorImageKeywords").tagsinput('removeAll');
	$("#AuthorImageDescription").val ('');

	$("#videoDemoAssetName").val('');
	$("#videoAssetKeywords").tagsinput('removeAll');
	$("#videoAssetDescription").val ('');








	$("label.error").hide();
	$(".error").removeClass("error");
	$('.fileupload').fileupload('reset');

	$('#msgdiv').html ('');
	return false;
}

function elementFadeOut(id)
{
	setTimeout(function(){
        $(id).html('');
    },9000);
}

function SaveMarketing () {

	var cType = getParameterByName('cType');

	if(cType != null && (cType == WLCMS_CONSTANTS_COURSE_TYPE.CLASSROOM_COURSE || cType == WLCMS_CONSTANTS_COURSE_TYPE.WEBINAR_COURSE)){
		var editor = CKEDITOR.instances.AuthorBackground;
		var vName = editor.getData();
		vName = $(vName).html ();
		var vNameEscaped = escape(vName);

		editor.updateElement();
		var authorBackGroundProvided = $("#frm_marketing").validate().element("#AuthorBackground");
		var authorImageProvided = $("#frm_marketing").validate().element("#authorImageTableField");

		if (!authorBackGroundProvided || !authorImageProvided  ) {
			TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
			return;
		}
        if(cType=='5' && !$("#frm_marketing").validate().element("#classInstructor")){
            TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
            return;

        }
	}

	course_id 		 =  getParameterByName('id');
	authorBackground = encodeURIComponent(CKEDITOR.instances.AuthorBackground.document.getBody().getHtml());
	intendedAudience = encodeURIComponent(CKEDITOR.instances.IntendedAudience.document.getBody().getHtml());
	preReqa 		 = encodeURIComponent(CKEDITOR.instances.PreReqs.document.getBody().getHtml());
	additionalMaterials = encodeURIComponent(CKEDITOR.instances.AdditionalMaterials.document.getBody().getHtml());
	eocInstructions  = encodeURIComponent(CKEDITOR.instances.EocInstructions.document.getBody().getHtml());
	duration = $("#frm_marketing").find("#duration").val();
	classInstructor =$("#classInstructor").val();















	APP.AJAX({
		  url: 'updateMarketing',
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'id='+ course_id +  '&authorBackground='  + authorBackground + '&classInstructorId='+classInstructor + '&intendedAudience=' +  intendedAudience + '&preReqa=' +  preReqa + '&additionalMaterials=' +  additionalMaterials + '&eocInstructions=' + eocInstructions + '&duration=' +duration,
		  async: false,
		  success: function(response) {

			  TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
		}
	 });

	for (var i in CKEDITOR.instances) {
		CKEDITOR.instances[i].resetDirty();
	}

	if (onChangeURL) {
		link = APP.CACHE;
		APP.CACHE = '';
		onChangeURL = false;
		window.location.assign(link)
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

function uploadAuthorImage(form, event){
	if (!$(form).valid()) {
		return false;
	}






	$('#frmAuthorImage').find("#id").val( getParameterByName ('id')) ;
	$('#frmAuthorImage').find("#assetUploadType").val ($("#assetUploadType").val());

	//disable the default form submission
	event.preventDefault();

	//grab all form data
	var formData = $(form).serialize();

	showProgressLoader("<div id='loader-label'>Uploading...</div>");


	APP.AJAX({
		  url: 'uploadAuthorImage',
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:formData,
		  async: false,
		  success: function(response) {

			  TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
			  $("#AuthorImageName").val('');
			$("#AuthorImageKeywords").tagsinput('removeAll');
			$("#AuthorImageDescription").val ('');








			$('#addAssetModal').modal('hide');


			// Call function to list the asset
			listMarketingAssets ();
			hideProgressLoader();
		}
	 });



}

function OnAuthorImageUpload () {
	var form = $("#frmAuthorImage")[0];
	$(form).find('.upload-360').uploader360().reset();
	$("#addAuthorImageUpload").find ('#upload-asset-label').html('Upload and Attach Image for Author');
	$("#assetUploadType").val(CourseAssetTypeEnum.AUTHORIMAGE);
	var validator = $("#addAuthorImageUpload").validate();
	validator.resetForm();
	$("#AuthorImageName").removeClass("error");
	$("#AuthorImageName").val('');
}

function OnCourseImageUpload () {
	var form = $("#frmAuthorImage")[0];
	$(form).find('.upload-360').uploader360().reset();
	$("#assetUploadType").val(CourseAssetTypeEnum.COURSEIMAGE);
	$("#addAuthorImageUpload").find ('#upload-asset-label').html('Upload and Attach Image for Course');
	var validator = $("#addAuthorImageUpload").validate();
	validator.resetForm();
	$("#AuthorImageName").removeClass("error");
}

function OnAuthorImageSearch () {
	$("#assetUploadType").val(CourseAssetTypeEnum.AUTHORIMAGE);
	$('#searchMarketingAssetModal').find("#searchMarketingabel").text ('Find Asset and Attach for Author');
}
function OnMarketingVideoSearch () {
	$("#assetUploadType").val(CourseAssetTypeEnum.VIDEODEMO);
	$('#searchVAssetModal').find("#asset-v-search-label").text ('Find Video Streaming Asset and Attach to Course');
}
// search VA Assets
function searchVAAsset() {
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	// manual validation of AJAX form

	var bSearch = false;

	$txtAssetSearch = $("div#searchVAssetModal").find ('#txtVideoSearchTerm');

	var valID;
	valID =  $('[name=g1]:checked').val();


	if ($txtAssetSearch.val().length < 3) {
		alert("Please provide at least 3 characters for your search criteria.");
	}
	else {
		$("#btsearchVAAsset").button('loading');
		APP.AJAX({
			url : 'getSlideAssetSearch?assetSearchTerm=' + $txtAssetSearch.val() +'&VAAssetsSearch=true',
			type : 'GET',
			async: false,
			success : function(data) {
				bSearch = true;
				asset_object = eval(data);
			},
			error : function (data) {

			}
		});

		if (bSearch) {
			$( "#lstAsset").html('');
			var r =[];
			var row_id;
			var d;
			var len = asset_object.length;

			//WLCMS-232
			if( len <= 0)
				r.push('<tr><td colspan=\'8\'>No search result(s) found.</td></tr>');

			for(var i = 0; i < len; i++)
			{
				d = asset_object[i];
				row_id = d.id + '_'  + d.assetversion_id;
			    r.push('<tr><td id=\"');
				r.push(row_id);
				r.push( '\"><input type=\"radio\" name=\"g3\" value=\"');
				r.push(row_id);
				r.push('\"/></td><td id=\'name\'>');
				r.push(d.name);
				r.push( '</td><td id=\'type\'>');
				r.push(d.assettype);
				r.push( '</td><td id=\'dimension\'>');
				r.push(d.height);
				r.push(' x ');
				r.push(d.width);
				r.push( '</td><td>N/A</td><td id=\'version\'>');
				r.push(d.version);
				r.push( '</td><td id=\'description\'>');
				r.push(d.description);
				r.push( '</td><td id=\'image\'><a href=\'');
				r.push(d.location);
				r.push('\' target=\'_blank\'');
				if ( d.assettype == 'Image'){
						r.push('><img width=\'50px\' src=\'');
						r.push(d.location);
						r.push('\'></img></a></td></tr>' );
				}
				else if ( d.assettype == 'Flash Object'){
						r.push('><img src=\'theme/executive/img/icons/swf.png\'></img></a></td></tr>');
				}
				else if ( d.assettype == 'Movie Clip'){
						r.push(' class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td></tr>');
				}else if ( d.assettype == 'VSC'){
						r.push(' class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td></tr>');
				}
			} // for asset_object.length
			$("#lstVideoBody").html(r.join(''));
		} // bSearch
	}
	hideProgressLoader();

	$("#btsearchVAAsset").button('reset');

	$('#searchAssetModal').modal('hide');

}


function getSlideVisualAssetTable (varSlideId, assetType) {

	if ($('#tblVideoTopDiv_'+ varSlideId).find(':first').find('.tools').find('#videoLink').is ('.expand')) {

		return;
	}

	bAssetData = false;

	APP.AJAX({
		 url:  'getAssetBySlide',
		 dataType: "text",
		 type: "POST",
		 cache: false,
		 data:'varSlideId='+ varSlideId + '&varAssetType='+ assetType,
		 async: false,
		 success: function(response) {
			Slidedata = $.parseJSON(response);
			bAssetData= true;
		}
	});
	if (bAssetData) {
		var html = '';
		var durationOrDimesion='';
		for(var i = 0; i < Slidedata.length; i++){
					durationOrDimesion = assetType==1 ?  Slidedata[i].width + ' x ' + Slidedata[i].height : secondsToTime(Slidedata[i].duration);
					html += '<tr id='+ Slidedata[i].versionId +  ' >'+
					'<td>' + Slidedata[i].name + '</td>' +
					'<td>' + Slidedata[i].assettype + '</td>' +
					'<td>' + durationOrDimesion + '</td>' +
					'<td> N/A </td>' +
					'<td>' + Slidedata[i].version + '</td>' +
					'<td>' + Slidedata[i].description + '</td>' +
			  '</tr>';
		}

		if(html.length > 0) {
			$("#video-icon_"+varSlideId).find("i").addClass("green");
			$('#tblDivVideo_'+varSlideId).find('[data-target=#searchVAssetModal]').attr("disabled","disabled");
			$('#tblDivVideo_'+varSlideId).find('[data-target=#addVAssetModal]').attr("disabled","disabled");
			$('#tblDivVideo_'+varSlideId).find('.icon-minus').closest('a').removeAttr("disabled");
		}
		if(html.length == 0){
			$("#video-icon_"+varSlideId).find("i").removeClass("green");
			$('#tblDivVideo_'+varSlideId).find('[data-target=#searchVAssetModal]').removeAttr("disabled");
			$('#tblDivVideo_'+varSlideId).find('[data-target=#addVAssetModal]').removeAttr("disabled");
			$('#tblDivVideo_'+varSlideId).find('.icon-minus').closest('a').attr("disabled","disabled");
		}
		$('#tblDivVideo_' + varSlideId + ' tbody').empty();
		$('#tblDivVideo_' + varSlideId + ' tbody').html(html);
	}

}
//end search VA Assets
//acceptVAAsset
function acceptVAAsset (){

	var valID;
	valID =  $('[name=g1]:checked').val();
	if (valID.length > 0 )
	{
		var arr = valID.split('_');

		var scene_id = $("#hidId").val();
		asset_id   = arr[0];
		assetVersion_id = arr[1];

		APP.AJAX({
			url : 'acceptAssetSearch',
			data: { scene_id: scene_id, assetVersion_id: assetVersion_id, asset_id : asset_id},
			type : 'POST',
			async: false,
			success : function(data) {
				getSlideVisualAssetTable (scene_id, 2)
			},
			error : function (data) {

			}
		});
	}
	// close dialog box

	$("div#searchVAssetModal").find ('#txtVASearchTerm').val ('');
	$( "#lstVABody").html ('');
	$('#searchVAssetModal').modal('hide');


	// Success Message
	TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
}
function acceptDialogBox (assetType) {
	var slideID = $("#hidId").val();
	//	BEGIN TITLE, MESSAGE AND BUTTONS
	// $("#myTable > tbody > tr").length
	var title = '<i class="icon-exclamation-sign"></i> Attention';
	var msg = '<p>This template supports only one [Piece of Audio]. Adding a new [Piece of Audio] will remove the one currently attached. Do you want to continue?</p>';
	switch (assetType) {
	case 'dynamicModal visual':





		TEMPLATE.RETURN_DYNAMIC_MODAL($('[name=g1]:checked').parent().siblings('#name').html(),
		$('[name=g1]:checked').parent().siblings('#version').html(),
		$('[name=g1]:checked').parent().siblings('#image').find('a').attr("href")
		);
			return;
		break;
	case 'dynamicModal audio':



		TEMPLATE.RETURN_DYNAMIC_MODAL($('[name=audioVal]:checked').parent().siblings('#name').html(),
		$('[name=audioVal]:checked').parent().siblings('#version').html(),
		$('[name=audioVal]:checked').parent().siblings('#link').find('a').attr("href"));
			return;
		break;

	case 'audio':
		if ($("#tblAudioAsset_" + slideID+ " > tbody > tr").length < 1) {
			acceptAudioAssetSearch();
			return;
		}
		msg = '<p>This template supports only one [Piece of Audio]. Adding a new [Piece of Audio] will remove the one currently attached. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="acceptAudioAssetSearch()" data-dismiss="modal">YES</button>'+
		'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';

		break;
	case 'visual':
		if ($("#tblVisualAsset_" + slideID + " > tbody > tr").length  < 1)	{
			acceptAssetSearch();
			return;
		}
		msg = '<p>This template supports only one [Piece of visual]. Adding a new [Piece of visual] will remove the one currently attached. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="acceptAssetSearch()" data-dismiss="modal">YES</button>'+
		'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';
		break;
	case 'video':
		if ($("#tblVideo_" + slideID+ " > tbody > tr").length < 1) {
			acceptVAAsset();
			return;
		}
		msg = '<p>This template supports only one [Piece of video]. Adding a new [Piece of video] will remove the one currently attached. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="acceptVAAsset()" data-dismiss="modal">YES</button>'+
		'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';
		break;
	case 'SupportMaterial':
		if ($("#tblVideo_" + slideID+ " > tbody > tr").length < 1) {
			acceptSupportMaterialAsset();
			return;
		}
		break;
	}

	//	END TITLE, MESSAGE AND BUTTONS
	$trgModal = $("#confirmationModal");
	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');


}
function searchVideoAsset() {
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	// manual validation of AJAX form

	var bSearch = false;

	$txtAssetSearch = $("div#searchMarketingAssetVideoModal").find ('#txtMarketingSearchTerm');

	if ($txtAssetSearch.val().length < 3) {
		alert("Please provide at least 3 characters for your search criteria.");
	}
	else {
		$("#btsearchMarketingAsset").button('loading');
		APP.AJAX({
			url : 'getSlideAssetSearch?assetSearchTerm=' + $txtAssetSearch.val() +'&VAAssetsSearch=true',
			type : 'GET',
			async: false,
			success : function(data) {
				bSearch = true;
				asset_object = eval(data);
			},
			error : function (data) {

			}
		});

		if (bSearch) {
			$( "#lstAsset").html('');
			var r =[];
			var row_id;
			var d;
			var len = asset_object.length;

			//WLCMS-232
			if( len <= 0)
				r.push('<tr><td colspan=\'8\'>No search result(s) found.</td></tr>');

			for(var i = 0; i < len; i++)
			{
				d = asset_object[i];
				row_id = d.id + '_'  + d.assetversion_id;
			    r.push('<tr><td id=\"');
				r.push(row_id);
				r.push( '\"><input type=\"radio\" name=\"g1\" value=\"');
				r.push(row_id);
				r.push('\"/></td><td id=\'name\'>');
				r.push(d.name);
				r.push( '</td><td id=\'type\'>');
				r.push(d.assettype);
				r.push( '</td><td id=\'dimension\'>');
				r.push(d.height);
				r.push(' x ');
				r.push(d.width);
				r.push( '</td><td>N/A</td><td id=\'version\'>');
				r.push(d.version);
				r.push( '</td><td id=\'description\'>');
				r.push(d.description);
				r.push( '</td><td id=\'image\'><a href=\'');
				r.push(d.location);
				r.push('\' target=\'_blank\'');
				if ( d.assettype == 'Image'){
						r.push('><img width=\'50px\' src=\'');
						r.push(d.location);
						r.push('\'></img></a></td></tr>' );
				}
				else if ( d.assettype == 'Flash Object'){
						r.push('><img src=\'theme/executive/img/icons/swf.png\'></img></a></td></tr>');
				}
				else if ( d.assettype == 'Movie Clip'){
						r.push(' class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td></tr>');
				}else if ( d.assettype == 'VSC'){
						r.push(' class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td></tr>');
				}
			} // for asset_object.length
			$("#lstMarketingAssetVideoBody").html(r.join(''));
		} // bSearch
	}
	hideProgressLoader();
	$txtAssetSearch = $("div#searchMarketingAssetVideoModal").find ('#txtMarketingSearchTerm').val('');
	$("#btsearchVAAsset").button('reset');

	$('#searchAssetModal').modal('hide');

}
function searchMarketingVideoAsset() {


	// manual validation of APP.AJAX form

	var bSearch = false;

	$txtAssetSearch = $("div#searchMarketingAssetVideoModal").find ('#txtMarketingSearchTerm');

	if ($txtAssetSearch.val().length < 3) {
		alert("Please provide at least 3 characters for your search criteria.");
	}
	else {

		showProgressLoader("<div id='loader-label'>Loading...</div>");

		$("#btsearchMarketingAsset").button('loading');
		APP.AJAX({
			url : 'getMarketingAssetSearch?assetSearchTerm=' + $txtAssetSearch.val() +'&MarketingAssetSearch=' + $("#assetUploadType").val(),
			type : 'GET',
			async: false,
			success : function(data) {
				bSearch = true;
				asset_object = eval(data);
			},
			error : function (data) {

			}
		});

		if (bSearch) {
			$( "#lstAsset").html('');
			var r =[];
			var row_id;
			var d;
			var len = asset_object.length;

			//WLCMS-232
			if( len <= 0)
				r.push('<tr><td colspan=\'8\'>No search result(s) found.</td></tr>');

			for(var i = 0; i < len; i++)
			{
				d = asset_object[i];
				row_id = d.id + '_'  + d.assetversion_id;
			    r.push('<tr><td id=\"');
				r.push(row_id);
				r.push( '\"><input type=\"radio\" name=\"g1\" value=\"');
				r.push(row_id);
				r.push('\"/></td><td id=\'name\'>');
				r.push(d.name);
				r.push( '</td><td id=\'type\'>');
				r.push(d.assettype);
				r.push( '</td><td id=\'dimension\'>');
				r.push(d.height);
				r.push(' x ');
				r.push(d.width);
				r.push( '</td><td>N/A</td><td id=\'version\'>');
				r.push(d.version);
				r.push( '</td><td id=\'description\'>');
				r.push(d.description);
				r.push( '</td><td id=\'image\'><a href=\'');
				r.push(d.location);
				r.push('\' target=\'_blank\'');
				r.push('><img width=\'50px\' src=\'');
				r.push(d.location);
				r.push('\'></img></a></td></tr>' );

			} // for asset_object.length
			$("#lstMarketingAssetVideoBody").html(r.join(''));
		} // bSearch
	}
	$("div#searchMarketingAssetVideoModal").find ('#txtMarketingSearchTerm').val('');
	$("#btsearchMarketingAsset").button('reset');


setTimeout(function() {
      // Do something after 5 seconds
	  	hideProgressLoader();
}, 1000);
}

function cancelVideoSearchUpload () {


	$("label.error").hide();
	$(".error").removeClass("error");
	$("#txtVideoSearchTerm").val('');
	$("#lstVideoBody").html('');
	$('#msgdiv').html ('');
	return false;
}


function cancelVideoUpload () {

	var form = $("#frmUploadVideoDemoAsset")[0];
	$(form).find(".upload-360").uploader360().reset();
	$("label.error").hide();
	$(".error").removeClass("error");
	$("#videoDemoAssetName").val('');
	$("#videoAssetKeywords").tagsinput('removeAll');
	$("#videoAssetDescription").val ('');
	$("span[id=filePreview]").empty();
	$("span[id=fileProgress]").empty();

	$('#videoFilePath').val ('');
	$('.uploaded-file').fileupload('reset');

	$('#msgdiv').html ('');
	return false;
}

function saveVideo ()
{
	var form = $('#frmUploadVideoDemoAsset')[0];
	if (!$(form).valid()) {
		return false;
	}
	course_id = getParameterByName ('id');
	var file_path = $('#videoFilePath').val();
	var asset_name = $('#videoDemoAssetName').val();
	var asset_desc = $("textarea[name='videoAssetDescription']").val();
	var audioAssetKeywords = $('#videoAssetKeywords').val();
	showProgressLoader("<div id='loader-label'>Uploading...</div>");
	APP.AJAX({
		url : 'acceptMarketingAssetVideoAdd',
		data: { id: course_id, filePath: file_path, videoAssetName : asset_name, audioAssetDescription: asset_desc,audioAssetKeywords: audioAssetKeywords },
		type : 'POST',
		async: false,
		success : function(data) {
			listMarketingVideoAssets ();
		},
		error : function (data) {

		}
	});
	hideProgressLoader();
	$("div#addVAssetModal").find ('#videoDemoAssetName').val ('');
	$("div#addVAssetModal").find ('#videoAssetKeywords').val ('');
	$("div#addVAssetModal").find ('#videoAssetDescription').val ('');
	$("div#addVAssetModal").find ('#videoFilePath').val ('');

	$('#addVAssetModal').modal('hide');

	// Success Message
	$('#successMsg').html("");
	$('#successMsg').append("<div  class='messages'>"+
		"<div role='alert' class='alert alert-success alert-dismissible fade in'>"+
		"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
		"<strong>Success! </strong>It's been saved.</div><script>elementFadeOut('.messages');</script></div>");
	$('#successMsg').show ();
}
function acceptMarketingVideoDialogBox () {

	var valID;
	valID =  $('[name=g3]:checked').val();

	if(valID == undefined){
		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_MARKETING_SEARCH});
		return;
	}

	course_id = getParameterByName ('id');
	var valID;
	valID =  $('[name=g3]:checked').val();
	if (valID.length > 0 )
	{
		var arr = valID.split('_');
		asset_id   = arr[0];
		assetVersion_id = arr[1];
		assetType = $("#assetUploadType").val();

		APP.AJAX({
			url : 'acceptMarketingAssetVideoSearch',
			data: { id: course_id, assetVersion_id: assetVersion_id, asset_id : asset_id, assetType: assetType },
			type : 'POST',
			async: false,
			success : function(data) {
				listMarketingVideoAssets ();

			},
			error : function (data) {

			}
		});

	}
	// close dialog box
	$("div#searchVAssetModal").find ('#txtMarketingSearchTerm').val ('');
	$( "#lstVideoBody").html ('');
	$('#searchVAssetModal').modal('hide');

	// Success Message
	$('#successMsg').html("");
	$('#successMsg').append("<div  class='messages'>"+
		"<div role='alert' class='alert alert-success alert-dismissible fade in'>"+
		"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
		"<strong>Success! </strong>It's been saved.</div><script>elementFadeOut('.messages');</script></div>");
	$('#successMsg').show ();
}


function listMarketingVideoAssets (){
	course_id = getParameterByName ('id');
	assetType = $("#assetUploadType").val();
	var r =[];
	var d;
	bValidData = false;

	APP.AJAX({
		url : 'listMarketingVideoAsset',
		data: { id: course_id },
		type : 'GET',
		async: false,
		success : function(data) {
			// Work on getting the listing going
			rowData = '';
			d = data;
			rowData = '<tr>';
			rowData +=	'<td class="Name">' + d.name + '</td>';
			rowData +=	'<td class="Type">' + d.assettype + '</td>';
			rowData +=	'<td class="Dimension">' + d.size + '</td>';
			rowData+= ( '<td id=\'image\'><a onclick="playVideo(this)" href="javascript:;"><img width="50px" src="theme/executive/img/vplayer_thumbnail.png" data-full-src="theme/executive/img/vplayer_thumbnail.png"></img></a></td></tr>' );
			rowData+='<input type="hidden" id="videoPath" value="'+d.location+'" />'
			rowData +=	'</tr>';




			staticRowData = 'tr class="td-header-bottom-border"';
			staticRowData +=	'<th> Asset name </th>';
			staticRowData +=	'<th> Dimension </th>';
			staticRowData +=	'<th> Size </th>';
			staticRowData +=	'<th>  </th>';
			staticRowData +=	'</tr>';

			$('#icc_list_3').html ('');
			$('#icc_list_3').html (rowData);
			$('#video_labels').html ('');
			$("#video_labels").removeClass("hide");
			$('#video_labels').html (staticRowData);
			$("#icc_drop_area_3").hide();
			$("#root_icc_browse_3").attr("disabled","disabled");
			$("#icc_remove_3").removeAttr("disabled");
			$("#icc_remove_3").removeClass("disabled");

			EnableDisable ();

		},
		error : function (data) {

		}
	});



}
function OnVideoDemoDetach () {
	$("#assetUploadType").val(CourseAssetTypeEnum.VIDEODEMO);
	detachDialogForVideo ();
}

function OnCourseImageSearch () {

	$("#assetUploadType").val(CourseAssetTypeEnum.COURSEIMAGE);
	$("#searchMarketingabel").text ('Find Asset and Attach for Course');
}

//
function cancelMarketingSearchAsset () {
	$('#txtMarketingSearchTerm').val('');
	$("#btsearchMarketingAsset").button('reset');
	$("#lstMarketingAssetBody").html('');
}

function searchMarketingAsset() {


	// manual validation of APP.AJAX form

	var bSearch = false;

	$txtAssetSearch = $("div#searchMarketingAssetModal").find ('#txtMarketingSearchTerm');

	if ($txtAssetSearch.val().length < 3) {
		alert("Please provide at least 3 characters for your search criteria.");
	}
	else {

		showProgressLoader("<div id='loader-label'>Loading...</div>");

		$("#btsearchMarketingAsset").button('loading');
		APP.AJAX({
			url : 'getMarketingAssetSearch?assetSearchTerm=' + $txtAssetSearch.val() +'&MarketingAssetSearch=' + $("#assetUploadType").val(),
			type : 'GET',
			async: false,
			success : function(data) {
				bSearch = true;
				asset_object = eval(data);
			},
			error : function (data) {

			}
		});

		if (bSearch) {
			$( "#lstAsset").html('');
			var r =[];
			var row_id;
			var d;
			var len = asset_object.length;

			//WLCMS-232
			if( len <= 0)
				r.push('<tr><td colspan=\'8\'>No search result(s) found.</td></tr>');

			for(var i = 0; i < len; i++)
			{
				d = asset_object[i];
				row_id = d.id + '_'  + d.assetversion_id;
			    r.push('<tr><td id=\"');
				r.push(row_id);
				r.push( '\"><input type=\"radio\" name=\"g1\" value=\"');
				r.push(row_id);
				r.push('\"/></td><td id=\'name\'>');
				r.push(d.name);
				r.push( '</td><td id=\'type\'>');
				r.push(d.assettype);
				r.push( '</td><td id=\'dimension\'>');
				r.push(d.height);
				r.push(' x ');
				r.push(d.width);
				r.push( '</td><td>N/A</td><td id=\'version\'>');
				r.push(d.version);
				r.push( '</td><td id=\'description\'>');
				r.push(d.description);
				r.push( '</td><td id=\'image\'><a href=\'');
				r.push(d.location);
				r.push('\' target=\'_blank\'');
				r.push('><img width=\'50px\' src=\'');
				r.push(d.location);
				r.push('\'></img></a></td></tr>' );

			} // for asset_object.length
			$("#lstMarketingAssetBody").html(r.join(''));
		} // bSearch
	}
	$("div#searchMarketingAssetModal").find ('#txtMarketingSearchTerm').val('');
	$("#btsearchMarketingAsset").button('reset');


setTimeout(function() {
      // Do something after 5 seconds
	  	hideProgressLoader();
}, 1000);
}


function acceptMarketingDialogBox () {

	course_id = getParameterByName ('id');
	var valID;
	if($("#assetUploadType").val() == CourseAssetTypeEnum.AUTHORIMAGE)
		valID =  $('[name=audioVal]:checked').val();
	else if($("#assetUploadType").val() == CourseAssetTypeEnum.COURSEIMAGE)
		valID =  $('[name=g1]:checked').val();
	else if($("#assetUploadType").val() == CourseAssetTypeEnum.COURSETHUMBNAILIMAGE)
		valID =  $('[name=g2]:checked').val();
	if (valID.length > 0 )
	{
		var arr = valID.split('_');
		asset_id   = arr[0];
		assetVersion_id = arr[1];
		assetType = $("#assetUploadType").val();

		APP.AJAX({
			url : 'acceptMarketingAssetSearch',
			data: { id: course_id, assetVersion_id: assetVersion_id, asset_id : asset_id, assetType: assetType },
			type : 'POST',
			async: false,
			success : function(data) {

				listMarketingAssets ();
			},
			error : function (data) {

			}
		});
	}
	// close dialog box
	$("div#searchAssetModal").find ('#txtAudioAssetSearchTerm').val ('');
	$( "#lstAudioAsset").html ('');
	$('#searchAssetModal').modal('hide');

	// Success Message
	$('#successMsg').html("");
	$('#successMsg').append("<div  class='messages'>"+
		"<div role='alert' class='alert alert-success alert-dismissible fade in'>"+
		"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
		"<strong>Success! </strong>It's been saved.</div><script>elementFadeOut('.messages');</script></div>");
	$('#successMsg').show ();
}

function listMarketingAssets (){
	course_id = getParameterByName ('id');

	assetType = $("#assetUploadType").val();
	var r =[];
	var d;
	bValidData = false;

	APP.AJAX({
		url : 'listMarketingAsset',
		data: { id: course_id, assetType: assetType },
		type : 'GET',
		async: false,
		cache: false,
		success : function(data) {
			// Work on getting the listing going
			rowData = '';
			d = data;
			rowData = '<tr>';
			rowData +=	'<td class="Name">' + d.name + '</td>';
			rowData +=	'<td class="Dimension">' + d.height + ' x ' + d.width + '</td>';
			rowData +=	'<td class="Size" >' + d.size + '</td>';
			rowData+= ( '<td id=\'image\'><a onclick="previewImage(this)" href="javascript:;"><img width="50px" src='+d.location+' data-full-src='+d.location+'></img></a></td></tr>' );
			rowData +=	'</tr>';

			staticRowData = 'tr class="td-header-bottom-border"';
			staticRowData +=	'<th> Asset name </th>';
			staticRowData +=	'<th> Dimension </th>';
			staticRowData +=	'<th> Size </th>';
			staticRowData +=	'<th>  </th>';
			staticRowData +=	'</tr>';


			if (assetType == CourseAssetTypeEnum.AUTHORIMAGE){
				$('#icc_list_0').html ('');
				$('#icc_list_0').html (rowData);
				$('#author_labels').html ('');
				$('#author_labels').html (staticRowData);
				$("#icc_drop_area_0").hide();
				$("#root_icc_browse_0").attr("disabled","disabled");
				$("#icc_remove_0").removeAttr("disabled");
				$("#icc_remove_0").removeClass("disabled");
				$("#author_labels").removeClass("hide");


			}else if(assetType == CourseAssetTypeEnum.COURSEIMAGE){
				$('#icc_list_1').html ('');
				$('#icc_list_1').html (rowData);
				$('#course_labels').html ('');
				$('#course_labels').html (staticRowData);
				$("#icc_drop_area_1").hide();
				$("#root_icc_browse_1").attr("disabled","disabled");
				$("#icc_remove_1").removeAttr("disabled");
				$("#icc_remove_1").removeClass("disabled");
				$("#course_labels").removeClass("hide");
			}else if(assetType == CourseAssetTypeEnum.COURSETHUMBNAILIMAGE){
				$('#icc_list_2').html ('');
				$('#icc_list_2').html (rowData);
				$('#thumbnail_labels').html ('');
				$('#thumbnail_labels').html (staticRowData);
				$("#icc_drop_area_2").hide();
				$("#root_icc_browse_2").attr("disabled","disabled");
				$("#icc_remove_2").removeAttr("disabled");
				$("#icc_remove_2").removeClass("disabled");
				$("#thumbnail_labels").removeClass("hide");
			}else if(assetType == CourseAssetTypeEnum.VIDEODEMO){
				$('#icc_list_3').html ('');
				$('#icc_list_3').html (rowData);
				$('#video_labels').html ('');
				$('#video_labels').html (staticRowData);
				$("#icc_drop_area_3").hide();
				$("#root_icc_browse_3").attr("disabled","disabled");
				$("#icc_remove_3").removeAttr("disabled");
				$("#icc_remove_3").removeClass("disabled");
				$("#video_labels").removeClass("hide");
			}
			EnableDisable ();
		},
		error : function (data) {

		}
	});



}

function OnAuthorImageDetach () {
	$("#assetUploadType").val(CourseAssetTypeEnum.AUTHORIMAGE);
	detachDialog ();
}

function OnCourseImageDetach () {
	$("#assetUploadType").val(CourseAssetTypeEnum.COURSEIMAGE);
	detachDialog ();
}
function OnCourseThumbnailDetach(){
	$("#assetUploadType").val(CourseAssetTypeEnum.COURSETHUMBNAILIMAGE);
	detachDialog ();

}
function detachDialogForVideo() {
	//	BEGIN TITLE, MESSAGE AND BUTTONS
	assetType = $("#assetUploadType").val ();
	var title = '<i class="icon-exclamation-sign"></i> Please Confirm';
	msg = '<p>This will detach video from this course. Do you want to continue?</p>';

	var btns = '<button type="button" class="btn blue" onclick="detachMarketingVideoAsset ()" data-dismiss="modal">YES</button>'+
	'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';

	//	END TITLE, MESSAGE AND BUTTONS
	$trgModal = $("#confirmationModal");
	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');
}

function detachMarketingVideoAsset () {

	course_id = getParameterByName ('id');
	assetType = $("#assetUploadType").val ();
	asset_id = -1;
	assetVersion_id = -1;
	APP.AJAX({
		url : 'acceptMarketingAssetVideoSearch',
		data: { id: course_id, assetVersion_id: assetVersion_id, asset_id : asset_id, assetType: assetType },
		type : 'POST',
		async: false,
		success : function(data) {
			$('#successMsg').append("<div  class='messages'>"+
					"<div role='alert' class='alert alert-success alert-dismissible fade in'>"+
					"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
					"<strong>Success!</strong> It's been saved.</div></div>");
				$('#successMsg').show ();
				elementFadeOut('.messages');
				detachOrCancelPress();
		},
		error : function (data) {

		}
	});

	$('#CourseImageVideoBody').html ('');


}
function detachDialog () {
	//	BEGIN TITLE, MESSAGE AND BUTTONS
	assetType = $("#assetUploadType").val ();
	var title = '<i class="icon-exclamation-sign"></i> Please Confirm';
	switch (assetType) {
		case CourseAssetTypeEnum.COURSEIMAGE:
			msg = '<p>This will detach course image from this course. Do you want to continue?</p>';
			break;
		case CourseAssetTypeEnum.AUTHORIMAGE:
			msg = '<p>This will detach author image from this course. Do you want to continue?</p>';
			break;
		case CourseAssetTypeEnum.VIDEODEMO:
			msg = '<p>This will detach author image from this course. Do you want to continue?</p>';
			break;
		case CourseAssetTypeEnum.COURSETHUMBNAILIMAGE:
			msg = '<p>This will detach thumbnail image from this course. Do you want to continue?</p>';
			break;

	}
	var btns = '<button type="button" class="btn blue" onclick="detachMarketingAsset ()" data-dismiss="modal">YES</button>'+
	'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';

	//	END TITLE, MESSAGE AND BUTTONS
	$trgModal = $("#confirmationModal");
	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');
}

function detachMarketingAsset()  {


	course_id = getParameterByName ('id');
	assetType = $("#assetUploadType").val ();
	asset_id = -1;
	assetVersion_id = -1;
	APP.AJAX({
		url : 'acceptMarketingAssetSearch',
		data: { id: course_id, assetVersion_id: assetVersion_id, asset_id : asset_id, assetType: assetType },
		type : 'POST',
		async: false,
		success : function(data) {
			$('#successMsg').append("<div  class='messages'>"+
					"<div role='alert' class='alert alert-success alert-dismissible fade in'>"+
					"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
					"<strong>Success!</strong> It's been saved.</div></div>");
				$('#successMsg').show ();
				elementFadeOut('.messages');

			detachOrCancelPress();
		},
		error : function (data) {

		}
	});




}

function detachOrCancelPress(){
	assetType = $("#assetUploadType").val();
	if (assetType == CourseAssetTypeEnum.AUTHORIMAGE){
				$("#icc_drop_area_0").show();
				$("#icc_drop_area_0").removeClass("hide");
				$("#root_icc_browse_0").removeAttr("disabled");
				$("#root_icc_browse_0").removeClass("disabled");
				$("#icc_remove_0").attr("disabled","disabled");
				$("#icc_remove_0").addClass("disabled");
				$(".portlet-body").find('#icc_list_0').html ('');
				$('#author_labels').html ('');


	}else if(assetType == CourseAssetTypeEnum.COURSEIMAGE){

				$("#icc_drop_area_1").show();
				$("#icc_drop_area_1").removeClass("hide");
				$("#root_icc_browse_1").removeAttr("disabled");
				$("#root_icc_browse_1").removeClass("disabled");
				$("#icc_remove_1").attr("disabled","disabled");
				$("#icc_remove_1").addClass("disabled");
				$('#icc_list_1').html ('');
				$('#course_labels').html ('');



	}else if(assetType == CourseAssetTypeEnum.COURSETHUMBNAILIMAGE){

				$("#icc_drop_area_2").show();
				$("#icc_drop_area_2").removeClass("hide");

				$("#icc_drop_area_2").attr("disabled","disabled");
				$("#icc_drop_area_2").addClass("disabled");
				$('#icc_list_2').html ('');
				$('#thumbnail_labels').html ('');
				$("#root_icc_browse_2").removeAttr("disabled");
				$("#root_icc_browse_2").removeClass("disabled");

	}else if(assetType == CourseAssetTypeEnum.VIDEODEMO){

				$("#icc_drop_area_3").show();
				$("#icc_drop_area_3").removeClass("hide");
				$("#root_icc_browse_3").removeAttr("disabled");
				$("#root_icc_browse_3").removeClass("disabled");
				$("#icc_remove_3").attr("disabled","disabled");
				$("#icc_remove_3").addClass("disabled");
				$('#icc_list_3').html ('');
				$('#video_labels').html ('');



	}
}

function showMarketingAssetDialog (trg) {
	APP.CACHE = trg;
}

function EnableDisable () {

	AuthorImageLength = $('#icc_list_0').children().length;
	if (AuthorImageLength > 0) {
		$("#icc_drop_area_0").hide();
		$("#root_icc_browse_0").attr("disabled","disabled");
		$("#icc_remove_0").removeAttr("disabled");

	} else {
		$('#lnkAuthorSearch').removeAttr("disabled");
		$('#lnkAuthorUpload').removeAttr("disabled");
		$("#icc_drop_area_0").show();
		$('#icc_remove_0').attr("disabled","disabled");
	}

	courseImageLength = $('#icc_list_1').children().length;
	if (courseImageLength > 0) {
		$("#icc_drop_area_1").hide();
		$("#root_icc_browse_1").attr("disabled","disabled");
		$('#icc_remove_1').removeAttr("disabled");

	} else {
		$("#icc_drop_area_1").show();

		$('#icc_remove_1').attr("disabled","disabled");
	}
	courseThumbnailImageLength = $('#icc_list_2').children().length;
	if (courseThumbnailImageLength > 0) {
		$("#icc_drop_area_2").hide();
		$("#root_icc_browse_2").attr("disabled","disabled");
		$('#icc_remove_2').removeAttr("disabled");
	} else {
		$("#icc_drop_area_2").show();
		$('#icc_remove_2').attr("disabled","disabled");

	}
	videoLength = $('#icc_list_3').children().length;
	if (videoLength > 0) {
		$("#icc_drop_area_3").hide();
		$("#root_icc_browse_3").attr("disabled","disabled");
		$('#icc_remove_3').removeAttr("disabled");
	} else {
		$("#icc_drop_area_3").show();
		$('#icc_remove_3').attr("disabled","disabled");

	}

}
function dnd(instance,n,type)
		{
			//	BEGIN IMAGE API **************************************************************
			if(typeof APP.UPLOAD_FILE.PLUPLOAD[instance] == "undefined"){
				//	SingleTon Apply

				var ext;
				var requestId=$("#requestId").val();
				if(type == 'image')
				{
					ext = "jpg,jpeg,png,gif,bmp";
				}
				else if(type == 'video')
				{
					ext = "mp4";
				}

				APP.UPLOAD_FILE.PLUPLOAD[instance] = new plupload.Uploader({
					this_type:type,
					this_instance:instance,
					selected_files:[],
					root_browse_button : "root_icc_browse_"+n,
					remove_button : "icc_remove_"+n,
					list_element : "icc_list_"+n,
					dragdrop : true,
					drop_element : "icc_drop_area_"+n,
					runtimes : 'html5,flash,silverlight,html4', //
					browse_button : "icc_browse_"+n, // you can pass in id...
					container: "icc_container_"+n, // ... or DOM Element itself document.getElementById('dw_container_1')
					url : "../upload",
					multi_selection: false,
					multipart_params: {
						requestId: requestId,
						fileServer: type == 'video'?'fms':'fileServer',
						chunkSize: 1024 * 1024 //1mb in bytes
					},

					filters : {
						max_file_size : '100mb',
						mime_types: [
							{title : type+" files", extensions : ext}
						]
					},

					// Flash settings
					//flash_swf_url : 'assets/plugins/plupload-2.1.8/js/Moxie.swf',

					// Silverlight settings
					//silverlight_xap_url : 'assets/plugins/plupload-2.1.8/js/Moxie.xap',

					init: {
						PostInit: function() {

							//	BEGIN - SAFARI ON WINDOWS
							var ua = navigator.userAgent.toLowerCase();
							if (ua.indexOf("safari/") !== -1 &&  // It says it's Safari
								ua.indexOf("windows") !== -1 &&  // It says it's on Windows
								ua.indexOf("chrom")   === -1     // It DOESN'T say it's Chrome/Chromium
								) {
								this.features.dragdrop = false;
							}
							//	END - SAFARI ON WINDOWS

							if(!this.features.dragdrop || typeof this.features.dragdrop == "undefined")
							{
								$(this.settings.drop_element[0]).addClass("hide");
							}

							var thisSettings = this.settings;
							$("#"+thisSettings.remove_button).click(function(e) {
								if($(this).hasClass("disabled"))
								{
									return;
								}

								APP.UPLOAD_FILE.DELETE(true,thisSettings.this_instance);
								$("#"+thisSettings.container +" .td-header-bottom-border").addClass("hide");
							});
						},

						FilesAdded: function(up, files) {

							//	Clear uploaded files
							while (this.files.length > 1)
							{
								this.removeFile(this.files[0]);
							}
							var trgSettings = this.settings;

							$("#"+trgSettings.list_element).html("");
							trgSettings.selected_files = [];
							trgSettings.rows = {};

							var refInstance = this.settings.this_instance;

							//	Add in row
							plupload.each(files, function(file) {
								var img = new Image();
	                            img.onload = function () {
	                                $("#image_width").val(this.width);
	                                $("#image_height").val(this.height);
	                            };
	                            img.src = $360.URL.createObjectURL(file.getNative());
								var str = '<tr id="'+file.id+'">'+
									  '<td class="name">'+file.name+'</td>'+
									  '<td class="status">'+
										'<div class="progress">'+
											'<div class="progress-bar progress-bar-success" style="width:0%">0%</div>'+
										'</div>'+
									  '</td>'+
									  '<td class="size">'+plupload.formatSize(file.size)+'</td>'+
									  '<td class="action"><a class="btn btn-default" onclick="APP.UPLOAD_FILE.DELETE(true,\''+refInstance+'\')"><i class="icon-thumbs-down-alt"></i> Cancel</a></td>'+
									'</tr>';

								$("#"+trgSettings.list_element).html(str);
								trgSettings.selected_files.push(file.id);
								trgSettings.rows[file.id] = $("#"+file.id);
							});

							$("#multiModal").modal("hide");
							APP.UPLOAD_FILE.CAN_ADD_MORE_FILES(false,this);
							APP.CHACHE = this;
							curr_instance=instance
							saveAndAttachAsset(true);









						},

						UploadProgress: function(up, file) {
							this.settings.rows[file.id].find('.progress-bar').css('width',file.percent+"%").html(file.percent+"%");
						},

						FileUploaded: function(up, uploadedFile, info) {
							var response = JSON.parse(info.response);
							if(type == 'image'){
								$("#addAssetModal").find("#filePath").val(response.data.filePath);
								cancelImageUpload();
								$(".bootstrap-tagsinput").find("input[type='text']").removeClass("hide");
								$("#addAssetModal").modal({backdrop: 'static', keyboard: false});
								if(instance=='inst_info_img'){
									$("#assetUploadType").val(CourseAssetTypeEnum.AUTHORIMAGE);
								}else if(instance=='course_cover_img'){
									$("#assetUploadType").val(CourseAssetTypeEnum.COURSEIMAGE);
								}else if(instance=='course_thumbnail_img'){
									$("#assetUploadType").val(CourseAssetTypeEnum.COURSETHUMBNAILIMAGE);
								}
							}
							else if(type == 'video'){
								$("#addVAssetModal").find("#videoFilePath").val(response.data.filePath);
								cancelImageUpload();
								$("#addVAssetModal").modal({backdrop: 'static', keyboard: false});
								$("#assetUploadType").val(CourseAssetTypeEnum.VIDEODEMO);
							}
							$("#"+this.settings.container +" .td-header-bottom-border").removeClass("hide");
							$('#'+uploadedFile.id).remove();

							var str;
							if(this.settings.this_type == "image")
							{
								str = '<tr id="'+uploadedFile.id+'" class="attached">'+
										  '<td>Asset Name XYZ</td>'+
										  '<td>850 x 565</td>'+
										  '<td>'+plupload.formatSize(uploadedFile.size)+'</td>'+
										  '<td class="text-center">'+
											'<a href="javascript:;" onclick="previewImage(this)">'+
												'<img style="width:50px" data-full-src="assets/img/c1.jpg" src="assets/img/c1.jpg"><br><i class="icon-zoom-in"></i> <small>Preview</small>'+
											'</a>'+
										  '</td>'+
										'</tr>';
							}
							else if(this.settings.this_type == "video")
							{
								str = '<tr id="'+uploadedFile.id+'" class="attached">'+
										  '<td>Asset Name XYZ</td>'+
										  '<td></td>'+
										  '<td>'+plupload.formatSize(uploadedFile.size)+'</td>'+
										  '<td class="text-center">'+
											'<a href="javascript:;" onclick="playVideo(this)">'+
												'<img data-full-src="assets/uploads/fields.mp4" src="assets/img/vplayer_thumbnail.png"><br><i class="icon-play-circle"></i> <small>Play</small>'+
											'</a>'+
										  '</td>'+
										'</tr>';
							}

							$("#"+this.settings.list_element).html(str);
							APP.UPLOAD_FILE.CHECK_DATA(this);
						},

						Error: function(up, err) {

							var err_msg;
							if(err.message == "File size error.")
							{
								err_msg = 'Sorry! File size is exceeding from 100mb.';
							}
							else if(err.message == "File extension error.")
							{
								err_msg = 'Sorry! File does not match the selected asset type.';
							}

							var str = '<div role="alert" class="alert alert-danger alert-dismissible">'+
										'<button data-dismiss="alert" class="close" type="button">'+
											'<span aria-hidden="true"></span><span class="sr-only">Close</span>'+
										'</button>'+
										err_msg +
									'</div>';

							$(".wrapper > .messages").html(str);

							window.setTimeout(function() {
								$(".wrapper > .messages").html("");
							}, 4000);
						}
					}
				});
				APP.UPLOAD_FILE.PLUPLOAD[instance].init();
				APP.UPLOAD_FILE.CHECK_DATA(APP.UPLOAD_FILE.PLUPLOAD[instance]);
			}
			//	END IMAGE API **************************************************************
		}

function openFindModal(n)
{
    $("#multiModal").on('hidden.bs.modal', function (e) {
        $(e.currentTarget).unbind();
        var trgModal;
        switch (n) {
            case 0:
                trgModal = $("#searchAssetModal");
                trgModal.find(".modal-title").html("Find and Attach Instructor Image to Course");
                break;
            case 1:
                trgModal = $("#searchAssetModal");
                trgModal.find(".modal-title").html("Find and Attach Cover Image to Course");
                break;
            case 2:
                trgModal = $("#searchAssetModal");
                trgModal.find(".modal-title").html("Find and Attach Thumbnail Image to Course");
                break;
            case 3:
                trgModal = $("#searchVAssetModal");
                trgModal.find(".modal-title").html("Find and Attach Promotional Video to Course");
                break;
        }
        setTimeout(function () {
            trgModal.modal({backdrop: 'static', keyboard: false});
            trgModal = null;
        }, 100);

    });
    $("#multiModal").modal("hide");
}

		function saveAndAttachAsset(doSave)
		{

			if(doSave)
			{
				//	Upload now
				APP.CHACHE.start();
			}
			else
			{
				APP.UPLOAD_FILE.DELETE(true,curr_instance);
			}


			APP.CHACHE = '';
		}

		function previewImage(trg)
		{
			$("#previewImgModal .modal-body").html("<img src='"+$(trg).find('img').data("full-src")+"' class='img-responsive' style='margin:auto;'>");
			$("#previewImgModal").modal('show');
		}

		function playVideo(trg)
		{
			var video_path = $("#icc_list_3").find("#videoPath").val();
			if(typeof playerInstance == "undefined")
			{
				jwplayer.key="NWa+NruTBASm39QxfCBvuv1UblvSsMtD+mrZiJgnxNI=";
				playerInstance = jwplayer('previewVideoPlayer');
			}

			playerInstance.setup({
				file: video_path,
				width :"100%"
			});
			$("#playVideoModal").modal('show');
		}

		function openBrowseModal(n)
		{
			var trgModal = $("#multiModal");
			switch(n){
				case 0:
					$("#assetUploadType").val(CourseAssetTypeEnum.AUTHORIMAGE);
					trgModal.find(".modal-title").html("Instructor Image");
				break;
				case 1:
					$("#assetUploadType").val(CourseAssetTypeEnum.COURSEIMAGE);
					trgModal.find(".modal-title").html("Course Cover Image");
				break;
				case 2:
					$("#assetUploadType").val(CourseAssetTypeEnum.COURSETHUMBNAILIMAGE);
					trgModal.find(".modal-title").html("Course Thumbnail Image");
				break;
				case 3:
					$("#assetUploadType").val(CourseAssetTypeEnum.VIDEODEMO);
					trgModal.find(".modal-title").html("Course Promotional Video");
				break;
			}

			trgModal.find('.big-icon-btn').addClass("hide");
			trgModal.find('#icc_browse_'+n).removeClass("hide");
			trgModal.find('#icc_find_'+n).removeClass("hide");
			trgModal.modal({backdrop: 'static', keyboard: false});
			trgModal = null;
		}

	function searchAssets (Dynamic) {

		showProgressLoader("<div id='loader-label'>Loading...</div>");
		if(Dynamic == undefined) Dynamic='searchAssetModal';
		if(Dynamic.type == 'button') Dynamic='searchAssetModal';
		var bSearch = false;
		$txtAssetSearch = $("div#"+Dynamic).find ('#txtAudioAssetSearchTerm');
		if ($txtAssetSearch.val().length < 3) {
			alert("Please provide at least 3 characters for your search criteria.");
		}
		else {

			$("div#"+Dynamic).find("#searchAssets").button('loading');
			APP.AJAX({
				url : 'getSlideAssetSearch?assetSearchTerm=' + $txtAssetSearch.val() + '&visualSearch=true',
				//processData : false,
				//contentType : JSON,
				type : 'GET',
				async: false,
				success : function(data) {

					bSearch = true;
					asset_object = eval(data);
				},
				error : function (data) {
					console.log ("error" + $.parseJSON (data));
				}
			});

			if (bSearch) {
				$("div#"+Dynamic).find( "#lstAudioAsset").html ('');
				var r =[];
				var row_id;
				var d;
				var len = asset_object.length;

				//WLCMS-232
				if( len <= 0)
					r.push('<tr><td colspan=\'8\'>No search result(s) found.</td></tr>');

				for(var i = 0; i < len; i++)
				{
					d = asset_object[i];
					row_id = d.id + '_'  + d.assetversion_id;
					r.push('<tr><td id=\'');
					r.push(row_id);
					if($("#assetUploadType").val() == CourseAssetTypeEnum.AUTHORIMAGE)
						r.push( '\'><input type=\'radio\' name=\'audioVal\' value=\'');
					else if($("#assetUploadType").val() == CourseAssetTypeEnum.COURSEIMAGE)
						r.push( '\'><input type=\'radio\' name=\'g1\' value=\'');
					else if($("#assetUploadType").val() == CourseAssetTypeEnum.COURSETHUMBNAILIMAGE)
						r.push( '\'><input type=\'radio\' name=\'g2\' value=\'');
					else if($("#assetUploadType").val() == CourseAssetTypeEnum.VIDEODEMO)
						r.push( '\'><input type=\'radio\' name=\'g3\' value=\'');
					r.push(row_id);
					r.push('\'/></td><td id=\'name\'>');
					r.push(d.name);
					r.push( '</td><td id=\'type\'>');
					r.push(d.assettype);
					r.push( '</td><td id=\'duration\'>');
					r.push(d.duration);
					r.push( '</td><td>N/A</td><td id=\'version\'>');
					r.push(d.version);
					r.push( '</td><td id=\'description\'>');
					r.push(d.description);
					r.push( '</td><td id=\'image\'><a href=\'');
					r.push(d.location);
					r.push('\' target=\'_blank\'');
					if ( d.assettype == 'Image'){
							r.push('><img width=\'50px\' src=\'');
							r.push(d.location);
							r.push('\'></img>' );

					}
					r.push('</a></td></tr>');



















				}
				$("div#"+Dynamic).find("#lstAudioAsset").html(r.join(''));

			}
		}

		$("div#"+Dynamic).find("#searchAssets").button('reset');
		setTimeout(function() {
	      // Do something after 5 seconds
		  	hideProgressLoader();
	}, 1000);

	}

	function acceptDialogBox (assetType) {
	var slideID = $("#hidId").val();
	//	BEGIN TITLE, MESSAGE AND BUTTONS

	var title = '<i class="icon-exclamation-sign"></i> Attention';
	var msg = '<p>This template supports only one [Piece of Audio]. Adding a new [Piece of Audio] will remove the one currently attached. Do you want to continue?</p>';
	switch (assetType) {
	case 'dynamicModal visual':





		TEMPLATE.RETURN_DYNAMIC_MODAL($('[name=g1]:checked').parent().siblings('#name').html(),
		$('[name=g1]:checked').parent().siblings('#version').html(),
		$('[name=g1]:checked').parent().siblings('#image').find('a').attr("href")
		);
			return;
		break;
	case 'dynamicModal audio':



		TEMPLATE.RETURN_DYNAMIC_MODAL($('[name=audioVal]:checked').parent().siblings('#name').html(),
		$('[name=audioVal]:checked').parent().siblings('#version').html(),
		$('[name=audioVal]:checked').parent().siblings('#link').find('a').attr("href"));
			return;
		break;

	case 'audio':
		if ($("#tblAudioAsset_" + slideID+ " > tbody > tr").length < 1) {
			acceptAudioAssetSearch();
			return;
		}
		msg = '<p>This template supports only one [Piece of Audio]. Adding a new [Piece of Audio] will remove the one currently attached. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="acceptAudioAssetSearch()" data-dismiss="modal">YES</button>'+
		'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';

		break;
	case 'visual':
		if ($("#tblVisualAsset_" + slideID + " > tbody > tr").length  < 1)	{
			acceptAssetSearch();
			return;
		}
		msg = '<p>This template supports only one [Piece of visual]. Adding a new [Piece of visual] will remove the one currently attached. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="acceptAssetSearch()" data-dismiss="modal">YES</button>'+
		'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';
		break;
	case 'video':
		if ($("#tblVideo_" + slideID+ " > tbody > tr").length < 1) {
			acceptVAAsset();
			return;
		}
		msg = '<p>This template supports only one [Piece of video]. Adding a new [Piece of video] will remove the one currently attached. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="acceptVAAsset()" data-dismiss="modal">YES</button>'+
		'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';
		break;
	case 'SupportMaterial':
		if ($("#tblVideo_" + slideID+ " > tbody > tr").length < 1) {
			acceptSupportMaterialAsset();
			return;
		}
		break;
	}

	//	END TITLE, MESSAGE AND BUTTONS
	$trgModal = $("#confirmationModal");
	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');


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

   function acceptDialog(){

	if($("#assetUploadType").val() == CourseAssetTypeEnum.VIDEODEMO)
		acceptMarketingVideoDialogBox();
	else
		acceptMarketingDialogBox();

   }
