var updateContentObjectId = 0;
$(function() {

	showHideDualSave();

	$.validator.addMethod("nameEmptyCheck", function(value, element) {

		return value != "";
	}, "Please enter your title.");

	$.validator.addMethod("lloEmptyCheck", function(value, element) {

		return value != "";
	}, "Please enter your LLO.");

	$.validator.addMethod("lloCommaCheck", function(value, element) {

		return value.indexOf(',') == -1;
	}, "Please do not use comma in your LLO.");

	$.validator.addMethod("checkForDuplicate", function (value, element) {
		   valid = true;
		   var gotFirstTime = true;
		   var actualValue = value;

		   var contentId = $("#content_id").val();
		   var form = null;

		   if(contentId == null || contentId == 'undefined' || contentId == ""){
			   form = $("#addLessonform");
		   }else{
			   form = $("#lessonSetupFormEmbedded" + $("#content_id").val());
		   }
		   form.find("input.checkForDuplicate").not(this).each(function () {
		   if ($(this).val() !== "") {

		    var doesExisit = ($(this).val().toUpperCase() ==  actualValue.toUpperCase()) ? true : false;
		    if (doesExisit == true) {


		    	if(gotFirstTime == true){
		    		gotFirstTime = false;

		    	}else{

		    		valid = false;
		    	}
		    }
		   }
		  });
		  return valid;
		 }, "LLO can not be duplicate.");


    // Setup form validation on the #register-form element
    $("#addLessonform").validate({














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

        },
		errorClass: 'error',
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass(errorClass).addClass(validClass);

        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass);

        }
    });

APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");

});

function clearHiddenContentId(){
	 $("#content_id").val("");
}
function checkDuplicateLLO(contentId){

	var result = true;
	var bVallid = true;
	if(contentId == null || contentId == 'undefined' || contentId == ""){
		$("#addLessonform .checkForDuplicate").each(function() {  // first pass, create name mapping
			bVallid = $("#addLessonform").validate().element($(this));
			if(bVallid == false && result == true){
				result =  false;
			}
		});
	}else{

		$("#lessonSetupFormEmbedded" + contentId + " .checkForDuplicate").each(function() {  // first pass, create name mapping
			bVallid = $("#lessonSetupFormEmbedded" + contentId).validate().element($(this));
			if(bVallid == false && result == true){
				result =  false;
			}
		});
	}

	return result;

}

function onKeyFocusOut(form){
	var contentId = $("#content_id").val();
	checkDuplicateLLO(contentId);
}


 function showHideDualSave(){
 	  var shouldShow = $('#lessons_accordion_0').children().length == 0 && $('#exam_accordion_02').children().length == 0 ? false : true;

	  $("#btnSaveUp").toggle(shouldShow);
	  $("#btnPreviewCourse").toggle(shouldShow);
	  $("#btnSaveDown").toggle(shouldShow);

 }

function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam)
        {
            return sParameterName[1];
        }
    }
}

function cancelLesson() {
	$("#Name").val('');
	$("#Description").val ('');
	$("#learningObjective").val ('');

	$("#addLessonform").validate().resetForm();
	$('#submitLesson').attr("data-dismiss", "modal");

	$table = $('#addLessonLLOtable tbody');
	//	DELETE
	$table.find('tr').each(function(index,element){
			$(element).remove();
	});

	$('#msgdiv').html ('');
	return false;
}

function AddLesson() {

	console.log("course_structure::addLess - begin")

	course_id = getUrlParameter ('id');
	var content_object = null;
	var b_addLesson = false;
	var bValid = false;

	bValid = bValid = $("#addLessonform").validate().element(".nameEmptyCheck");
	var lloValid = checkDuplicateLLO();
	if (bValid == false || lloValid == false)
	{
		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
		$('#submitLesson').removeAttr("data-dismiss");
		return false;
	}

	var arrLLO = [];
	var index = 0;
	// make an array of learning objective
	$('#addLessonLLOtable > tbody > tr').each(function () {
			arrLLO[index++] = $(this).find(".form-control").val ();
    });


	// serialize array for submission
	arrLLO = JSON.stringify (arrLLO);

	APP.AJAX({
		url : 'addLesson?id='+course_id,
	data: { id: course_id, name: $("#Name").val(),description:$("#Description").val(), learningObject: arrLLO},
		//processData : false,
		//contentType : JSON,
		type : 'POST',
		async: false,
		success : function(data) {
			b_addLesson = true;
			content_object = eval(data);
		},
		error : function (data) {
			console.log ("error" + eval (data));
		}
	});

	if (b_addLesson)
	{
		var $divClone = $("div#lesson_accordion").clone();

		$divClone.attr("id", content_object.id);
		$divClone.attr("item_type", "ContentObject");

		$divClone.find("#label").click(function ()
		{
			getSlidesByContentObject(content_object.id );
		});
		$divClone.find("#setup").click(function (event)
		{
			getContentObjectForEdit2 (content_object.id, event );
		});
		$divClone.find("#lessonDeleteButton").click(function ()
		{
			remove_panel(this, 1 , content_object.id);
		});
		$divClone.find("#slide_1").attr("id", "slide_acc_" +content_object.id);

		$divClone.find("#setup").attr("href", "#setup_" +content_object.id);
		$divClone.find("#label").attr("href", "#list_" +content_object.id);
		$divClone.find ("#label").append("<i class='icon-folder-open'></i> " );

		$divClone.find ("#label").append("<span id='lesson_panel-title_"+content_object.id+"'>"+ htmlEncode($("#Name").val()) +"</span>");
		$divClone.find("#label").attr("title", "Lesson Components");

		$divClone.find("#Name").attr("id", "txtTitle_" +content_object.id);
		$divClone.find("#Description").attr("id", "txtDesc_" +content_object.id);
		$divClone.find("#learningObjective").attr("id", "txtlearningObjective_" +content_object.id);

		// Change this so that new slides can be appended
		$divClone.find("#slide_0_0").attr("id", "slide_" + content_object.id);

		$divClone.find("#addSlideID").attr("data-lesson-id",  content_object.id);
		$divClone.find("#importPowerPointID").attr("data-lesson-id",  content_object.id);
		$divClone.find("#addVideoSlideID").attr("data-lesson-id",  content_object.id);

		// Add Quiz Link Setup
		$divClone.find("#addQuizID").attr("data-lesson-id",  content_object.id);
		$divClone.find("#quiz_container").attr("id",  'quiz_container'+ content_object.id);
		$divClone.find("#addQuizLinkDiv_").attr("id",  'addQuizLinkDiv_' + content_object.id);

		$divClone.find("#setup_0").attr("id", "setup_" + content_object.id);
		$divClone.find("#list_0").attr("id", "list_" + content_object.id);

		$divClone.find('#lessonSetupFormEmbedded').attr('id', 'lessonSetupFormEmbedded'+content_object.id);

		$divClone.find('#addLessonFooter').attr('href', '#list_'+content_object.id);
		$divClone.find("#supportMaterial_").attr("id", "supportMaterial_" + content_object.id);

		$("div#lessons_accordion_0").append($divClone);
		$divClone.show();
		showHideDualSave();
		$("#lesson_panel-title_" +content_object.id ).click();
	}

	$("#Name").val('');
	$("#Description").val ('');
	$("#learningObjective").val ('');
	$('#addLessonLLOtable > tbody').html ('');
	$('#submitLesson').attr("data-dismiss", "modal");
	$("#addLessonform").get(0).reset();
	SetArrowVisibilityContentObject();
	console.log("course_structure::addLess - end");
	getSlidesByContentObject(content_object.id);

}

