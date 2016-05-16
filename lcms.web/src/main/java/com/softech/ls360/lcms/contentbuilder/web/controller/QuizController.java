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

import com.softech.ls360.lcms.contentbuilder.model.AssessmentItem;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemAnswer;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.CourseSettings;
import com.softech.ls360.lcms.contentbuilder.model.QuizConfiguration;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.model.CourseSettings;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.service.ICourseSettingsService;
import com.softech.ls360.lcms.contentbuilder.service.IQuizService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;

@Controller
public class QuizController {

//	@Autowired
	private static Logger logger = LoggerFactory.getLogger(QuizController.class);

	@Autowired
	IQuizService quizService;
	
	@Autowired
	VU360UserService vu360UserService;
	
	@Autowired
	ICourseSettingsService courseSettingsService;	
	
	@Autowired
	ICourseService courseService;
	
	// 	load Quiz
	@RequestMapping(value = "getQuizConfiguration", method = RequestMethod.GET)
	public @ResponseBody
	ContentObject getQuizConfiguration(HttpServletRequest request, HttpServletResponse response)throws Exception {
	
		
		return null;
	}
	
	// insert Quiz configuration
	@RequestMapping(value = "postQuizConfiguration", method = RequestMethod.POST)
	public @ResponseBody
	ContentObject postQuizConfiguration(HttpServletRequest request, HttpServletResponse response)throws Exception {
		logger.info("QuizController::postQuizConfiguration - BEGIN");
			
		//WLCMS-482
		if(request.getParameter("InvalidateQuizMastery")!=null && Boolean.parseBoolean(request.getParameter("InvalidateQuizMastery"))==true){
			CourseSettings courseSettings = new CourseSettings();
			courseSettings.setCourse_Id(Long.parseLong(request.getParameter("id")));
			courseSettings = courseSettingsService.getCourseSettings(courseSettings);
			courseSettings.setPassAllQuizzes(false);
			courseSettings = courseSettingsService.update(courseSettings);
		}

		int noTargetQuestions = Integer.parseInt(request.getParameter("noTargetQuestions"));		
		boolean randomizeQuestions = Boolean.parseBoolean(request.getParameter("randomizeQuestions"));
		boolean randomizeAnswers = Boolean.parseBoolean(request.getParameter("randomizeAnswers"));
		int timeforQuiz = Integer.parseInt(request.getParameter("timeforQuiz"));
		String gradeQuestions = request.getParameter("gradeQuestions");		
		boolean allowReviewaftGrading = Boolean.parseBoolean( request.getParameter("allowReviewaftGrading"));
		int scorePassQuiz = Integer.parseInt(request.getParameter("scorePassQuiz"));
		int noAttemptsPermitted = Integer.parseInt(request.getParameter("noAttemptsPermitted"));
		String actionOnFailtoPass = request.getParameter("actionOnFailtoPass");
		

		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		QuizConfiguration qzConfiguration = new QuizConfiguration();
		qzConfiguration.setNoTargetQuestions(noTargetQuestions);
		qzConfiguration.setRandomizeQuestions(randomizeQuestions);
		qzConfiguration.setRandomizeAnswers(randomizeAnswers);
		qzConfiguration.setTimeforQuiz(timeforQuiz);
		qzConfiguration.setGradeQuestions(gradeQuestions);
		qzConfiguration.setAllowReviewaftGrading(allowReviewaftGrading);
		qzConfiguration.setScorePassQuiz(scorePassQuiz);
		qzConfiguration.setNoAttemptsPermitted(noAttemptsPermitted);
		qzConfiguration.setActionOnFailtoPass(actionOnFailtoPass);
		qzConfiguration.setCourseId(Integer.parseInt(request.getParameter("id")));
		qzConfiguration.setContentId(Integer.parseInt(request.getParameter("content_id")));
		qzConfiguration.setUserId((int)user.getAuthorId());
		
		
		quizService.insertQuizConfiguration(qzConfiguration);
		courseService.updateCourseModifiedDate(Long.parseLong(request.getParameter("id")));		
		logger.info("QuizController::postQuizConfiguration - END");
		
		return null;
	}
	
	
	// insert Quiz configuration
	@RequestMapping(value = "updateQuizConfiguration", method = RequestMethod.POST)
	public @ResponseBody
	ContentObject updateQuizConfiguration(HttpServletRequest request, HttpServletResponse response)throws Exception {
		logger.info("QuizController  ::   updateQuizConfiguration - BEGIN.....................................................");
		
		System.out.println(Boolean.parseBoolean(request.getParameter("InvalidateQuizMastery")));	
		//WLCMS-482
		if(request.getParameter("InvalidateQuizMastery")!=null && Boolean.parseBoolean(request.getParameter("InvalidateQuizMastery"))==true){
			CourseSettings courseSettings = new CourseSettings();
			courseSettings.setCourse_Id(Long.parseLong(request.getParameter("id")));
			courseSettings = courseSettingsService.getCourseSettings(courseSettings);
			courseSettings.setPassAllQuizzes(false);
			courseSettings = courseSettingsService.update(courseSettings);
		}

		int noTargetQuestions = Integer.parseInt(request.getParameter("noTargetQuestions"));		
		boolean randomizeQuestions = Boolean.parseBoolean(request.getParameter("randomizeQuestions"));
		boolean randomizeAnswers = Boolean.parseBoolean(request.getParameter("randomizeAnswers"));
		int timeforQuiz = Integer.parseInt(request.getParameter("timeforQuiz"));
		String gradeQuestions = request.getParameter("gradeQuestions");		
		boolean allowReviewaftGrading = Boolean.parseBoolean(request.getParameter("allowReviewaftGrading"));
		int scorePassQuiz = Integer.parseInt(request.getParameter("scorePassQuiz"));
		int noAttemptsPermitted = Integer.parseInt(request.getParameter("noAttemptsPermitted"));
		String actionOnFailtoPass = request.getParameter("actionOnFailtoPass");
		
		//VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		QuizConfiguration qzConfiguration = new QuizConfiguration();
		qzConfiguration.setNoTargetQuestions(noTargetQuestions);
		qzConfiguration.setRandomizeQuestions(randomizeQuestions);
		qzConfiguration.setRandomizeAnswers(randomizeAnswers);
		qzConfiguration.setTimeforQuiz(timeforQuiz);
		qzConfiguration.setGradeQuestions(gradeQuestions);
		qzConfiguration.setAllowReviewaftGrading(allowReviewaftGrading);
		qzConfiguration.setScorePassQuiz(scorePassQuiz);
		qzConfiguration.setNoAttemptsPermitted(noAttemptsPermitted);
		qzConfiguration.setActionOnFailtoPass(actionOnFailtoPass);
		qzConfiguration.setCourseId(Integer.parseInt(request.getParameter("id")));
		qzConfiguration.setContentId(Integer.parseInt(request.getParameter("content_id")));
		//qzConfiguration.setUserId((int)user.getAuthorId());
		
		
		quizService.updateQuizConfiguration(qzConfiguration);
		courseService.updateCourseModifiedDate(Integer.parseInt(request.getParameter("id")));		
		logger.info("QuizController::updateQuizConfiguration - END.....................................................");
		
		return null;
	}
	
