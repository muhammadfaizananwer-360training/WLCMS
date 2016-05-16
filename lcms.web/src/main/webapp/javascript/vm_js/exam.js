 function initilizeValidator(setupForm){

 	$setupForm = $(setupForm);

 	$setupForm.validate({
 	    rules: {

 	    	timePermittedForUpdateExam:  {
 	    		digits: true,
 				min:1
 			},

 			scoreRequiredPassForUpdateExam:  {
 	    		digits: true,
 				range: [0,100]
 			},

 			attemptsPermittedUpdateExam:  {
 	    		digits: true,
 				min:1
 			},
 			noTargetQuestionsForUpdateExam:  {
 	    		digits: true,
 				min:0
 			}
 		},
 	    messages: {

 	    	timePermittedForUpdateExam :{
 				digits: "Please enter a number greater than zero.",
 				min:"Please enter a number greater than zero."
 			},
 			scoreRequiredPassForUpdateExam :{
 				digits: "Please enter a number between 0 and 100.",
 				range: "Please enter a number between 0 and 100."
 			},
 			attemptsPermittedUpdateExam :{
 				digits: "Please enter a number greater than or equal to one.",
 				min:"Please enter a number greater than or equal to one."
 			},
 	    	noTargetQuestionsForUpdateExam :{
 				digits: "Please enter a number greater than zero.",
 				min:"Please enter a number greater than zero."
 			}
 	    },
 		errorPlacement: function(error, element) {

 			error.insertAfter(element);
 	    },
 	    submitHandler: function(form){
 			form.submit();
 	    },

 		invalidHandler: function(event, validator) {
 			var errors = validator.numberOfInvalids();
 			if (errors) {
 			$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Not so fast! Required fields are missing. Complete the fields, then click <strong>Save</strong>.</div>');
 			} else {
 				$("#msgdiv").html('');
 			}
 			elementFadeOut('.messages');
 			elementFadeOut("#msgdiv");
 		}

 });




 }


 $(function() {

	  //custom validation rule - text only

  $("#frmAddExamModal").validate({
    rules: {

    	timePermittedForExam:  {
    		digits: true,
			min:1
		},

		scoreRequiredPassForExam:  {
    		digits: true,
			range: [0,100]
		},

		attemptsPermittedExam:  {
    		digits: true,
			min:1
		},
		noTargetQuestionsForExam:  {
    		digits: true,
			min:0
		}
	},
    messages: {

    	timePermittedForExam :{
			digits: "Please enter a number greater than zero.",
			min:"Please enter a number greater than zero."
		},
		scoreRequiredPassForExam :{
			digits: "Please enter a number between 0 and 100.",
			range: "Please enter a number between 0 and 100."
		},
		attemptsPermittedExam :{
			digits: "Please enter a number greater than or equal to one.",
			min:"Please enter a number greater than or equal to one."
		},
    	noTargetQuestionsForExam :{
			digits: "Please enter a number greater than zero.",
			min:"Please enter a number greater than zero."
		}
    },
	errorPlacement: function(error, element) {

		error.insertAfter(element);
    },
    submitHandler: function(form){
		form.submit();
    },

	invalidHandler: function(event, validator) {
		var errors = validator.numberOfInvalids();
		if (errors) {
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Not so fast! Required fields are missing. Complete the fields, then click <strong>Save</strong>.</div>');
		} else {
			$("#msgdiv").html('');
		}
		elementFadeOut('.messages');
		elementFadeOut("#msgdiv");
	}

});


});






// addQuiz Modal Save event listener
function examAddUpdate(trg, bCheckValidation) {



	course_id = getUrlParameter ("id");

	var chkrandomizeQuestionsForExam= 0;
	var chkrandomizeAnswersForExam =0;
	var allowReviewaftGradingForExam=0;

	var noTargetQuestionsForExam = $('#noTargetQuestionsForExam').val();				  //	Number of target questions per exam

	if($('#chkrandomizeQuestionsForExam').prop('checked'))  							  // 	check box  - Randomize questions
		chkrandomizeQuestionsForExam=1;

	if($('#chkrandomizeAnswersForExam').prop('checked'))    							  // 	check box  - Randomize answers
		chkrandomizeAnswersForExam = 1;

	var timePermittedForExam = $('#timePermittedForExam').val();			  			  //	Time permitted for exam (minutes)
	var oboGradeQuestionsForExam = $('#oboGradeQuestionsForExam').val();			  	  //	Grade questions

	if($('#allowReviewaftGradingForExam').prop('checked')) 							  	  //	Allow review after grading
		allowReviewaftGradingForExam=1;

	var scoreRequiredPassForExam = $('#scoreRequiredPassForExam').val();			  	  //	Score required to pass exam
	var attemptsPermittedExam = $('#attemptsPermittedExam').val();			  			  //	Number of attempts permitted
	var actionOnFailtoPassForExam = $('#actionOnFailtoPassForExam').val();			  	  //	Action to take if fail to pass

	if (bCheckValidation) {
		//check if form data is not valid, then do nothing an send the control to form back
		if(! $("#frmAddExamModal").valid())
			return;
		var objCourseSettings = getCourseSettings();

		if ( (objCourseSettings.attemptTheExam == true || objCourseSettings.passTheExam == true ) && ( scoreRequiredPassForExam > 0 && actionOnFailtoPassForExam == '3')  ) {
			validationMessageAddUpdateExam ("AddExam");
			return false;
		}

	}

	APP.AJAX({
		  url: 'examAddUpdate',
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'course_id=' + course_id + '&noTargetQuestionsForExam='+ noTargetQuestionsForExam
							+'&chkrandomizeQuestionsForExam='+ chkrandomizeQuestionsForExam +  '&chkrandomizeAnswersForExam='+ chkrandomizeAnswersForExam
							+'&timePermittedForExam='+ timePermittedForExam + '&oboGradeQuestionsForExam='+oboGradeQuestionsForExam
							+'&allowReviewaftGradingForExam='+ allowReviewaftGradingForExam + '&scoreRequiredPassForExam='+ scoreRequiredPassForExam
							+'&attemptsPermittedExam='+ attemptsPermittedExam +'&actionOnFailtoPassForExam='+ $('#actionOnFailtoPassForExam').val(),
		  async: false,
		  success: function(response) {

			  TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});



		}
	 });

	// closing add exam screen
	$('#addExamModal').modal ('hide');
	//hide Add Final Exam hyperlink
	$('#AddFinalExamLink').css("display", "none");

	// displaying exam strip
	var $divClone = $("div#examStrip").clone();
	$divClone.attr("id", "exam_accordion_02");
	$divClone.find("#id_for_exam_setup_form").attr("id", "exam_setup_0");
	$divClone.find("#id_for_exam_list").attr("id", "exam_list_0");
	$("div#exam_accordion_0").append($divClone);
	$("div#exam_accordion_0").find (".panel-title .accordion-toggle ").click();
	$divClone.show();
	showHideDualSave();
}



//---------------------------------------------------------------------------------------------------------------------------------------------
// Deleting/Disabling Exam - Start
//---------------------------------------------------------------------------------------------------------------------------------------------
function remove_Exam_panel(trg)
{

	$trgModal = $("#confirmationModal");

	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';

	var msg = '<p>Are you sure you want to turn off the exam for this course?</p>';

	var btns = '<button type="button" class="btn blue" onclick="confirm_Exam_remove_Attempt1(true)" data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default" onclick="confirm_Exam_remove(false)" data-dismiss="modal">NO</button>';
	//	END TITLE, MESSAGE AND BUTTONS

	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');

}

