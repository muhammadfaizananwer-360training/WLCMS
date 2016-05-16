var gBankId = 0;
var gContentId = 0;

function configureQuizSetupValidator(container) {

	$(container).find("#frmEditQuizSetup").validate({
		// Specify the validation rules
		rules: {
			'embtimeforQuiz': {
				required: true,
				minStrict: -1,
				number: true
			},
			'embscorePassQuiz': {
				required: true,
				range: [0, 100],
				number: true
			},
			'embnoAttemptsPermitted': {
				required: true,
				minStrict: 0,
				equalStrict: 1,
				number: true
			}
		},
		// Specify the validation error messages
		messages: {
			'embtimeforQuiz': "Please enter a number greater than zero.",
			'embscorePassQuiz': "Please enter a number between 0 and 100.",
			'embnoAttemptsPermitted': "Please enter a number greater than or equal to one."

		},

		invalidHandler: function (event, validator) {
                    var errors = validator.numberOfInvalids();

                    if (errors) {
                        TopMessageBar.displayMessageTopBar({vType:2,  vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST,bFadeOut:true});

                    } else {
                        $("#msgdiv").html('');
                    }
                    elementFadeOut("#msgdiv");
                },
		submitHandler: function (form) {

                },
		errorClass: 'error',
		unhighlight: function (element, errorClass, validClass) {
                    $(element).removeClass(errorClass).addClass(validClass);

                },
		highlight: function (element, errorClass, validClass) {
                    $(element).addClass(errorClass).removeClass(validClass);

                }
	});
}
$(function() {
    // Setup form validation on the #register-form element
    $("#addQuestionModalForm").validate({

        // Specify the validation rules

       rules: {
    	   'questionTitle': "required"
        },
        // Specify the validation error messages
       messages: {
    	   'questionTitle': "Please enter question title"
        },
		submitHandler: function(form)
        {

        },
		invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();

			if (errors) {
				TopMessageBar.displayMessageTopBar({vType:2,  vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST,bFadeOut:true});

			} else {
				$("#msgdiv").html('');
			}
			elementFadeOut("#msgdiv");
		},
		errorClass: 'error',
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass(errorClass).addClass(validClass);

        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass);

        }
    });

	$.validator.addMethod('minStrict', function (value, el, param) {
		return value > param;
	});

	$.validator.addMethod('equalStrict', function (value, el, param) {
		return value = param;
	});

	$.validator.addMethod('maxStrict', function (value, el, param) {
		return value <= param;
	});

	$.validator.addMethod('lessthan', function (value, el, param) {
		return value < 0 ;
	});
// Setup form validation on the #register-form element
   $("#frmAddQuizModal").validate({
        // Specify the validation rules
       rules: {
    	   'timeforQuiz' : {
					required: true,
					minStrict: -1,
					number: true
				},
			'scorePassQuiz' : {
					required: true,
					range:[0,100],
					number: true
			},
			'noAttemptsPermitted':{
					required: true,
					minStrict : 0,
					equalStrict: 1,
					number: true
			}
        },
        // Specify the validation error messages
       messages: {
    	   'timeforQuiz': "Please enter a number greater than zero." ,
		   'scorePassQuiz' : "Please enter a number between 0 and 100.",
		   'noAttemptsPermitted':"Please enter a number greater than or equal to one."

        },

		invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();

			if (errors) {
				TopMessageBar.displayMessageTopBar({vType:2,  vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST,bFadeOut:true});

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

});

function getQuizMasterySetting() {
	var courseId = getUrlParameter ('id');
	var obj;
	targetUrl = "getCourseSettingObj";
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

	return obj;

}

// addQuiz Modal Save event listener
function postQuizConfiguration(InvalidateQuizMastery) {

	$trg = $(APP.CACHE);


	content_id= $trg.data('lesson-id');
	course_id = getUrlParameter ("id");

	var vchkrandomizeAnswers = false;
	var  vchkrandomizeQuestions = false;
	var vallowReviewaftGrading = false;
	// check for value of checkboxes



	if($('#chkrandomizeAnswers').is( ":checked" ))
		vchkrandomizeAnswers = true;

	if($('#chkrandomizeQuestions').is( ":checked" ))
		vchkrandomizeQuestions = true;

	if($('#allowReviewaftGrading').is( ":checked" ))
		vallowReviewaftGrading = true;




	APP.AJAX({
		  url: 'postQuizConfiguration',
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'id='+ course_id +  '&content_id=' + content_id + '&allowReviewaftGrading='+ vallowReviewaftGrading +  '&noTargetQuestions='+ $('#noTargetQuestions').val() + '&noAttemptsPermitted='+ $('#noAttemptsPermitted').val () +  '&scorePassQuiz='+ $('#scorePassQuiz').val () + '&content_id='+ content_id + '&randomizeQuestions='+ vchkrandomizeQuestions +'&randomizeAnswers='+ vchkrandomizeAnswers + '&timeforQuiz='+$('#timeforQuiz').val() + '&gradeQuestions='+ $('#opgradeQuestions').val()+'&actionOnFailtoPass='+ $('#actionOnFailtoPass').val()+ '&InvalidateQuizMastery='+InvalidateQuizMastery,
		  async: false,
		  success: function(response) {

			  TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});

				$('#quiz_container'+ content_id ).show ();
				$('#addQuizLinkDiv_' + content_id ).hide ();

				$('#addQuizModal').modal('hide');
				if(!$("#quiz_banks_" +content_id).is(':visible')) $("#quizBankTitle_" +content_id ).click();

		}
	 });

	APP.CACHE = '';
}

function postQuizConfigurationValidation(trg, event) {
	//WLCMS-482
	var form = $(trg).closest('form')[0];
	if (!$(form).valid()) {
		if(typeof(event) != "undefined"){
			event.preventDefault();
			event.stopPropagation();
			return false;
		}
	}
	course_setting = getQuizMasterySetting ();

	var scorePassQuiz = $('#addQuizModal').find('#scorePassQuiz').val ();
	if(scorePassQuiz>0 && $('#addQuizModal').find('#actionOnFailtoPass').val()=='Go To Next Lesson'
				&& course_setting.passAllQuizzes == true)
	{
		if(event !=null){
			event.preventDefault();
			event.stopPropagation();
		}

		$trgModal = $("#confirmationModal");
		//	BEGIN TITLE, MESSAGE AND BUTTONS
		var title = '<i class="icon-exclamation-sign"></i> Please Confirm';

		var msg = '<p>The settings for this course indicate that the learner must pass the quiz. Do you want to change this setting and allow learners to move on even if they have not mastered the content?</p>';

		var btns = '<button type="button" class="btn blue" onclick="postQuizConfiguration(true)" data-dismiss="modal">YES</button>'+
					'<button type="button" class="btn btn-default" onclick="postQuizConfigurationNo()" data-dismiss="modal">NO</button>';
		//	END TITLE, MESSAGE AND BUTTONS

		$trgModal.find(".modal-title").html(title);
		$trgModal.find(".modal-body").html(msg);
		$trgModal.find(".modal-footer").html(btns);

		$trgModal.modal('show');
	}
	else{
		postQuizConfiguration(false);
	}
}

function postQuizConfigurationNo() {

	$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Just a moment!</b> Please set this field to either "Retake Lesson" or "Lock course."</div>');
	elementFadeOut("#msgdiv");
	$("#addQuizModal").find('#MaxAttempHandlerDiv_Quiz').addClass("highlighted");
	setTimeout(
    function() { $("#addQuizModal").find('#MaxAttempHandlerDiv_Quiz').removeClass('highlighted'); },
    9000
	);
	$("#addQuizModal").find('#MaxAttempHandlerDiv_Quiz').focus();

}

function updateQuizConfigurationNo(contentObjectId) {

		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Just a moment!</b> Please set this field to either "Retake Lesson" or "Lock course."</div>');
		elementFadeOut("#msgdiv");
		$("#quizSetup_"+contentObjectId).find('#MaxAttempHandlerDiv_Quiz').addClass("highlighted");


		setTimeout(
        function() { $("#quizSetup_"+contentObjectId).find('#MaxAttempHandlerDiv_Quiz').removeClass('highlighted'); },
        9000
		);
		$("#quizSetup_"+contentObjectId).find('#MaxAttempHandlerDiv_Quiz').focus();
}