	@RequestMapping(value = "getQuizSetup", method = RequestMethod.POST)
	public @ResponseBody
	QuizConfiguration getQuizSetup(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		int courseId = Integer.parseInt(request.getParameter("id"));	
		
		QuizConfiguration qzConfiguration = new QuizConfiguration();
		qzConfiguration.setCourseId(courseId);
		qzConfiguration.setContentId(Integer.parseInt(request.getParameter("contentId")));
		return  quizService.getQuizConfiguration (qzConfiguration);
		
	}
		
	@RequestMapping(value = "getbankList", method = RequestMethod.POST)
	public @ResponseBody
	List<AssessmentItemBank> getbankList(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		ContentObject objContent = new ContentObject ();		
		objContent.setCourseID (Integer.parseInt(request.getParameter("id")));
		objContent.setID(Integer.parseInt(request.getParameter("contentId")));
		
		List<AssessmentItemBank>  lstAsessmentItemBank = quizService.getQuizBankListign(objContent);
		return lstAsessmentItemBank;
	}
	
	@RequestMapping(value = "getQuestionList", method = RequestMethod.POST)
	public @ResponseBody
	List<AssessmentItem> getQuestionList(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
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
	
	@RequestMapping(value = "getQuestion", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItem getQuestion(HttpServletRequest request, HttpServletResponse response)throws Exception {
		int asssementItemId = Integer.parseInt( request.getParameter("asssementItemId"));
		
		AssessmentItem assessmentItem = new AssessmentItem ();
		assessmentItem.setId(asssementItemId);

		assessmentItem = quizService.getAssessmentItem (assessmentItem);
		return assessmentItem;
	}
	
	@RequestMapping(value = "getQuestionAnswer", method = RequestMethod.POST)
	public @ResponseBody
	List<AssessmentItemAnswer>  getQuestionAnswer(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		//AssessmentItemAnswer assementItemAnswer = new AssessmentItemAnswer();
		AssessmentItem assmentItem = new AssessmentItem ();
		int asssementItemId = Integer.parseInt( request.getParameter("asssementItemId"));
		assmentItem.setId(asssementItemId);
	    
		List<AssessmentItemAnswer> lstAssementItemAnswer = quizService.getAssessmentItemAnswer(assmentItem);
		return lstAssementItemAnswer;
	}
	
	
	@RequestMapping(value = "getDetailAnswer", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItemAnswer getDetailAnswer(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		AssessmentItemAnswer assementItemAnswer = new AssessmentItemAnswer();
		
		int asssementItemId = Integer.parseInt( request.getParameter("answerId"));
		assementItemAnswer.setId(asssementItemId);
	    
	 assementItemAnswer = quizService.getDetailAssessmentItemAnswer(assementItemAnswer);
		return assementItemAnswer;
	}
	
	
	@RequestMapping(value = "updateAnswer", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItemAnswer updateAnswer(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		AssessmentItemAnswer assementItemAnswer = new AssessmentItemAnswer();
		
		int asssementItemId = Integer.parseInt( request.getParameter("answerId"));
		
		assementItemAnswer.setId(asssementItemId);
		assementItemAnswer.setValue(request.getParameter("answer")); 
		assementItemAnswer.setLabel(request.getParameter("answer")); 
		assementItemAnswer.setFeedBack(request.getParameter("feedback"));
		assementItemAnswer.setIsCorrectTF(Boolean.parseBoolean( request.getParameter("choice")));
	    
		 assementItemAnswer = quizService.updateAssessmentItemAnswer(assementItemAnswer);
		return assementItemAnswer;
	}
	
	@RequestMapping(value = "postQuestion", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItem postQuestion(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		int complexity = Integer.parseInt(request.getParameter("complexity"));		
		String questionType = request.getParameter("questionType");
		String question = request.getParameter("question");
		if(question!=null)
			question = question.replaceAll("&plussign;", "+");
		
		boolean randomizeAnswer = Boolean.parseBoolean(request.getParameter("randomizeAnswer"));
		String Answer =  request.getParameter("answers");
		if(Answer!=null)
			Answer = Answer.replaceAll("&plussign;", "+");
		
		int bank_id = Integer.parseInt (request.getParameter("bank_id"));
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
	}
	
	@RequestMapping(value = "postQuestionNo_Quiz", method = RequestMethod.POST)
	public @ResponseBody
	boolean postQuestionNo_Quiz(HttpServletRequest request, HttpServletResponse response)throws Exception {
		int course_id = Integer.parseInt(request.getParameter("course_id"));		
		int bank_id = Integer.parseInt(request.getParameter("bank_id"));
		int contentObject_id = Integer.parseInt(request.getParameter("contentObject_id"));
		int No_of_Question = Integer.parseInt(request.getParameter("No_of_Question"));
		
		System.out.println("course_id:"+course_id);
		System.out.println("bank_id:"+bank_id);
		System.out.println("contentObject_id:"+contentObject_id);
		
		//LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		quizService.saveQuizNo_of_questions(contentObject_id, No_of_Question,bank_id, (int)user.getAuthorId());
		
		return true;	
	}
	
	//getQuestionNo_Quiz
	@RequestMapping(value = "getQuestionNo_Quiz", method = RequestMethod.POST)
	public @ResponseBody
	int getQuestionNo_Quiz(HttpServletRequest request, HttpServletResponse response)throws Exception {
		int course_id = Integer.parseInt(request.getParameter("course_id"));		
		int bank_id = Integer.parseInt(request.getParameter("bank_id"));
		int contentObject_id = Integer.parseInt(request.getParameter("contentObject_id"));
		//int No_of_Question = Integer.parseInt(request.getParameter("No_of_Question"));
		
		System.out.println("course_id:"+course_id);
		System.out.println("bank_id:"+bank_id);
		System.out.println("contentObject_id:"+contentObject_id);
		
		return quizService.getQuizNo_of_questions(contentObject_id, bank_id);
		
	}

	
	@RequestMapping(value = "updateQuestion", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItem updateQuestion(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		AssessmentItem assessmentItem = new AssessmentItem ();
		
		int assessmentItemId = Integer.parseInt(request.getParameter("question_id")); 
		int complexity = Integer.parseInt(request.getParameter("complexity"));		
		String questionType = request.getParameter("questionType");
		String question = request.getParameter("question");
		if(question!=null)
			question = question.replaceAll("&plussign;", "+");
		
		boolean randomizeAnswer = Boolean.parseBoolean(request.getParameter("randomizeAnswer"));
		
		//LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		assessmentItem.setId(assessmentItemId);
		assessmentItem.setCreateUserId((int) user.getAuthorId());
		assessmentItem.setContentOwner_Id((int) user.getContentOwnerId());
		assessmentItem.setComplexity(complexity);
		assessmentItem.setQuestionType(questionType);
		assessmentItem.setQuestionEM(question);
		assessmentItem.setDisableRandomizeAnswerChoiceTF(randomizeAnswer);
		
		assessmentItem = quizService.updateAssessmentItem(assessmentItem);
		courseService.updateCourseModifiedDate(Integer.parseInt(request.getParameter("course_id")));
		return assessmentItem;	
	}
	

	@RequestMapping(value = "postQuestionAnswer", method = RequestMethod.POST)
	public @ResponseBody
	List<AssessmentItemAnswer> postQuestionAnswer(HttpServletRequest request, HttpServletResponse response)throws Exception {
	
		List <AssessmentItemAnswer> lstAssessmentItemAnswer = new ArrayList<AssessmentItemAnswer>();
		
		
		String Answer =  request.getParameter("Answers");
		if(Answer!=null)
			Answer = Answer.replaceAll("&plussign;", "+");
		
		int asseessmentItemId = Integer.parseInt (request.getParameter("asssementItemId"));
		String [] answerArray = (Answer!=null?Answer.split("::"):new String[0]);
		
	
		//LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// SET ANSWERS
		for (String rwAnswer : answerArray) {
			String [] colAnswer = rwAnswer.split("--");
			AssessmentItemAnswer objAssessmentItemAnswer = new AssessmentItemAnswer ();
			
			objAssessmentItemAnswer.setAssessmentItem_Id(asseessmentItemId);
			objAssessmentItemAnswer.setLabel(colAnswer[0]);
			objAssessmentItemAnswer.setValue(colAnswer[0]);
			if (colAnswer.length > 1)
				objAssessmentItemAnswer.setIsCorrectTF(Boolean.parseBoolean(colAnswer[1]));
			if (colAnswer.length > 2)
				objAssessmentItemAnswer.setFeedBack(colAnswer[2]);
			
			objAssessmentItemAnswer.setCreateUserId((int)user.getAuthorId());
			
			lstAssessmentItemAnswer.add(objAssessmentItemAnswer);
		}
		
		lstAssessmentItemAnswer = quizService.insertAssessmentItemAnswer (lstAssessmentItemAnswer);
		return lstAssessmentItemAnswer;
	}
	
	@RequestMapping(value = "deleteQuestionAnswer", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItemAnswer deleteQuestionAnswer(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		int answerId =   Integer.parseInt (request.getParameter("answerId"));
		int asseessmentItemId = Integer.parseInt (request.getParameter("asssementItemId"));
		
		AssessmentItemAnswer assessmentItemAnswser = new AssessmentItemAnswer ();
		
		assessmentItemAnswser.setAssessmentItem_Id(asseessmentItemId);
		assessmentItemAnswser.setId(answerId);
		
		quizService.deleteAssessmentItemAnswer(assessmentItemAnswser);
		return assessmentItemAnswser;
		
	}
	
	@RequestMapping(value = "deleteQuestion", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItem deleteQuestion (HttpServletRequest request, HttpServletResponse response)throws Exception {
		AssessmentItem assessmentItem = new AssessmentItem();
		int assessmentItemId =   Integer.parseInt (request.getParameter("asssementItemId"));
		assessmentItem.setId (assessmentItemId);
		quizService.deleteAssessmentItem(assessmentItem);
		courseService.updateCourseModifiedDate(Integer.parseInt(request.getParameter("course_id")));
		return assessmentItem;
	}
	
	@RequestMapping(value = "deleteAssessmentItemBank", method = RequestMethod.POST)
	public @ResponseBody
	AssessmentItemBank deleteAssessmentItemBank (HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		AssessmentItemBank assessmentItemBank = new AssessmentItemBank();
		int assessmentItemBankId =   Integer.parseInt (request.getParameter("asssementItemBankId"));
		assessmentItemBank.setId (assessmentItemBankId);
		quizService.deleteAssessmentItem(assessmentItemBank);
		return assessmentItemBank;
	}

	@RequestMapping(value = "disableQuiz", method = RequestMethod.POST)
	public @ResponseBody
	boolean disableQuiz (HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		int contentID = Integer.parseInt (request.getParameter("contentID"));
		int courseID  = Integer.parseInt (request.getParameter("courseID"));
		
		quizService.disableQuiz(courseID, contentID);
		courseService.updateCourseModifiedDate(courseID);
		return true;
	}
	
	
	
}