function getContentObjectForEdit2(content_id, e) {

	bValid = false;

	updateContentObjectId = content_id;

	if($("#hidId").val()==content_id && $('#setup_'+content_id).hasClass( "panel-collapse bg-gray-2 a1 collapse in" )) {

		if ($("#lessonSetupFormEmbedded" + content_id).find('.checkForDuplicate').length > 0) {

			bValid = checkDuplicateLLO(content_id);
			if (!bValid){
				TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
				e.preventDefault();
				e.stopPropagation ();
				return false;
			}
		}

		if (!$("#lessonSetupFormEmbedded" + content_id).find('#txtTitle_'+content_id).val()) {
			TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
			$("#lessonSetupFormEmbedded" + content_id).find('#txtTitle_'+content_id).addClass('error');
			e.preventDefault();
			e.stopPropagation ();
			return false;
		}

	}



	if($("#hidId").val()==content_id && $('#setup_'+content_id).hasClass( "panel-collapse bg-gray-2 a1 collapse in" ))
	{
		save();
	}else{

		$("#content_id").val("");
		var obj = null;
		b_setup = false;

		resetHidenForSave();
		$("#hidFunctionName").val("updateContentObject");
		$("#hidId").val(content_id);
		$("#hidLessonId").val(content_id);


		getContentObjectSetup (content_id, e);

		// This variable help to keep tracking of open box and their controls
		APP.CACHE_BAR = content_id;

		//bootstrap
		APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");
	}
}

function getContentObjectSetup (content_id, e) {

		courseDescriptionOverlayUrl = "getContentObjectForEdit";
		APP.AJAX({
				url: courseDescriptionOverlayUrl,
				dataType: "text",
				type: "POST",
				cache: false,
				data:'varCobjectId='+content_id,
				async: false,
				success: function(response) {
					 obj = $.parseJSON(response);
					 b_setup = true;
				}
		});

		if (b_setup) {
			var $lessondiv = $("div#" + content_id);
			$lessondiv.find("#setup_0").attr("id", "setup_" + content_id);

			var $divClone = $("div#setupform").clone();

			$divClone.attr("id", content_id);

			$divClone.find ("#name").val (obj.name);
			$divClone.find ("#description").val (obj.description);

			$divClone.find ('#name').attr("id", "txtTitle_" + content_id);
			$divClone.find ('#description').attr("id", "txtDesc_" + content_id);
			$divClone.find ('#learningObjective').attr("id", "txtlearningObjective_" + content_id);

			$divClone.find('#lessonSetupFormEmbedded').attr('id', 'lessonSetupFormEmbedded'+content_id);

			$divClone.find ("#footer").attr("href", "#setup_" +content_id);

			/*
				Lesson Learning Object Listing
			*/
			lstContentObject = obj.lstContentObject;

			$("#content_id").val(content_id);


			if(lstContentObject.length < 1){
				$divClone.find('#removeObjective').attr('disabled','disabled');
				$divClone.find('#learningObjectiveCheckbox').attr('disabled','disabled');
			}else{

				$divClone.find('#removeObjective').attr('removeAttr');
				$divClone.find('#learningObjectiveCheckbox').attr('removeAttr');
			}
			for ( var i = 0; i < lstContentObject.length;  i++)
			{
				var LLOtr =  $('textarea#LLOtemplate').val();
				var $LLOtr = $(LLOtr);
				$LLOtr.find ('#LLOname').attr ('value',lstContentObject[i].name);
				$LLOtr.find ('#LLOname').attr ('id',lstContentObject[i].id);
				$divClone.find ('#llotable2 tbody').append ($LLOtr);
				LLOtr = '';
			}
			$divClone.show ();
			$lessondiv.find("#setup_" + content_id ).html($divClone);
		}
}

function deleteLLO (id, contentObject_id)
{
	if (id.indexOf("LLO") >= 0)
		return;

	APP.AJAX({
		url: 'deleteLLO',
		dataType: "text",
		type: "POST",
		cache: false,
		data:'lessonId='+id +'&contentObject_id='+contentObject_id,
		async: false,
		success: function(response) {

		}
   });
}