// addQuiz Modal Save event listener
function updateQuizConfiguration(contentObjectId,InvalidateQuizMastery) {

	course_id = getUrlParameter ("id");
	var vchkrandomizeAnswers = false;
	var  vchkrandomizeQuestions = false;
	var vallowReviewaftGrading = false;

	if($('#quiz_policies_' + contentObjectId).find('#embchkrandomizeQuestions').is( ":checked" ))
		vchkrandomizeQuestions  = true;

	if($('#quiz_policies_' + contentObjectId).find('#embchkrandomizeAnswers').is( ":checked" ))
		vchkrandomizeAnswers = true;

	if($('#quiz_policies_' + contentObjectId).find('#emballowReviewaftGrading').is( ":checked" ))
		vallowReviewaftGrading = true;

	APP.AJAX({
		  url: 'updateQuizConfiguration',
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'id='+ course_id + '&content_id=' + contentObjectId + '&noTargetQuestions='+ $('#quiz_policies_' + contentObjectId).find('#embnoTargetQuestions').val()
							+ '&noAttemptsPermitted='+ $('#quiz_policies_' + contentObjectId).find('#embnoAttemptsPermitted').val () +
							'&scorePassQuiz='+ $('#quiz_policies_' + contentObjectId).find('#embscorePassQuiz').val ()
							+ '&content_id='+ contentObjectId + '&randomizeQuestions='+  vchkrandomizeQuestions +
							'&randomizeAnswers='+ vchkrandomizeAnswers +
							'&timeforQuiz='+$('#quiz_policies_' + contentObjectId).find('#embtimeforQuiz').val() +
							'&gradeQuestions='+ $('#quiz_policies_' + contentObjectId).find('#embopgradeQuestions').val()+
							'&actionOnFailtoPass='+ $('#quiz_policies_' + contentObjectId).find('#embactionOnFailtoPass').val() +
							'&allowReviewaftGrading='+  vallowReviewaftGrading + '&InvalidateQuizMastery='+InvalidateQuizMastery
							,
		  async: false,
		  success: function(response) {

			  TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
						$("#hidQuizContentObjectId").val(0);

		}
	 });
	if(InvalidateQuizMastery)
		$('#quiz_policies_' + contentObjectId).attr("class","panel-collapse a1 collapse");
}

function updateQuizConfigurationValidation(contentObjectId,event) {
	//WLCMS-482
	var container = $('#quiz_policies_' + contentObjectId)[0];
	if(!$(container).hasClass( "panel-collapse a1 collapse in" ))
		return;

	if(!$(container).find("#frmEditQuizSetup").valid()) {
		if(typeof(event) != "undefined") {
			event.preventDefault();
			event.stopPropagation();
		}
		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
		return false;
	}

	course_setting = getQuizMasterySetting ();

	var scorePassQuiz = $(container).find('#embscorePassQuiz').val ();
	if(scorePassQuiz>0 && $(container).find('#embactionOnFailtoPass').val()=='Go To Next Lesson'
				&& course_setting.passAllQuizzes == true)
	{
		if(event !=null){
			event.preventDefault();
			event.stopPropagation();
		}

		$trgModal = $("#confirmationModal");
		//	BEGIN TITLE, MESSAGE AND BUTTONS
		var title = '<i class="icon-exclamation-sign"></i> Please Confirm';

		var msg = '<p>The settings for this course indicate that the learner must pass the quiz. Do you want to change this setting and allow learners to move on even if they have not mastered the content?</p>';

		var btns = '<button type="button" class="btn blue" onclick="updateQuizConfiguration('+contentObjectId+',true)" data-dismiss="modal">YES</button>'+
					'<button type="button" class="btn btn-default" onclick="updateQuizConfigurationNo('+contentObjectId+')" data-dismiss="modal">NO</button>';
		//	END TITLE, MESSAGE AND BUTTONS

		$trgModal.find(".modal-title").html(title);
		$trgModal.find(".modal-body").html(msg);
		$trgModal.find(".modal-footer").html(btns);

		$trgModal.modal('show');
	}
	else{
		updateQuizConfiguration(contentObjectId,false);
	}
}

function getQuizSetup (trg,event) {
	course_id = getUrlParameter ("id");
	$trg = $(trg);
	contentObjectId    = $trg.data('lesson-id');

	if($('#quiz_policies_' + contentObjectId).hasClass( "panel-collapse a1 collapse in" )){
		updateQuizConfigurationValidation(contentObjectId,event);
		return;
	}

	resetHidenForSave();
	$("#hidQuizContentObjectId").val(contentObjectId);
	quizsetup_id = '#quiz_policies_' + contentObjectId;
	var quizSetuptemplate =  "";
	var $quizSetuptemplate = ""
	quizSetuptemplate =  $('div#quizSetup').clone();
	$quizSetuptemplate = $(quizSetuptemplate);

	$quizSetuptemplate.attr('id','quizSetup_' + contentObjectId);

	bQuizSetup = false;

	APP.AJAX({
		url: "getQuizSetup",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'id=' + course_id + '&contentId=' + contentObjectId ,
		async: false,
		success: function(response) {
			objQuizSetup = $.parseJSON(response);
			bQuizSetup = true;
		}
    });

	if (bQuizSetup) {
		$quizSetuptemplate.find('#embnoTargetQuestions').val(objQuizSetup.noTargetQuestions);

		$quizSetuptemplate.find('#embnoAttemptsPermitted').val(objQuizSetup.noAttemptsPermitted);
		$quizSetuptemplate.find('#embscorePassQuiz').val (objQuizSetup.scorePassQuiz);

		if (objQuizSetup.randomizeQuestions)
			$quizSetuptemplate.find('#embchkrandomizeQuestions').prop("checked", true);
		else
			$quizSetuptemplate.find('#embchkrandomizeQuestions').prop("checked", false);

		if(objQuizSetup.randomizeAnswers)
			$quizSetuptemplate.find('#embchkrandomizeAnswers').prop("checked", true);
		else
			$quizSetuptemplate.find('#embchkrandomizeAnswers').prop("checked", false);

		if (objQuizSetup.allowReviewaftGrading)
			$quizSetuptemplate.find('#emballowReviewaftGrading').prop("checked", true);
		else
			$quizSetuptemplate.find('#emballowReviewaftGrading').prop("checked", false);

		$quizSetuptemplate.find('#embtimeforQuiz').val(objQuizSetup.timeforQuiz);


		if(objQuizSetup.gradeQuestions=='AfterEachQuestionIsAnswered')
			$quizSetuptemplate.find("#embopgradeQuestions").children("option[value='AfterEachQuestionIsAnswered']").prop('selected',true)
		else
			$quizSetuptemplate.find("#embopgradeQuestions").children("option[value='AfterAssessmentIsSubmitted']").prop('selected',true)


		if(objQuizSetup.actionOnFailtoPass=='Retake Lesson')
			$quizSetuptemplate.find("#embactionOnFailtoPass").children("option[value='Retake Lesson']").prop('selected',true)
		else if(objQuizSetup.actionOnFailtoPass=='Go To Next Lesson')
			$quizSetuptemplate.find("#embactionOnFailtoPass").children("option[value='Go To Next Lesson']").prop('selected',true)
		else
			$quizSetuptemplate.find("#embactionOnFailtoPass").children("option[value='Lock Course']").prop('selected',true)
	}

	$quizSetuptemplate.find ('#setupfooter').attr('data-parent', '#quiz_container' + contentObjectId);
	$quizSetuptemplate.find('#setupfooter').attr('href', quizsetup_id);
	$quizSetuptemplate.show ();
	$('#quiz_container' + contentObjectId).find (quizsetup_id).html ($quizSetuptemplate);
	configureQuizSetupValidator($('#quiz_container' + contentObjectId)[0]);
	APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");
}

//listfooter
function quizList (trg) {

	isFromExamORQuiz = "Quiz";
	course_id = getUrlParameter ("id");
	$trg = $(trg);
	contentObjectId    = $trg.data('lesson-id');

	quizBanktemplate =  $('div#quizList').html ();
	$quizBanktemplate = $(quizBanktemplate);

	$('#quiz_container' + contentObjectId).find ('#quiz_banks_' + contentObjectId ).html($quizBanktemplate);
	$quizBanktemplate.find ('#listfooter').attr('data-parent', '#quiz_container' + contentObjectId);
	$quizBanktemplate.find('#listfooter').attr('href', '#quiz_banks_' + contentObjectId);

	// get List of Assessment Item banks,
	// Pass dynamic ID so that they can work out what needs to be done.
	getbankBar ('#quiz_banks_' +  contentObjectId , contentObjectId);
}