function confirm_Exam_remove_Attempt1(confirm)
{

	//--------------------------------------------------------------------------
	//----Start---- Following code is written because of this issue -> WLCMS-835
	//--------------------------------------------------------------------------
	var courseId = getUrlParameter ('id');
	var obj;
	targetUrl = "getCourseSettingByJSON";
	APP.AJAX({
				url: targetUrl,
				dataType: "text",
				type: "GET",
				cache: false,
				data:'&id='+courseId,
				async: false,
				success: function(response) {
					 obj = $.parseJSON(response);
				}
	});

	if(obj.status=="false"){
		confirm_Exam_remove(true);
		return;
	}
	//--------------------------------------------------------------------------
	//---End-------------------------------------------------------------------
	//--------------------------------------------------------------------------


	$trgModal = $("#confirmationModal");

	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="icon-exclamation-sign"></i> Please Confirm';

	var msg = '<p>This course currently has the exam as a completion criteria. Do you want to remove this requirement?</p>';

	var btns = '<button type="button" class="btn blue" onclick="confirm_Exam_remove(true)" data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default" onclick="confirm_Exam_remove(false)" data-dismiss="modal">NO</button>';
	//	END TITLE, MESSAGE AND BUTTONS

	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');

}

function confirm_Exam_remove(confirm){
	if(confirm)
	{

		disableFinalExam();

		$bar = $("div#exam_accordion_02");
		$bar.animate({height: "0px"}, 500);
		window.setTimeout(function() {
			$bar.remove();
			showHideDualSave();
		}, 500);
	}

}

function validationMessageAddUpdateExam (originCalledFrom) {
	console.log ("validationMessageAddUpdateExam :: Begin ");
	$trgModal = $("#confirmationModal");

	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="icon-exclamation-sign"></i> Please Confirm';

	var msg = '<p>The settings for this course indicate that the learner must pass the exam. Do you want to change this setting and allow learners to move on even if they have not mastered the content?</p>';

	var btns = '<button type="button" class="btn blue" onclick="updateExamConfiguration(\''+ originCalledFrom + '\',true);	" data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default" onclick="updateExamConfiguration(\''+ originCalledFrom + '\',false)" data-dismiss="modal">NO</button>';
	//	END TITLE, MESSAGE AND BUTTONS

	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');

	console.log ("validationMessageAddUpdateExam :: End ");
}


function updateExamConfiguration (originCalledFrom,bUpdateConfiguration) {

	console.log (" updateExamConfiguration " + bUpdateConfiguration );
	if (bUpdateConfiguration){
		var courseId = getUrlParameter ('id');
		targetUrl = "disableFinalExam";
		APP.AJAX({
			url: targetUrl,
			dataType: "text",
			type: "POST",
			cache: false,
			data:'&varCourseId='+courseId + '&disablexam=false',
			async: false,
			success: function(response) {
				 obj = $.parseJSON(response);
			}
		});
		originCalledFrom == 'AddExam' ? examAddUpdate (null, false) : updateExam (false);
	}else{
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Just a moment!</b> Please set this field to either "Retake course" or "Lock course.</div>');
		elementFadeOut("#msgdiv");
		if (originCalledFrom == 'AddExam'){
			$("#addExamModal").find('#actionOnFailtoPassForExam').addClass("highlighted");
		}else{
			$('#actionOnFailtoPassForUpdateExamNew').addClass("highlighted");
		}
		setTimeout(
	    function() { $("#addQuizModal").find('#MaxAttempHandlerDiv_Quiz').removeClass('highlighted'); },
	    9000
		);
		$("#addQuizModal").find('#MaxAttempHandlerDiv_Quiz').focus();
	}

}

function getCourseSettings(){
	var courseId = getUrlParameter ('id');

	targetUrl = "getCourseSettings";
	APP.AJAX({
		url: targetUrl,
		dataType: "text",
		type: "GET",
		cache: false,
		data:'&varCourseId='+courseId,
		async: false,
		success: function(response) {
			 obj = $.parseJSON(response);
		}
	});
	return obj;
}


function disableFinalExam(){
var courseId = getUrlParameter ('id');

	targetUrl = "disableFinalExam";
	APP.AJAX({
		url: targetUrl,
		dataType: "text",
		type: "POST",
		cache: false,
		data:'&varCourseId='+courseId,
		async: false,
		success: function(response) {
			 obj = $.parseJSON(response);
		}
	});

	$('#AddFinalExamLink').css("display", "block");
}
//---------------------------------------------------------------------------------------------------------------------------------------------
// Deleting/Disabling Exam - End
//---------------------------------------------------------------------------------------------------------------------------------------------

// use this function to get exam setup form for and display Edit mode.
function getExamforEdit(event)
{
	var courseId = getUrlParameter ('id');
	if($('#exam_setup_0').hasClass( "panel-collapse bg-gray-2 a1 collapse in" ))
	{
		if(updateExam(true))
		{










		}else{
			event.preventDefault();
			event.stopPropagation();
			return false;
		}


	}else{
		resetHidenForSave();
		$("#hidGetExamforEdit").val(courseId);
	var obj = null;
	b_setup = false;

	var Url = "getExamforEdit";
	APP.AJAX({
				url: Url,
				dataType: "text",
				type: "POST",
				cache: false,
				data:'course_id='+courseId,
				async: false,
				success: function(response) {
					 obj = $.parseJSON(response);
					 b_setup = true;
				}
		   });


		if (b_setup) {
			var $examdiv = $("div#exam_setup_0");

			var $divClone = $("div#exam_setup_01").clone();
			$divClone.attr("id", "mainExamSetupForm");
			//-------------------------------------------------------------------------------------------------------------
			// change fields id
			//-------------------------------------------------------------------------------------------------------------
			$divClone.find("#noTargetQuestionsForUpdateExam").attr("id", "noTargetQuestionsForUpdateExamNew");		//1
			$divClone.find("#chkrandomizeQuestionsForUpdateExam").attr("id", "chkrandomizeQuestionsForUpdateExamNew");	//2
			$divClone.find("#chkrandomizeAnswersForUpdateExam").attr("id", "chkrandomizeAnswersForUpdateExamNew");		//3
			$divClone.find("#timePermittedForUpdateExam").attr("id", "timePermittedForUpdateExamNew");					//4
			$divClone.find("#oboGradeQuestionsForUpdateExam").attr("id", "oboGradeQuestionsForUpdateExamNew");			//5
			$divClone.find("#allowReviewaftGradingForUpdateExam").attr("id", "allowReviewaftGradingForUpdateExamNew");	//6
			$divClone.find("#scoreRequiredPassForUpdateExam").attr("id", "scoreRequiredPassForUpdateExamNew");			//7
			$divClone.find("#attemptsPermittedUpdateExam").attr("id", "attemptsPermittedUpdateExamNew");				//8
			$divClone.find("#actionOnFailtoPassForUpdateExam").attr("id", "actionOnFailtoPassForUpdateExamNew");		//9



			//-------------------------------------------------------------------------------------------------------------
			// set the fields data according to database --  Start
			//-------------------------------------------------------------------------------------------------------------
			$divClone.find ("#noTargetQuestionsForUpdateExamNew").val (obj.noTargetQuestions);
			$divClone.find ("#timePermittedForUpdateExamNew").val (obj.timeforExam);
			$divClone.find ("#scoreRequiredPassForUpdateExamNew").val (obj.scorePassExam);
			$divClone.find ("#attemptsPermittedUpdateExamNew").val (obj.noAttemptsPermitted);


			if(obj.randomizeQuestions)
				$divClone.find ("#chkrandomizeQuestionsForUpdateExamNew").prop("checked", true);
			else
				$divClone.find ("#chkrandomizeQuestionsForUpdateExamNew").prop("checked", false);


			if(obj.randomizeAnswers)
				$divClone.find('#chkrandomizeAnswersForUpdateExamNew').attr('checked', true);
			else
				$divClone.find('#chkrandomizeAnswersForUpdateExamNew').attr('checked', false);


			if(obj.allowReviewaftGrading)
				$divClone.find('#allowReviewaftGradingForUpdateExamNew').attr('checked', true);
			else
				$divClone.find('#allowReviewaftGradingForUpdateExamNew').attr('checked', false);



			if(obj.gradeQuestions=='AfterEachQuestionIsAnswered')
				$divClone.find("#oboGradeQuestionsForUpdateExamNew").children("option[value='1']").prop('selected',true)
			else
				$divClone.find("#oboGradeQuestionsForUpdateExamNew").children("option[value='2']").prop('selected',true)


		/*	if(obj.actionOnFailtoPass=='Retake Content')
				$divClone.find("#actionOnFailtoPassForUpdateExamNew").children("option[value='1']").prop('selected',true)
			else
				$divClone.find("#actionOnFailtoPassForUpdateExamNew").children("option[value='2']").prop('selected',true)
		*/

		if(obj.actionOnFailtoPass=='Retake Content')
			$divClone.find("#actionOnFailtoPassForUpdateExamNew").children("option[value='1']").prop('selected',true);
		else if(obj.actionOnFailtoPass=='Lock Course')
			$divClone.find("#actionOnFailtoPassForUpdateExamNew").children("option[value='2']").prop('selected',true);
		else if(obj.actionOnFailtoPass=='Continue Course')
			$divClone.find("#actionOnFailtoPassForUpdateExamNew").children("option[value='3']").prop('selected',true);


			//-------------------------------------------------------------------------------------------------------------
			// set the fields data according to database --  End
			//-------------------------------------------------------------------------------------------------------------

			$divClone.css("display", "block");
			$examdiv.html($divClone);
			var divForm = $divClone.find("#form_exam_setup_01");
			initilizeValidator(divForm);
			APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");
		}


	}
}