function getSlideSetup(varSlideId, event){
	 parent_id = $("#slide_1_" + varSlideId).parent ().attr('id');
	 console.log (parent_id);
		if(!($("#hidId").val()==varSlideId && $('#slideSetup_2_'+varSlideId).hasClass( "panel-collapse a1 collapse in" )))
		{

			//set hidden field value for save button
			resetHidenForSave();
			$("#hidFunctionName").val("updateSlide");
			$("#hidId").val(varSlideId);

			// slide Id setting
			$("#sceneId").val(varSlideId);

			 courseDescriptionOverlayUrl = "/lcms/getSlideById";

			 APP.AJAX({
						  url: courseDescriptionOverlayUrl,
						  dataType: "text",
						  type: "POST",
						  cache: false,
						  data:'varSlideId='+varSlideId,
						  async: false,
						  success: function(response) {
								 obj = $.parseJSON(response);
						}
				   });

			var $divClone = $("div#slideSetup").clone();

			 $('#slideSetup_2_'+varSlideId).remove("");

			// change id of parent div of slide setup
			$divClone.attr("id", "slideSetup_2_"+varSlideId);


			// set controls values
			var slideTitle = $divClone.find ('#title');



			var editor = CKEDITOR.instances["cke_txtTitle_" + varSlideId];


			slideTitle.val(obj.name);

			$divClone.find ('#duration').val (obj.duration);

          	$divClone.find ('#slidsetupform_').attr("id", "slidsetupform_" + varSlideId);
			$divClone.find ('#slidsetupform_' + varSlideId).attr("name", "slidsetupform_" + varSlideId);

			$divClone.find ('#hidSlideTemplateIdForSetupForm').attr("id", "hidSlideTemplateIdForSetupForm_" + varSlideId);
			$divClone.find ('#hidSlideTemplateIdForSetupForm_'+varSlideId).val (obj.templateID);

			// change template icon '<i tag' id
			$divClone.find ('#slideTemplateIconForSetupForm').attr("id", "slideTemplateIconForSetupForm_" + varSlideId);

			$divClone.find ('#slideTemplateNameForSetupForm').attr("id", "slideTemplateNameForSetupForm_" + varSlideId);

			getSlideTemplateId();
			switch(String(obj.templateID)){
				case Template_Visual_Left_ID.toString():

					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html("Visual Left");
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-0');
					break;
				case Template_Visual_Right_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html("Visual Right");
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-1');
					break;
				case Template_Visual_Top_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html("Visual Top");
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-2');
					break;
				case Template_Visual_Bottom_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html("Visual Bottom");
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-3');
					break;
				case Template_Visual_Streaming_Center_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html(WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_TXT_L);
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-10');
					break;
				case Template_MC_Template_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html("MC Scenario");
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-11');
					break;
				case Template_DND_Matching_Template_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html("Drag and Drop Matching");
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-14');
					break;
				case Template_DND_Image_Template_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_IMAGE_TEXT_HEADING);
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-25');
					break;
				case Template_DND_Category_Template_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_CATEGORY_TEXT_HEADING);
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-15');
					break;
				case Template_CharColumn_Template_ID.toString():
					$divClone.find ('#slideTemplateNameForSetupForm_'+varSlideId).html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_CharColumn_TEXT_HEADING);
					$divClone.find ('#slideTemplateIconForSetupForm_'+varSlideId).attr('class', 'temp-icon-13');
					break;
			}


			// change slides controls name
			$divClone.find ('#title').attr("name", "txtTitle_"+ varSlideId);
			$divClone.find ('#duration').attr("name", "txtduration_"+ varSlideId);
			$divClone.find ('#displayRatioWidescreen').attr("name", "displayRatio_"+ varSlideId);
			$divClone.find ('#displayRatioStandard').attr("name", "displayRatio_"+ varSlideId);

			// change slides controls id
			$divClone.find ('#title').attr("id", "txtTitle_"+ varSlideId);
			$divClone.find ('#duration').attr("id", "txtduration_"+ varSlideId);
			$divClone.find ('#displayRatioWidescreen').attr("id", "displayRatioWidescreen_"+ varSlideId);
			$divClone.find ('#displayRatioStandard').attr("id", "displayRatioStandard_"+ varSlideId);
			$divClone.find ('#displayselection').attr("id", "displayselection_"+ varSlideId);
			$divClone.find ('#displayselection_'+varSlideId).attr('style', 'display: none');

			// change slides footer div id that setup form can close
			$divClone.find ("#footer").attr("href", "#slideSetup_2_" +varSlideId);
			$divClone.find ("#footer").attr("data-parent", "#" + parent_id);

			if(obj.templateID == Template_Visual_Streaming_Center_ID.toString())
			{
				$divClone.find ("#displayselection_"+varSlideId).css("display","block");
				if(obj.displayStandardTF == true)
				{
				    $divClone.find ("#displayRatioStandard_"+ varSlideId)[0].checked = true;
				}
			     else
				{
			    	$divClone.find ("#displayRatioWidescreen_"+ varSlideId)[0].checked = true;
				}
			}
			else
			{
				$divClone.find ("#displayselection_"+varSlideId).css("display","none");
			}





			// adding div into parent
			$("div#slide_1_"+varSlideId).append($divClone);



			//validation slidesetup form
			var form = $divClone.find ('#slidsetupform_' + varSlideId);
			form.validate();

			$('#txtTitle_'+varSlideId).rules( "add", {
				required: true,
				  messages: {
					  required: WLCMS_LOCALIZED.TITLE_REQUIRED_MESSAGE
				  }
				});

			var txtDuration = form.find('#txtduration_'+varSlideId);
			$('#txtduration_'+varSlideId).rules( "add", {
			  digits: true,
			  messages: {
				  digits: WLCMS_LOCALIZED.WHOLE_NUMBER_FILED_MESSAGE
			  }
			});






			APP.CKEDITOR("txtTitle_" + varSlideId, "TITLE");

		}else{
			save(event);
		}
		APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");
}

