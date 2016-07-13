
var isSlideTypeVSC;
function getSlideComponent(varSlideId){
	//It must be converted to string to work in === type checking operator
	varSlideId = varSlideId.toString();
	getSlideTemplateId();
	if(!($("#hidId").val()===varSlideId && $('#slideSetup_1_'+varSlideId).hasClass( "panel-collapse a2 collapse in" )))
	{
			var elem = document.getElementById("resultsSMLstSlide");
			if(elem!=null) {
				elem.parentElement.removeChild(elem);
			}
    	  //This code is used to shut down the lat opened Panel
		  //set hidden field value for save button
			$("#hidId").val(varSlideId);
			$("#sceneId").val(varSlideId);  //This variable is set to shut the previous open scene
			// hide[remove slide setup div if exist]
			$('#slideSetup_2_'+varSlideId).remove("");
			$('#slideSetup_1_'+varSlideId).remove("");

			/**get latest slide Object, because after updating from slide setup screen the attributes/templateID has changed
			 * so we need fresh object to determine recent templateID.
			 */
			APP.AJAX({
				  url: "/lcms/getSlideById",
				  dataType: "text",
				  type: "POST",
				  cache: false,
				  data:'varSlideId='+varSlideId,
				  async: false,
				  success: function(response) {
				   slide_Obj = $.parseJSON(response);
				   isSlideTypeVSC = (slide_Obj.embedCode!=null && slide_Obj.embedCode!='')?true:false;
				}
		   });
			 parent_id = $("#slide_1_" + varSlideId).parent ().attr('id');
			 if(slide_Obj!=null && slide_Obj.templateType == SLIDE_TEMPLATE_TYPE.CUSTOM_MC){
					var $divClone = $("div#slideComponent_CustomTemplate").clone();
					//to be loaded in 'portlet-body form' class
					//$divClone.attr("class", "panel-collapse a2 collapse in")
    				$("div#slide_1_"+varSlideId).append($divClone);
					// href setting for slide box
					$divClone.find ('#slideHref').attr("data-parent", "#" + parent_id );
					$divClone.find ('#slideHref').attr("href", "#slideSetup_1_" +varSlideId);
					$divClone.find ('#slideHref').attr("id", "tblDivVisualAsset_" + varSlideId);
					$divClone.find("#embedCode").attr("id","embedCode_"+varSlideId);
					$divClone.find("#embedCode").attr("name","embedCode_"+varSlideId);
					$divClone.find("#hidEmbedCode").attr("id","hidEmbedCode_"+varSlideId);
					$divClone.find("#hidEmbedCode").attr("name","hidEmbedCode_"+varSlideId);
					$divClone.find("#embedCode_"+varSlideId).val(slide_Obj.embedCode);
					$divClone.find("#hidEmbedCode_"+varSlideId).val(slide_Obj.embedCode);
					$divClone.attr("id", "slideSetup_1_"+varSlideId);
					APP.AJAX({
						  url: "/lcms/getSlide_MC_SCENE_XML_ById",
						  dataType: "text",
						  type: "POST",
						  cache: false,
						  data:'varSlideId='+varSlideId,
						  async: false,
						  success: function(response) {
						   slide_MC_SCENE_XML = response;
						   TEMPLATE.DATA = slide_MC_SCENE_XML;
						}
				   });
				 	$('#slideSetup_1_'+varSlideId).find('.portlet-body.form').load('..'+slide_Obj.slideTemplateURL+'w_form.html'
					, function() {
						populateXml(varSlideId);
						$('#slideSetup_1_'+varSlideId).find('.portlet-body.form').append($('#DivSlidePreview').html());
						// set the preview slide button's [join slide id with button name]
    					$divClone.find ('#btnSlidePreview').attr("id", "btnSlidePreview_" + varSlideId);
						$divClone.find("#btnSlidePreview_"+varSlideId).click(function ()
						{
							previewSlide(varSlideId);
						});
					});
				 	$('#hidFunctionName').val('MC_SCENE_XML');
				 	return;
			}
    		// display slide component div
    		var $divClone = $("div#slideComponent").clone();
			$divClone.attr("id", "slideSetup_1_"+varSlideId);
			$divClone.find("#embedCode").attr("id","embedCode_"+varSlideId);
			$divClone.find("#embedCode").attr("name","embedCode_"+varSlideId);
			$divClone.find("#hidEmbedCode").attr("id","hidEmbedCode_"+varSlideId);
			$divClone.find("#hidEmbedCode").attr("name","hidEmbedCode_"+varSlideId);
			$divClone.find("#embedCode_"+varSlideId).val(slide_Obj.embedCode);
			$divClone.find("#hidEmbedCode_"+varSlideId).val(slide_Obj.embedCode);
			$divClone.find("#embedV_0").attr("id","embedCodeVideo_"+varSlideId);
			$divClone.find("#embedV_1").addClass("sel");
			// href setting for slide box
			$divClone.find ('#slideHref').attr("data-parent", "#"+  parent_id);
			$divClone.find ('#slideHref').attr("href", "#slideSetup_1_" +varSlideId);
			$divClone.find ('#slideHref').attr("id", "tblDivVisualAsset_" + varSlideId);
			// set the preview slide button's [join slide id with button name]
			$divClone.find ('#btnSlidePreview').attr("id", "btnSlidePreview_" + varSlideId);
    		//Setting slide icons (audio-icon, camera-icon )
			$divClone.find ('#SlideText').attr("id", "SlideText_" + varSlideId);
			$divClone.find ('#tblAudioAssetTopDiv').find("#audio-icon").attr("id", "audio-icon_" + varSlideId);
			$divClone.find ('#tblVideoTopDiv').find("#video-icon").attr("id", "video-icon_" + varSlideId);
			$divClone.find ('#tblVisualAssetTopDiv').find("#camera-icon").attr("id", "camera-icon_" + varSlideId);
			$divClone.find ('#SlideCloseCaption').attr("id", "SlideCloseCaption_" + varSlideId);
     		$divClone.find ('#tblDivVisualAsset').attr("id", "tblDivVisualAsset_" + varSlideId);
			$divClone.find ('#tblVisualAssetTopDiv').attr("id", "tblVisualAssetTopDiv_" + varSlideId);
			$divClone.find ('#tblVisualAsset').attr("id", "tblVisualAsset_" + varSlideId);
			$divClone.find ('#tblDivAudioAsset').attr("id", "tblDivAudioAsset_" + varSlideId);
			$divClone.find ('#tblAudioAssetTopDiv').attr("id", "tblAudioAssetTopDiv_" + varSlideId);
			$divClone.find ('#tblAudioAsset').attr("id", "tblAudioAsset_" + varSlideId);
			$divClone.find ('#SceneCloseCaption_1').attr("id", "SceneCloseCaption_1_" + varSlideId);
			$divClone.find ('#SceneText_1').attr("id", "SceneText_1_" + varSlideId);
		    $divClone.find("div[title=\"Text\"]").attr("id", "Title_1_" + varSlideId);
			$divClone.find("div[title=\"Closed Captioning\"]").attr("id", "ClosedCaptioning_1_" + varSlideId);
			$divClone.find('[name="SceneText"]').attr("id","SceneText_" + varSlideId);
			$divClone.find('[name="SceneCloseCaption"]').attr("id","SceneCloseCaption_" + varSlideId);
			// Id setting for collapse/expand arrow that use for clicking of arrow sign. this is done for Visual and Audio box only.
    		// 'Text' and 'Closed Captioning' arrows are working fine without any settings
			$divClone.find ('#visualAssetLink').attr("id", "visualAssetLink_" + varSlideId);
			$divClone.find ('#audioAssetLink').attr("id", "audioAssetLink_" + varSlideId);
     		//video Streaming slide
			$divClone.find ('#tblDivVideo').attr("id", "tblDivVideo_" + varSlideId);
			$divClone.find ('#tblVideoTopDiv').attr("id", "tblVideoTopDiv_" + varSlideId);
			$divClone.find ('#tblVideo').attr("id", "tblVideo_" + varSlideId);
			$divClone.find("#tblVisualAssetTopDiv_"+varSlideId).click(function ()
			{
				getSlideVisualTable (varSlideId, 1);
			});
			$divClone.find("#tblAudioAssetTopDiv_"+varSlideId).click(function ()
			{
				getSlideVisualTable (varSlideId, 0);
			});
    		// attached click event on video Streaming
			$divClone.find("#tblVideoTopDiv_"+varSlideId).click(function ()
			{
				var divVideo = $("#tblDivVideo_"+varSlideId);
				getSlideVisualAssetTable (varSlideId, 2);
				isSlideHasData();
			});
			$divClone.find("#btnSlidePreview_"+varSlideId).click(function ()
			{
				previewSlide(varSlideId);
			});
			//$divClone.attr("class", "panel-collapse a2 collapse in")
			$("div#slide_1_"+varSlideId).append($divClone);
			APP.BODY_COLLAPSES("CLOSE");
			if(slide_Obj!==null && slide_Obj.templateID === Template_Visual_Streaming_Center_ID){
				$divClone.find("#tblVisualAssetTopDiv_" + varSlideId).remove ();
				$divClone.find ("#tblAudioAssetTopDiv_" + varSlideId).remove ();
				$divClone.find ("#Title_1_" + varSlideId).parent().remove ();
			} else
				APP.CKEDITOR("SceneText_"+varSlideId,"TEXT");
    		//The video bar is shown only with templateID=5
			if(slide_Obj!==null && slide_Obj.templateID !== Template_Visual_Streaming_Center_ID){
				$divClone.find("#tblVideoTopDiv_" + varSlideId).remove ();
			}
			APP.CKEDITOR("SceneCloseCaption_"+varSlideId, "STANDARD");
	}else{
			if(slide_Obj!==null && slide_Obj.templateType === WLCMS_CONSTANTS.SLIDE_TEMPLATE_TYPE){
				update_MC_SCENE_XML(varSlideId);
			}
			else
			$('#slideSetup_1_'+varSlideId).remove();
	}
	/**
	 * Retrieve data for icons, which has data Do icon green otherwise gray.
	 * WLCMS-194
	*/
	APP.AJAX({
		 url:  '/lcms/isSlideComponentsHasData',
		 dataType: "text",
		 type: "POST",
		 cache: false,
		 data:'varSlideId='+ varSlideId,
		 async: false,
		 success: function(response) {
    		slideCompData = $.parseJSON(response);
			if(slideCompData.slideTextIcon === 'true'){
				$("#SlideText_"+varSlideId).find("i").addClass("green");
			}
			else if(slideCompData.slideTextIcon === 'false') {
				$("#SlideText_"+varSlideId).find("i").removeClass("green");
			}

			if(slideCompData.slideVideoIcon === 'true'){
				$("#camera-icon_"+varSlideId).find("i").addClass("green");
			}
			else if(slideCompData.slideVideoIcon === 'false') {
				$("#camera-icon_"+varSlideId).find("i").removeClass("green");
			}

			if(slideCompData.slideAudioIcon === 'true'){
				$("#audio-icon_"+varSlideId).find("i").addClass("green");
			}
			else if(slideCompData.slideAudioIcon === 'false') {
				$("#audio-icon_"+varSlideId).find("i").removeClass("green");
			}

			if(slideCompData.closeCaptionIcon === 'true'){
				$("#SlideCloseCaption_"+varSlideId).find("i").addClass("green");
			}
			else if(slideCompData.closeCaptionIcon === 'false') {
				$("#SlideCloseCaption_"+varSlideId).find("i").removeClass("green");
			}
			else if(slideCompData.VSC === 'false') {
				$("#video-icon_"+varSlideId).find("i").removeClass("green");
			}
			if(slideCompData.VSC === 'true'){
				$("#video-icon_"+varSlideId).find("i").addClass("green");
			}
			APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");
		}
	});
	if(slide_Obj.isEmbedCode){
		$divClone.find("#embedV_1").click();
   }else{
	   	$divClone.find("#embedV_0" ).click();
   }
}