function updateExam(bDoValidation) {

	console.log ("updateExam :: validation : " +  bDoValidation );

	isSaved = false;
	course_id = getUrlParameter ("id");

	var chkrandomizeQuestionsForExam= 0;
	var chkrandomizeAnswersForExam =0;
	var allowReviewaftGradingForExam=0;

	var noTargetQuestionsForExam = $('#noTargetQuestionsForUpdateExamNew').val();				  //	Number of target questions per exam

	if($('#chkrandomizeQuestionsForUpdateExamNew').prop('checked'))  							  // 	check box  - Randomize questions
		chkrandomizeQuestionsForExam=1;

	if($('#chkrandomizeAnswersForUpdateExamNew').prop('checked'))    							  // 	check box  - Randomize answers
		chkrandomizeAnswersForExam = 1;

	var timePermittedForExam = $('#timePermittedForUpdateExamNew').val();			  			  //	Time permitted for exam (minutes)
	var oboGradeQuestionsForExam = $('#oboGradeQuestionsForUpdateExamNew').val();			  	  //	Grade questions

	if($('#allowReviewaftGradingForUpdateExamNew').prop('checked')) 							  	  //	Allow review after grading
		allowReviewaftGradingForExam=1;

	var scoreRequiredPassForExam = $('#scoreRequiredPassForUpdateExamNew').val();			  	  //	Score required to pass exam
	var attemptsPermittedExam = $('#attemptsPermittedUpdateExamNew').val();			  			  //	Number of attempts permitted
	var actionOnFailtoPassForExam = $('#actionOnFailtoPassForUpdateExamNew').val();			  	  //	Action to take if fail to pass

	if (bDoValidation){
		var element = $("#noTargetQuestionsForUpdateExamNew");
		var divForm = element.closest("#form_exam_setup_01");
		var  bValidated = divForm.valid();
		if(!bValidated){
			return false;
		}

		var objCourseSettings = getCourseSettings();
		console.log ("attemptTheExam : " + objCourseSettings.attemptTheExam  );
		console.log ("passTheExam:" + objCourseSettings.passTheExam);
		console.log ("actionOnFailtoPassForExam: " + actionOnFailtoPassForExam);
		console.log ("scoreRequiredPassForExam : " + scoreRequiredPassForExam);
		if ( (objCourseSettings.attemptTheExam == true || objCourseSettings.passTheExam == true ) &&
										( scoreRequiredPassForExam > 0 && actionOnFailtoPassForExam == '3')  ) {
			validationMessageAddUpdateExam ("updateExam");
			return false;
		}
	}

	APP.AJAX({
		  url: 'examAddUpdate',
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'course_id=' + course_id + '&noTargetQuestionsForExam='+ noTargetQuestionsForExam
							+'&chkrandomizeQuestionsForExam='+ chkrandomizeQuestionsForExam +  '&chkrandomizeAnswersForExam='+ chkrandomizeAnswersForExam
							+'&timePermittedForExam='+ timePermittedForExam + '&oboGradeQuestionsForExam='+oboGradeQuestionsForExam
							+'&allowReviewaftGradingForExam='+ allowReviewaftGradingForExam + '&scoreRequiredPassForExam='+ scoreRequiredPassForExam
							+'&attemptsPermittedExam='+ attemptsPermittedExam +'&actionOnFailtoPassForExam='+ actionOnFailtoPassForExam,
		  async: false,
		  success: function(response) {
			  isSaved= true;



		}
	 });


	// displaying exam strip
	return isSaved;
}





//-----------------------------------------------------------------------------------------------
//Start - Function use to get exam's elements list[Question bank]
//-----------------------------------------------------------------------------------------------
function getQuestionBankList(){
		$('#exam_list_0').html("");
		courseDescriptionOverlayUrl = "/lcms/getQuestionBankList";
		varCourseId = getUrlParameter ("id");

		var obj;
		APP.AJAX({
	    	  url: courseDescriptionOverlayUrl,
	          dataType: "text",
			  type: "POST",
	          cache: false,
	          data:'varCourseId='+varCourseId,
	          async: false,
	          success: function(response) {
			         obj = $.parseJSON(response);

			}

	   });


		var $divClone = $("#examElementsList").clone();
		$divClone.find('#exam_0_banks_dummy').attr('id','exam_0_banks');

		$divClone.attr('id','examElementsList_' + 1);

        for ( var i = 0; i < obj.length; i++) {
			var varItemBank = obj[i];
		 	var $divCloneBankBar = $("#questionBankBar").clone();
			$divCloneBankBar.attr('id','questionBankBar_' + i);
			$divCloneBankBar.css("display", "block");
			$divCloneBankBar.find('#questionBanksTitle').attr('id','questionBanksTitle_' + i);

			$divCloneBankBar.find('#questionBankBarExpendLink').attr('href','#exam_0_bank_0_' + varItemBank.id);
			$divCloneBankBar.find('#questionBankBarExpendLink').attr('id','questionBankBarExpendLink_' + varItemBank.id);
			$divCloneBankBar.find ("#exam_0_bank_0").attr("id", "exam_0_bank_0_"+varItemBank.id);
			$divCloneBankBar.find("#questionBankBarExpendLink_" + varItemBank.id).live("click", function()
			{
				getGuestionBankBarExpendArea (	this	);
			});

			$divCloneBankBar.find('#questionBanksTitle_'+i).text(varItemBank.name);
			$divClone.find('#exam_0_banks').append ($divCloneBankBar);
		}

		$divClone.css("display", "block");
		$('#exam_list_0').append ($divClone);

}