function updateSlide(varSlideId){

	/*
		Check for Validation
	*/

		var allInstances=CKEDITOR.instances;

	for ( var i in allInstances ){

		if(allInstances[i].name == 'txtTitle_'+varSlideId){

			var vName = allInstances[i].getData();
			 allInstances[i].updateElement();
		}
	}

	var  form = $("#slidsetupform_"+ varSlideId);

	var vValidTitle = form.validate().element('#txtTitle_'+varSlideId);
	var vValidDuration = form.validate().element('#txtduration_'+varSlideId);

	if (!$('#txtTitle_'+varSlideId).val() || !vValidTitle || vValidDuration == false) {
		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
		$('#txtTitle_'+varSlideId).addClass('error');
		return false;
	}

	var bValid = false;


	courseDescriptionOverlayUrl = "/lcms/updateSlide";

	var title = $('#txtTitle_'+varSlideId).val();

	var titleDec	= htmlDecode(title);
	var titleEscaped = escape(title);

	var displayRatio_Standard = 0 ,displayRatio_Widescreen = 0 ;


	if($('#displayRatioStandard_' +varSlideId)[0].checked ==  true)

		{
		displayRatio_Standard = true;
		}

	else
		{
		displayRatio_Widescreen = true ;
		}


	APP.AJAX({
    	  url: courseDescriptionOverlayUrl,
          dataType: "text",
		  type: "POST",
          cache: false,
          data:'varSlideId='+varSlideId+ '&varTitle='+ titleEscaped +'&varDuration='+$('#txtduration_'+varSlideId).val() + '&displayRatioStandard=' + displayRatio_Standard + '&displayRatioWidescreen=' + displayRatio_Widescreen
          ,
          async: false,
          success: function(response) {
		         obj = $.parseJSON(response);









		}
	 });
	var title =$('#txtTitle_'+varSlideId).text();
	$("#slide_panel_title_"+varSlideId).text("");


	$("#slide_panel_title_"+varSlideId).text(titleDec);
	return true;
}

function elementFadeOut(id)
{
	setTimeout(function(){
        $(id).html('');
    },9000);
}



//-----------------------------------------------------------------------------------------------
//Start - Function use to get Slides by Content Object
//-----------------------------------------------------------------------------------------------
function getSlidesByContentObject(contentObjectId){

	if($('#list_'+contentObjectId).hasClass( "panel-collapse bg-gray-2 a2 collapse in" ))
		return;

	getSlidesByContentAjax (contentObjectId);
}

function getSlidesByContentAjax (contentObjectId){
	courseDescriptionOverlayUrl = "/lcms/getSlidesByContentObject";
	$("#hidLessonId").val(contentObjectId);
	APP.AJAX({
	    	  url: courseDescriptionOverlayUrl,
	          dataType: "text",
			  type: "POST",
	          cache: false,
	          data:'varCobjectId='+contentObjectId,
	          async: false,
	          success: function(response) {
			         obj = $.parseJSON(response);
					 showSlides(obj, contentObjectId);

			}
	   });

	   // change IDs for Quiz object.
	quizbar = $("#quizbar").clone ();
	quizbar = $(quizbar).html();
	$quizbar = $(quizbar);


	$quizbar.find ('#lesson_1_quiz_1_policies_').attr('id','quiz_policies_' + contentObjectId );
	$quizbar.find ('#lesson_1_quiz_1_banks_').attr('id','quiz_banks_' + contentObjectId);
	$quizbar.find ('#quizBankTitle').attr('id','quizBankTitle_' +  contentObjectId);
	$quizbar.find ('#quizSetupTitle').attr('id', 'quizSetupTitle_' + contentObjectId);

	$quizbar.find ('#quizBankTitle_' +  contentObjectId).attr('href','#quiz_banks_' +  contentObjectId);
	$quizbar.find ('#quizSetupTitle_' + contentObjectId).attr('href', '#quiz_policies_' + contentObjectId);


	$quizbar.find ('#quizBankTitle_' +  contentObjectId).attr('data-lesson-id', contentObjectId);
	$quizbar.find ('#quizSetupTitle_' +  contentObjectId).attr('data-lesson-id', contentObjectId);

	$quizbar.find ('#quizSetupTitle_' + contentObjectId).attr('data-parent', '#quiz_container' + contentObjectId);
	$quizbar.find ('#quizBankTitle_' + contentObjectId).attr('data-parent', '#quiz_container' + contentObjectId);


	$('#list_' + contentObjectId).find('#quiz_container').attr('id','quiz_container' + contentObjectId);
	$('#list_' + contentObjectId).find('#quiz_container' + contentObjectId).html ($quizbar);

	// get ALLOWQUIZTF field from CONTENTOBJECT table
	// use this field to decide weather to display 'add quiz' link or not
	courseDescriptionOverlayUrl = "/lcms/getContentObjectAllowQuiz";

	APP.AJAX({
    	  url: courseDescriptionOverlayUrl,
          dataType: "text",
		  type: "POST",
          cache: false,
		  data:'varCobjectId='+contentObjectId,
          async: false,
          success: function(response) {
		  obj = $.parseJSON(response);
			if(obj.allowQuiz=="1")
			{
				$("#addQuizLinkDiv_"+contentObjectId).css("display", "none");
				$("#quiz_container"+contentObjectId).css("display", "block");
			}else{
				$("#addQuizLinkDiv_"+contentObjectId).css("display", "block");
				$("#quiz_container"+contentObjectId).css("display", "none");
			}
		}
	});


	// use this function to get Support Material list
	var getSupportMaterialListUrl = "/lcms/getSupportMaterialList";

	APP.AJAX({
    	  url: getSupportMaterialListUrl,
          dataType: "text",
		  type: "POST",
          cache: false,
		  data:'varCourseId='+contentObjectId,
          async: false,
          success: function(response) {
		  obj = $.parseJSON(response);
			showSupportMaterialList(obj,contentObjectId);
		}
	});
}