function update_MC_SCENE_XML(varSlideId)
{
	setXml();
	if(TEMPLATE.IS_VALID)
		{
			//save in DB
			var updateSlideUrl = "/lcms/updateSlide_MC_SCENE_XML";
			APP.AJAX({
						url: updateSlideUrl,
						dataType: "text",
						type: "POST",
						cache: false,
						data:'varSlideId='+ varSlideId + '&MC_SCENE_XML='+escape(TEMPLATE.DATA) ,
						async: false,
						success: function(response)
							   {
								console.log($.parseJSON(response));
							   }
				   });
			$('#hidFunctionName').val('');
		return true;
	}
	else
	return false;
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
					durationOrDimesion = assetType===1 ?  Slidedata[i].width + ' x ' + Slidedata[i].height : secondsToTime(Slidedata[i].duration);
					html += '<tr id='+ Slidedata[i].versionId +  ' >'+
					'<td>' + Slidedata[i].name + '</td>' +
					'<td>' + Slidedata[i].assettype + '</td>' +
					'<td>' + durationOrDimesion + '</td>' +
					'<td> ' + Slidedata[i].size + '</td>' +
					'<td>' + Slidedata[i].version + '</td>' +
					'<td>' + Slidedata[i].description + '</td>' +
					'<td>  <a href=' + Slidedata[i].location + ' target=_blank  class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td>'+
			  '</tr>';
		}
		if(html.length > 0) {
			$("#video-icon_"+varSlideId).find("i").addClass("green");
			$('#tblDivVideo_'+varSlideId).find('[data-target=#searchVAssetModal]').attr("disabled","disabled");
			$('#tblDivVideo_'+varSlideId).find('[data-target=#addVAssetModal]').attr("disabled","disabled");
			$('#tblDivVideo_'+varSlideId).find('.icon-minus').closest('a').removeAttr("disabled");
		}
		if(html.length === 0){
			$("#video-icon_"+varSlideId).find("i").removeClass("green");
			$('#tblDivVideo_'+varSlideId).find('[data-target=#searchVAssetModal]').removeAttr("disabled");
			$('#tblDivVideo_'+varSlideId).find('[data-target=#addVAssetModal]').removeAttr("disabled");
			$('#tblDivVideo_'+varSlideId).find('.icon-minus').closest('a').attr("disabled","disabled");
		}
		$('#tblDivVideo_' + varSlideId + ' tbody').empty();
		$('#tblDivVideo_' + varSlideId + ' tbody').html(html);
	}
}