function getGuestionBankBarExpendArea(eventObjectId){

	var getAssessmentItembankId = eventObjectId.id.replace('questionBankBarExpendLink_','');
	var objNoQuestion = 2;

	if($('#questionBankBarExpendLink_'+ getAssessmentItembankId).attr('class') == 'accordion-toggle'){

		$('#exam_0_bank_0_'+ getAssessmentItembankId).html("");

		$("#hidItemBankId").val(getAssessmentItembankId);
		var $divClone = $("div#questionBankSetupAndQuestionList").clone();
		$divClone.find("#questionBankSetupAndQuestionListCloseLink").attr('href','#exam_0_bank_0_' + getAssessmentItembankId);
		$divClone.find("#questionBankSetupAndQuestionListCloseLink").attr("id", "questionBankSetupAndQuestionListCloseLink_" + getAssessmentItembankId);
		//---------------------------------------------------------------------------------------------------------
		//Question Bar addition -- Start
		resetHidenForSave();
		$("#hidExamQuestionBankId").val(getAssessmentItembankId);
		$divClone.find("#exam_0_bank_0_questions").attr("id", "exam_0_bank_0_questions_" + getAssessmentItembankId);

		questionListUrl = "/lcms/getExamQuestionList";
		varCourseId = getUrlParameter ("id");

		APP.AJAX({
			url: "getQuestionNo_Exam",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'course_id='+ varCourseId +'&bank_id='+ getAssessmentItembankId ,
			async: false,
			success: function(response) {

				objNoQuestion = $.parseJSON(response);


			}
	    });

		var obj;
		APP.AJAX({
	    	  url: questionListUrl,
	          dataType: "text",
			  type: "POST",
	          cache: false,
	          data:'assessmentId=' + getAssessmentItembankId,
	          async: false,
	          success: function(response) {
			         obj = $.parseJSON(response);

			}

	   });

        for ( var i = 0; i < obj.length; i++) {
			var $questiondivClone = $("div#examQuestionBar").clone();

			$questiondivClone.attr("id", "examQuestionBar_" + obj[i].id);
			// Following regular expression is use to remove html tags e.g: <p> etc.
			$questiondivClone.find("#examQuestionTitle").html(obj[i].questionEM.replace(/<(?:.|\n)*?>/gm, ''));

			$questiondivClone.find("#examQuestionTitle").attr("id", "examQuestionTitle_" + obj[i].id);







			//set data in delete button
			$questiondivClone.find("#examQuestionDeleteLink").attr("data-question-id", obj[i].id);
			$questiondivClone.find("#examQuestionDeleteLink").attr("data-bank-id", getAssessmentItembankId);

			$questiondivClone.find("#examQuestionBarTitleLink").attr("href", "exam_0_bank_0_question_" + obj[i].id);
			$questiondivClone.find("#examQuestionBarTitleLink").attr("id", "examQuestionBarTitleLink_" + obj[i].id);


			$questiondivClone.show();

			$divClone.find("#exam_0_bank_0_questions_" + getAssessmentItembankId).append($questiondivClone);
		}




		//Question Bar addition -- End


		$divClone.attr("id", "questionBankSetupAndQuestionList_" + getAssessmentItembankId);
		$divClone.show();
		$('#exam_0_bank_0_'+ getAssessmentItembankId).html ($divClone);

		$divClone.find('#Exam_No_Questions').attr ('id', 'Exam_No_Questions_'+getAssessmentItembankId);
		$divClone.find('#Exam_No_Questions_'+getAssessmentItembankId).attr ('data-bank-id', getAssessmentItembankId);
		$('#Exam_No_Questions_'+getAssessmentItembankId).val(objNoQuestion);


	}else{ // work for collapse question bank area
		postQuestionNo_Exam(varCourseId,getAssessmentItembankId);
		$('#exam_0_bank_0_'+ getAssessmentItembankId).html("");
		$("#hidExamQuestionBankId").val(0);
	}
		$("#Exam_No_Questions_" + getAssessmentItembankId).focus();
}

function postQuestionNo_Exam(course_id,bank_id)
{
	 var numOfQuestions = $('#exam_0_bank_0_'+ bank_id).find("span[id^=examQuestionTitle_]").length;
	 var numOfQuestions_tobe_asked = $('#Exam_No_Questions_'+bank_id).val();


	 var bMessageShownAlready = false;
	 if(numOfQuestions_tobe_asked>numOfQuestions)
	 {
			$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Just a moment!</b> The number of target questions for this question bank cannot exceed the total number of questions in this bank. Please add the required number of questions, or adjust the target number of questions.</div>');
			elementFadeOut("#msgdiv");
			bMessageShownAlready = true;

	 }
	 else if(numOfQuestions_tobe_asked==0){
		$('#msgdiv').html('<div class=\'alert alert-warning alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Attention!</b> The target number of questions is currently set to 0. This means that none of the questions in this question bank will be included in the exam. You may want to set the target number of questions to a number greater than 0, but less than or equal to the total number of questions in this bank.</div>');
		elementFadeOut("#msgdiv");
		bMessageShownAlready = true;
	}else if(numOfQuestions_tobe_asked < 0){
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Careful!</b> The target number of questions may not be a negative number. Set the target number to a value greater than or equal to 0 but less than or equal to the total number of questions in this bank</div>');
		elementFadeOut("#msgdiv");
		bMessageShownAlready = true;
		return;
	}else
	{
		 	$("#msgdiv").html('');
	}



		elementFadeOut("#msgdiv");

		APP.AJAX({
			url: "postQuestionNo_Exam",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'course_id='+ course_id +'&bank_id='+ bank_id + '&No_of_Question=' + numOfQuestions_tobe_asked,
			async: false,
			success: function(response) {
				 objQuestion = $.parseJSON(response);
			}
	    });

		if(!bMessageShownAlready){

			$('#msgdiv').html('<div class=\'alert alert-success alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><strong>Success!</strong> It\'s been saved.</div>');
			elementFadeOut("#msgdiv");
		}


	}



function removeQuestion (trg) {


	$trg = $(trg);

	question_id  = $trg.data('question-id');



	remove_panel (trg, 6, question_id);

















}


// Target Number OF Questions in Add Final Exam section Field Validation

function bankvalidation(trg){

	$trg = $(trg);
	$name = $trg .attr("name");

	$TargetQuestionval = $trg.val();


	var getAssessmentItembankId = $trg.data('bank-id');



	var numOfQuestions = $('#exam_0_bank_0_'+ getAssessmentItembankId).find("span[id^=examQuestionTitle_]").length;
	var numOfQuestions_tobe_asked = $('#Exam_No_Questions_'+getAssessmentItembankId).val();

	if ( $name == 'Final_No_Questions'){
		if( $TargetQuestionval == '' || $TargetQuestionval < 1 ){

			$trg.siblings().replaceWith("<span class='input-group-addon addon2''><i class='icon-exclamation red-text'></i></span></div>");
		 }
		 else if ( numOfQuestions < $TargetQuestionval){

			$trg.siblings().replaceWith("<span class='input-group-addon addon2''><i class='icon-warning-sign' style='color: #ffb848'></i></span></div>");
		 }
		 else{

			$trg.siblings().replaceWith("<span class='input-group-addon addon2''><i class='icon-ok green-text'></i></span></div>");
		 }
	 }


}

