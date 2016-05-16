package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.AssessmentItem;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemAnswer;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.CourseSettings;
import com.softech.ls360.lcms.contentbuilder.model.ExamConfiguration;
import com.softech.ls360.lcms.contentbuilder.model.SearchCourseFilter;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.service.ICourseSettingsService;
import com.softech.ls360.lcms.contentbuilder.service.IExamService;
import com.softech.ls360.lcms.contentbuilder.service.IQuizService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.utils.JsonResponse;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;


@Controller
public class ExamController {

	private static Logger logger = LoggerFactory.getLogger(ExamController.class);
	
	@Autowired
	IExamService examService;
	
	@Autowired
	VU360UserService vu360UserService;
	
	@Autowired
	IQuizService quizService;
	
	@Autowired
	ICourseSettingsService courseSettingsService;	
	
	@Autowired
	ICourseService courseService;
	
	// Add Exam configuration
	@RequestMapping(value = "examAddUpdate", method = RequestMethod.POST)
	public @ResponseBody
	ContentObject examAddUpdate(HttpServletRequest request, HttpServletResponse response)throws Exception {
	
		logger.info("ExamController.java ::	examAddUpdate - BEGIN");
		String course_id = request.getParameter("course_id");
		String noTargetQuestionsForExam = request.getParameter("noTargetQuestionsForExam");
		String chkrandomizeQuestionsForExam = request.getParameter("chkrandomizeQuestionsForExam");
		String chkrandomizeAnswersForExam = request.getParameter("chkrandomizeAnswersForExam");
		String timePermittedForExam = request.getParameter("timePermittedForExam");
		String oboGradeQuestionsForExam = request.getParameter("oboGradeQuestionsForExam");
		String allowReviewaftGradingForExam = request.getParameter("allowReviewaftGradingForExam");
		String scoreRequiredPassForExam = request.getParameter("scoreRequiredPassForExam");
		String attemptsPermittedExam = request.getParameter("attemptsPermittedExam");
		String actionOnFailtoPassForExam = request.getParameter("actionOnFailtoPassForExam");
		
		//LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
		ExamConfiguration examConfiguration = new ExamConfiguration();
		examConfiguration.setNoTargetQuestions(  Integer.parseInt(StringUtil.ifNullReturnZero(noTargetQuestionsForExam)) );
		if(StringUtil.ifNullReturnZero(chkrandomizeQuestionsForExam).equals("1"))
			examConfiguration.setRandomizeQuestions( true);
		
		if(StringUtil.ifNullReturnZero(chkrandomizeAnswersForExam).equals("1"))
			examConfiguration.setRandomizeAnswers(true);
		
		examConfiguration.setTimeforExam( Integer.parseInt(StringUtil.ifNullReturnZero(timePermittedForExam)));
		
		if(StringUtil.ifNullReturnEmpty(oboGradeQuestionsForExam).equals("1"))
			examConfiguration.setGradeQuestions("AfterEachQuestionIsAnswered");
		else if(StringUtil.ifNullReturnEmpty(oboGradeQuestionsForExam).equals("2"))
			examConfiguration.setGradeQuestions("AfterAssessmentIsSubmitted");
		
		
		if(StringUtil.ifNullReturnZero(allowReviewaftGradingForExam).equals("1"))
			examConfiguration.setAllowReviewaftGrading(true);
		
		examConfiguration.setScorePassExam( Integer.parseInt(StringUtil.ifNullReturnZero(scoreRequiredPassForExam)) );
		examConfiguration.setNoAttemptsPermitted( Integer.parseInt(StringUtil.ifNullReturnZero(attemptsPermittedExam)) );
		
		if(StringUtil.ifNullReturnEmpty(actionOnFailtoPassForExam).equals("1"))
			examConfiguration.setActionOnFailtoPass("Retake Content");
		else if(StringUtil.ifNullReturnEmpty(actionOnFailtoPassForExam).equals("2"))	
			examConfiguration.setActionOnFailtoPass("Lock Course");
		else if(StringUtil.ifNullReturnEmpty(actionOnFailtoPassForExam).equals("3"))	
			examConfiguration.setActionOnFailtoPass("Continue Course");
		
		
		//examConfiguration.isAllowReviewaftGrading
		
		
		examConfiguration.setCourseId(Integer.parseInt(course_id));
		examConfiguration.setUserId((int)user.getAuthorId());
		
		
		examService.saveExamConfiguration(examConfiguration);
				
		courseService.updateCourseModifiedDate(Integer.parseInt(course_id));
		
		logger.info("ExamController.java ::	examAddUpdate - End");
		return null;
	}
	
	
	