function getSlideVisualTable(varSlideId, assetType){
	if(assetType===1)
		var divClone = $("div#tblDivVisualAsset_"+varSlideId);
	else
		var divClone = $("div#tblDivAudioAsset_"+varSlideId);
			 var visualAssetURL = "/lcms/getAssetBySlide";
			 APP.AJAX({
				 url:  visualAssetURL,
				 dataType: "text",
				 type: "POST",
				 cache: false,
				 data:'varSlideId='+ varSlideId + '&varAssetType='+ assetType,
				 async: false,
				 success: function(response) {
					Slidedata = $.parseJSON(response);
				}
			});
			var html = '';
			var durationOrDimesion='';
			for(var i = 0; i < Slidedata.length; i++){
						durationOrDimesion = assetType===1 ?  Slidedata[i].width + ' x ' + Slidedata[i].height : secondsToTime(Slidedata[i].duration);
						html += '<tr  id='+ Slidedata[i].versionId +  ' >' +
						'<td>' + Slidedata[i].name + '</td>' +
						'<td>' + Slidedata[i].assettype + '</td>' +
						'<td>' + durationOrDimesion + '</td>' +
						'<td>' +Slidedata[i].size+ '</td>' +
						'<td>' + Slidedata[i].version + '</td>' +
						'<td>' + Slidedata[i].description + '</td>'
						if ( Slidedata[i].assettype === 'Image'){
							html += '<td>  <a href=' + Slidedata[i].location + ' target=_blank ><img width=50px src=' +Slidedata[i].location+ '></img></a></td></tr>'
						}
						else if ( Slidedata[i].assettype === 'Flash Object'){
							html += '<td>  <a href=' + Slidedata[i].location + ' target=_blank ><img src=\'theme/executive/img/icons/swf.png\'/></a></td>'
						}
						else if ( Slidedata[i].assettype === 'Movie Clip' || Slidedata[i].assettype === 'Audio Clip'){
							html +=	 '<td>  <a href=' + Slidedata[i].location + ' target=_blank  class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td>'
						}else if ( Slidedata[i].assettype === 'VSC'){
							html +=	 '<td>  <a href=' + Slidedata[i].location + ' target=_blank  class=\"btn blue-text\"><i class=\"glyphicon glyphicon-play\"></i></a></td>'
						}

			}
			if(assetType==1){
				$('#tblVisualAsset_'+varSlideId+' tbody').empty();
				$('#tblVisualAsset_'+varSlideId+' tbody').append(html);
				if(html.length > 0) {
					$("#camera-icon_"+varSlideId).find("i").addClass("green");
					$('#tblVisualAsset_'+varSlideId).find('[data-target=#searchAssetModal]').attr("disabled","disabled");
					$('#tblVisualAsset_'+varSlideId).find('[data-target=#addVisualAssetModal]').attr("disabled","disabled");
					$('#tblVisualAsset_'+varSlideId).find('.icon-minus').closest('a').removeAttr("disabled");
				} else if(html.length === 0){
					$("#camera-icon_"+varSlideId).find("i").removeClass("green");
					$('#tblVisualAsset_'+varSlideId).find('[data-target=#searchAssetModal]').removeAttr("disabled");
					$('#tblVisualAsset_'+varSlideId).find('[data-target=#addVisualAssetModal]').removeAttr("disabled");
					$('#tblVisualAsset_'+varSlideId).find('.icon-minus').closest('a').attr("disabled","disabled");
				}

			}else{
				$('#tblAudioAsset_'+varSlideId+' tbody').empty();
				$('#tblAudioAsset_'+varSlideId+' tbody').append(html);
				if(html.length > 0) {
					$("#audio-icon_"+varSlideId).find("i").addClass("green");
					$('#tblAudioAsset_'+varSlideId).find('[data-target=#searchAAssetModal]').attr("disabled","disabled");
					$('#tblAudioAsset_'+varSlideId).find('[data-target=#addAssetModal]').attr("disabled","disabled");
					$('#tblAudioAsset_'+varSlideId).find('.icon-minus').closest('a').removeAttr("disabled");
				}
				if(html.length === 0){
					$("#audio-icon_"+varSlideId).find("i").removeClass("green");
					$('#tblAudioAsset_'+varSlideId).find('[data-target=#searchAAssetModal]').removeAttr("disabled");
					$('#tblAudioAsset_'+varSlideId).find('[data-target=#addAssetModal]').removeAttr("disabled");
					$('#tblAudioAsset_'+varSlideId).find('.icon-minus').closest('a').attr("disabled","disabled");
				}
			}
}
function secondsToTime(secs){
    secs = Math.round(secs);
    var hours = Math.floor(secs / (60 * 60));

    var divisor_for_minutes = secs % (60 * 60);
    var minutes = Math.floor(divisor_for_minutes / 60);

    var divisor_for_seconds = divisor_for_minutes % 60;
    var seconds = Math.ceil(divisor_for_seconds);

    if (hours   < 10) {hours   = "0"+hours;}
    if (minutes < 10) {minutes = "0"+minutes;}
    if (seconds < 10) {seconds = "0"+seconds;}
    return hours+":"+minutes+":"+seconds;
}