function getbankBar (bankListivId, contentObjectId) {

	course_id = getUrlParameter ("id");

	b_bankList = false;
	//GET bank bar list
	APP.AJAX({
		url: "getbankList",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'contentId='+contentObjectId + '&id=' +course_id ,
		async: false,
		success: function(response) {
			 lstQuestionBank = $.parseJSON(response);
			 b_bankList = true;
		}
    });

	$('#quiz_container' + contentObjectId).find (bankListivId).find ('#lesson_1_quiz_1_bank_1_').attr('id','lesson_1_quiz_1_bank_1' + contentObjectId);

	if (b_bankList) {
		for(var i = 0; i < lstQuestionBank.length; i++) {

			objQuestionBank = lstQuestionBank[i];
			quizBanktemplate =  $('div#bankBar').clone ();
			$quizBanktemplate = $(quizBanktemplate);
			$quizBanktemplate.find ('#bankTitle_').attr('id','bankTitle_' + objQuestionBank.id);
			quizBanktemplate.attr('id','bankBar'+ contentObjectId);
			$quizBanktemplate.find ('#bankTitle_'+ objQuestionBank.id).html('<i class="icon-question-bank"><i></i></i>' + objQuestionBank.name);
			$quizBanktemplate = $quizBanktemplate.html();
			$quizBanktemplate = $($quizBanktemplate);

			$quizBanktemplate.find ('#bankTitle_'+ objQuestionBank.id).attr('data-bank-id', objQuestionBank.id);
			$quizBanktemplate.find ('#bankTitle_'+ objQuestionBank.id).attr('data-lesson-id', contentObjectId);

			$quizBanktemplate.find ('#bankTitle_'+ objQuestionBank.id).attr('href','#questions_' + objQuestionBank.id + '_' + contentObjectId );
			$quizBanktemplate.find ('#bankTitle_'+ objQuestionBank.id).attr('data-parent','#lesson_1_quiz_1_bank_1' + contentObjectId );
			$quizBanktemplate.find ('#lesson_1_quiz_1_bank_1_questions_').attr('id','questions_' + objQuestionBank.id + '_' + contentObjectId);

			$('#quiz_container' + contentObjectId).find (bankListivId).find('#lesson_1_quiz_1_bank_1' + contentObjectId).append($quizBanktemplate);
		}
	}
}

function postQuestionNo_Quiz(course_id, bank_id, contentObject_id) {
	 if (!$( '#questions_' + bank_id + '_' + contentObject_id ).is( '.collapsed' ) )  {

			 var numOfQuestions = $('#questions_' + bank_id + '_' + contentObject_id).find("a[id^=question_item_]").length;
			 var numOfQuestions_tobe_asked = $('#Quiz_No_Questions_'+bank_id).val();




			if (numOfQuestions_tobe_asked < 0 ) {

					$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Careful!</b> The target number of questions may not be a negative number. Set the target number to a value greater than or equal to 0 but less than or equal to the total number of questions in this bank </div>');
					elementFadeOut("#msgdiv");
					if (e != null ){
						e.stopPropagation ();
						e.preventDefault ();
					}
					return false;
			}
			 var bMsgAlreadyDisplayed = true;
			 if(numOfQuestions_tobe_asked>numOfQuestions)
			 {
					$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Just a moment!</b> The number of target questions for this question bank cannot exceed the total number of questions in this bank. Please add the required number of questions, or adjust the target number of questions.</div>');
					elementFadeOut("#msgdiv");
					bMsgAlreadyDisplayed = false;

			 }
			 else if(numOfQuestions_tobe_asked==0){
				$('#msgdiv').html('<div class=\'alert alert-warning alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Attention!</b> The target number of questions is currently set to 0. This means that none of the questions in this question bank will be included in the quiz. You may want to set the target number of questions to a number greater than 0, but less than or equal to the total number of questions in this bank.</div>');
				elementFadeOut("#msgdiv");
				bMsgAlreadyDisplayed = false;
			}
			 else
				 {
				 	$("#msgdiv").html('');
				 }


				elementFadeOut("#msgdiv");


				APP.AJAX({
				url: "postQuestionNo_Quiz",
				dataType: "text",
				type: "POST",
				cache: false,
				data:'course_id='+ course_id +'&bank_id='+ bank_id + '&contentObject_id=' + contentObject_id + '&No_of_Question=' + $('#Quiz_No_Questions_'+bank_id).val(),
				async: false,
				success: function(response) {
					 objQuestion = $.parseJSON(response);
				}
		    });


			return bMsgAlreadyDisplayed;
			}
}
function getBankList(trg) {

	$trg = $(trg);

	bank_id    			= $trg.data('bank-id');
	contentObject_id    = $trg.data('lesson-id');
	course_id 			= getUrlParameter ("id");
	objNoQuestion = 0;
	//WLCMS-117:starts

	if ($( '#questions_' + bank_id + '_' + contentObject_id ).hasClass( 'panel-collapse a1 collapse in' ) ) {
		var bitPstQuestNoQuiz = postQuestionNo_Quiz(course_id,bank_id,contentObject_id);

		if(bitPstQuestNoQuiz)
			TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});

		$("#hidQuizNoOfQuestionContentObjectId").val(0);
		$("#hidQuizNoOfQuestionBankId").val(0);
	}
	 else
	 {
		 	resetHidenForSave();
			$("#hidQuizNoOfQuestionContentObjectId").val(contentObject_id);
			$("#hidQuizNoOfQuestionBankId").val(bank_id);

			APP.AJAX({
			url: "getQuestionNo_Quiz",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'course_id='+ course_id +'&bank_id='+ bank_id + '&contentObject_id=' + contentObject_id + '&No_of_Question=' + $('#Quiz_No_Questions_'+bank_id).val(),
			async: false,
			success: function(response) {

				 objNoQuestion = $.parseJSON(response);


			}
	    });
	 }

	//WLCMS-117:ends

	quizBanktemplate =  $('#bankList').clone ();
	$quizBanktemplate = $(quizBanktemplate);

	$quizBanktemplate.find('#footerBankList').attr ('href','#questions_' + bank_id + '_' + contentObject_id );
	$quizBanktemplate.find('#footerBankList').attr ('data-parent', '#lesson_1_quiz_1_bank_1' + contentObject_id );

	$quizBanktemplate.find('#addQuestion').attr ('data-bank-id', bank_id);
	$quizBanktemplate.find('#addQuestion').attr ('data-lesson-id', contentObject_id);
	$quizBanktemplate.find('#addQuestion').attr('id', '#addQuestion' + bank_id + contentObject_id);


//	Holder
	$quizBanktemplate.find('#lesson_1_quiz_1_bank_1_Q1_').attr('id','question_bars_' + contentObject_id + '_'  + bank_id);
	$quizBanktemplate.find('#Quiz_No_Questions').attr ('id', 'Quiz_No_Questions_'+bank_id);
	$quizBanktemplate.find('#Quiz_No_Questions_'+bank_id).attr ('data-bank-id', bank_id);

	$('#quiz_container' + contentObject_id).find('#questions_' + bank_id + '_' + contentObject_id + '> .panel-body').html($quizBanktemplate.html ());

	getQuestionBar ('#questions_' + bank_id+ '_' + contentObject_id, contentObject_id, bank_id );
	$('#Quiz_No_Questions_'+bank_id).val(objNoQuestion);
}