function showSupportMaterialList(data, contentObjectId){

	$('#supportMaterial_'+contentObjectId).html("");

	for ( var i = 0, len = data.length; i < len; ++i) {
		var arrSupportMaterial = data[i];
		var $divClone = $("div#supportMaterialStrip").clone();

		$divClone.find("#divSupportMaterial_Setup").attr("id", "divSupportMaterial_Setup_"+arrSupportMaterial.id);
		$divClone.find ("#linkSupportMaerial").attr("onclick", 'getSupportMaterialInfo('+ arrSupportMaterial.id +','+ contentObjectId + ')' );

		$divClone.attr("id", "supportMaterialStrip_"+contentObjectId+"_"+arrSupportMaterial.id );

		$divClone.find('#linkSupportMaerial').attr('href', "#divSupportMaterial_Setup_"+arrSupportMaterial.id);
		$divClone.find('#linkSupportMaerial').attr('data-parent', "#supportMaterial_"+contentObjectId);

		$divClone.find ('#supportMaterial_title').text(arrSupportMaterial.assetName);
		$divClone.find ("#supMaterialDelButton").attr("onclick", 'remove_panel(this, 7 , '+ arrSupportMaterial.id +')' );
		$("div#supportMaterial_"+contentObjectId).append($divClone);
		$divClone.show();
	}

	setArrowVsbltySpprtMtrl(contentObjectId);
}

function getSupportMaterialInfo(SupportMaterialId, contentObjectId){

	// use this url hit to get Support Material's Asset information
	var getSupportMaterialDetailUrl = "/lcms/getSupportMaterialDetail";
	APP.AJAX({
    	  url: getSupportMaterialDetailUrl,
          dataType: "text",
		  type: "POST",
          cache: false,
		  data:'varLessonId='+SupportMaterialId,
          async: false,
          success: function(response) {
		  obj = $.parseJSON(response);

		}
	});

	var $divClone = $("div#supportMaterialSetup").clone();
	$divClone.attr("id", "supportMaterialSetup_"+SupportMaterialId );
	$divClone.find ('#suppMatSetupfooter').attr('data-parent', '#supportMaterialStrip_' + SupportMaterialId + '_' +contentObjectId);
	$divClone.find ('#suppMatSetupfooter').attr('href', '#divSupportMaterial_Setup_' + SupportMaterialId);

	$divClone.find ('#audioAssetName').val(obj.assetName);
	$divClone.find ('#audioAssetDescription').val(obj.description);
	$divClone.find ('#supportMatFileName').val(obj.fileName);
	$divClone.find ('#supportMatFilePath').attr('href', obj.location);

	var keywordItems = obj.keywords.split(',');
	var arrkeyword =[];

	if(obj.keywords.length>0){
		for (var i = 0; i < keywordItems.length; i++){
			arrkeyword.push("<span class='tag label label-info'>"+ keywordItems[i] +"</span>");
		}

		$divClone.find("#smKeywords").html(arrkeyword.join(''));
	}else{
		$divClone.find("#smKeywords").html("<input type='text' disabled>");
	}

	$divClone.css("display", "block");
	$("div#divSupportMaterial_Setup_"+SupportMaterialId).html("");
	$("div#divSupportMaterial_Setup_"+SupportMaterialId).append($divClone);
}

function showSlides(data, contentObjectId) {

	//set hidden field value for save button
  resetHidenForSave();
  $("#hidFunctionName").val("updateContentObject");
  $("#hidId").val(contentObjectId);

  console.log (contentObjectId);

  $('#slide_acc_'+contentObjectId).html("");
      for ( var i = 0, len = data.length; i < len; ++i) {
          var varSlide = data[i];
			$( "#slide_acc_" + contentObjectId).append("<div item_type='Scene' class='panel panel-default' id='slide_1_"+ varSlide.id +"' lessonScene_id='"+varSlide.contentObjectScene_id+"'><div class='panel-heading'>"+
			"<div class='holder'><div class='anchors move-btn'><a class='up' title="+ WLCMS_LOCALIZED.TITLE_SHOW_SLIDE_MOVE_UP +" href='javascript:;'><i class='icon-arrow-up'></i></a><a href='javascript:;' class='down' title="+ WLCMS_LOCALIZED.TITLE_SHOW_SLIDE_MOVE_DOWN +" ><i class='icon-arrow-down'></i></a></div>"+
			"<div class='anchors'><a title='Slide Setup' onclick='getSlideSetup("+ varSlide.id +", event)' class='accordion-toggle' data-toggle='collapse' data-parent='#slide_acc_"+ contentObjectId +"' href='#slideSetup_2_"+varSlide.id+"'><i class='icon-cog'></i></a></div>"+
			"<div class='panel-title'><a onclick='getSlideComponent("+ varSlide.id +");isSlideHasData()' class='accordion-toggle' data-toggle='collapse' title = 'Slide Components' data-parent='#slide_acc_"+ contentObjectId +"' href='#slideSetup_1_"+varSlide.id+" '><i class='icon-file'></i> <span id='slide_panel_title_"+ varSlide.id +"'>"+ $('<div/>').html(varSlide.name).text() +"</span></a></div>"+
			"<div onclick='remove_panel(this, 2 , "+ varSlide.id +")' class='remove-btn' title="+WLCMS_LOCALIZED.LABEL_TEXT_DELETE+" ><i class='icon-remove'></i></div></div></div></div>");

			// change IDs for Quiz object.
			$("#lesson_1_quiz_1").attr('id','quiz_' + contentObjectId);
  }
      SetArrowVisibilityScene(contentObjectId);
	var divContentObject = $("div#list_"+contentObjectId);
	divContentObject.find("#lessonsCompFooterLink_CloseLesson").attr("href", "#list_"+contentObjectId);

}// Function End
//-----------------------------------------------------------------------------------------------
//End - Function use to get Slides by Content Object
//-----------------------------------------------------------------------------------------------