// display question component area
function getGuestionComponentArea(eventObjectId){

	isFromExamORQuiz = "Exam";
	var assessmentItemId = eventObjectId.id.replace('examQuestionBarTitleLink_','');

	var bSuccess = true;
	if($('#exam_0_bank_0_question_'+assessmentItemId).hasClass( "panel-collapse a2 collapse in" )){

		// updating exam values
		bSuccess = updateExamQuestion(assessmentItemId);



		if(bSuccess){

			$("#hidExamQuestionId").val(0);
			$('#exam_0_bank_0_question_'+assessmentItemId).remove();

		// displaying success message
			TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});

		}
	}

	else if(!$('#exam_0_bank_0_question_'+assessmentItemId).hasClass( "panel-collapse a2 collapse in" ))
	{
		resetHidenForSave();
		$("#hidExamQuestionId").val(assessmentItemId);
		$('#exam_0_bank_0_question_'+assessmentItemId).remove();
		var $divClone = $("div#exam_0_bank_0_question_0").clone();
		$divClone.attr("id", "exam_0_bank_0_question_" + assessmentItemId);
		$divClone.find("#examQuestionCmpnntCmplxty").attr("id", "examQuestionCmpnntCmplxty_" + assessmentItemId);

		$divClone.find("#qustnCmpnntClseLnk").attr("href", "#exam_0_bank_0_question_" + assessmentItemId);
		$divClone.find("#qustnCmpnntClseLnk").attr("id", "qustnCmpnntClseLnk_" + assessmentItemId);
		$divClone.attr("class", "panel-collapse a2 collapse in")

		//------------------------------------------------------------------------------------------------------
		// changing form controls name and ids
		$divClone.find("#qustnCmpnntComplexity").attr("id", "qustnCmpnntComplexity_" + assessmentItemId);
		$divClone.find("#qustnCmpnntComplexity").attr("name", "qustnCmpnntComplexity_" + assessmentItemId);

		$divClone.find("#qustnCmpnntQustnTyp").attr("id", "qustnCmpnntQustnTyp_" + assessmentItemId);
		$divClone.find("#qustnCmpnntQustnTyp").attr("name", "qustnCmpnntQustnTyp_" + assessmentItemId);

		$divClone.find("#qustnCmpnntQustnLbl").attr("id", "qustnCmpnntQustnLbl_" + assessmentItemId);
		$divClone.find("#qustnCmpnntQustnLbl").attr("name", "qustnCmpnntQustnLbl_" + assessmentItemId);
		// table div where we put question options
		$divClone.find("#tbodytblAnswerChoicesUpdateExam").attr("id", "tbodytblAnswerChoicesUpdateExamAct");
		$divClone.find("#chkAnswrRndmztnForUpdateExam").attr("id", "chkAnswrRndmztnForUpdateExamAct");
		$divClone.find("#btnUpdtExamDelQstnOptionTemp").attr("id", "btnUpdtExamDelQstnOption");
		$divClone.find("#chkUpdtExamAnswersSlctAllTemp").attr("id", "chkUpdtExamAnswersSlctAll");
		//------------------------------------------------------------------------------------------------------


		//------------------------------------------------------------------------------------------------------
		// START - get question from database and fill/select question form controls
		//------------------------------------------------------------------------------------------------------
		b_getQuestion = false;
		// Call AJAX to fill data
		APP.AJAX({
			url: "getQuestion",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'asssementItemId='+assessmentItemId,
			async: false,
			success: function(response) {
				 b_getQuestion = true;
				 objQuestion = $.parseJSON(response);
			}
		});

		$divClone.find('#qustnCmpnntComplexity_'+assessmentItemId +' option[value=\''+ objQuestion.complexity  + '\']').attr('selected','selected');
		$divClone.find('#qustnCmpnntQustnTyp_'+assessmentItemId +' option[value=\''+ objQuestion.questionType  + '\']').attr('selected','selected');
		$divClone.find('#qustnCmpnntQustnLbl_'+assessmentItemId).val(objQuestion.questionEM.replace(/<(?:.|\n)*?>/gm, ''));


		if(objQuestion.disableRandomizeAnswerChoiceTF + '' == 'true'){
			$divClone.find('#chkAnswrRndmztnForUpdateExamAct').attr('checked', true);
		}else{
			$divClone.find('#chkAnswrRndmztnForUpdateExamAct').attr('checked', false);
		}



		// second request for get question choices
		APP.AJAX({
		url: "getQuestionAnswer",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemId='+ assessmentItemId,
		async: false,
			success: function(response) {
				b_answerList = true;
				 lstAnswers = $.parseJSON(response);
			}
		});


		$divClone.find('#tbodytblAnswerChoicesUpdateExamAct').find('tbody').html ('');

		for(var i = 0; i < lstAnswers.length; i++) {
			objAnswer = lstAnswers[i];

			answerTemplate =  $('#rwAnswer').clone();
			$answerTemplate = $(answerTemplate);

			strQuestionChoice = "<a onclick=\"getExamDetailAnswerChoice(this)\" href=\"javascript:;\" data-target=\"#addExamAnsChoiceModalForEdit\" data-toggle=\"modal\" title=" + objAnswer.label  + "><!--<i class=\"icon-plus\">--></i>" + objAnswer.label +"</a>";

			$answerTemplate.find ('.choice').html (strQuestionChoice);
			$answerTemplate.find ('.correct').html ((objAnswer.isCorrectTF == true) ? 'true' : 'false' );
			$answerTemplate.find ('.feedback').html (objAnswer.feedBack);
			$answerTemplate.attr('id','rwAnswer_0_' + assessmentItemId + '_' + objAnswer.id);
			$answerTemplate.show ();
			$divClone.find('#tbodytblAnswerChoicesUpdateExamAct' ).append ($answerTemplate);
		}

		//------------------------------------------------------------------------------------------------------
		// END - get question from database and fill/select question form controls
		//------------------------------------------------------------------------------------------------------

		$("#examQuestionBar_" + assessmentItemId).append ( $divClone );

		APP.CKEDITOR ('qustnCmpnntQustnLbl_' + assessmentItemId,'STANDARD');
		APP.BODY_COLLAPSES("CLOSE");

	}
}

function PopulateAnswer(assessmentItemId)
{
	APP.AJAX({
		url: "getQuestionAnswer",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemId='+ assessmentItemId,
		async: false,
			success: function(response) {
				b_answerList = true;
				 lstAnswers = $.parseJSON(response);
			}
		});

	var $divClone = $("#exam_0_bank_0_question_" + assessmentItemId);
	$divClone.find('#tbodytblAnswerChoicesUpdateExamAct').html ('');

	for(var i = 0; i < lstAnswers.length; i++) {
		objAnswer = lstAnswers[i];

		answerTemplate =  $('#rwAnswer').clone();
		$answerTemplate = $(answerTemplate);

		strQuestionChoice = "<a onclick=\"getExamDetailAnswerChoice(this)\" href=\"javascript:;\" data-target=\"#addExamAnsChoiceModalForEdit\" data-toggle=\"modal\" title=" + objAnswer.label  + "><!--<i class=\"icon-plus\">--></i>" + objAnswer.label +"</a>";

		$answerTemplate.find ('.choice').html (strQuestionChoice);
		$answerTemplate.find ('.correct').html ((objAnswer.isCorrectTF == true) ? 'true' : 'false' );
		$answerTemplate.find ('.feedback').html (objAnswer.feedBack);
		$answerTemplate.attr('id','rwAnswer_0_' + assessmentItemId + '_' + objAnswer.id);
		$answerTemplate.show ();

		$divClone.find('#tbodytblAnswerChoicesUpdateExamAct' ).append ($answerTemplate);
	}

}

