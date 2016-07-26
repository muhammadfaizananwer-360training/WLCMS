/**
	This is globle constants
**/

var WLCMS_CONSTANTS = {

   'TEMPLATE_TYPE_FOR_STREAMING_ID': '2032',
   'TEMPLATE_TYPE_FOR_STREAMING_TXT': 'Streaming Video',
   'TEMPLATE_TYPE_FOR_STREAMING_TXT_L': 'Streaming Video',
   'TEMPLATE_TYPE_FOR_STREAMING_DES': 'This template supports only a single visual component, streaming MP4 video.',
   'TEMPLATE_TYPE_FOR_STREAMING_IMG': 'theme/executive/img/icons/templates/IconVisualStreamingBottonTemplate2.png',
   'VALIDATION_PLURAL_MESSAGE': '<div class=\'alert alert-danger alert-dismissible fade in\' ><button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Not so fast! Required fields are missing. Complete the fields, then click <strong>Save</strong>.</div>',
   'VALIDATION_SINGULAR_MESSAGE': '<div class=\'alert alert-danger alert-dismissible fade in\' ><button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Not so fast! A required field is missing. Complete the field, then click <strong>Save</strong>.</div>',
   'VALIDATION_ONE_TRUE_ANSWER':  '<label for=\'answerchoices\' generated=\'true\' class=\'answer-error\'>Please mark one answer choice as the correct answer.</label>',
   'VALIDATION_NUMBER_OF_ANSWER': '<label for=\'answerchoices\' generated=\'true\' class=\'answer-error\'>This field must not be empty.</label>',
   'VALIDATION_SINGLE_ANSWER': '<label for=\'answerchoices\' generated=\'true\' class=\'answer-error\'>Multiple-choice questions must have at least two answer choices.</label>'
  };

var WLCMS_CONSTANTS_TEMPLATE_MC_Scenario = {
		'TEMPLATE_TYPE_FOR_STREAMING_TEXT_ID': 'Activity - MC Scenario',
		'TEMPLATE_TYPE_FOR_STREAMING_TEXT': 'MC Scenario',
		'TEMPLATE_TYPE_FOR_STREAMING_DES': 'This scenario template features a picture (JPG, GIF, PNG), scenario text (option to scroll), and multiple-choice question with feedback for correct and incorrect responses.',
		'TEMPLATE_TYPE_IMG': 'theme/executive/img/icons/templates/icon_11b.png'

};;

var WLCMS_CONSTANTS_COURSE_TYPE = {
		'ONLINE_COURSE': '4',
		'CLASSROOM_COURSE': '5',
		'WEBINAR_COURSE': '6'
};;

var WLCMS_CONSTANTS_COURSE_STATUS = {
		'STATUS_PUBLISH': 'Published',
		'STATUS_NOT_STARTED': 'Not Started'
};;

var SLIDE_TEMPLATE_TYPE = {
		'CUSTOM_MC': 'MC'
};;

var MODAL_CKEDITOR_INIT = {
	addSlideModal:{locked:false,trgs:[{id: "name", type:"TITLE"}]},
	addAnsChoiceModal:{locked:false,trgs:[{id: "ans-ckeditor-1", type:"TITLE"},{id: "feedback-ckeditor-1", type:"TITLE"}]},
	//addAnsChoiceModal3:{locked:false,trgs:[{id: "ans-ckeditor-3", type:"TITLE"},{id: "feedback-ckeditor-3", type:"TITLE"}]},
	addQuestionModal:{locked:false,trgs:[{id: "questionTitle", type:"STANDARD"}]},
	addAnsChoiceModal2:{locked:false,trgs:[{id: "ans-ckeditor-2", type:"TITLE"}, {id: "feedback-ckeditor-2", type:"TITLE"}]},
	addExamQuestionModal:{locked:false,trgs:[{id: "questionExamTitle", type:"STANDARD"}]},
	//addExamAnsChoiceModalForEdit:{locked:false,trgs:[{id: "choiceTextForEditExam", type:"TITLE"}, {id: "choiceFeedbackForEditExam", type:"TITLE"}]},
	addExamAnsChoiceModal:{locked:false,trgs:[{id: "new_ans_choice_ckeditor_Exam", type:"TITLE"}, {id: "new_ans_choice_feedback_Exam", type:"TITLE"}]},
	addExamAnsChoiceModalForUpdate:{locked:false,trgs:[{id: "choiceTextForUpdateExam", type:"TITLE"}, {id: "choiceFeedbackForUpdateExam", type:"TITLE"}]}
};