//-----------------------------------------------------------------------------------------------
//Start - Function use to get Content Object and display in Edit Mode
//-----------------------------------------------------------------------------------------------
function updateContentObject(contentObjectId){

	if (!checkDuplicateLLO (contentObjectId))
		return false;

	arrLLO = [];
	arrLLOId = [];
	indexLLO = 0;
	/*
		Check for Validation
	*/

	$tr = $('#setup_'+contentObjectId).find('#llotable2 > tbody > tr');
	$tr = $tr.find (".lloEmptyCheck");
	// make an array of learning objective
	$tr.each(function () {
		if ($tr.has('.lloEmptyCheck')) {
			arrLLO[indexLLO] = $(this).val ();
			arrLLOId[indexLLO++] = $(this).attr ('id');
		}
	});

    var titleText = $('#txtTitle_'+contentObjectId).val();
	var titleTextEscaped = escape(titleText);
	course_id = getUrlParameter ("id");
	courseDescriptionOverlayUrl = "updateContentObject";
	APP.AJAX({
		  url: courseDescriptionOverlayUrl,
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'varCobjectId='+contentObjectId+ '&varName='+titleTextEscaped+'&varDescription='+$('#txtDesc_'+contentObjectId).val()+'&varlearningObjective='+arrLLO + '&varlearningObjectiveIds='+arrLLOId + '&id='+course_id,
		  async: false,
		  success: function(response) {
				 obj = $.parseJSON(response);

				 if(obj.status=="SUCCESS"){









				 }
			APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");
		}
	 });

	$("#lesson_panel-title_"+contentObjectId).text("");
	$("#lesson_panel-title_"+contentObjectId).text($('#txtTitle_'+contentObjectId).val());
	return true;

}//**** Function end  ****//

function embedCodeSave(embedCode){

	slideId = $("#hidId").val();
	embedHidId = "#hidEmbedCode_"+slideId;
	embedCode = $("#embedCode_"+slideId).val();
	isEmbedCodeVideoChecked=true;
	if(embedCode === undefined)
		return;

	if($("#embedCode_"+slideId).attr("required") && !$("#embedCode_"+slideId).valid())
		return;
	if($("#embedCodeVideo_"+slideId).is(":checked")){
		isEmbedCodeVideoChecked=false;
	}

		APP.AJAX({
			url: "updateSlideEmbedCode",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'varSlideId='+slideId+'&embedCode='+(embedCode)+'&isEmbedCodeValUpdate='+isEmbedCodeVideoChecked,
			async: false,
			success: function(response) {
				 obj = $.parseJSON(response);
				 if(obj.status == 'SUCCESS'){
						$(embedHidId).val(embedCode);
						isSlideTypeVSC = true;
						isSlideHasData();
					}


			}
		});




}

function embedCodeSaveForUploadVideo(embedCode){

	slideId = $("#hidId").val();
	if(!$("#embedCodeRadioModal").is(":checked") || !$("#embedCodeModal").valid())
		return;
		APP.AJAX({
			url: "updateSlideEmbedCode",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'varSlideId='+slideId+'&embedCode='+embedCode+'&isEmbedCodeValUpdate=true',
			async: false,
			success: function(response) {
				 obj = $.parseJSON(response);
				 if(obj.status == 'SUCCESS'){
						activateEmbedCodeVideoModal();
						$("#embedCode_"+slideId).val(embedCode);
						isSlideTypeVSC = true;
						isSlideHasData();



						$(".sel").click();
						$(".sel").attr("checked",true);

					}


			}
		});




}

function activateEmbedCodeVideoModal(){

	$("#embedCodeVideoModal").attr('checked', 'checked');
	enableDisableEmbedCode(false,"#embedCodeVideoModal","#embedCodeModal",false);

}