function detachAsset (assetType) {
	trg = $(APP.CACHE);
	slide_id = trg.closest('table').attr ('id');
	slide_id = slide_id.split('_');
	slide_id = slide_id[1];
	var course_id = getParameterByName ('id');
	assetVersion_id = trg.closest('table').find(' tbody tr:first').attr('id');
	APP.AJAX({
		url: 'detachSlideAsset',
		dataType: "text",
		type: "POST",
		cache: false,
		data:'varSlideId='+ slide_id+ '&assetVersion_id='+ assetVersion_id+'&course_id='+course_id + assetVersion_id +'&assetType='+assetType,
		async: false,
		success: function(response) {
				trg.closest('table').find(' tbody tr:first').remove ();
		}
	});
	switch(assetType) {
	case 1:	// Video
			$("#camera-icon_"+slide_id).find("i").removeClass("green");
			$('#tblVisualAsset_'+slide_id).find('[data-target=#searchAssetModal]').removeAttr("disabled");
			$('#tblVisualAsset_'+slide_id).find('[data-target=#addVisualAssetModal]').removeAttr("disabled");
			$('#tblVisualAsset_'+slide_id).find('.icon-minus').closest('a').attr("disabled","disabled");
		break;
	case 2: // Audio
			$("#audio-icon_"+slide_id).find("i").removeClass("green");
			$('#tblAudioAsset_'+slide_id).find('[data-target=#searchAAssetModal]').removeAttr("disabled");
			$('#tblAudioAsset_'+slide_id).find('[data-target=#addAssetModal]').removeAttr("disabled");
			$('#tblAudioAsset_'+slide_id).find('.icon-minus').closest('a').attr("disabled","disabled");
		break;
	case 3: // Streaming
			$("#video-icon_"+slide_id).find("i").removeClass("green");
			$('#tblDivVideo_'+slide_id).find('[data-target=#searchVAssetModal]').removeAttr("disabled");
			$('#tblDivVideo_'+slide_id).find('[data-target=#addVAssetModal]').removeAttr("disabled");
			$('#tblDivVideo_'+slide_id).find('.icon-minus').closest('a').attr("disabled","disabled");
		break;
	}
	APP.CACHE = '';
}

function confirmDetachObject (assetType, trg, event) {
	$trgModal = $("#confirmationModal");
	APP.CACHE = trg;
	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';
	switch(assetType) {
	case 1:	// Video
		var msg = '<p>This action will remove the Visual asset from this template. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="detachAsset('+assetType +');isSlideHasData(); " data-dismiss="modal">YES</button>'+
					'<button type="button" class="btn btn-default"  data-dismiss="modal">NO</button>';
		break;
	case 2: // Audio
		var msg = '<p>This action will remove the Audio asset from this template. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="detachAsset('+assetType +'); " data-dismiss="modal">YES</button>'+
					'<button type="button" class="btn btn-default"  data-dismiss="modal">NO</button>';
		break;
	case 3: // Streaming
		var msg = '<p>This action will remove the Streaming asset from this template. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick=" detachAsset('+assetType +');" data-dismiss="modal">YES</button>'+
					'<button type="button" class="btn btn-default" data-dismiss="modal">NO</button>';
		break;
	}
	//	END TITLE, MESSAGE AND BUTTONS
	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);
	$trgModal.modal('show');
}

function procedYesNo (yn, sid){

	if(yn){
			//save Text editor here first
			var updateSlideTextUrl = "/lcms/updateSlideText";
			 if(activeEditorForText!=null){
			 	 var vSceneTextData = activeEditorForText.getData();
				 //vSceneTextData = vSceneTextData.replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, "\"")
				 vSceneTextData = escape(vSceneTextData);
				 APP.AJAX({
						url: updateSlideTextUrl,
						dataType: "text",
						type: "POST",
						cache: false,
						data:'varSlideId='+ sid + '&varTitle='+vSceneTextData  + '&varOrientationKey=Text',
						async: false,
						success: function(response)
							   {
								activeEditorForText.resetDirty();
							   }
				   });
			   }
				//save CC editor here first
				if(activeEditorForCC!=null){
					 var updateSlideCloseCaptionUrl = "/lcms/updateSlideCloseCaption";
					 var vSceneCCtData = activeEditorForCC.getData();
					 vSceneCCtData = vSceneCCtData.replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, "\"");
					 APP.AJAX({
							url: updateSlideCloseCaptionUrl,
							dataType: "text",
							type: "POST",
							cache: false,
							data:'varSlideId='+ sid + '&varTitle='+vSceneCCtData  + '&varOrientationKey=VOText',
							async: false,
        					success: function(response){
								activeEditorForCC.resetDirty();
							}
				   });
			 	}
			   previewSlide(sid);
	}
	else{
		var id = getUrlParameter('id');
		launchPlayer(sid, id);

	}
}

function previewSlide(varSlideId){

	var id = getUrlParameter('id');
	if(activeEditorForText!==null && activeEditorForText.checkDirty()
	|| activeEditorForCC!=null && activeEditorForCC.checkDirty()){
		$trgModal = $("#confirmationModal");
					//	BEGIN TITLE, MESSAGE AND BUTTONS
					var title = '<i class="icon-exclamation-sign"></i> Please Confirm';
					var msg = '<p>In order to preview, you must first save. Would you like to save now?</p>';
					var btns = '<button type="button" class="btn blue" onclick="procedYesNo(true,'+varSlideId+')" data-dismiss="modal">YES</button>'+
								'<button type="button" class="btn btn-default" onclick="procedYesNo(false,'+varSlideId+')" data-dismiss="modal">NO</button>';
					//	END TITLE, MESSAGE AND BUTTONS
					$trgModal.find(".modal-title").html(title);
					$trgModal.find(".modal-body").html(msg);
					$trgModal.find(".modal-footer").html(btns);
					$trgModal.modal('show');
	}else{
		launchPlayer(varSlideId, id);
	}
}


function launchPlayer(varSlideId, id){
		var visualAssetURL = "/lcms/previewSlide";
		APP.AJAX({
			 url:  visualAssetURL,
			 dataType: "text",
			 type: "POST",
			 cache: false,
			 data:'varSceneId='+ varSlideId + '&id=' + id,
			 async: false,
			 success: function(response) {
				Slidedata = $.parseJSON(response);
			}
		});

	openWin(Slidedata.status);

}

function deleteSlide(slideId){
var courseId = getUrlParameter ('id');

	courseDescriptionOverlayUrl = "deleteSlide";
	APP.AJAX({
				url: courseDescriptionOverlayUrl,
				dataType: "text",
				type: "POST",
				cache: false,
				data:'varSceneId='+slideId+'&varCourseId='+courseId,
				async: false,
				success: function(response) {
					 obj = $.parseJSON(response);
				}
	});
}
function deleteLesson(lessonId){
	var courseId = getUrlParameter ('id');

	courseDescriptionOverlayUrl = "deleteLesson";
	APP.AJAX({
				url: courseDescriptionOverlayUrl,
				dataType: "text",
				type: "POST",
				cache: false,
				data:'varLessonId='+lessonId+'&varCourseId='+courseId,
				async: false,
				success: function(response) {
					 obj = $.parseJSON(response);
				}
	});
	SetArrowVisibilityContentObject();
}