function getQuestionBar (questionBarId, contentObject_id, AssessmentID) {

	course_id = getUrlParameter ("id");

	if (!AssessmentID)
		return;

	b_bankList = false;
	//GET bank bar list
	APP.AJAX({
		url: "getQuestionList",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'assessmentId='+AssessmentID,
		async: false,
		success: function(response) {
			 b_bankList = true;
			 lstQuestions = $.parseJSON(response);
		}
    });

	if (b_bankList) {
		for(var i = 0; i < lstQuestions.length; i++) {
			objQuestionQuestion = lstQuestions[i];

			quizBanktemplate =  $('div#questionBar').html ();
			$quizBanktemplate = $(quizBanktemplate);


			$quizBanktemplate.find("#quizQuestionTitleText").html(objQuestionQuestion.questionEM.replace(/<(?:.|\n)*?>/gm, ''));
			$quizBanktemplate.find("#quizQuestionTitleText").attr("id", "quizQuestionTitleText_" + objQuestionQuestion.id);

			$quizBanktemplate.find('#quizQuestionTitle').attr('data-lesson-id', contentObject_id);
			$quizBanktemplate.find('#quizQuestionTitle').attr('data-bank-id', AssessmentID);
			$quizBanktemplate.find('#quizQuestionTitle').attr('data-question-id', objQuestionQuestion.id );

			//	Anchors
			$quizBanktemplate.find('#quizQuestionTitle').attr('href','#question_comp_' + objQuestionQuestion.id  + '_' + AssessmentID);
			$quizBanktemplate.find('#quizQuestionTitle').attr('data-parent', '#question_bars_' + contentObject_id  + '_' + AssessmentID);
			$quizBanktemplate.find('#quizQuestionTitle').attr('id','question_item_' + objQuestionQuestion.id  + '_' + AssessmentID);

			//	Panel
			$quizBanktemplate.find('#lesson_1_quiz_1_bank_1_Q1_ans_').attr('id','question_comp_' + objQuestionQuestion.id  + '_' + AssessmentID);


			//	Holder
			$(questionBarId).find('#lesson_1_quiz_1_bank_1_Q1_').attr('id','question_bars_' + contentObject_id + '_'  + AssessmentID);

			$('#question_bars_' + contentObject_id + '_' + AssessmentID).append ($quizBanktemplate);
		}
	}
}

function validateEditAnswerChoice (question_id){
	return $('#tblAnswerChoices_' + question_id + '_').find ('tbody tr .correct:contains("true")').length>0;
}

function getQuestion (trg, event) {

	isFromExamORQuiz = "Quiz";
	$trg = $(trg);
	course_id 			= getUrlParameter ("id");
	bank_id    			= $trg.data('bank-id');
	contentObject_id    = $trg.data('lesson-id');
	question_id 		= $trg.data('question-id');

	$('input[name=hidBankId]').val(bank_id);
	$('input[name=hidLessonId]').val(contentObject_id);
	$('input[name=hidQuestionId]').val(question_id);


	// if the Question have class collapsed then  question needs to be updated
	if ($( '#question_comp_' + question_id + '_' + bank_id ).hasClass( 'panel-collapse a1 collapse in' ) ) {
		if (! $( '#question_item_' + question_id + '_' + bank_id ).is( '.collapsed' ) )  {
			if (validateEditAnswerChoice (question_id)) {
				var isquestionUpdated = updateQuestion (bank_id, contentObject_id, question_id, event);

				$('#tblAnswerChoices_' + question_id + '_').parent ().removeClass("error");
				$('#tblAnswerChoices_' + question_id + '_').parent ().parent ().find ('.answer-error').remove ();

				if(isquestionUpdated){
					TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
					$("#hidQuizQuestionId").val(0);
				}
			}else {
				TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
				$('#tblAnswerChoices_' + question_id + '_').parent ().addClass("error");
				// solve error $parent is null here
				if ($('#tblAnswerChoices_' + question_id + '_').parent ().parent ().find ('label.answer-error').length <= 0)
					$('#tblAnswerChoices_' + question_id + '_').parent ().parent ().append("<label for='Quizanswerchoices' generated='true' class='answer-error'>Please mark one the answer choice as the correct answer.</label>");
				event.preventDefault();
				event.stopPropagation();
				return false;
			}



			return;
		}
	}
	resetHidenForSave();
	$("#hidQuizQuestionContentObjectId").val(contentObject_id);
	$("#hidQuizQuestionBankId").val(bank_id);
	$("#hidQuizQuestionId").val(question_id);

	b_getQuestion = false;
	// Call AJAX to fill data
	APP.AJAX({
		url: "getQuestion",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemId='+question_id,
		async: false,
		success: function(response){
			 b_getQuestion = true;
			 objQuestion = $.parseJSON(response);
		}
    });
	if (b_getQuestion) {

		quizBanktemplate =  $('div#questionList').html ();
		$quizBanktemplate = $(quizBanktemplate);

		// Set DATA
		$quizBanktemplate.find ('#questionTitleEmb').attr('id','questionTitleEmb' + question_id  + '_' + bank_id);

		questionTitle = objQuestion.questionEM;

		$quizBanktemplate.find ('#questionTitleEmb' + question_id  + '_' + bank_id).val (questionTitle );

		$quizBanktemplate.find('#embQuestionType option[value=\''+ objQuestion.questionType  + '\']').attr('selected','selected');
		$quizBanktemplate.find('#embComplexity option[value=\''+ objQuestion.complexity  + '\']').attr('selected','selected');



		//answerChoice
		$quizBanktemplate.find('#answerChoice').attr('data-bank-id', bank_id);
		$quizBanktemplate.find('#answerChoice').attr('data-question-id', objQuestion.id );
		$quizBanktemplate.find('#answerChoice').attr('id','answerChoice' + objQuestion.id + '_' + bank_id);
		$quizBanktemplate.find('#tbQuestionAnswer2').attr('id','tblAnswerChoices_' + objQuestion.id + '_' );

		//	Anchors
		$quizBanktemplate.find('#question_com_footer').attr('href','#question_comp_' + question_id + '_' + bank_id);
		$quizBanktemplate.find('#question_com_footer').attr('data-parent', '#question_bar_' + question_id + '_' + bank_id);
		$quizBanktemplate.find('#question_com_footer').attr('id','question_bar_footer' + question_id + '_' + bank_id);

		$('#question_comp_' + question_id  + '_' + bank_id + " > .panel-body").html($quizBanktemplate);

		APP.CKEDITOR ('questionTitleEmb' + question_id  + '_' + bank_id,'STANDARD');

		var correctVal = objQuestion.disableRandomizeAnswerChoiceTF;

		if(objQuestion.disableRandomizeAnswerChoiceTF)
			$quizBanktemplate.find('#embchkAnswerRandomization').prop("checked", true);
		else
			$quizBanktemplate.find('#embchkAnswerRandomization').prop("checked", false);

		$quizBanktemplate.find("#btnUpdtQuizDelQstnOptionTemp").attr("id", "btnUpdtQuizDelQstnOption");
		$quizBanktemplate.find("#chkUpdtQuizAnswersSlctAllTemp").attr("id", "chkUpdtQuizAnswersSlctAll");
		APP.BODY_COLLAPSES("CLOSE");
	}
}

function openQuiz (trg) {
	// set default values in add quiz form
	$('#chkrandomizeQuestions').prop('checked', true);
	$('#chkrandomizeAnswers').prop('checked', true);
	$('#allowReviewaftGrading').prop('checked', true);

	$( "#timeforQuiz" ).val(15);
	$( "#scorePassQuiz" ).val(70);
	$( "#noAttemptsPermitted" ).val(3);

	$('#opgradeQuestions').prop('selectedIndex', 0);
	$('#actionOnFailtoPass').prop('selectedIndex', 0);

	var form = $("#frmAddQuizModal").validate();
	form.resetForm();
	$("#frmAddQuizModal").find(".error").removeClass("error");

	APP.CACHE = trg;
}

function openAddQuestion(trg){
	$("#btnQuizDelQstnOption").attr("disabled", true);
	$("#chkQuizAnswersSlctAll").attr("disabled", true);
	APP.CACHE = [];
	APP.CACHE[0] = trg;

	var form = $("#addQuestionModalForm").validate();
    form.resetForm();
	$("#tbQuestionAnswer2").parent().parent ().parent ().find("label.answer-error").remove();
	$("#tbQuestionAnswer2").parent().removeClass("error");
}

function chkCorrectQuestionAnswer () {

	var table = $("#tblAnswerChoices");
	table.find('tr').each(function (i, el) {
        var $tds = $(this).find('td'),
        choice  = $tds.eq(1).text(),
        correct = $tds.eq(2).text(),
        feedback = $tds.eq(3).text();
        if ( correct == 'true') {
			$('#isCorrect').attr("disabled", true);
			return false;
		} else {
			$('#isCorrect').removeAttr("disabled");
		}

    });
}

