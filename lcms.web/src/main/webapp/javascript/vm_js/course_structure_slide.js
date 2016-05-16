var content_id;

$(document).ready(function(){
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_1");
	APP.ACCORDION_MOVEABLE();
	APP.BODY_COLLAPSES("CLOSE");
	modalEvents ('.modal');
	APP.CKEDITOR ('feedback-ckeditor-3','TITLE');
	APP.CKEDITOR ('ans-ckeditor-3','TITLE');
	APP.CKEDITOR ('choiceTextForEditExam','TITLE');
	APP.CKEDITOR ('choiceFeedbackForEditExam','TITLE');

	elementFadeOut('.messages');
	APP.ACCORDION_EVENT ('#lessons_accordion_0');/////////////////////////////////////////////////////////////////////////////
	APP.MODAL_EVENT("#addVisualAssetModal");
	APP.MODAL_EVENT("#addAssetModal");
	APP.MODAL_EVENT("#selectTemplateModal");
	APP.MODAL_EVENT("#addSlideModal");
});

// BEGIN MODAL EVENT
function modalEvents(ID){
   $(ID).on('hidden.bs.modal', function (e) {

   })
   $(ID).on('show.bs.modal', function (e) {
		if (typeof($(e.relatedTarget).data('lesson-id')) !== "undefined"){
			content_id = $(e.relatedTarget).data('lesson-id');
		};
		try {
			var getID = $(e.relatedTarget).data('target');
			getID = String(getID).substr(1,getID.length);
		}catch (e){
		}
		if (getID !== 'undefined'){
			var _trg = MODAL_CKEDITOR_INIT[getID];
			if(_trg != null && !_trg.locked){
				_trg.locked = true;
    			for(var i=0;i<_trg.trgs.length;i++){
					APP.CKEDITOR(_trg.trgs[i].id,_trg.trgs[i].type);
				};
			}
		}
   })
}
//END MODAL EVENT
function showProgressLoader(message){
			$("#loader-overlay").html(message);
			$("#bg-loader").show();
    		$("#loader-overlay").show();
		}
function hideProgressLoader(){
	$("#loader-overlay").fadeOut();
    $("#bg-loader").fadeOut();
}

$(function() {

    // Setup form validation on the #register-form element
    $("#addSlideform").validate({
        // Specify the validation rules
        rules: {
        	name: "required",
        	duration: "digits",
			hidSlideTemplateIdForAddForm:"required"
        },
        // Specify the validation error messages
        messages: {
        	name: "Please enter your title here",
        	duration: "Please enter a positive whole number for this field.",
        	hidSlideTemplateIdForAddForm:"Please select any template"
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
        submitHandler: function(form)
        {
			form.submit();
        },
		errorClass: 'error',
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass(errorClass).addClass(validClass);
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass);
        },
        ignore: []
    });
});
function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] === sParam)
        {
            return sParameterName[1];
        }
    }
}

function cancelSlide() {
	resetAddSlidePannel();
	return false;
}
function resetAddVideoSlidePannel(){
	var form  = $("#addVideoSlideform")[0];
	$(form).find("input[type=text], textarea").val("");
	$(form).find('#addVideoSlideUploader').uploader360().reset();
	$(form).find('#submitSlide').removeAttr("data-dismiss");
	$(form).validate().resetForm();
	removeJqueryErrorMessages();
}

function removeJqueryErrorMessages(){
	$("label.error").hide();
	$(".error").removeClass("error");
}

function resetBulkUploadPannel(){
	var form  = $("#addBulkCourseImportform")[0];
	$(form).find("input[type=text], textarea").val("");
	$(form).find('#addBulkCourseImportUploader').uploader360().reset();
	$(form).find('#submitSlide').removeAttr("data-dismiss");
	$(form).validate().resetForm();
}

function resetAddSlidePannel(){
	var form  = $("#addSlideform")[0];
	var editor = CKEDITOR.instances.name;
	if(editor !== null && editor !== 'undefined'){
		editor.setData ('');
		editor.updateElement ();
	}
	$("#duration").val('');
	$(form).find("#hidSlideTemplateIdForAddForm").val("");
	$(form).find("#slideTemplateIconForAddForm").removeClass();
	$(form).find("#slideTemplateIconForAddForm").addClass("icon-question");
	$(form).find("#slideTemplateNameForAddForm").text ("None Selected");
	$(form).validate().resetForm();
	$(form).find('#submitSlide').removeAttr("data-dismiss");
	$(form).find('#msgdiv').html ('');
}