function save(event){


















	var bank_id    			= $("#hidBankId").val();
	var contentObject_id    = $("#hidLessonId").val();
	var question_id 		= $("#hidQuestionId").val();

	if(bank_id!="0" && contentObject_id!="0" && question_id!="0")
	{

		updateQuestion(bank_id, contentObject_id, question_id, event);
		if($("#hidQuestionMessageId").val() === "-1")
		{
			$('input[name=hidQuestionMessageId]').val("0");
			return false;
		}

	}
	var bSuccess = true;
	var varClass = $("#setup_"+$('#hidId').val()).attr('class');
	var varHiddenFieldVal = $('#hidFunctionName').val();


	if( (varHiddenFieldVal=='updateContentObject') && ( varClass=="panel-collapse bg-gray-2 a1 collapse in"))
	{
		if(!updateContentObject($('#hidId').val())) {
			return false;
		} else {
			getContentObjectSetup ($('#hidId').val(), event );
			console.log ("SAVE :: getContentObjectForEdit2 ")
		}

	}else if((varHiddenFieldVal=='updateSlide')){
		var varClass = $("#slideSetup_2_"+$('#hidId').val()).attr('class');
		if(varClass=="panel-collapse a1 collapse in")
			if (!updateSlide($('#hidId').val())){
				if(event != 'undefined' || event != null){
					event.preventDefault();
					event.stopPropagation ();
				}
				return false;
			}
	}else if(varHiddenFieldVal=='MC_SCENE_XML'){
		if(!update_MC_SCENE_XML($('#sceneId').val()))
			return;
	}

	var divCloneTxT = $("div#slide_1_"+ $('#sceneId').val()).find("div#SceneTextEditor");
	if(divCloneTxT.css('display') == 'block'){
		getSlideTextEditor(document.getElementById("Title_1_"+ $('#sceneId').val()), "2");
	}

	var divCloneCC = $("div#slide_1_"+ $('#sceneId').val()).find("div#SceneCloseCaptionEditor");
	if(divCloneCC.css('display') == 'block'){
		getSlideCloseCaptionEditor(document.getElementById("ClosedCaptioning_1_"+ $('#sceneId').val()), "2");
	}

	//Quiz Configuration
	if($("#hidQuizContentObjectId").val() != 0)
	{
		var hidQuizContentObjectId = $("#hidQuizContentObjectId").val();
		bSuccess = updateQuizConfigurationValidation($("#hidQuizContentObjectId").val(), event);
		$("#hidQuizContentObjectId").val(hidQuizContentObjectId);//reset 0 value to container id so validation could work after first save
	}

	if($("#hidQuizNoOfQuestionContentObjectId").val()!=0 && $("#hidQuizNoOfQuestionBankId").val()!=0)
	{
		postQuestionNo_Quiz(getUrlParameter ("id"),$("#hidQuizNoOfQuestionBankId").val(),$("#hidQuizNoOfQuestionContentObjectId").val());
	}


	if($("#hidQuizQuestionContentObjectId").val()!=0 && $("#hidQuizQuestionBankId").val()!=0 && $("#hidQuizQuestionId").val()!=0)
	{
		updateQuestion ($("#hidQuizQuestionBankId").val(), $("#hidQuizQuestionContentObjectId").val(), $("#hidQuizQuestionId").val());
	}

 	if($("#hidGetExamforEdit").val() != 0)
 	{
		if(!updateExam(true)){
			bSuccess =  false;
		}
 	}
	if($("#hidExamQuestionBankId").val()!= 0)
	{
		postQuestionNo_Exam(getUrlParameter ("id"),$("#hidExamQuestionBankId").val());
	}

	if($("#hidExamQuestionId").val() != 0)
	{
		if(!updateExamQuestion($("#hidExamQuestionId").val())){
			bSuccess = false;
		}
	}

	if(bSuccess){
		$('#successMsg').html("");
		$('#successMsg').append("<div  class='messages'>"+
			"<div role='alert' class='alert alert-success alert-dismissible fade in'>"+
			"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
			WLCMS_LOCALIZED.SAVE_MESSAGE+"</div></div>");
		elementFadeOut("#successMsg");
	}


	//WLCMS-496
	if(activeEditorForText!=null)
		activeEditorForText.resetDirty();
	if(activeEditorForCC!=null)
		activeEditorForCC.resetDirty();

}

function resetHidenForSave(){














}

//-----------------------------------------------------------------------------------------------
//End - Function use to get Content Object and display in Edit Mode
//-----------------------------------------------------------------------------------------------

$(document).ready(function(){
	$(document).on('click','.move-btn > .up',function() {
		var textarea = [];
		var parent = $(this).parent().parent().parent().parent();














		parent.find('textarea').each(function(i, element) {
			textarea[i] =  $(element).attr('id');
			var editorInstance = CKEDITOR.instances[textarea[i]];
			if (editorInstance){
				editorInstance.destroy();
			}
		});

		parent.insertBefore(parent.prev());
		parent.find('.up').first().css('visibility', 'visible');
		parent.find('.down').first().css('visibility', 'visible');
		parent.next().find('.up').first().css('visibility', 'visible');
		parent.next().find('.down').first().css('visibility', 'visible');






		$.each( textarea, function( key, value ) {
			APP.CKEDITOR(value, $( '#' + value).attr ('data-config'));
		});




		var item_type = parent.attr("item_type");

		if(item_type == 'ContentObject'){
			var itemId = parent.attr("id");
			SetArrowVisibilityContentObject();
		}
		else if(item_type == 'SupportMaterial'){
			sDivIds = parent.attr('id').split('_');
			setSupportMaterialOrder(sDivIds[1], sDivIds[2], 1); // '1' means 'Up'
			return;
		}else{
			item_type ='Scene';

			itemId = parent.attr("lessonscene_id");
			var contentObjectId = parent.parent().attr('id').replace('slide_acc_','');
			SetArrowVisibilityScene(contentObjectId);
		}

		setCourseDisplayOrder(itemId,item_type,1);
	});

	$(document).on('click','.move-btn > .down',function() {
		var textarea = [];
		var parent = $(this).parent().parent().parent().parent();
		$(this).parent().parent().slideDown();
		/*if (parent.find('.caption').text ().trim().contains('Slide Setup')  ) {
			textarea =  parent.find('textarea').attr('id');
			var editorInstance = CKEDITOR.instances[textarea];
			if (editorInstance){
				editorInstance.destroy();
			}
		}*/
		parent.find('textarea').each(function(i, element) {
			textarea[i] =  $(element).attr('id');
			var editorInstance = CKEDITOR.instances[textarea[i]];
			if (editorInstance){
				editorInstance.destroy();
			}
		});

		parent.insertAfter(parent.next());
		parent.find('.up').first().css('visibility', 'visible');
		parent.find('.down').first().css('visibility', 'visible');
		parent.prev().find('.up').first().css('visibility', 'visible');
		parent.prev().find('.down').first().css('visibility', 'visible');






		$.each( textarea, function( key, value ) {
			APP.CKEDITOR(value, $( '#' + value).attr ('data-config'));
		});


		var item_type = parent.attr("item_type");
		if(item_type == 'ContentObject'){
			var itemId = parent.attr("id");
			SetArrowVisibilityContentObject();
		}
		else if(item_type == 'SupportMaterial'){
			sDivIds = parent.attr('id').split('_');
			setSupportMaterialOrder(sDivIds[1], sDivIds[2], 0); // '0' means 'down'
			return;
		}
		else{
			item_type ='Scene';
			itemId = parent.attr("lessonscene_id");
			var contentObjectId = parent.parent().attr('id').replace('slide_acc_','');
			SetArrowVisibilityScene(contentObjectId);
		}

		setCourseDisplayOrder(itemId,item_type,0);

	});
	SetArrowVisibilityContentObject();
});