function updateExamQuestion(question_id) {

	valComplexity  = $('#qustnCmpnntComplexity_' + question_id ).val();
    valQuestionType = $('#qustnCmpnntQustnTyp_' + question_id ).val ();

	valQuestion = 	CKEDITOR.instances['qustnCmpnntQustnLbl_' + question_id ].getData();
	// replace all '+' sign into '&plussign;'
	valQuestion = valQuestion.replace(/\+/g,'&plussign;');

	if(valQuestion == null || valQuestion.replace(/^\s+|\s+$/g, '').length < 1){

		// displaying success message
		$('#successMsg').html("");
		$('#successMsg').append("<div  class='messages'>"+
			"<div role='alert' class='alert alert-danger alert-dismissible fade in'>"+
			"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
			"<b>Attention.</b> The question field is required. Please enter the question you wish to ask students in this field.</div></div>");
		elementFadeOut('.messages');
		return false;
	}

	valQuestion = escape(valQuestion);
    b_UpdateQuestion = false;


	//	updating exam options
    APP.AJAX({
		url: "updateQuestion",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'question_id='+ question_id + '&complexity=' + valComplexity + '&questionType=' + valQuestionType + '&question=' + valQuestion + '&randomizeAnswer=' + $('#chkAnswrRndmztnForUpdateExamAct').prop("checked")+'&course_id='+getUrlParameter ("id"),
		async: false,
		success: function(response) {
			b_UpdateQuestion = true;
			 objQuestion = $.parseJSON(response);
		}
    });

	//re-set question title on question bar
	$("#examQuestionTitle_"+question_id).html(CKEDITOR.instances['qustnCmpnntQustnLbl_' + question_id ].getData().replace(/<(?:.|\n)*?>/gm, ''));

	// Updating question's option values
	answerArray = '';
	var table = $('#tbodytblAnswerChoicesUpdateExamAct');
	console.log("Length:" + table.length);
	console.log("Length2:" + table.find('tr').length);

	if(table != null && table.find('tr').length < 2){

		// displaying success message
		$('#successMsg').html("");
		$('#successMsg').append("<div  class='messages'>"+
			"<div role='alert' class='alert alert-danger alert-dismissible fade in'>"+
			"<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>"+
			"<b>Attention.</b> This question type requires at least two answer choices. Please enter the required number of choices.</div></div>");
		elementFadeOut('.messages');
		return false;
	}
	table.find('tr').each(function (i, el) {

		$el = $(el);

		if ($el.is('tr')) {
			// submit only new Answers
			if ($el.attr('id') == '') {
				var $tds = $(this).find('td'),
				choice  =  escape(replaceAnswerText($tds.eq(1).text())),
				correct = $tds.eq(2).text(),
				feedback = escape(replaceAnswerText($tds.eq(3).text()));
				if (choice != '')
					answerArray += choice + '--' + correct + '--' + feedback + '::';
			}
		}
    });

	if (answerArray == '')
		return true;


	//Post new added Answers
	APP.AJAX({
		url: "postQuestionAnswer",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemId='+ question_id+'&Answers=' + answerArray,
		async: false,
		success: function(response) {
			 lstAnswers = $.parseJSON(response);
			 PopulateAnswer(question_id);
		}
    });

	return true;
}

function replaceAnswerText(valQuestion){
	// replace all '+' sign into '&plussign;'
	return valQuestion.replace(/\+/g,'&plussign;');
}

function AfterremoveQuestion(trg)
{

	$trg = $(trg);

	var bank_id = trg.attr('id').replace('exam_0_bank_0_questions_','');



	 var numOfQuestions = $('#exam_0_bank_0_'+ bank_id).find("span[id^=examQuestionTitle_]").length;
	 var numOfQuestions_tobe_asked = $('#Exam_No_Questions_'+bank_id).val();



	 if(numOfQuestions_tobe_asked>numOfQuestions)
	 {
			$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Just a moment!</b> The number of target questions for this question bank cannot exceed the total number of questions in this bank. Please add the required number of questions, or adjust the target number of questions.</div>');
			elementFadeOut("#msgdiv");

	 }
}


// Add New Answer Choice in 'Add New Question' form
function acceptExamAnswer () {

	answer = CKEDITOR.instances['new_ans_choice_ckeditor_Exam'].getData();

	if(answer == ''){
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Not So Fast!</b> A required field is missing. Complete the field, then click <b>Save</b></div>');
		elementFadeOut("#msgdiv");
		$('#btAcceptExamAnswer').removeAttr("data-dismiss");
		return false;
	}

	feedback = CKEDITOR.instances['new_ans_choice_feedback_Exam'].getData();
	correctChoice = $('#isCorrectExam').is(':checked') ? "true" : "false";
	$answerRow = $('#rwAnswerExam').clone ();

	$answerRow.attr('id','');
	strQuestionChoice = "<a onclick=\"getExamDetailAnswerChoice(this)\" href=\"javascript:;\" data-target=\"#addExamAnsChoiceModalForEdit\" data-toggle=\"modal\" title=" + $(answer).text()  + "><!--<i class=\"icon-plus\">--></i>" + $(answer).text() +"</a>";
	$answerRow.find ('.choice').html (strQuestionChoice);
	$answerRow.find ('.correct').html (correctChoice);
	$answerRow.find ('.feedback').html ($(feedback).text ());

	$answerRow.show ();
	$('#tblAnswerChoicesExam').append($answerRow);

	CKEDITOR.instances['new_ans_choice_ckeditor_Exam'].setData('');
	CKEDITOR.instances['new_ans_choice_feedback_Exam'].setData('');
	 $('#isCorrectExam').attr('checked', false);

	$('#btAcceptExamAnswer').attr("data-dismiss", "modal");

	$("#btnExamDelQstnOption").attr("disabled", false);
	$("#chkExamAnswersSlctAll").attr("disabled", false);
}

// Add New Answer Choice on 'Update New Question' form
function acceptExamAnswerForUpdate () {

	answer = CKEDITOR.instances['choiceTextForUpdateExam'].getData();
	if(answer == ''){
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Not So Fast!</b> A required field is missing. Complete the field, then click <b>Save</b></div>');
		elementFadeOut("#msgdiv");
		$('#btAcceptExamAnswerForUpdate').removeAttr("data-dismiss");
		return false;
	}

	feedback = CKEDITOR.instances['choiceFeedbackForUpdateExam'].getData();
	correctChoice = $('#isCorrectForUpdateExam').is(':checked') ? "true" : "false";
	$answerRow = $('#rwAnswerExam').clone ();

	$answerRow.attr('id','');


	strQuestionChoice = "<a onclick=\"getExamDetailAnswerChoice(this)\" href=\"javascript:;\" data-target=\"#addExamAnsChoiceModalForEdit\" data-toggle=\"modal\" title=" + $(answer).text()  + "><!--<i class=\"icon-plus\">--></i>" + $(answer).text() +"</a>";

	$answerRow.find ('.choice').html ( strQuestionChoice );
	$answerRow.find ('.correct').html (correctChoice);
	$answerRow.find ('.feedback').html ($(feedback).text ());

	$answerRow.show ();
	$('#tbodytblAnswerChoicesUpdateExamAct').append($answerRow);

	CKEDITOR.instances['choiceTextForUpdateExam'].setData('');
	CKEDITOR.instances['choiceFeedbackForUpdateExam'].setData('');
	 $('#isCorrectForUpdateExam').attr('checked', false);

	$('#btAcceptExamAnswerForUpdate').attr("data-dismiss", "modal");

	$("#btnUpdtExamDelQstnOption").attr("disabled", false);
	$("#chkUpdtExamAnswersSlctAll").attr("disabled", false);
}