	@RequestMapping(value = "disableFinalExam", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse  disableFinalExam(HttpServletRequest request, HttpServletResponse response)throws Exception {
		JsonResponse res = new JsonResponse();
		
		
		if(request.getParameter("varCourseId")!=null){
			String varLessonId = request.getParameter("varCourseId");
			long courseId = Long.parseLong(StringUtil.ifNullReturnZero(varLessonId));
			
			/*CourseSettings courseSettings = new CourseSettings();
			courseSettings.setCourse_Id(courseId);
			courseSettings = courseSettingsService.getCourseSettings(courseSettings);
			courseSettings.setPassTheExam(false);*/
			
			if(request.getParameter("disablexam") == null){
				//courseSettings.setAttemptTheExam(false);
				examService.disableFinalExam( courseId );
			}
			//courseSettings = courseSettingsService.update(courseSettings);
			
			res.setStatus("SUCCESS");
			courseService.updateCourseModifiedDate(courseId);
		}else
			res.setStatus("Fail");
		
		
		return res;
	}
	
	@RequestMapping(value = "getCourseSettings", method = RequestMethod.GET)
	public @ResponseBody
	CourseSettings CourseSettings (HttpServletRequest request, HttpServletResponse response)throws Exception {
		CourseSettings courseSettings = new CourseSettings();
		
		if(request.getParameter("varCourseId")!=null){
			String varLessonId = request.getParameter("varCourseId");
			long courseId = Long.parseLong(StringUtil.ifNullReturnZero(varLessonId));						
			courseSettings.setCourse_Id(courseId);
			courseSettings = courseSettingsService.getCourseSettings(courseSettings);
		}
		
		return courseSettings;
		
	}
	
	
	
	@RequestMapping(value = "getExamforEdit", method = RequestMethod.POST)
	public @ResponseBody
	ExamConfiguration getExamforEdit(HttpServletRequest request, HttpServletResponse response)throws Exception {
	
		logger.info("ExamController.java ::	getExamforEdit - BEGIN");
		
		String course_id = request.getParameter("course_id");
		ExamConfiguration objCC = examService.getExamforEdit(Integer.parseInt(  StringUtil.ifNullReturnZero(course_id) ));
		
		logger.info("ExamController.java ::	getExamforEdit - End");
		return objCC;
	}
	
	
	
	
	@RequestMapping(value="/getQuestionBankList", method = RequestMethod.POST)
	@ResponseBody
	public List<AssessmentItemBank> getQuestionBankList(HttpServletRequest request, HttpServletResponse response)throws Exception{
		logger.info("In getQuestionBankList .......:End");
		List <AssessmentItemBank> AIBList = null;
		try{
			String varCourseId = request.getParameter("varCourseId");
			AIBList = examService.getQuestionBankList( Integer.parseInt(  StringUtil.ifNullReturnZero(varCourseId) ));
		} catch (Exception ex){
			logger.error(ex.getMessage());
		}		
		
		logger.info("In getQuestionBankList .......:End");
		return AIBList;
	}
	
	
	@RequestMapping(value = "getExamQuestionList", method = RequestMethod.POST)
	public @ResponseBody
	List<AssessmentItem> getExamQuestionList(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		AssessmentItemBank bank = new AssessmentItemBank ();
		//LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int assessmentId =Integer.parseInt( request.getParameter("assessmentId"));		
		bank.setId (assessmentId);
		bank.setContentOwnerId((int)user.getContentOwnerId());
		List<AssessmentItem>  lstAssessmentItem = quizService.getAssessmentItemList(bank);
		
		return lstAssessmentItem;
	}
	
	@RequestMapping(value = "getQuestionNo_Exam", method = RequestMethod.POST)
	public @ResponseBody
	int getQuestionNo_Exam(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		int course_id = Integer.parseInt(request.getParameter("course_id"));		
		int bank_id = Integer.parseInt(request.getParameter("bank_id"));
		
		int QuestionNo = examService.getExamNo_of_questions(bank_id, course_id);
		
		return QuestionNo;
	}
	
	@RequestMapping(value = "postQuestionNo_Exam", method = RequestMethod.POST)
	public @ResponseBody
	boolean postQuestionNo_Exam(HttpServletRequest request, HttpServletResponse response)throws Exception {
		int course_id = Integer.parseInt(request.getParameter("course_id"));		
		int bank_id = Integer.parseInt(request.getParameter("bank_id"));
		int No_of_Question = Integer.parseInt(request.getParameter("No_of_Question"));
		
		System.out.println("course_id:"+course_id);
		System.out.println("bank_id:"+bank_id);
		
		//LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		examService.saveExamNo_of_questions(course_id, No_of_Question,bank_id, (int)user.getAuthorId());
		
		return true;	
	}	
	
	// @RequestMapping(value="uploadVisualAsset", method = RequestMethod.POST)
	// public ModelAndView uploadVisualAsset(MultipartHttpServletRequest request) {
	
	@RequestMapping(value = "saveExamQuestion", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItem saveExamQuestion(HttpServletRequest request, HttpServletResponse response)throws Exception {
		boolean randomizeAnswer=false;
		/*int complexity = Integer.parseInt(request.getParameter("examComplexity"));		
		String questionType = request.getParameter("examQuestionType");
		String question = request.getParameter("questionExamTitle");
		
		if(request.getParameter("examRandomizeAnswer")!=null && request.getParameter("examRandomizeAnswer").equalsIgnoreCase("on"))
			randomizeAnswer = true;
		
		
		//String Answer =  request.getParameter("examAnswers");
		 
		
		if(request.getParameter("examRandomizeAnswer")!=null && request.getParameter("examRandomizeAnswer").equalsIgnoreCase("on"))
			randomizeAnswer = true;
		*/
		
		int bank_id = Integer.parseInt (request.getParameter("hidExamBankId"));
		
		
		int complexity = Integer.parseInt(request.getParameter("complexity"));		
		String questionType = request.getParameter("questionType");
		String question = request.getParameter("question");
		if(question!=null)
			question = question.replaceAll("&plussign;", "+");
		
	//	boolean randomizeAnswer = Boolean.parseBoolean(request.getParameter("randomizeAnswer"));

		if(request.getParameter("randomizeAnswer")!=null && request.getParameter("randomizeAnswer").equalsIgnoreCase("on"))
			randomizeAnswer = true;
		
		
		String Answer =  request.getParameter("answers");
		if(Answer!=null)
			Answer = Answer.replaceAll("&plussign;", "+");
		//int bank_id = Integer.parseInt (request.getParameter("bank_id"));
		String [] answerArray = (Answer!=null?Answer.split("::"):new String[0]);
		List <AssessmentItemAnswer> lstAssessmentItemAnswer = new ArrayList<AssessmentItemAnswer>();
	
		//LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// SET ANSWERS
		for (String rwAnswer : answerArray) {
			String [] colAnswer = rwAnswer.split("--");
			AssessmentItemAnswer objAssessmentItemAnswer = new AssessmentItemAnswer ();
			objAssessmentItemAnswer.setLabel(colAnswer[0]);
			objAssessmentItemAnswer.setValue(colAnswer[0]);
			if (colAnswer.length > 1)
				objAssessmentItemAnswer.setIsCorrectTF(Boolean.parseBoolean(colAnswer[1]));
			if (colAnswer.length > 2)
				objAssessmentItemAnswer.setFeedBack(colAnswer[2]);
			objAssessmentItemAnswer.setCreateUserId((int)user.getAuthorId());
			
			lstAssessmentItemAnswer.add(objAssessmentItemAnswer);
		}
		
		AssessmentItem assessmentItem = new AssessmentItem ();

		assessmentItem.setCreateUserId((int) user.getAuthorId());
		assessmentItem.setContentOwner_Id((int) user.getContentOwnerId());
		assessmentItem.setComplexity(complexity);
		assessmentItem.setQuestionType(questionType);
		assessmentItem.setQuestionEM(question);
		assessmentItem.setDisableRandomizeAnswerChoiceTF(randomizeAnswer);
		
		assessmentItem = quizService.insertAssessmentItem(assessmentItem, lstAssessmentItemAnswer);
		
		quizService.associateAssessmentItemBank(assessmentItem.getId(), bank_id, (int)user.getAuthorId());
		courseService.updateCourseModifiedDate(Integer.parseInt(request.getParameter("id")));
		return assessmentItem;
		
		
	/*	
		boolean randomizeAnswer=false;
		int complexity = Integer.parseInt(request.getParameter("examComplexity"));		
		String questionType = request.getParameter("examQuestionType");
		String question = request.getParameter("questionExamTitle");
		
		if(request.getParameter("examRandomizeAnswer")!=null && request.getParameter("examRandomizeAnswer").equalsIgnoreCase("on"))
			randomizeAnswer = true;
		
		
		//String Answer =  request.getParameter("examAnswers");
		int bank_id = Integer.parseInt (request.getParameter("hidExamBankId"));
		//String [] answerArray = Answer.split("#");
		List <AssessmentItemAnswer> lstAssessmentItemAnswer = new ArrayList<AssessmentItemAnswer>();
	
		
		
		
		LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
	
		
		String[] examChoice = request.getParameterValues("examChoice[]");
		String[] examCorrect = request.getParameterValues("examCorrect[]");
		String[] examFeedback = request.getParameterValues("examFeedback[]");
		//TODO - need to fix this issue 
		examChoice = examChoice[0].split(",");
		examCorrect = examCorrect[0].split(",");
		examFeedback = examFeedback[0].split(",");
		
		// SET ANSWERS
		for (int i=0; i< examChoice.length ; i++) {
			//String [] colAnswer = rwAnswer.split(",");
			AssessmentItemAnswer objAssessmentItemAnswer = new AssessmentItemAnswer ();
			objAssessmentItemAnswer.setLabel(examChoice[i]);
			objAssessmentItemAnswer.setValue(examChoice[i]);
			
			if(examCorrect[i].equals("true"))
				objAssessmentItemAnswer.setIsCorrectTF(true);
			else
				objAssessmentItemAnswer.setIsCorrectTF(false);
			
			
			objAssessmentItemAnswer.setFeedBack(examFeedback[i]);
			objAssessmentItemAnswer.setCreateUserId((int)user.getVu360UserID());
			
			lstAssessmentItemAnswer.add(objAssessmentItemAnswer);
		}
		
		AssessmentItem assessmentItem = new AssessmentItem ();

		assessmentItem.setCreateUserId((int) user.getAuthorId());
		assessmentItem.setContentOwner_Id((int) user.getContentOwnerId());
		assessmentItem.setComplexity(complexity);
		assessmentItem.setQuestionType(questionType);
		assessmentItem.setQuestionEM(question);
		assessmentItem.setDisableRandomizeAnswerChoiceTF(randomizeAnswer);
		
		assessmentItem = quizService.insertAssessmentItem(assessmentItem, lstAssessmentItemAnswer);
		quizService.associateAssessmentItemBank(assessmentItem.getId(), bank_id, (int)user.getVu360UserID());
		return assessmentItem;		
		*/
	}
}