function addVideoSlide(btn) {
	getSlideTemplateId();
	var form = $(btn).closest('form')[0];
	var vName = $(form).find("#slideTitle").val();
	//defaults
	$(form).find("#hidSlideTemplateIdForAddForm").val(Template_Visual_Streaming_Center_ID);
	$(form).find("#duration").val("0");
	//extra params for creating Asset
	var extraParams = {
			cboAssetTypeVisual: "mp4",
			requestId : $(form).find("#requestId").val(),
			filePath : $(form).find("#filePath").val(),
	}
	addSlide(btn,vName,extraParams);
}


function importExcelCourseFile(btn) {
	showProgressLoader("<div id='loader-label'>Processing...</div>");
	var courseIdImport = $("#courseId").val();
	var form = $(btn).closest('form')[0];
	var filePathImport = $(form).find('#filePath').val();
	APP.AJAX({
		url : 'processParsingFile',
		data: {courseId:courseIdImport, filePath:filePathImport},
		//processData : false,
		//contentType : JSON,
		type : 'POST',
		async: true,
		success : function(data) {
			object =  data;
			if(object === "true"){
				TopMessageBar.displayMessageTopBar({vType:1, vMsg: WLCMS_LOCALIZED.COURSE_UPLOADED_SUCCESSFULY_MESSAGE, bFadeOut:true});
				$(form).find('#submitSlide').attr("data-dismiss", "modal");
				$("#addExcepUploadModal").modal('hide');
				getLessonsByIdAjax(courseIdImport);

			}else{
				TopMessageBar.displayMessageTopBar({vType:2, vMsg: object, bFadeOut:true});
			}
			hideProgressLoader();
		},
		error : function (data) {
			hideProgressLoader();
			console.log ("error" + eval (data));
		}
	});
}

function getLessonsByIdAjax (courseId){
	var lessonURL = "/lcms/coursestructure";
	showProgressLoader("<div id='loader-label'>Loading Lessons...</div>");
	APP.AJAX({
	    	  url: lessonURL,
	          dataType: "text",
			  type: "GET",
	          cache: false,
	          data:'lessonOnly=true&id=' + courseId,
	          async: true,
	          success: function(response) {
	        	 hideProgressLoader();
	        	  $("#lessons_accordion_0").html(response);
                          SetArrowVisibilityContentObject();
						  showHideDualSave();
	          }
			});
}