// Modal listener
function addQuestion() {

	$trg 				= $(APP.CACHE[0]);
	bank_id    			= $trg.data('bank-id');
	contentObject_id    = $trg.data('lesson-id');
	question_id    		= $trg.data('question-id');

	var editor = CKEDITOR.instances.questionTitle;
	var vName = editor.getData();
	vName = $(vName).text ();
	editor.updateElement();
	bValid = $("#addQuestionModalForm").validate().element("#questionTitle");
	var len = $('#addQuestionModal').find('#tbQuestionAnswer2 tbody tr').length;
	var errorMsg = (bValid == false && len==0) ? WLCMS_CONSTANTS.VALIDATION_PLURAL_MESSAGE : WLCMS_CONSTANTS.VALIDATION_SINGULAR_MESSAGE;
	var bAnswerChoiceValidation = $('#addQuestionModal').find ('#tblAnswerChoices tr .correct:contains("true")').length>0;
	console.log ("Answer Length :" + len + " - Question Title Validation :" + bValid + " - Answer have true : " + bAnswerChoiceValidation  ) ;

	if (bValid == false || len < 2 || bAnswerChoiceValidation ==  false){
		$('#msgdiv').html(errorMsg);
		elementFadeOut("#msgdiv");
		$('#btaddQuestionSave').removeAttr("data-dismiss");
		errormsg = '';

		if (bAnswerChoiceValidation ==  false && len > 1){
			errormsg = WLCMS_CONSTANTS.VALIDATION_ONE_TRUE_ANSWER;
		}
		if(len==0){
			errormsg = WLCMS_CONSTANTS.VALIDATION_NUMBER_OF_ANSWER;
		}
		if(len==1){
			errormsg = WLCMS_CONSTANTS.VALIDATION_SINGLE_ANSWER;
		}
		if ($('#addQuestionModal').find("#tbQuestionAnswer2").parent().parent().find ('.answer-error').length <= 0 )
			$('#addQuestionModal').find("#tbQuestionAnswer2").parent().parent().append(errormsg);
		else
			$('#addQuestionModal').find("#tbQuestionAnswer2").parent().parent().find ('.answer-error').html(errormsg);

		$('#addQuestionModal').find("#tbQuestionAnswer2").parent().addClass("error");
		return false;
	}

	answerArray = '';
	var table = $("#tblAnswerChoices");
	table.find('tr').each(function (i, el) {
		var $tds = $(this).find('td'),
		choice  = escape(replaceAnswerText($tds.eq(1).text())),
		correct = $tds.eq(2).text(),
		feedback = escape(replaceAnswerText($tds.eq(3).text()));
		answerArray += choice + '--' + correct + '--' + feedback + '::';
	});

	valQuestion = CKEDITOR.instances['questionTitle'].getData();
	valQuestion = valQuestion.replace(/\+/g,'&plussign;');
	valQuestion = escape(valQuestion);

	// get Assessment ID, content ID
	bPostQuestion = false;
	var randomizeAnswer = false;
	if($('input[name="randomizeAnswer"]').is( ":checked" )){
		randomizeAnswer = true;
	}


	APP.AJAX({
			url: "postQuestion",
			dataType: "text",
			type: "POST",
			cache: false,
			data:'id='+ course_id +'&bank_id='+ bank_id + '&complexity=' + $('#complexity').val() + '&questionType=' + $('#questionType').val() + '&question=' + valQuestion + '&randomizeAnswer=' + randomizeAnswer + '&answers=' + answerArray,
			async: false,
			success: function(response) {
				 bPostQuestion = true;
				 objQuestion = $.parseJSON(response);
			}
		});
		if (bPostQuestion) {

			TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});

			quizBanktemplate =  $('div#questionBar').html ();
			$quizBanktemplate = $(quizBanktemplate);

			$quizBanktemplate.find('#quizQuestionTitle').attr('data-lesson-id', contentObject_id);
			$quizBanktemplate.find('#quizQuestionTitle').attr('data-bank-id', bank_id);
			$quizBanktemplate.find('#quizQuestionTitle').attr('data-question-id', objQuestion.id );

			$quizBanktemplate.find('#quizQuestionTitle').attr('href','#question_comp_' + objQuestion.id  + '_' + bank_id);
			$quizBanktemplate.find('#quizQuestionTitle').attr('data-parent', '#question_bars_' + contentObject_id  + '_' + bank_id);

			$quizBanktemplate.find("#quizQuestionTitleText").html($(objQuestion.questionEM).text().replace(/<(?:.|\n)*?>/gm, ''));
			$quizBanktemplate.find("#quizQuestionTitleText").attr("id", "quizQuestionTitleText_" + objQuestion.id);

			$quizBanktemplate.find('#quizQuestionTitle').attr('id','question_item_' + objQuestion.id  + '_' + bank_id);

				//	Anchors
			$quizBanktemplate.find('#question_com_footer').attr('href','#question_comp_' + objQuestion.id + '_' + bank_id);
			$quizBanktemplate.find('#question_com_footer').attr('data-parent', '#question_bar_' + objQuestion.id + '_' + bank_id);
			$quizBanktemplate.find('#question_com_footer').attr('id','question_bar_footer' + objQuestion.id + '_' + bank_id);

			//answerChoice
			$quizBanktemplate.find('#answerChoice').attr('data-bank-id', objQuestion.id);
			$quizBanktemplate.find('#answerChoice').attr('data-question-id', objQuestion.id );
			$quizBanktemplate.find('#answerChoice').attr('id','answerChoice' + objQuestion.id + '_' + bank_id);
			$quizBanktemplate.find('#tbQuestionAnswer2').attr('id','tblAnswerChoices_' + objQuestion.id + '_' );

			$quizBanktemplate.find('#lesson_1_quiz_1_bank_1_Q1_ans_').attr('id','question_comp_' + objQuestion.id + '_' + bank_id);

			//	Holder
			$('#questions_' + bank_id+ '_' + contentObject_id ).find('#lesson_1_quiz_1_bank_1_Q1_ans_').attr('id','question_bars_' + contentObject_id + '_'  + bank_id);

			$('#question_bars_' + contentObject_id + '_' + bank_id ).append ($quizBanktemplate);

			$quizBanktemplate.show();
			APP.BODY_COLLAPSES("CLOSE");

			CKEDITOR.instances['questionTitle'].setData('');
			$('#btaddQuestionSave').attr("data-dismiss", "modal");

			$('#addQuizModal').modal ('hide');
			$('#tblAnswerChoices').html('');
			$('#isCorrect').removeAttr("disabled");
			$('#complexity').children("option[value='0']").prop('selected',true);
			$('#randomizeAnswer').prop('checked', false);
			APP.CACHE = '';

			$("#tbQuestionAnswer2").parent().parent ().parent ().find("label.answer-error").remove();
			$("#tbQuestionAnswer2").parent().removeClass("error");

			return true;

		}
}

function cancelAnswer () {

	CKEDITOR.instances['ans-ckeditor-1'].setData('');
	CKEDITOR.instances['feedback-ckeditor-1'].setData('');
	$ ('#isCorrect').attr ('checked', false);
	$('#isCorrect').removeAttr("disabled");
}

function acceptAnswer () {
	$("#tbQuestionAnswer2").parent().parent().find("label.answer-error").remove();
	$("#tbQuestionAnswer2").parent().removeClass("error");
	$(".table-scrollable").siblings("label").remove();
	answer = CKEDITOR.instances['ans-ckeditor-1'].getData();
	if(answer == ''){
		TopMessageBar.displayMessageTopBar({vType:2,  vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST,bFadeOut:true});
		$('#btAcceptAnswer').removeAttr("data-dismiss");
		return;
	}
	feedback = CKEDITOR.instances['feedback-ckeditor-1'].getData();
	correctChoice = $('#isCorrect').is(':checked') ? "true" : "false";

	$answerRow = $('#rwAnswer').clone ();


	$answerRow.attr('id','');
	strQuestionChoice = "<a onclick=\"getDetailAnswerChoice(this)\" href=\"javascript:;\" data-target=\"#addAnsChoiceModal3\" data-toggle=\"modal\" title=" + $(answer).text()  + "><!--<i class=\"icon-plus\">--></i>" + $(answer).text() +"</a>";
	$answerRow.find ('.choice').html (strQuestionChoice);
	$answerRow.find ('.correct').html (correctChoice);
	$answerRow.find ('.feedback').html ($(feedback).text ());

	$answerRow.show ();
	$('#tblAnswerChoices').append($answerRow);

	CKEDITOR.instances['ans-ckeditor-1'].setData('');
	CKEDITOR.instances['feedback-ckeditor-1'].setData('');
	 $('#isCorrect').attr('checked', false);
	$('#isCorrect').removeAttr("disabled");
	$('#btAcceptAnswer').attr("data-dismiss", "modal");

	$("#btnQuizDelQstnOption").attr("disabled", false);
	$("#chkQuizAnswersSlctAll").attr("disabled", false);
}