//-----------------------------------------------------------------------------------------------
//--------------  Start --------------Audio asset form script------------------------------------
//-----------------------------------------------------------------------------------------------
function addAudioAsset(form){

	TEMPLATE.GARBAGE_CLEANER_LOCKED = true;
	audioAssetName = $(form).find("#audioAssetName").val();
	if (!$(form).valid()) {
		return false;
	}
	$(form).find('input[name="slideIdForAudioAsset"]').val($("#sceneId").val());
	$(form).find('input[name="courseIdForAudioAsset"]').val(getUrlParameter ('id'));
	var file = $(form).find('.upload-360').uploader360().files[0];
	$(form).find('#duration').val(file.media.duration);
  // Create the iframe...
    var iframe = document.createElement("iframe");
    iframe.setAttribute("id", "upload_iframe");
    iframe.setAttribute("name", "upload_iframe");
    iframe.setAttribute("width", "0");
    iframe.setAttribute("height", "0");
    iframe.setAttribute("border", "0");
    iframe.setAttribute("style", "width: 0; height: 0; border: none;");
	// Add to document...
	if($(form).find('#isCustomTemplate').val() == 'true') {
        form.parentNode.parentNode.parentNode.appendChild(iframe);
    }
	else {
        form.parentNode.appendChild(iframe);
    }
    window.frames['upload_iframe'].name = "upload_iframe";
	iframeId = document.getElementById("upload_iframe");
	var eventHandler = function () {
            if (iframeId.detachEvent){ iframeId.detachEvent("onload", eventHandler);}
            else {iframeId.removeEventListener("load", eventHandler, false);}

            // Message from server...
            if (iframeId.contentDocument) {
                content = iframeId.contentDocument.body.innerHTML;
            } else if (iframeId.contentWindow) {
                content = iframeId.contentWindow.document.body.innerHTML;
            } else if (iframeId.document) {
                content = iframeId.document.body.innerHTML;
            }
			hideProgressLoader();
            // Del the iframe...
            setTimeout('iframeId.parentNode.removeChild(iframeId)', 250);
			// Success message
            TopMessageBar.displayMessageTopBar({vType:1, vMsg: WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
			if($(form).find('#isCustomTemplate').val() === 'true'){
				if($(form).find('#attachAssetWithSlide').is(":checked")=== true)
					TEMPLATE.RETURN_DYNAMIC_MODAL(audioAssetName,'1',content);
			}
			getSlideVisualTable ($("#sceneId").val(), 0);
			cancelAudioAssetUploadForm();
        }
    if (iframeId.addEventListener) iframeId.addEventListener("load", eventHandler, true);
    if (iframeId.attachEvent) iframeId.attachEvent("onload", eventHandler);
    // Set properties of form...
    form.setAttribute("target", "upload_iframe");
    form.setAttribute("action", "uploadAudioAsset");
    form.setAttribute("method", "post");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("encoding", "multipart/form-data");
    // Submit the form...
	showProgressLoader("<div id='loader-label'>Uploading...</div>");
    form.submit();
	// reset form

    $(form).closest('.modal').modal('hide');
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

$(function() {
    // Setup form validation on the #register-form element
    $("#frmUploadAudioAsset").validate({
        // Specify the validation rules
        rules: {
        	audioAssetName: "required"
        },
        // Specify the validation error messages
        messages: {
        	audioAssetName: "Please enter your asset name here"
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
		ignore : []
    });
  });


function cancelAudioAssetUploadForm() {
	var form = $("#frmUploadAudioAsset")[0];
	$("#audioAssetName").val('');
	$("#frmUploadAudioAsset input[name=audioAssetKeywords]").tagsinput('removeAll');
	$("#audioAssetDescription").val ('');
	$('#audioAssetFile').val ('');
	$('#attachAssetWithSlide').attr('checked', true);
	$('#attachAssetWithAssetGroup').attr('checked', true);
	$(form).get(0).reset();
	$(form).validate().resetForm();
	$(form).find('#isCustomTemplate').val('false');
	$(form).find('#duration').val(0);
	$('#audioUploader').uploader360().reset();
	$('#msgdiv').html ('');
	return false;
}
//-----------------------------------------------------------------------------------------------
//--------------  End   --------------Audio asset form script------------------------------------
//-----------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------
//--------------  Start --------------Visual asset form script-----------------------------------
//-----------------------------------------------------------------------------------------------
function addVisualAsset(form){
	TEMPLATE.GARBAGE_CLEANER_LOCKED = true;
	visualAssetName = $(form).find("#visualAssetName").val();
	if (!$(form).valid()) {
		return false;
	}
	//image width*height if provided
	var file = $(form).find('.upload-360').uploader360().files[0];
	if(file.image) {
		$(form).find('#imageWidth').val(file.image.width);
		$(form).find('#imageHeight').val(file.image.height);
	} else if (file.media) {
		$(form).find('#duration').val(file.media.duration);
	}
	$(form).find('input[name="slideIdForAsset"]').val($("#sceneId").val());
	$(form).find('input[name="courseIdForAsset"]').val(getUrlParameter ('id'));
  // Create the iframe...
    var iframe = document.createElement("iframe");
    iframe.setAttribute("id", "upload_iframe");
    iframe.setAttribute("name", "upload_iframe");
    iframe.setAttribute("width", "0");
    iframe.setAttribute("height", "0");
    iframe.setAttribute("border", "0");
    iframe.setAttribute("style", "width: 0; height: 0; border: none;");
	// Add to document...
	if($(form).find('#isCustomTemplate').val() === 'true')
	{

		form.parentNode.parentNode.parentNode.appendChild(iframe);
	}
	else {
        form.parentNode.appendChild(iframe);
    }
    window.frames['upload_iframe'].name = "upload_iframe";
	iframeId = document.getElementById("upload_iframe");
	var eventHandler = function () {
            if (iframeId.detachEvent){ iframeId.detachEvent("onload", eventHandler);}
            else{ iframeId.removeEventListener("load", eventHandler, false);}

            // Message from server...
            if (iframeId.contentDocument) {
                content = iframeId.contentDocument.body.innerHTML;
            } else if (iframeId.contentWindow) {
                content = iframeId.contentWindow.document.body.innerHTML;
            } else if (iframeId.document) {
                content = iframeId.document.body.innerHTML;
            }
			hideProgressLoader();
            // Del the iframe...
            setTimeout('iframeId.parentNode.removeChild(iframeId)', 250);
			// Success message
        TopMessageBar.displayMessageTopBar({vType: 1, vMsg: WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut: true});
        if ($(form).find('#isCustomTemplate').val() === 'true') {
            var filePath = $360.URL.createObjectURL(file.getNative());
            if ($(form).find('#visualAttachAssetWithSlide').is(":checked") === true) {
                TEMPLATE.RETURN_DYNAMIC_MODAL(visualAssetName, '1', filePath);
            }
            $(form).find('#btnSubmitVisualAssetForm').attr("data-dismiss", "modal");
        } else {
            getSlideVisualTable($("#sceneId").val(), 1);
        }
        }
    if (iframeId.addEventListener){ iframeId.addEventListener("load", eventHandler, true);}
    if (iframeId.attachEvent){ iframeId.attachEvent("onload", eventHandler);}
    // Set properties of form...
    form.setAttribute("target", "upload_iframe");
    form.setAttribute("action", "uploadVisualAsset");
    form.setAttribute("method", "post");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("encoding", "multipart/form-data");
    //seting values in hidden fields in audio asset upload form
	showProgressLoader("<div id='loader-label'>Uploading...</div>");
  // Submit the form...
	$(form).find("#cboAssetTypeVisual").prop("disabled",false);
	form.submit();
	// reset form
	$("#visualAssetName").val('');
	$("#visualAssetKeywords").tagsinput('removeAll');
	$("#visualAssetDescription").val ('');
	$('#visualAssetFile').val ('');
	$('#txtAreaVisualFileUpload').val ('');
	$('#visualAttachAssetWithSlide').attr('checked', true);
	$('#visualAttachAssetWithAssetGroup').attr('checked', true);
	$('#visualUploader').uploader360().reset();
    $(form).closest('.modal').modal('hide');

}

function addVideoAsset(form){
	if (!$(form).valid()) {
		return false;
	}
	//get duration of uploaded video in seconds and set it to form field
	var file = $(form).find('.upload-360').uploader360().files[0];
	if(file && file.media && file.media.duration && (!isNaN(file.media.duration))) {
		//get floor of the value as mostly used format of its display value
		var duration = Math.floor(file.media.duration);
		$(form).find('#duration').val(duration);
	} else {
		$(form).find('#duration').val("0");
	}
	// Create the iframe...
    var iframe = document.createElement("iframe");
    iframe.setAttribute("id", "upload_iframe");
    iframe.setAttribute("name", "upload_iframe");
    iframe.setAttribute("width", "0");
    iframe.setAttribute("height", "0");
    iframe.setAttribute("border", "0");
    iframe.setAttribute("style", "width: 0; height: 0; border: none;");
	// Add to document...
    form.parentNode.appendChild(iframe);
    window.frames['upload_iframe'].name = "upload_iframe";
	iframeId = document.getElementById("upload_iframe");
	var eventHandler = function () {
             if (iframeId.detachEvent){ iframeId.detachEvent("onload", eventHandler);}
            else{ iframeId.removeEventListener("load", eventHandler, false);}
            // Message from server...
            if (iframeId.contentDocument) {
                content = iframeId.contentDocument.body.innerHTML;
            } else if (iframeId.contentWindow) {
                content = iframeId.contentWindow.document.body.innerHTML;
            } else if (iframeId.document) {
                content = iframeId.document.body.innerHTML;
            }
			hideProgressLoader();
            // Del the iframe...
            setTimeout('iframeId.parentNode.removeChild(iframeId)', 250);
			// Success message
            TopMessageBar.displayMessageTopBar({vType:1, vMsg: WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
		 	// refresh the list
			if($(form).find('#isCustomTemplate').val() === 'true'){
				if($(form).find('#visualAttachAssetWithSlide').is(":checked")=== true)
					TEMPLATE.RETURN_DYNAMIC_MODAL(videoAssetName,'1',content);
			}
			getSlideVisualAssetTable ($("#sceneId").val(), 2);
        }
    if (iframeId.addEventListener){ iframeId.addEventListener("load", eventHandler, true);}
    if (iframeId.attachEvent){ iframeId.attachEvent("onload", eventHandler);}
    // Set properties of form...
    form.setAttribute("target", "upload_iframe");
    form.setAttribute("action", "uploadVisualAsset");
    form.setAttribute("method", "post");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("encoding", "multipart/form-data");
    //seting values in hidden fields in audio asset upload form
	$("#slideIdForAsset_video").val($("#sceneId").val()) ;
	$("#courseIdForAsset_video").val( getUrlParameter ('id')) ;
    // Submit the form...
	showProgressLoader("<div id='loader-label'>Uploading...</div>");
    form.submit();
	// reset form
	$("#frmUploadVideoAsset").find("#videoAssetName").val('');
	$("#frmUploadVideoAsset").find("#visualAssetKeywords").tagsinput('removeAll');
	$("#frmUploadVideoAsset").find("#visualAssetDescription").val ('');
	$("#frmUploadVideoAsset").find('#visualAssetFile').val ('');
	$("#frmUploadVideoAsset").find('#txtAreaVisualFileUpload').val ('');
	$("#frmUploadVideoAsset").find('#visualAttachAssetWithSlide').attr('checked', true);
	$("#frmUploadVideoAsset").find('#visualAttachAssetWithAssetGroup').attr('checked', true);
	$("#frmUploadVideoAsset").find('#btnSubmitVideoAssetForm').attr("data-dismiss", "modal");
	$('#videoUploader').uploader360().reset();
	$(form).closest('.modal').modal('hide');

}

function cancelVisualAssetUploadForm() {
	var form = $("#frmUploadVisualAsset")[0];
	$("#visualAssetName").val('');
	$("#visualAssetKeywords").tagsinput('removeAll');
	$("#visualAssetDescription").val ('');
	$('#visualAttachAssetWithSlide').attr('checked', true);
	$('#visualAttachAssetWithAssetGroup').attr('checked', true);
	$(form).get(0).reset();
	$(form).validate().resetForm();
	$(form).find('#isCustomTemplate').val('false');
	$('#visualUploader').uploader360().reset();
	$(form).find("#cboAssetTypeVisual").prop("disabled",false);
	$('#msgdiv').html ('');
	$(form).find('#imageWidth').val(0);
	$(form).find('#imageHeight').val(0);
	$(form).find('#duration').val(0);
	return false;
}

function cancelVideoAssetUploadForm() {
	$("#videoAssetName").val('');
	$("#frmUploadVideoAsset input[name=visualAssetKeywords]").tagsinput('removeAll');
	$("#visualAssetDescription").val ('');
	$('#visualAttachAssetWithSlide').attr('checked', true);
	$('#visualAttachAssetWithAssetGroup').attr('checked', true);
	$("#frmUploadVideoAsset").get(0).reset();

	$("label.error").hide();
	$(".error").removeClass("error");
	$('#msgdiv').html ('');
	$('#videoUploader').uploader360().reset();
	$("#frmUploadVideoAsset").validate().resetForm();
	return false;
}

$(function() {
    // Setup form validation on the #register-form element
	$.validator.addMethod("validateExtensionForVisualAsset",
		function(value, element) {
			var ext = value.split('.').pop().toLowerCase();
			if( $("#cboAssetTypeVisual").val()==="Image" && $.inArray(ext, ['gif','png','jpg','jpeg','bmp']) !== -1)
				return true;
			else if($("#cboAssetTypeVisual").val()==="MovieClip" && $.inArray(ext, ['flv','wmv','mp4']) !== -1)
				return true;
			else if($("#cboAssetTypeVisual").val()==="FlashObject" && $.inArray(ext, ['swf']) !== -1)
				return true;
			else
				return false;
		},
	"Please provide a file with a valid file type");
    $("#frmUploadVisualAsset").validate({
        // Specify the validation rules
        rules: {
        	visualAssetName: "required",
        	filePath:  {
									validateExtensionForVisualAsset : true
								}
        },
        // Specify the validation error messages
        messages: {
        	visualAssetName: "Please enter your asset name here",
        	filePath: {
        		validateExtensionForVisualAsset:"Uploaded file type is not supported."
        	}
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

  });

function populateVisualAssetModel(TrgModal,isOpen){
}




function populateAudioAssetModel(TrgModal,isOpen){

}
//-----------------------------------------------------------------------------------------------
//--------------  End   --------------Visual asset form script-----------------------------------
//-----------------------------------------------------------------------------------------------


function populateTemplateSelectionModel(TrgModal,isOpen){
	if(isOpen){
		var TemplateType =  $("#hidSlideTemplateIdForSetupForm_"+$("#sceneId").val()).val();
	if(TemplateType===Template_Visual_Left_ID){
			$("#temp_desc_area_TopLabel").text("Visual Left");
    		$("#temp_desc_area_TopDesc").text("This template puts a single visual component, such as a picture (JPG, GIF, PNG) or animation (SWF) on the left, and text on the right.");
			$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/IconVisualLeftTemplate2.png');
	}else if(TemplateType===Template_Visual_Right_ID){
			$("#temp_desc_area_TopLabel").text("Visual Right");
    		$("#temp_desc_area_TopDesc").text("This template puts text on the left, and a single visual component, such as a picture (JPG, GIF, PNG) or animation (SWF) on the right.");
			$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/IconVisualRightTemplate2.png');
	}else if(TemplateType===Template_Visual_Top_ID){
			$("#temp_desc_area_TopLabel").text("Visual Top");
    		$("#temp_desc_area_TopDesc").text("This template puts a single visual component, such as a picture (JPG, GIF, PNG) or animation (SWF), below the title, and text below that visual element.");
			$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/IconVisualTopTemplate2.png');
	}else if(TemplateType===Template_Visual_Bottom_ID){
			$("#temp_desc_area_TopLabel").text("Visual Bottom");
    		$("#temp_desc_area_TopDesc").text("This template puts text below the title, and a single visual component, such as a picture (JPG, GIF, PNG) or animation (SWF), below the text.");
			$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/IconVisualBottomTemplate2.png');
	}else if(TemplateType=== Template_Visual_Streaming_Center_ID){
			$("#temp_desc_area_TopLabel").text(WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_TXT);
			$("#temp_desc_area_TopDesc").text(WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_DES);
			$("#temp_desc_area_img").attr('src', WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_IMG);
	}
	}
}


function onTemplatesSelectionClick(TemplateType){
	$("#isCustomTemplate").val("false");
	if(TemplateType===Template_Visual_Left_ID){
			$("#temp_desc_area_TopLabel").text("Visual Left");
    		$("#temp_desc_area_TopDesc").text("This template puts a single visual component, such as a picture (JPG, GIF, PNG) or animation (SWF) on the left, and text on the right.");
			$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/IconVisualLeftTemplate2.png');
			$("#hidTemplateIdForSetupFormSelection").val(Template_Visual_Left_ID);
	}else if(TemplateType===Template_Visual_Right_ID){
			$("#temp_desc_area_TopLabel").text("Visual Right");
    		$("#temp_desc_area_TopDesc").text("This template puts text on the left, and a single visual component, such as a picture (JPG, GIF, PNG) or animation (SWF) on the right.");
			$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/IconVisualRightTemplate2.png');
			$("#hidTemplateIdForSetupFormSelection").val(Template_Visual_Right_ID);
	}else if(TemplateType===Template_Visual_Top_ID){
			$("#temp_desc_area_TopLabel").text("Visual Top");
            $("#temp_desc_area_TopDesc").text("This template puts a single visual component, such as a picture (JPG, GIF, PNG) or animation (SWF), below the title, and text below that visual element.");
			$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/IconVisualTopTemplate2.png');
			$("#hidTemplateIdForSetupFormSelection").val(Template_Visual_Top_ID);

	}else if(TemplateType===Template_Visual_Bottom_ID){
			$("#temp_desc_area_TopLabel").text("Visual Bottom");
    		$("#temp_desc_area_TopDesc").text("This template puts text below the title, and a single visual component, such as a picture (JPG, GIF, PNG) or animation (SWF), below the text.");
			$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/IconVisualBottomTemplate2.png');
			$("#hidTemplateIdForSetupFormSelection").val(Template_Visual_Bottom_ID);

	}else if(TemplateType === Template_Visual_Streaming_Center_ID){
			$("#temp_desc_area_TopLabel").text(WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_TXT);
			$("#temp_desc_area_TopDesc").text(WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_DES);
			$("#temp_desc_area_img").attr('src', WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_IMG);
			$("#hidTemplateIdForSetupFormSelection").val(Template_Visual_Streaming_Center_ID);

	}else if(TemplateType === Template_MC_Template_ID){
		$("#temp_desc_area_TopLabel").text(WLCMS_CONSTANTS_TEMPLATE_MC_Scenario.TEMPLATE_TYPE_FOR_STREAMING_TEXT);
		$("#temp_desc_area_TopDesc").text(WLCMS_CONSTANTS_TEMPLATE_MC_Scenario.TEMPLATE_TYPE_FOR_STREAMING_DES);
		$("#temp_desc_area_img").attr('src', WLCMS_CONSTANTS_TEMPLATE_MC_Scenario.TEMPLATE_TYPE_IMG);
		$("#hidTemplateIdForSetupFormSelection").val(Template_MC_Template_ID);
		$("#isCustomTemplate").val("true");

	}else if(TemplateType === Template_DND_Matching_Template_ID){
		$("#temp_desc_area_TopLabel").text('Drag and Drop Matching');
    	$("#temp_desc_area_TopDesc").text('This sorting template allows you to define the left and right side of a text-based match, with feedback for correct and incorrect user response.');
		$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/icon_14b.png');
		$("#hidTemplateIdForSetupFormSelection").val(Template_DND_Matching_Template_ID);
		$("#isCustomTemplate").val("true");

	}else if(TemplateType === Template_DND_Image_Template_ID){
		$("#temp_desc_area_TopLabel").text(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_IMAGE_TEXT_HEADING);
        $("#temp_desc_area_TopDesc").text(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_IMAGE_TEXT);
		$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/icon_25b.png');
		$("#hidTemplateIdForSetupFormSelection").val(Template_DND_Image_Template_ID);
		$("#isCustomTemplate").val("true");
	}else if(TemplateType === Template_DND_Category_Template_ID){
		$("#temp_desc_area_TopLabel").text(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_CATEGORY_TEXT_HEADING);
        $("#temp_desc_area_TopDesc").text(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_CATEGORY_TEXT);
		$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/icon_15b.png');
		$("#hidTemplateIdForSetupFormSelection").val(Template_DND_Category_Template_ID);
		$("#isCustomTemplate").val("true");
	}else if(TemplateType === Template_CharColumn_Template_ID){
    	$("#temp_desc_area_TopLabel").text(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_CharColumn_TEXT_HEADING);
    	$("#temp_desc_area_TopDesc").text(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_CharColumn_TEXT);
		$("#temp_desc_area_img").attr('src', 'theme/executive/img/icons/templates/icon_13b.png');
		$("#hidTemplateIdForSetupFormSelection").val(Template_CharColumn_Template_ID);
		$("#isCustomTemplate").val("true");
	}
}

function updtSlctdSldeTmpltUserConfirmed() {
	var slideId  = $("#sceneId").val();
	var selectedTemplateId = $("#hidTemplateIdForSetupFormSelection").val();
	var courseId = getParameterByName ('id');
    //function name definition for Ajax call that is defined into SlideController.java
	var updateSelectedSlideTemplate = "updateSelectedSlideTemplate";
	APP.AJAX({
		  url: updateSelectedSlideTemplate,
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'varSlideId=' + slideId + '&varSelectedSlideTemplateId=' + selectedTemplateId +'&courseId='+courseId,
		  async: false,
		  success: function(response) {
				 obj = $.parseJSON(response);
				 if(obj.status=="SUCCESS"){
					TopMessageBar.displayMessageTopBar({vType:1, vMsg: WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
					elementFadeOut('.messages');
				 }
		}
	 });
	toggleDisplayResolutionArea(false, "_" + $("#sceneId").val());
	 switch(selectedTemplateId){
		case Template_Visual_Left_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html("Visual Left");
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-0');
			toggleDisplayResolutionArea(false, "_" + $("#sceneId").val());
			break;
		case Template_Visual_Right_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html("Visual Right");
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-1');
			break;
		case Template_Visual_Top_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html("Visual Top");
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-2');
			break;
		case Template_Visual_Bottom_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html("Visual Bottom");
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-3');
			break;
		case Template_Visual_Streaming_Center_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html(WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_TXT_L);
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-10');
			toggleDisplayResolutionArea(true, "_" + $("#sceneId").val());
			break;
		case Template_MC_Template_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html('MC Scenario');
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-11');
			break;
		case Template_DND_Image_Template_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_IMAGE_TEXT_HEADING);
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-25');
			break;
		case Template_DND_Category_Template_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_CATEGORY_TEXT_HEADING);
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-15');
			break;
		case Template_CharColumn_Template_ID.toString():
			$("#slideTemplateNameForSetupForm_"+slideId).html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_CharColumn_TEXT_HEADING);
			$('#slideTemplateIconForSetupForm_'+slideId).attr('class', 'temp-icon-13');
			break;
	 }
	$("#hidSlideTemplateIdForSetupForm_"+$("#sceneId").val()).val(selectedTemplateId);
	$("#hidTemplateIdForSetupFormSelection").val('');
}


function isTemplateSlctnFrmForAddOrUpdate(isForAdd){
	// 1 means Template selction form open for Add
	// 2 means Template selction form open for update
	getSlideTemplateId();
	$("#isTemplateSlctnFrmForAddOrUpdate").val(isForAdd);
}


function setDefaultValueForAddSlides(TrgModal,isOpen){
	//set hidden field value for Template ID [Visual Right should be default Template for slide addition]
	$("#hidSlideTemplateIdForAddForm").val("");
	$("#displayselection").css("display","none");

}

function onPPTSlideExtracted(uploader,arg){
	var activityResult = JSON.parse(arg.activityResponse.data);
	$(uploader).find("#slides").val(activityResult.slides);
	arg.uploadResponse.data.filePath = activityResult.folderPath;
}

function getRelatedRefreshedModal (htmlPage){
		var modal = null;
		switch(htmlPage) {
		case 'UploadAudioAsset':
			modal = "#addAssetModal";
			cancelAudioAssetUploadForm();
		break;
		case 'UploadImageAsset':
			modal = "#addVisualAssetModal";
			cancelVisualAssetUploadForm();
			$(modal).find("#cboAssetTypeVisual").prop("disabled",true);
			$(modal).find("#cboAssetTypeVisual").val('Image');
		break;

	}
	if(modal) {
		var hdnIsCustomTemplate = $(modal).find('#isCustomTemplate');
		if(hdnIsCustomTemplate.length === 0) {
			$(modal).find('form').append("<input type='hidden' id='isCustomTemplate' name='isCustomTemplate' value='true' />");
		}
		else {
			hdnIsCustomTemplate.val(true);
		}
	}
	return modal;
}

function enableDisableEmbedCode(isEmbedCodeEnable,videoEmbedId,embedCode,isAllowAjaxCall){
	videoId=videoEmbedId;
	embedCodeId=embedCode;
	if(isEmbedCodeEnable){
		$(videoId).parent().parent().addClass("deactivate");
		$(embedCodeId).parent().removeClass("deactivate");
		$(embedCodeId).removeAttr("disabled");
		$(embedCodeId).prop("required",true);
	}else{
		$(embedCodeId).parent().addClass("deactivate");
		$(videoId).parent().parent().removeClass("deactivate");
		$(embedCodeId).attr("disabled","disabled");
		$(embedCodeId).removeAttr("required");
	}
	if(isAllowAjaxCall) {
       updateSlideEmbedCodeBit(isEmbedCodeEnable);
    }

function updateSlideEmbedCodeBit(isEmbedCode){
	varSlideId = $("#hidId").val();
	APP.AJAX({
			  url: "updateSlideEmbedCodeBit",
			  dataType: "text",
			  type: "POST",
			  cache: false,
			  data:'varSlideId='+varSlideId+'&isEmbedCode='+isEmbedCode,
			  async: false,
			  success: function(response) {
			   slide_Obj = $.parseJSON(response);
			}
	   });

	}
}

function isSlideHasData(){
	varSlideId = $("#hidId").val();
	if(isSlideTypeVSC){
		$("#video-icon_"+varSlideId).find("i").addClass("green");
	}
}


$(function() {
	$("#frmUploadVideoAsset").validate({
    // Specify the validation rules
    rules: {
    	videoAssetName: "required"
    },
    // Specify the validation error messages
    messages: {
    	videoAssetName: "Please enter asset name here"
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
	ignore: []
});


});