function cancelAnswerForUpdateExam () {

	CKEDITOR.instances['choiceTextForUpdateExam'].setData('');
	CKEDITOR.instances['choiceFeedbackForUpdateExam'].setData('');
	$ ('#isCorrectForUpdateExam').attr ('checked', false);
}

function previewlink() {
	var strWindowFeatures = "location=yes,height=600,width=800,scrollbars=yes,status=yes";
	var URL = "https://player.360training.com/ICP4/CoursePlayerPreviewer.aspx?COURSEID="+getUrlParameter ('id')+"&VARIANT=En-US&BRANDCODE=DEFAULT&PREVIEW=true";
	var win = window.open(URL, "_blank");
}
/* Exam Answer Choice Edit Work */

function getExamDetailAnswerChoice(trg) {
	isFromExamORQuiz = "Exam";
	$trg = $(trg).parent ().parent ();

	APP.CACHE = trg;

	strIds = $trg.attr('id');
	answer_id = 0;
	if ( strIds ) {
		strIds = $trg.attr ('id');
		arr = strIds.split('_');
		answer_id = arr[3];
	}

	table = $trg.parent ().parent ();

	if (answer_id) {
		//GET bank bar list
		APP.AJAX({
			url: "getDetailAnswer",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'answerId=' + answer_id,
			async: false,
			success: function(response) {
				objAnswer = $.parseJSON(response);
				bGetAnswerDetail = true;
			}
		});
		if (bGetAnswerDetail) {
			CKEDITOR.instances['choiceTextForEditExam'].setData(objAnswer.value);
			CKEDITOR.instances['choiceFeedbackForEditExam'].setData(objAnswer.feedBack);
			var correctVal = objAnswer.isCorrectTF;

			if( correctVal) {
				$('#addExamAnsChoiceModalForEdit').find ('#isCorrectForEditExam').prop("checked", true);
				$("#addExamAnsChoiceModalForEdit").find("#isCorrectForEditExam").removeAttr("disabled");
				$("#addExamAnsChoiceModalForEdit").find("#isCorrectForEditExam").attr("disabled", false);
			}
			else {
				if(table.find('td.correct:contains("true")').length>0)
						$("#addExamAnsChoiceModalForEdit").find("#isCorrectForEditExam").attr("disabled", true);

				$('#addExamAnsChoiceModalForEdit').find ('#isCorrectForEditExam').prop("checked", false);
			}
		}
	}

	if (answer_id == 0) {
		$columns = $trg.find ('td');
		jQuery.each($columns, function(i, item) {
			$item = $(item);
			if ($item.hasClass('choice') )
				CKEDITOR.instances['choiceTextForEditExam'].setData($item.text ());

			if ($item.hasClass('feedback') )
				CKEDITOR.instances['choiceFeedbackForEditExam'].setData($item.text ());

			if ($item.hasClass('correct')){
				if ($item.text ()== 'true')  {
					$('#addExamAnsChoiceModalForEdit').find ('#isCorrectForEditExam').prop("checked", true);
					$("#addExamAnsChoiceModalForEdit").find("#isCorrectForEditExam").removeAttr("disabled");
					$("#addExamAnsChoiceModalForEdit").find("#isCorrectForEditExam").attr("disabled", false);
				}
				else {
					if(table.find('td.correct:contains("true")').length>0)
						$("#addExamAnsChoiceModalForEdit").find("#isCorrectForEditExam").attr("disabled", true);

					$('#addExamAnsChoiceModalForEdit').find ('#isCorrectForEditExam').prop("checked", false);
				}
			}
		});

	}

}

// Edit Answer Choice on 'Update New Question' form
function acceptExamAnswerForEdit () {


	answer = CKEDITOR.instances['choiceTextForEditExam'].getData();
	if(answer == ''){
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Not So Fast!</b> A required field is missing. Complete the field, then click <b>Save</b></div>');
		elementFadeOut("#msgdiv");
		$('#btAcceptExamAnswerForEdit').removeAttr("data-dismiss");
		return false;
	}
	feedback = CKEDITOR.instances['choiceFeedbackForEditExam'].getData();
	correctChoice = $('#isCorrectForEditExam').is(':checked') ? "true" : "false";

	$trg = $(APP.CACHE);
	$trg = $trg.parent ().parent();
	strIds = $trg.attr('id');
	if ( strIds ) {
		strIds  = $trg.attr('id');
		var arr = strIds.split('_');
		answer_id = arr[3];
	}
	bEditAnswer = false;
	if (answer_id) {
		//GET bank bar list
		APP.AJAX({
			url: "updateAnswer",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'answerId=' + answer_id + '&answer=' + $(answer).text () + '&feedback=' + $(feedback).text () + '&choice=' +  correctChoice,
			async: false,
			success: function(response) {
				bEditAnswer = true;
			}
		});
		APP.CACHE = '';
	}

	strQuestionChoice = "<a onclick=\"getDetailAnswerChoice(this)\" href=\"javascript:;\" data-target=\"#addAnsChoiceModal3\" data-toggle=\"modal\" title=" + $(answer).text()  + "><!--<i class=\"icon-plus\">--></i>" + $(answer).text() +"</a>";
	$trg.find ('.choice').html (strQuestionChoice);
	$trg.find ('.correct').html (correctChoice);
	$trg.find ('.feedback').html ($(feedback).text ());

	$('#isCorrectForEditExam').attr ('checked', false);
	$('#isCorrectForEditExam').removeAttr("disabled");
	$('#btAcceptAnswer3').attr("data-dismiss", "modal");



	CKEDITOR.instances['choiceTextForEditExam'].setData('');
	CKEDITOR.instances['choiceFeedbackForEditExam'].setData('');
	$('#isCorrectForUpdateExam').attr('checked', false);

	$('#btAcceptExamAnswerForEdit').attr("data-dismiss", "modal");

}


function cancelAnswerForEditExam () {

	CKEDITOR.instances['choiceTextForEditExam'].setData('');
	CKEDITOR.instances['choiceFeedbackForEditExam'].setData('');
	$ ('#isCorrectForEditExam').attr ('checked', false);

	$("#addExamAnsChoiceModalForEdit").find("#isCorrectForEditExam").removeAttr("disabled");
	$("#addExamAnsChoiceModalForEdit").find("#isCorrectForEditExam").attr("disabled", false);
}



function chk_CorrectAnswer_add(){
	if($("tbody#tblAnswerChoicesExam").find('td.correct:contains("true")').length>0){
		$("#addExamAnsChoiceModal").find("#isCorrectExam").attr("disabled", true);
	}
	else{
		$("#addExamAnsChoiceModal").find("#isCorrectExam").removeAttr("disabled");
	}

}

function chk_CorrectAnswer_update(){
	if($("tbody#tbodytblAnswerChoicesUpdateExamAct").find('td.correct:contains("true")').length>0){
		$("#addExamAnsChoiceModalForUpdate").find("#isCorrectForUpdateExam").attr("disabled", true);
	}
	else{
		$("#addExamAnsChoiceModalForUpdate").find("#isCorrectForUpdateExam").removeAttr("disabled");
	}
}