function addSlide (btn,title,extraParams)
{
	var form = $(btn).closest('form')[0];
	var vName = title;
	if(vName == null) {
		var editor = CKEDITOR.instances.name;
		vName = editor.getData();
		editor.updateElement();
	}
    var vNameEscaped = escape(vName);
	if (!$(form).valid()) {
		return false;
	}
	course_id = getUrlParameter ('id');
	var displayRatio_Standard = 0 ,displayRatio_Widescreen = 0 ;
	if($(form).find('input:radio[name=displayRatio]')[0].checked ===  true) {
		displayRatio_Standard = true;
	} else {
		displayRatio_Widescreen = true ;
	}
	//request params
	var params = { id: course_id, name: vName,duration:$(form).find("#duration").val(), templateID:$(form).find("#hidSlideTemplateIdForAddForm").val(), contentObject_id: content_id ,displayRatioStandard:displayRatio_Standard,displayRatioWidescreen:displayRatio_Widescreen };
	if(extraParams){
		$.extend(params,extraParams);
	}
	APP.AJAX({
		url : 'addSlide?id='+course_id,
		data: params,
		//processData : false,
		//contentType : JSON,
		type : 'POST',
		async: false,
		success : function(data) {
			b_addSlide = true;
			slide_object = eval(data);
			TopMessageBar.displayMessageTopBar({vType:1, vMsg: WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
		},
		error : function (data) {
			console.log ("error" + eval (data));
		}
	});
	if (b_addSlide)
	{
		var $divClone = $("div#slideHeaderBar").clone();

		// Set funcion to call on click
		var isCustomTemplate = $("#isCustomTemplate").val();
		var templateId =  $("#hidSlideTemplateIdForAddForm").val();
		$divClone.find ("#label").attr("onclick", 'getSlideComponent('+ slide_object.id +');isSlideHasData();' );
		$divClone.find ("#slide_setup").attr("onclick", 'getSlideSetup('+ slide_object.id +', null)');

		$divClone.find("#slideDeleteButton").click(function ()
		{
			remove_panel(this, 2 , slide_object.id);
		});

		$divClone.find("#slide_setup").attr("href", "#slideSetup_2_" + slide_object.id);
		$divClone.find("#slide_setup").attr("data-parent", '#slide_acc_' + content_id);
		$divClone.find ("#label").attr("title", 'Slide Components' );
		$divClone.find ("#label").attr("data-parent", '#slide_acc_'+ content_id );
		$divClone.find ("#label").attr("href", '#slideSetup_1_'+slide_object.id );
		$divClone.find ("#label").append("<i class=\"icon-file\"> </i><span id='slide_panel_title_"+slide_object.id +"'>&nbsp;" +  $('<div/>').html(vName).text() + "</span> " );
		$divClone.attr("id", "slide_1_" +  slide_object.id  );
		$divClone.attr("item_type", 'Scene');
		$divClone.attr("lessonScene_id", slide_object.contentObjectScene_id  );

		$(form).find("#name").val('');
		$(form).find("#duration").val('');
		if(editor) {
			editor.setData ('');
			editor.updateElement ();
		}
		$divClone.show ();

		$(form).find('#submitSlide').attr("data-dismiss", "modal");
		$( "div#slide_acc_" + content_id).append($divClone);

		$(form).find("#hidSlideTemplateIdForAddForm").val("");
		$(form).find("#slideTemplateIconForAddForm").removeClass();
		$(form).find("#slideTemplateIconForAddForm").addClass("icon-question");
		//$("#slideTemplateNameForAddForm").text ("None Selected");
		$("#slide_panel_title_" +slide_object.id ).click();
	}
	//SetArrowVisibilityScene(content_id);
}

// BEGIN Asset Search and Accept
function searchAssets (Dynamic) {
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	// manual validation of AJAX form
	if(Dynamic === undefined) Dynamic='searchAssetModal';
	if(Dynamic.type === 'button') Dynamic='searchAssetModal';
	var bSearch = false;
	$txtAssetSearch = $("div#"+Dynamic).find ('#visualAssetSearchTerm');
	if ($txtAssetSearch.val().length < 3) {
		hideProgressLoader();
		alert("Please provide at least 3 characters for your search criteria.");
	}
	else {
	$("div#"+Dynamic).find("#searchAssets").button('loading');
		APP.AJAX({
			url : 'getSlideAssetSearch?assetSearchTerm=' + $txtAssetSearch.val() +'&visualSearch=true',
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
			$("div#"+Dynamic).find( "#lstAsset").html('');
			var r =[];
			var row_id;
			var d;
			var len = asset_object.length;
			//WLCMS-232
			if( len <= 0) {
				r.push('<tr><td colspan=\'8\'>No search result(s) found.</td></tr>');
			}
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
				if ( d.assettype === 'Image'){
						r.push('><img width=\'50px\' src=\'');
						r.push(d.location);
						r.push('\'></img></a></td></tr>' );
				}
				else if ( d.assettype === 'Flash Object'){
						r.push('><img src=\'theme/executive/img/icons/swf.png\'/></a></td></tr>');
				}
				else if ( d.assettype === 'Movie Clip'){
						r.push(' class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td></tr>');
				}else if ( d.assettype === 'VSC'){
						r.push(' class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td></tr>');
				}
			} // for asset_object.length
			$("div#"+Dynamic).find("#lstAsset").html(r.join(''));
		} // bSearch
	}
	$("div#"+Dynamic).find("#searchAssets").button('reset');
	setTimeout(function() {
	      // Do something after 5 seconds
		  	hideProgressLoader();
	}, 1000);

}

function cancelAssetSearch() {

	$( "#lstAsset").html ('');
	$("div#searchAssetModal").find ('#visualAssetSearchTerm').val ('');
}

function acceptAssetSearch() {

	var valID;
	valID =  $('[name=g1]:checked').val();
	var course_id = getParameterByName ('id');

	if(valID === undefined){
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Please select a visual asset to add, currently no visual asset is selected.</div>');
		elementFadeOut("#msgdiv");
		return;
	}
	var arr = valID.split('_');
	var scene_id = $("#hidId").val();
	asset_id   = arr[0];
	assetVersion_id = arr[1];
	APP.AJAX({
		url : 'acceptAssetSearch',
		data: { scene_id: scene_id, assetVersion_id: assetVersion_id, asset_id : asset_id,course_id : course_id},
		type : 'POST',
		async: false,
		success : function(data) {
			getSlideVisualTable (scene_id, 1);
		},
		error : function (data) {
		}
	});

	// close dialog box
	$("div#searchAssetModal").find ('#visualAssetSearchTerm').val ('');
	$( "#lstAsset").html ('');
	$('#searchAssetModal').modal('hide');
	// Success Message
	TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
}
// END Asset SEArch and Acccept

// BEGIN Audio Asset search and Accept
function searchAudioAssets (Dynamic) {

showProgressLoader("<div id='loader-label'>Loading...</div>");
if(Dynamic === undefined) Dynamic='searchAAssetModal';
if(Dynamic.type === 'button') Dynamic='searchAssetModal';
	var bSearch = false;
	$txtAssetSearch = $("div#"+Dynamic).find ('#txtAudioAssetSearchTerm');
	if ($txtAssetSearch.val().length < 3) {
		alert("Please provide at least 3 characters for your search criteria.");
	}
	else {
		$("div#"+Dynamic).find("#searchAudioAssets").button('loading');
		APP.AJAX({
			url : 'getSlideAssetSearch?assetSearchTerm=' + $txtAssetSearch.val() + '&audioSearch=true',
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
			if( len <= 0) {
                r.push('<tr><td colspan=\'8\'>No search result(s) found.</td></tr>');
            }

			for(var i = 0; i < len; i++)
			{
				d = asset_object[i];
				row_id = d.id + '_'  + d.assetversion_id;
			    r.push('<tr><td id=\'');
				r.push(row_id);
				r.push( '\'><input type=\'radio\' name=\'audioVal\' value=\'');
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
				r.push( '</td><td id=\'link\'>');
				r.push('<a href=\'');
				r.push(d.location);
				r.push('\' class=\'btn blue-text\' target=\'_blank\'><i class=\'glyphicon glyphicon-play\'></i></a></td></tr>');

			}
			$("div#"+Dynamic).find("#lstAudioAsset").html(r.join(''));
		}
	}
	hideProgressLoader();
	$("div#"+Dynamic).find("#searchAudioAssets").button('reset');
}

function acceptAudioAssetSearch () {
	var valID;
	valID =  $('[name=audioVal]:checked').val();
	var course_id = getParameterByName("id");
	if(valID === undefined){
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Please select an audio asset to add, currently no audio asset is selected.</div>');
		elementFadeOut("#msgdiv");
		return;
	}
	if (valID.length > 0 )
	{
		var arr = valID.split('_');
		var scene_id = $("#hidId").val();
		asset_id   = arr[0];
		assetVersion_id = arr[1];
		APP.AJAX({
			url : 'acceptAssetSearch',
			data: { scene_id: scene_id, assetVersion_id: assetVersion_id, asset_id : asset_id,course_id:course_id},
			type : 'POST',
			async: false,
			success : function(data) {
				getSlideVisualTable (scene_id, 0);
			},
			error : function (data) {
			}
		});
	}
    // close dialog box
	$("div#searchAAssetModal").find ('#txtAudioAssetSearchTerm').val ('');
	$( "#lstAudioAsset").html ('');
	$('#searchAAssetModal').modal('hide');
	// Success Message
	TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
}

function cancelAudioAssetSearch() {
	$( "#lstAudioAsset").html ('');
	$("div#searchAAssetModal").find ('#txtAudioAssetSearchTerm').val ('');
}
// END Audio Asset search and Accept

// search VA Assets
function searchVAAsset() {
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	// manual validation of AJAX form
	var bSearch = false;
	$txtAssetSearch = $("div#searchVAssetModal").find ('#txtVASearchTerm');
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
				if ( d.assettype === 'Image'){
						r.push('><img width=\'50px\' src=\'');
						r.push(d.location);
						r.push('\'></img></a></td></tr>' );
				}
				else if ( d.assettype === 'Flash Object'){
						r.push('><img src=\'theme/executive/img/icons/swf.png\' /></a></td></tr>');
				}
				else if ( d.assettype === 'Movie Clip'){
						r.push(' class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td></tr>');
				}else if ( d.assettype === 'VSC'){
						r.push(' class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td></tr>');
    			}
    		} // for asset_object.length
			$("#lstVABody").html(r.join(''));
		} // bSearch
	}
	hideProgressLoader();
	$txtAssetSearch = $("div#searchVAssetModal").find ('#txtVASearchTerm').val('');
	$("#btsearchVAAsset").button('reset');
	$('#searchAssetModal').modal('hide');
}
//end search VA Assets
//acceptVAAsset
function acceptVAAsset (){
	var valID;
	valID =  $('[name=g1]:checked').val();
	var course_id = getParameterByName("id");
	if (valID.length > 0 )
	{
		var arr = valID.split('_');
		var scene_id = $("#hidId").val();
		asset_id   = arr[0];
		assetVersion_id = arr[1];
		APP.AJAX({
			url : 'acceptAssetSearch',
			data: { scene_id: scene_id, assetVersion_id: assetVersion_id, asset_id : asset_id,course_id:course_id},
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


//search search Support Material Asset Assets
function searchSupportMaterialAsset() {
	showProgressLoader("<div id='loader-label'>Loading...</div>");
	// manual validation of AJAX form
	var bSearch = false;
	txtAssetSearch = $("#txtSupportMaterialSearch").val();
	if (txtAssetSearch.length < 3) {
		alert("Please provide at least 3 characters for your search criteria.");
	}
	else {
		$("#btsearchVAAsset").button('loading');
		APP.AJAX({
			url : 'getSlideAssetSearch?assetSearchTerm=' + txtAssetSearch +'&SupportMaterialSearch=true',
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
				row_id = d.id + '_'  + d.assetversion_id + '_'  + d.assettype;
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
				if ( d.assettype === 'Image'){
						r.push('><img width=\'50px\' src=\'');
						r.push(d.location);
						r.push('\'></img></a></td></tr>' );
				}
				else {
						r.push(' class=\"btn blue-text\"><h4><i class=\"glyphicon glyphicon-file inside-icon-plus\"></i></h4></a></td></tr>');
				}
			} // for asset_object.length
			$("#lstSupportMaterialAsset").html(r.join(''));
		} // bSearch
	}
	hideProgressLoader();
	$("#txtSupportMaterialSearch").val('');
	$("#btsearchVAAsset").button('reset');
	$('#searchAssetModal').modal('hide');
}

// Support material
function acceptSupportMaterialAsset () {
	var valID;
	valID =  $('[name=g1]:checked').val();
	var var_lesson_Id  =  $("#hidLessonId").val();
	var selectedAssetName = $('[name=g1]:checked').parent().siblings('#name').html();

	if(valID === undefined){
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Please select an asset to add, currently no asset is selected.</div>');
		elementFadeOut("#msgdiv");
		$('#btnSaveAssetSupportMat').removeAttr("data-dismiss");
		return;
	}
	if (valID.length > 0 )
	{
		var arr = valID.split('_');
		var var_course_id  =  getUrlParameter ('id');
		asset_id   = arr[0];
		var_asset_type = arr[2];
		APP.AJAX({
			url : 'attachSupportMaterial',
			data: { asset_type: var_asset_type, asset_id : asset_id, lesson_Id: var_lesson_Id, course_id: var_course_id},
			type : 'POST',
			async: false,
			success : function(data) {
			},
			error : function (data) {
			}
		});
	}
	// close dialog box
	$("#txtSupportMaterialSearch").val('');
	$( "#lstSupportMaterialAsset").html ('');
	$('#btnSaveAssetSupportMat').attr("data-dismiss", "modal");
	// Success Message
	TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
	// display newly added Support material bar
	getAddSupportMaterialBar(selectedAssetName);
	setArrowVsbltySpprtMtrl(var_lesson_Id);
}

function cancelVAAsset () {
	$( "#lstVABody").html ('');
	$("div#searchVAssetModal").find ('#txtVASearchTerm').val ('');
}

function elementFadeOut(id)
{
	setTimeout(function(){
        $(id).html('');
    },9000);
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