function cancelQuestion () {

	CKEDITOR.instances['questionTitle'].setData('');
	$('#tblAnswerChoices').html('');
	$('#complexity').children("option[value='0']").prop('selected',true);
	$('#randomizeAnswer').prop('checked', false);


	$("#tbQuestionAnswer2").parent().parent().find("lable.answer-error").remove();
	$("#tbQuestionAnswer2").parent().removeClass("error");

	APP.CACHE = '';
}

function addAnswer2(trg) {
	APP.CACHE = trg;

	$trg = $(trg);
	question_id = $trg.parents("table:first").attr('id');
	var table = $('#' + question_id);

    table.find('tr').each(function (i, el) {
		$el = $(el);
		if ($el.is('tr')) {
			var $tds = $(this).find('td'),
			choice  = $tds.eq(1).text(),
			correct = $tds.eq(2).text(),
			feedback = $tds.eq(3).text();
			if (correct == 'true')	 {
				$('#isCorrect2').attr("disabled", true);
				return false;
			}else{
				$('#isCorrect2').removeAttr("disabled");
			}
		}
    });
}

function acceptAnswer2 () {

	answer = CKEDITOR.instances['ans-ckeditor-2'].getData();
	if(answer == ''){
		TopMessageBar.displayMessageTopBar({vType:2,  vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST,bFadeOut:true});
		$('#btAcceptAnswer2').removeAttr("data-dismiss");
		return;
	}

	feedback = CKEDITOR.instances['feedback-ckeditor-2'].getData();
	correctChoice = $('#isCorrect2').is(':checked') ? "true" : "false";


	$answerRow = $('#rwAnswer').clone ();

	$answerRow.attr('id','');
	strQuestionChoice = "<a onclick=\"getDetailAnswerChoice(this)\" href=\"javascript:;\" data-target=\"#addAnsChoiceModal3\" data-toggle=\"modal\" title=" + $(answer).text()  + "><!--<i class=\"icon-plus\">--></i>" + $(answer).text() +"</a>";
	$answerRow.find ('.choice').html (strQuestionChoice);
	$answerRow.find ('.correct').html (correctChoice);
	$answerRow.find ('.feedback').html ($(feedback).text ());
	$answerRow.show ();

	$trg = $(APP.CACHE);
	question_id = $trg.parents("table:first").attr('id');
	$('#' + question_id).find('tbody').append($answerRow);

	CKEDITOR.instances['ans-ckeditor-2'].setData('');
	CKEDITOR.instances['feedback-ckeditor-2'].setData('');
	$('#isCorrect2').attr ('checked', false);
	$('#isCorrect2').removeAttr("disabled");

	console.log ("acceptAnswer2:: before data-dissmiss");

	$('#btAcceptAnswer2').attr("data-dismiss", "modal");

	$('#addAnsChoiceModal2').modal ('hide');

	APP.CACHE = '';

	$("#btnUpdtQuizDelQstnOption").attr("disabled", false);
	$("#chkUpdtQuizAnswersSlctAll").attr("disabled", false);
}

function correctanswervalidation (event){
	var question_id = $("#hidQuizQuestionId").val();
	var bank_id = $("#hidQuizNoOfQuestionBankId").val();
	var question__id = '#tblAnswerChoices_' + question_id + "_";
	var table = $(question__id);

	var bPostQuestion = true;
	var len = $('#tblAnswerChoices_' + question_id + '_  tbody tr').length;
	var bAnswerChoiceValidation = $('#tblAnswerChoices_' + question_id + '_').find ('tbody tr .correct:contains("true")').length>0;
	console.log ("Answer Length :" + len +  " - Answer have true : " + bAnswerChoiceValidation  ) ;
	var errormsg = '';
	if ( len < 2 || bAnswerChoiceValidation ==  false){
		$('#msgdiv').html( WLCMS_CONSTANTS.VALIDATION_PLURAL_MESSAGE);
		elementFadeOut("#msgdiv");
		if (bAnswerChoiceValidation ==  false && len > 1){
			errormsg = WLCMS_CONSTANTS.VALIDATION_ONE_TRUE_ANSWER;

			$('#tblAnswerChoices_' + question_id + '_').parent().addClass("error");
			bPostQuestion = false;
		}
		if(len==0){
			errormsg = WLCMS_CONSTANTS.VALIDATION_NUMBER_OF_ANSWER;

			$('#tblAnswerChoices_' + question_id + '_').parent().addClass("error");
			bPostQuestion = false;
		}
		if(len==1){
			errormsg = WLCMS_CONSTANTS.VALIDATION_SINGLE_ANSWER;

			$('#tblAnswerChoices_' + question_id + '_').parent().addClass("error");
			bPostQuestion = false;
		}
		if ($('#tblAnswerChoices_' + question_id + '_').parent().parent().find ('.answer-error').length <= 0 )
			$('#tblAnswerChoices_' + question_id + '_').parent().parent().append(errormsg);
		else
			$('#tblAnswerChoices_' + question_id + '_').parent().parent().find ('.answer-error').html(errormsg);

		if (event) {
			event.preventDefault();
			event.stopPropagation();
		}
	}

	return bPostQuestion;
}

function finalExamCorrectAnswerValidation (){
	var bPostQuestion = false;
	var question__id = '#tbodytblAnswerChoicesUpdateExamAct';
	var table = $(question__id);
		table.find('tr').each(function (i, el) {
        var $tds = $(this).find('td'),
        choice  = $tds.eq(1).text(),
        correct = $tds.eq(2).text(),
        feedback = $tds.eq(3).text();

		$(question__id).parent().parent().parent().find("lable.answer-error").remove();
		$(question__id).parent().parent().removeClass("error");
		$(".table-scrollable").siblings("label").remove();

    if ( correct == 'true') {
		$(question__id).parent().parent().find("lable.answer-error").remove();
		$(question__id).parent().parent().removeClass("error");
		$(".table-scrollable").siblings("label").remove();
		bPostQuestion = false;
		return bPostQuestion;
		} else {
			bPostQuestion = true;
			$(question__id).parent().parent().addClass("error");
		}
		if ( bPostQuestion == true ){
			$(question__id).parent().parent().parent().append("<label for='Quizanswerchoices' generated='true' class='answer-error'>Please mark one the answer choice as the correct answer.</label>");
			}
				return bPostQuestion;
		});
		return bPostQuestion;
}

function cancelAnswer2 () {
	CKEDITOR.instances['ans-ckeditor-2'].setData('');
	CKEDITOR.instances['feedback-ckeditor-2'].setData('');
	$('#isCorrect2').attr ('checked', false);
	$('#isCorrect2').removeAttr("disabled");

	APP.CACHE = '';
}

function updateQuestion (bank_id, contentObject_id, question_id, event)
{
	valQuestion = 	CKEDITOR.instances['questionTitleEmb' + question_id + '_' + bank_id].getData();
	valComplexity = $('#question_comp_' + question_id + '_' + bank_id ).find ('#embComplexity').val ();
	valQuestionType = $('#question_comp_' + question_id + '_' + bank_id ).find ('#embQuestionType').val ();
    b_UpdateQuestion = false;
    var disableAnswerRandomization = false;

	if (valQuestion.length <= 0 ) {
		$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button><b>Attention.</b> The question field is required. Please enter the question you wish to ask students in this field.</div>');
		elementFadeOut("#msgdiv");
		$('input[name=hidQuestionMessageId]').val("-1");
		return false;
	}
	if($('input[name="embchkAnswerRandomization"]').is( ":checked" )){
		disableAnswerRandomization = true;
	}


	// replace all '+' sign into '&plussign;'
	valQuestion = valQuestion.replace(/\+/g,'&plussign;');
	valQuestion = escape(valQuestion);

	jQuery.ajax({
		url: "updateQuestion",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'question_id='+ question_id + '&complexity=' + valComplexity + '&questionType=' + valQuestionType + '&question=' + valQuestion + '&randomizeAnswer=' + disableAnswerRandomization+'&course_id='+getUrlParameter ("id"),
		async: false,
		success: function(response) {
			b_UpdateQuestion = true;
			 objQuestion = $.parseJSON(response);
		}
    });


	var isErrorInAnswerEdit = addAnswerEdit (bank_id,question_id, event);

	if (isErrorInAnswerEdit && b_UpdateQuestion) {
		// any post processing needs to be here?
		valQuestion = CKEDITOR.instances['questionTitleEmb' + question_id + '_' + bank_id].getData().replace(/<(?:.|\n)*?>/gm, '');
		$('#quizQuestionTitleText_' + question_id ).html (valQuestion);

		return true;
	}else{
		return false;
	}
}