// Modal listener
function addExamQuestion(form) {

	course_id = getUrlParameter ("id");
	var bank_id  = $("#hidItemBankId").val();
	var editor = CKEDITOR.instances.questionExamTitle;
	var vName = editor.getData();
	vName = $(vName).text ();
	editor.updateElement();

	bValid = $("#addExamQuestionModalForm").validate().element("#questionExamTitle");
	var bAnswerChoiceValidation = $('#tbQuestionAnswer2 tbody tr').find ('.correct:contains("true")').length > 0;
	var len = $('#tbQuestionAnswer2 > tbody > tr').length;

	if (bValid == false || len < 2 || !bAnswerChoiceValidation )
	{




		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Not so fast! Required fields are missing. Complete the fields, then click <strong>Save</strong>.</div>');
		elementFadeOut("#msgdiv");
		$('#btaddExamQuestionSave').removeAttr("data-dismiss");
		errormsg = '';
		if (bAnswerChoiceValidation ==  false && len > 1){
			errormsg = WLCMS_CONSTANTS.VALIDATION_ONE_TRUE_ANSWER;
			$("#tblAnswerChoicesExam").parent().parent().addClass("error");
		}
		if(len==0){
			errormsg = WLCMS_CONSTANTS.VALIDATION_NUMBER_OF_ANSWER;
			$("#tblAnswerChoicesExam").parent().parent().addClass("error");
		}
		if(len==1){
			errormsg = WLCMS_CONSTANTS.VALIDATION_SINGLE_ANSWER;
			$("#tblAnswerChoicesExam").parent().parent().addClass("error");
		}
		if (!$("#tblAnswerChoicesExam").parent().parent().parent ().parent().find ('.answer-error').length > 0 )
			$("#tblAnswerChoicesExam").parent().parent().parent().append(errormsg);
		else
			$("#tblAnswerChoicesExam").parent().parent().parent().find ('.answer-error').html(errormsg);
		return false;
	}
	bCorrectAnsIn = false;
	answerArray = '';
	var table = $("#tblAnswerChoicesExam");
    table.find('tr').each(function (i, el) {
        var $tds = $(this).find('td'),
        choice  = escape(replaceAnswerText($tds.eq(1).text())),
        correct = $tds.eq(2).text(),
        feedback = escape(replaceAnswerText($tds.eq(3).text()));
        answerArray += choice + '--' + correct + '--' + feedback + '::';
		if (correct == 'true')
			bCorrectAnsIn = true;
	});
		$("#tblAnswerChoicesExam").parent().parent().parent().find("lable.answer-error").remove();
		$("#tblAnswerChoicesExam").parent().parent().removeClass("error");
		$(".table-scrollable").siblings("label").remove();
		$("#btaddExamQuestionSave").attr("data-dismiss","modal");

		if ( bCorrectAnsIn ) {
			valQuestion = CKEDITOR.instances['questionExamTitle'].getData();
			valQuestion = valQuestion.replace(/\+/g,'&plussign;');
			valQuestion = escape(valQuestion);

			var chkexamRandomizeAnswer = false;
			if($('#examRandomizeAnswer').is( ":checked" ))
				chkexamRandomizeAnswer = true;

			// get Assessment ID, content ID
			bPostQuestion = false;
			var objQuestion;
			APP.AJAX({
				url: "saveExamQuestion",
				dataType: "text",
				type: "POST",
				cache: false,
				data:'id='+ course_id +'&hidExamBankId='+ bank_id + '&complexity=' + $('#examComplexity').val() + '&questionType=' + $('#examQuestionType').val() + '&question=' + valQuestion + '&randomizeAnswer=' + chkexamRandomizeAnswer + '&answers=' + answerArray,
				async: false,
				success: function(response) {
					 bPostQuestion = true;
					 objQuestion = $.parseJSON(response);
				}
			});


			//----------------------------------------------------------------------------
			// - Start - Adding exam strip/bar into assessment item bank area
			var $questiondivClone = $("div#examQuestionBar").clone();
			$questiondivClone.attr("id", "examQuestionBar_" + objQuestion.id);
			// Following regular expression is use to remove html tags e.g: <p> etc.
			$questiondivClone.find("#examQuestionTitle").html(objQuestion.questionEM.replace(/<(?:.|\n)*?>/gm, ''));
			$questiondivClone.find("#examQuestionTitle").attr("id", "examQuestionTitle_" + objQuestion.id);
			$questiondivClone.find("#examQuestionBarTitleLink").attr("href", "exam_0_bank_0_question_" + objQuestion.id);
			$questiondivClone.find("#examQuestionBarTitleLink").attr("id", "examQuestionBarTitleLink_" + objQuestion.id);

			$questiondivClone.find("#examQuestionDeleteLink").attr("data-question-id", objQuestion.id);
			$questiondivClone.find("#examQuestionDeleteLink").attr("data-bank-id", bank_id);

			$questiondivClone.show();

			$("div#exam_0_bank_0_questions_" + bank_id).append ($questiondivClone);
			// - End - Adding exam strip/bar into assessment item bank area
			//----------------------------------------------------------------------------
			CKEDITOR.instances['questionExamTitle'].setData('');
			$('#btaddExamQuestionSave').attr("data-dismiss", "modal");
			$('#tblAnswerChoicesExam').html('');
			$('#examRandomizeAnswer').prop('checked', false);
			$('#examComplexity').children("option[value='0']").prop('selected',true);
			return false;
		} else {
				bPostQuestion = true;
				$("#tblAnswerChoicesExam").parent().parent().addClass("error");
				$('#btaddExamQuestionSave').removeAttr("data-dismiss");

		}
		if ( bPostQuestion == true ){
				$("#tblAnswerChoicesExam").parent().parent().parent().append("<label for='finalexamanswerchoices' generated='true' class='answer-error'>Please mark one the answer choice as the correct answer.</label>");
		}

}

function cancelExamQuestion () {
	CKEDITOR.instances['questionExamTitle'].setData('');
	$('#tblAnswerChoicesExam').html('');
	$('#examComplexity').children("option[value='0']").prop('selected',true);
	$('#examRandomizeAnswer').prop('checked', false);

	$("#tblAnswerChoicesExam").parent().parent().removeClass("error");

			$("#tblAnswerChoicesExam").parent().parent().parent().find ('.answer-error').remove();

	APP.CACHE = '';
}



// validate Add Question form for Exam
$(function() {

    // Setup form validation on the #register-form element
    $("#addExamQuestionModalForm").validate({

        // Specify the validation rules

       rules: {
    	   'questionExamTitle': "required"
        },
        // Specify the validation error messages
       messages: {
    	   'questionExamTitle': "Please enter question title"
        }


    });

});



function resetOpenExamForm() {


	// set default values in add Exam form
	$( "#noTargetQuestionsForExam" ).val(60);
	$('#chkrandomizeQuestionsForExam').prop('checked', true);
	$('#chkrandomizeAnswersForExam').prop('checked', true);
	$( "#timePermittedForExam" ).val(60);
	$('#oboGradeQuestionsForExam').children("option[value='2']").prop('selected',true);
	$('#allowReviewaftGradingForExam').prop('checked', true);
	$( "#scoreRequiredPassForExam" ).val(70);
	$( "#attemptsPermittedExam" ).val(3);
	$('#actionOnFailtoPassForExam').children("option[value='1']").prop('selected',true);


	var form = $("#frmAddExamModal").validate();
    form.resetForm();

	$("#timePermittedForExam").removeClass("error");
	$("#scoreRequiredPassForExam").removeClass("error");
	$("#attemptsPermittedExam").removeClass("error");

}
function resetAddExamQuestionModal(){
	$("#btnExamDelQstnOption").attr("disabled", true);
	$("#chkExamAnswersSlctAll").attr("disabled", true);
	var validator = $("#addExamQuestionModalForm").validate();
	validator.resetForm();

}