function setSupportMaterialOrder(lessonId, smId, direction){

	var courseId = getUrlParameter ('id');
	setSpprtMtrDisplayOrder = "setSpprtMtrDisplayOrder";
	APP.AJAX({
				url: setSpprtMtrDisplayOrder,
				dataType: "text",
				type: "POST",
				cache: false,
				data:'varSMId='+smId+'&varLessonId='+lessonId+'&direction='+direction,
				async: false,
				success: function(response) {
					 obj = $.parseJSON(response);

				}
	});

	setArrowVsbltySpprtMtrl(lessonId);
}

function setCourseDisplayOrder(itemId,item_type,direction){
	var courseId = getUrlParameter ('id');


	courseDescriptionOverlayUrl = "setCourseDisplayOrder";
	APP.AJAX({
				url: courseDescriptionOverlayUrl,
				dataType: "text",
				type: "POST",
				cache: false,
				data:'varItemId='+itemId+'&varCourseId='+courseId+'&item_type='+item_type+'&direction='+direction,
				async: false,
				success: function(response) {
					 obj = $.parseJSON(response);

				}
	});

}

function SetArrowVisibilityContentObject(){
	var fistItem=$( "DIV[item_type='ContentObject']" ).first().find('.up:first');
	fistItem.css('visibility', 'hidden');

	var lastItem2=$( "DIV[item_type='ContentObject']:nth-last-child(2)" ).find('.down:first');
	lastItem2.css('visibility', 'visible');

	var lastItem=$( "DIV[item_type='ContentObject']" ).last().find('.down:first');
	lastItem.css('visibility', 'hidden');

}

function SetArrowVisibilityScene(contentObjectId){
	var fistItemScene=$('#slide_acc_'+contentObjectId).find( "DIV[item_type='Scene']" ).first().find('.up');
	fistItemScene.css('visibility', 'hidden');

	var lastItem2=$('#slide_acc_'+contentObjectId).find( "DIV[item_type='Scene']:nth-last-child(2)" ).find('.down');
	lastItem2.css('visibility', 'visible');

	var lastItemScene=$('#slide_acc_'+contentObjectId).find( "DIV[item_type='Scene']" ).last().find('.down');
	lastItemScene.css('visibility', 'hidden');
}

function setArrowVsbltySpprtMtrl(contentObjectId){
	var fistItemScene=$('#supportMaterial_'+contentObjectId).find( "DIV[item_type='SupportMaterial']" ).first().find('.up');
	fistItemScene.css('visibility', 'hidden');

	var lastItem2=$('#supportMaterial_'+contentObjectId).find( "DIV[item_type='SupportMaterial']:nth-last-child(2)" ).find('.down');
	lastItem2.css('visibility', 'visible');

	var lastItemScene=$('#supportMaterial_'+contentObjectId).find( "DIV[item_type='SupportMaterial']" ).last().find('.down');
	lastItemScene.css('visibility', 'hidden');
}

function preview(url){
	var courseId = getUrlParameter ('id');
	var url = url + "?VARIANT=En-US&BRANDCODE=DEFAULT&PREVIEW=true&COURSEID=" +courseId+ "&SESSION="+guid();
	openWin(url);
}

var launchWindow;
function openWin(url)
{
	var isResizable = 1;
	if(navigator.appName == 'Microsoft Internet Explorer')
	{
	   if(navigator.appVersion.indexOf('MSIE 6') != -1)
	   {
			if(navigator.appMinorVersion.indexOf('SP3') == -1)
			{
				isResizable = 0;
			}
	   }
	}

	if (isResizable == 0)
	{
	 if ( launchWindow != null ) {
		launchWindow.close();
	}
	launchWindow=window.open(url,'','menubar=0,scroll bars=no,width=1024,height=660,top=0,left=0,resizable=0,location=0,toolbar=0,directories=0');
	}
	else
	{
	if ( launchWindow != null ) {
		launchWindow.close();
		}
	launchWindow=window.open(url,'','menubar=0,scroll bars=no,width=1024,height=660,top=0,left=0,resizable=1,location=0,toolbar=0,directories=0');
	}

}

function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}

function initSMAssetForms(){
	$("#collapseAddSM").attr("style", "height: auto;")
	$("#collapseAddSM").attr('class', 'panel-collapse collapse');

	$("#collapseFindSM").attr("style", "height: auto;")
	$("#collapseFindSM").attr('class', 'panel-collapse collapse');

	$("#collapseTypeSM").attr("style", "height: auto;")
	$("#collapseTypeSM").attr('class', 'panel-collapse collapse in');

	// reset attach material form
	$('#txtSupportMaterialSearch').val ('');
	$( "#lstSupportMaterialAsset").html ('');
	// reset create material form
	$('#audioAssetName').val ('');
	$("#audioAssetKeywords").tagsinput('removeAll');
	$("#audioAssetDescription").val ('');
	$('#smFileUpload').val ('');

	$('#attachAssetWithAssetGroup').attr('checked', true);



	$("#frmsmUploadAsset").get(0).reset();
	$('#btnSubmitAudioAssetForm').attr("data-dismiss", "modal");
	$("label.error").hide();
    $(".error").removeClass("error");

    $('#supportingMaterialUploader').uploader360().reset();
}