function addAnswerEdit (bank_id,question_id, event) {

	tbl_length = $('#tblAnswerChoices_' + question_id + '_').find ('tbody > tr').length;

	// should not move curcor ahead if 'Answer Choices' box is not open in 'Question component'



	if (! correctanswervalidation (event) ){
		$('#msgdiv').html(WLCMS_CONSTANTS.VALIDATION_PLURAL_MESSAGE);
		elementFadeOut("#msgdiv");
		$('input[name=hidQuestionMessageId]').val("-1");
		return false;
	}else{
		$('#tblAnswerChoices_' + question_id + '_').parent ().removeClass("error");
		$('#tblAnswerChoices_' + question_id + '_').parent ().parent ().find ('label.answer-error').remove ();
	}

	answerArray = '';

	var table = $('#tblAnswerChoices_' + question_id + '_');
    table.find('tr').each(function (i, el) {
		$el = $(el);
		if ($el.is('tr')) {
			// submit only new Answers
			if ($el.attr('id') == '') {
				var $tds = $(this).find('td'),
				choice  = escape(replaceAnswerText($tds.eq(1).text())),
				correct = $tds.eq(2).text(),
				feedback = escape(replaceAnswerText($tds.eq(3).text()));
				if (choice != '')
					answerArray += choice + '--' + correct + '--' + feedback + '::';
			}
		}
    });

	if (answerArray == '')
		return true;


	b_postAnswer = false;
	//Post new added Answers
	APP.AJAX({
		url: "postQuestionAnswer",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemId='+ question_id+'&Answers=' + answerArray,
		async: false,
		success: function(response) {
			b_postAnswer = true;
			TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
			getAnswerChoiceAJAX(bank_id,question_id);
		}
    });

	return true
}

//BEGIN ANS DELETE
function delete_answers_choice (trg, target)
{
	$table = $(trg).parent().parent().parent().parent().parent();
	$allChecker = $table.find('thead');
	$allChecker = $allChecker.find('.checker');
	$table = $table.find('tbody');

	var count = $table.find ('tr').length;
	var checkbox  = $table.find("input:checked").length;

	if (APP.CACHE.length <= 0) {
		APP.CACHE = ['1','2','3'];

	}
	APP.CACHE[3] = [$table,$allChecker];


	if (count > 0 && checkbox > 0){
		$trgModal = $("#confirmationModal");
		//	BEGIN TITLE, MESSAGE AND BUTTONS
		var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';
		var msg = '<p>Are you sure you want to delete selected answer(s)?</p>';
		var btns = '<button type="button" class="btn blue" onclick="ANS_CONFIRM_REMOVE(true,\''+target+'\')" data-dismiss="modal">YES</button>'+
					'<button type="button" class="btn btn-default" onclick="ANS_CONFIRM_REMOVE(false,\''+target+'\')" data-dismiss="modal">NO</button>';
		//	END TITLE, MESSAGE AND BUTTONS

		$trgModal.find(".modal-title").html(title);
		$trgModal.find(".modal-body").html(msg);
		$trgModal.find(".modal-footer").html(btns);

		$trgModal.modal('show');
	}
}
//	END ANS DELETE

//BEGIN ANS CONFIRM DELETE
function ANS_CONFIRM_REMOVE (confirm, target){
	// 'target' parameter tell us the target forms of Question's choices. Question's choices can be created from following target

	if(confirm)
	{
	$table = APP.CACHE[3][0];

		$table.children().each(function(index,element){

			if($(element).find('.checks > input').prop('checked') == true){
				if($(element).attr('id') != null){
					var data_id = ($(element).attr('id')).split('_');
					deleteAssessmentItemAnswer (data_id[2], data_id[3]);
				}
				$(element).remove();
			}

		});

		if($table.children().length==0){
			disableObjectsQstnChces(target);
		}

		$(APP.CACHE[1]).prop("checked",false);
	}
}

function disableObjectsQstnChces(target){
	switch (target) {
		case 'ADD_EXAM':
				$("#chkExamAnswersSlctAll").attr("disabled", true);
				$("#btnExamDelQstnOption").attr("disabled", true);
			break;
		case 'UPDATE_EXAM':
				$("#btnUpdtExamDelQstnOption").attr("disabled", true);
				$("#chkUpdtExamAnswersSlctAll").attr("disabled", true);
			break;
		case 'ADD_QUIZ':
				$("#btnQuizDelQstnOption").attr("disabled", true);
				$("#chkQuizAnswersSlctAll").attr("disabled", true);
			break;
		case 'UPDATE_QUIZ':
				$("#btnUpdtQuizDelQstnOption").attr("disabled", true);
				$("#chkUpdtQuizAnswersSlctAll").attr("disabled", true);
			break;
	}
}
//	END ANS CONFIRM DELETE

//	BEGIN ANS CHECKALL
function ANS_CHECKBOX (trg,isChecker){

	$table = $(trg).parent().parent().parent().parent();

	if(isChecker)
	{
		//	HEADER CHECKBOX CONTROL BODY CHECKBOXES
		$tbody = $table.find('tbody');

		$tbody.children().each(function(index, element){

			//	Update Body CheckBoxs
			$(element).find('.checks > input').prop('checked',trg.checked);

		});
	}
	else
	{

		//	BODY CHECKBOX CONTROL HEADER CHECKBOXES
		$tbody = $table.find('tbody');

		$allChecker = $table.find('thead');
		$allChecker = $allChecker.find('.checker');

		if(trg.checked)
		{
			$isCheckAll = true;

			$tbody.children().each(function(index, element){

				if($(element).find('.checks > input').prop('checked') == false)
				{
					$isCheckAll = false;
				}

			});

			$allChecker.prop('checked',$isCheckAll);
		}
		else
		{
			$allChecker.prop('checked',trg.checked);
		}
	}
}
//	END ANS CHECKALL


function deleteAssessmentItemAnswer (question_id, answer_id) {

	//GET bank bar list
	APP.AJAX({
		url: "deleteQuestionAnswer",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemId='+ question_id + '&answerId=' + answer_id,
		async: false,
		success: function(response) {

		}
    });
}

function getAnswerChoice (trg, event) {

	$trg = $(trg);
	bank_id    			= $trg.data('bank-id');
	question_id    		= $trg.data('question-id');

	// Check out if the div is collapsing or expanding
	$parent = $trg.parent().parent();
	b_getAnswler = $parent.find('.tools').find('a').hasClass('collapse');

	if (b_getAnswler){
		var len = $('#tblAnswerChoices_' + question_id + '_  tbody tr').length;
		var bAnswerChoiceValidation = $('#tblAnswerChoices_' + question_id + '_').find ('tbody tr .correct:contains("true")').length>0;
		console.log ("Answer Length :" + len +  " - Answer have true : " + bAnswerChoiceValidation  ) ;
		var errormsg = '';
		if ( len < 2 || bAnswerChoiceValidation ==  false){
			$('#msgdiv').html( WLCMS_CONSTANTS.VALIDATION_PLURAL_MESSAGE);
			elementFadeOut("#msgdiv");
			if (bAnswerChoiceValidation ==  false && len > 1){
				errormsg = WLCMS_CONSTANTS.VALIDATION_ONE_TRUE_ANSWER;

				$('#tblAnswerChoices_' + question_id + '_').parent().addClass("error");
			}
			if(len==0){
				errormsg = WLCMS_CONSTANTS.VALIDATION_NUMBER_OF_ANSWER;

				$('#tblAnswerChoices_' + question_id + '_').parent().addClass("error");
			}
			if(len==1){
				errormsg = WLCMS_CONSTANTS.VALIDATION_SINGLE_ANSWER;

				$('#tblAnswerChoices_' + question_id + '_').parent().addClass("error");
			}
			if ($('#tblAnswerChoices_' + question_id + '_').parent().parent().find ('.answer-error').length <= 0 )
				$('#tblAnswerChoices_' + question_id + '_').parent().parent().append(errormsg);
			else
				$('#tblAnswerChoices_' + question_id + '_').parent().parent().find ('.answer-error').html(errormsg);

			event.preventDefault();
			event.stopPropagation();
			return false;
		}
		else{
			$('#tblAnswerChoices_' + question_id + '_').parent ().removeClass("error");
			$('#tblAnswerChoices_' + question_id + '_').parent ().parent ().find ('label.answer-error').remove ();
		}

		addAnswerEdit (bank_id,question_id, event);
		return;
	}
	getAnswerChoiceAJAX(bank_id,question_id);

}

function getAnswerChoiceAJAX(bank_id,question_id){
	b_answerList = false;
	//GET bank bar list
	APP.AJAX({
		url: "getQuestionAnswer",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemId='+ question_id,
		async: false,
		success: function(response) {
			b_answerList = true;
			 lstAnswers = $.parseJSON(response);
		}
    });

	if (b_answerList) {
		$('#tblAnswerChoices_' + question_id + '_').find('tbody').html ('');
		for(var i = 0; i < lstAnswers.length; i++) {
			objAnswer = lstAnswers[i];

			answerTemplate =  $('#rwAnswer').clone();
			$answerTemplate = $(answerTemplate);
			strQuestionChoice = "<a onclick=\"getDetailAnswerChoice(this)\" href=\"javascript:;\" data-target=\"#addAnsChoiceModal3\" data-toggle=\"modal\" title=" + objAnswer.label  + "><!--<i class=\"icon-plus\">--></i>" + objAnswer.label +"</a>";


			$answerTemplate.find ('.choice').html (strQuestionChoice);
			$answerTemplate.find ('.correct').html ((objAnswer.isCorrectTF == true) ? 'true' : 'false' );
			$answerTemplate.find ('.feedback').html (objAnswer.feedBack);
			$answerTemplate.attr('id','rwAnswer_' + bank_id + '_' + question_id + '_' + objAnswer.id);



			$answerTemplate.show ();
			$('#tblAnswerChoices_' + question_id + '_').append ($answerTemplate);

		}
	}
}

function getDetailAnswerChoice(trg) {


	if (APP.CACHE.length > 0)
			APP.CACHE[1] = trg;
	else
			APP.CACHE = trg;

	$trg = $(trg);
	$trg = $trg.parent ().parent();

	strIds = $trg.attr('id');
	answer_id = 0;

	if (strIds) {
		var arr = strIds.split('_');
		answer_id = arr[3];
	}

	var table = $trg.parent ().parent ();

	bGetAnswerDetail = false;

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
		if (bGetAnswerDetail){
			CKEDITOR.instances['ans-ckeditor-3'].setData(objAnswer.value);
			CKEDITOR.instances['feedback-ckeditor-3'].setData(objAnswer.feedBack);
			var correctVal = objAnswer.isCorrectTF;

			if( correctVal) {
				$('#addAnsChoiceModal3').find ('#isCorrect3').prop("checked", true);
				$("#addAnsChoiceModal3").find("#isCorrect3").removeAttr("disabled");
				$("#addAnsChoiceModal3").find("#isCorrect3").attr("disabled", false);

			}
			else {
				if(table.find('td.correct:contains("true")').length>0)
						$("#addAnsChoiceModal3").find("#isCorrect3").attr("disabled", true);

				$('#addAnsChoiceModal3').find ('isCorrect3').prop("checked", false);
			}
		}
	} else{
		$columns = $trg.find ('td');
		jQuery.each($columns, function(i, item) {
			$item = $(item);
			if ($item.hasClass('choice') )
				CKEDITOR.instances['ans-ckeditor-3'].setData($item.text ());

			if ($item.hasClass('feedback') )
				CKEDITOR.instances['feedback-ckeditor-3'].setData($item.text ());

			if ($item.hasClass('correct')){
				if ($item.text ()== 'true')  {
					$('#addAnsChoiceModal3').find ('#isCorrect3').prop("checked", true);
					$("#addAnsChoiceModal3").find("#isCorrect3").removeAttr("disabled");
					$("#addAnsChoiceModal3").find("#isCorrect3").attr("disabled", false);
				}
				else {
					if(table.find('td.correct:contains("true")').length>0)
						$("#addAnsChoiceModal3").find("#isCorrect3").attr("disabled", true);

					$('#addAnsChoiceModal3').find ('#isCorrect3').prop("checked", false);
				}
			}
		});
	}

}

function acceptAnswer3 () {

	answer = CKEDITOR.instances['ans-ckeditor-3'].getData();
	if(answer == ''){
		TopMessageBar.displayMessageTopBar({vType:2,  vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST,bFadeOut:true});
		$('#btAcceptAnswer3').removeAttr("data-dismiss");
		return;
	}

	feedback = CKEDITOR.instances['feedback-ckeditor-3'].getData();
	correctChoice = $('#isCorrect3').is(':checked') ? "true" : "false";

	$trg  = APP.CACHE.length > 0 ? $(APP.CACHE[1]) : $(APP.CACHE);


	$trg = $trg.parent ().parent();

	if (strIds) {
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

	CKEDITOR.instances['ans-ckeditor-3'].setData('');
	CKEDITOR.instances['feedback-ckeditor-3'].setData('');
	$('#isCorrect3').attr ('checked', false);
	$('#isCorrect3').removeAttr("disabled");
	$('#btAcceptAnswer3').attr("data-dismiss", "modal");

	//

}


function cancelAnswer3 () {
	CKEDITOR.instances['ans-ckeditor-3'].setData('');
	CKEDITOR.instances['feedback-ckeditor-3'].setData('');
	$('#isCorrect3').attr ('checked', false);
	$('#isCorrect3').removeAttr("disabled");

	APP.CACHE = '';
}


function removeAssessmentBank (trg) {

	$trg = $(trg);
	trg1 = $trg.siblings('.panel-title').html();
	$trg = $(trg1);

	bank_id    	 = $trg.data('bank-id');

	remove_panel (trg, 3, bank_id);
}

function removeQuizQuestion (trg) {
	$trg = $(trg);
	trg1 = $trg.siblings('.panel-title').html();
	$trg = $(trg1);


	question_id  = $trg.data('question-id');
	// remove from UI
	remove_panel (trg, 4, question_id);
}

function removeQuiz(trg) {
	$trg = $(trg);
	trg1 = $trg.siblings ('.panel-title').html ();
	$trg1 = $(trg1);

	contentObjectId    = $trg1.data('lesson-id');

	remove_Quiz_panel(trg1, contentObjectId);
}

function deleteAssessmentItemBank (id) {

	APP.AJAX({
		url: "deleteAssessmentItemBank",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemBankId='+ id,
		async: false,
		success: function(response) {
		}
    });

}
function deleteAssessmentItem(id) {

	APP.AJAX({
		url: "deleteQuestion",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'asssementItemId='+ id+'&course_id='+getUrlParameter("id"),
		async: false,
		success: function(response) {
		}
    });
}

//---------------------------------------------------------------------------------------------------------------------------------------------
// Deleting/Disabling Exam - Start
//---------------------------------------------------------------------------------------------------------------------------------------------
function remove_Quiz_panel(trg, id)
{

	$trgModal = $("#confirmationModal");

	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';

	var msg = '<p>Are you sure you want to turn off the quiz for this lesson?</p>';

	var btns = '<button type="button" class="btn blue" onclick="confirm_Quiz_remove(true,'+id+ ')" data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default" onclick="confirm_Quiz_remove(false)" data-dismiss="modal">NO</button>';
	//	END TITLE, MESSAGE AND BUTTONS

	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');

}

function confirm_Quiz_remove(confirm, id){

	if(confirm){
		deleteQuiz(id);
	}

}

function deleteQuiz (id) {

	course_id = getUrlParameter ("id");

	bDisableQuiz = false;

	APP.AJAX({
		url: "disableQuiz",
		dataType: "text",
		type: "POST",
		cache: false,
		data:'contentID='+ id + '&courseID='+course_id,
		async: false,
		success: function(response) {
			bDisableQuiz = true;
		}
    });

	if (bDisableQuiz) {
		$('#quiz_container'+id).hide ();
		$('#addQuizLinkDiv_'+id).show ();
	}
}
