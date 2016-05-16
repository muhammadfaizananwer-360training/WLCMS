package com.softech.ls360.lcms.contentbuilder.dao.impl;


import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.QuizDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItem;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemAnswer;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.Quiz;
import com.softech.ls360.lcms.contentbuilder.model.QuizConfiguration;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;


public class QuizDAOImpl extends GenericDAOImpl<Quiz> implements QuizDAO {

	private static Logger logger = LoggerFactory.getLogger(QuizDAOImpl.class);
	
	@Transactional	
	@Override
	public QuizConfiguration insertQuizConfiguration(QuizConfiguration qz) {
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(qz.getCourseId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(qz.getContentId()) , Integer.class, ParameterMode.IN);
		//SPCallingParams sparam3 = LCMS_Util.createSPObject("USER_ID", String.valueOf(qz.getUserId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("QUIZ_MAXIMUMNOATTEMPT", String.valueOf(qz.getNoAttemptsPermitted()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("QUIZ_NOQUESTION", String.valueOf(qz.getNoTargetQuestions()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("QUIZ_MASTERYSCORE", String.valueOf(qz.getScorePassQuiz()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("QUIZ_ACTIONTOTAKEAFTERFAILINGMAXATTEMPT", String.valueOf(qz.getActionOnFailtoPass()) , String.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("QUIZ_ENFORCEMAXIMUMTIMELIMIT", String.valueOf(qz.getTimeforQuiz()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam9 = LCMS_Util.createSPObject("QUIZ_RANDOMIZEANSWERS",  String.valueOf(qz.isRandomizeAnswers()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("QUIZ_RANDOMIZEQUESTION",  String.valueOf(qz.isRandomizeQuestions()), Boolean.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("QUIZ_GRADEQUESTIONS", qz.getGradeQuestions() , String.class, ParameterMode.IN);
		
		// field name in DB = 'SHOWQUESTIONANSWERSUMMARY', field in GUI = 'Allow review after grading'
		SPCallingParams sparam14 = LCMS_Util.createSPObject("Quiz_ALLOWREVIEWAFTERGRADING", String.valueOf(qz.isAllowReviewaftGrading()) , String.class, ParameterMode.IN);
		
		SPCallingParams sparam12 = LCMS_Util.createSPObject("ASSESSMENTCONFIGURATION_ID", String.valueOf(""), Integer.class, ParameterMode.OUT);
		SPCallingParams sparam13 = LCMS_Util.createSPObject("QUIZ_ASSESSMENTCONFIGURATION_ASSESSMENTITEMBANK_IDS", String.valueOf(""), String.class, ParameterMode.OUT);
		
		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_INSERT_QUIZ_CONFIGURATION", sparam1, sparam2, sparam4, sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11, sparam14, sparam12,  sparam13);
		qr.execute();
		
		
		Integer assementId = (Integer)qr.getOutputParameterValue("ASSESSMENTCONFIGURATION_ID");
		qz.setId(assementId);
		
		
		return qz;
	}
	
	@Override
	@Transactional
	public QuizConfiguration updateQuizConfiguration(QuizConfiguration qz) {
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(qz.getCourseId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(qz.getContentId()) , Integer.class, ParameterMode.IN);
		//SPCallingParams sparam3 = LCMS_Util.createSPObject("USER_ID", String.valueOf(qz.getUserId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("QUIZ_MAXIMUMNOATTEMPT", String.valueOf(qz.getNoAttemptsPermitted()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("QUIZ_NOQUESTION", String.valueOf(qz.getNoTargetQuestions()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("QUIZ_MASTERYSCORE", String.valueOf(qz.getScorePassQuiz()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("QUIZ_ACTIONTOTAKEAFTERFAILINGMAXATTEMPT", String.valueOf(qz.getActionOnFailtoPass()) , String.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("QUIZ_ENFORCEMAXIMUMTIMELIMIT", String.valueOf(qz.getTimeforQuiz()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam9 = LCMS_Util.createSPObject("QUIZ_RANDOMIZEANSWERS",  String.valueOf(qz.isRandomizeAnswers()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("QUIZ_RANDOMIZEQUESTION",  String.valueOf(qz.isRandomizeQuestions()), Boolean.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("QUIZ_GRADEQUESTIONS", qz.getGradeQuestions() , String.class, ParameterMode.IN);
		
		// field name in DB = 'SHOWQUESTIONANSWERSUMMARY', field in GUI = 'Allow review after grading'
		SPCallingParams sparam12 = LCMS_Util.createSPObject("Quiz_ALLOWREVIEWAFTERGRADING", String.valueOf(qz.isAllowReviewaftGrading()), String.class, ParameterMode.IN);
		
		//SPCallingParams sparam12 = LCMS_Util.createSPObject("ASSESSMENTCONFIGURATION_ID", qz.getGradeQuestions() , String.class, ParameterMode.IN);
		
		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATE_QUIZ_CONFIGURATION", sparam1, sparam2, sparam4, sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11, sparam12);
		qr.execute();
		
		return qz;
	}
	
	@Transactional
	@Override
	public QuizConfiguration getQuizConfiguration (QuizConfiguration qz) {
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(qz.getCourseId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(qz.getContentId()),  Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("EXAMTYPE", "Quiz" , String.class, ParameterMode.IN);
		Object[] contentObjectRows = callStoredProc("LCMS_WEB_GET_QUIZ_CONFIGURATION", sparam1, sparam2, sparam3).toArray();
		Object[] contentObjectRow;
		
		try{
			for (Object QuizConfig : contentObjectRows) {
				contentObjectRow = (Object[]) QuizConfig;
				
				qz.setNoAttemptsPermitted(  Integer.parseInt(  StringUtil.ifNullReturnZero(contentObjectRow[0].toString())  ));					
				qz.setNoTargetQuestions(  Integer.parseInt(  StringUtil.ifNullReturnZero(contentObjectRow[1].toString())  )  );	
				qz.setScorePassQuiz(Integer.parseInt(  StringUtil.ifNullReturnZero(contentObjectRow[2].toString())) );
				qz.setActionOnFailtoPass(StringUtil.ifNullReturnZero(contentObjectRow[3].toString()));
				qz.setTimeforQuiz(Integer.parseInt(  StringUtil.ifNullReturnZero(contentObjectRow[4].toString())));

				if(contentObjectRow[5]!=null)
					qz.setRandomizeAnswers(	Boolean.parseBoolean(contentObjectRow[5].toString())	 );
				
				if(contentObjectRow[6]!=null)
					qz.setRandomizeQuestions(Boolean.parseBoolean(contentObjectRow[6].toString()) );		

				qz.setGradeQuestions(  contentObjectRow[8].toString()  );			
				qz.setAllowReviewaftGrading(   Boolean.parseBoolean(contentObjectRow[8].toString())  );
				
				//field in GUI = 'Grade questions'
				qz.setGradeQuestions( StringUtil.ifNullReturnEmpty(contentObjectRow[7]) );		
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return qz;
	}
	
	
	@Transactional
	@Override
	public boolean saveQuizNo_of_questions(int contentObject_Id,int no_of_Questions,int bank_Id,int loggedUser_Id) throws Exception{
		try{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(contentObject_Id) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("NO_OF_QUESTION", String.valueOf(no_of_Questions) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("BANK_ID", String.valueOf(bank_Id), Integer.class, ParameterMode.IN);
		
		SPCallingParams sparam4 = LCMS_Util.createSPObject("USER_ID", String.valueOf(loggedUser_Id), Integer.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("SAVE_QUIZ_NO_OF_QUESTIONS_INFO", sparam1, sparam2, sparam3, sparam4);
		qr.execute();
		}catch(Exception exp)
		{
			System.out.println(exp.toString());
		}
		return true;
	}
	
	
	@Override
	@Transactional
	public List<Integer> getQuizNo_of_questions(int contentObject_Id,int bank_Id){
		try{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(contentObject_Id) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("BANK_ID", String.valueOf(bank_Id) , Integer.class, ParameterMode.IN);

		//StoredProcedureQuery qr = createQueryParameters("GET_QUIZ_NO_OF_QUESTIONS_INFO", sparam1, sparam2);
		Object[] QuizNo_of_questions =  callStoredProc("GET_QUIZ_NO_OF_QUESTIONS_INFO",sparam1,  sparam2).toArray();
		
		int no_of_question = Integer.parseInt(QuizNo_of_questions[0].toString());
		List<Integer> list = new ArrayList<Integer>();
		list.add(no_of_question);
		
		
		return list;
		}catch(Exception exp){
			System.out.println(exp.toString());
			int no_of_question = 0;
			List<Integer> list = new ArrayList<Integer>();
			list.add(no_of_question);
			return list;	
		}
	}	

	@Override
	@Transactional
	public List<AssessmentItemBank> getQuizBankListign(ContentObject objContent) {

		//SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(objContent.getCourseID()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(objContent.getID()) , Integer.class, ParameterMode.IN);

		Object[] assessmentBanks =  callStoredProc("LCMS_WEB_GET_BANK_BY_LESSON",  sparam2).toArray();
		List<AssessmentItemBank> lstAssessmentBank = new ArrayList<AssessmentItemBank>();
		
		Object[] assessmentBank;
			
		for (Object obj : assessmentBanks) {
			
			assessmentBank = (Object[]) obj;
			AssessmentItemBank objAssessmentBank = new AssessmentItemBank();			
			objAssessmentBank.setId(Integer.parseInt(assessmentBank[0].toString()));
			objAssessmentBank.setName(assessmentBank[1].toString());
			
			lstAssessmentBank.add(objAssessmentBank);
		}

		 return lstAssessmentBank;
	}

	@Override
	@Transactional
	public List<AssessmentItem> getAssessmentItemList(AssessmentItemBank bank) throws Exception {
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("ASSESSMENTITEMBANK_ID", String.valueOf(bank.getId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNERID", String.valueOf(bank.getContentOwnerId()) , Integer.class, ParameterMode.IN);

		Object[] assessmentItems =  callStoredProc("SEARCH_ASSESSMENTITEMBANK_ASSESSMENTITEMS", sparam1, sparam2).toArray();
		List<AssessmentItem> lstAssessmentItem = new ArrayList<AssessmentItem>();
		
		Object[] assessmentItem;
			
		for (Object obj : assessmentItems) {
			
			try {			
				assessmentItem = (Object[]) obj;
				AssessmentItem objAssessmentItem = new AssessmentItem();			
				objAssessmentItem.setId(Integer.parseInt(assessmentItem[0].toString()));
				Clob cb = (Clob)assessmentItem[1];
				objAssessmentItem.setQuestionEM(StringUtil.clobStringConversion(cb));
				
				objAssessmentItem.setQuestionType(assessmentItem[2].toString());
				objAssessmentItem.setASSESSMENTITEM_GUID(assessmentItem[3].toString());
				//objAssessmentItem.setQuestion_Id(new Integer (StringUtils.isEmpty(assessmentItem[4].toString()) ? "0" : assessmentItem[4].toString() ));
				//objAssessmentItem.setQuestion_Id(new Integer (assessmentItem[4] == null ? "0" : assessmentItem[4].toString() ));
				objAssessmentItem.setSequence(TypeConvertor.AnyToInteger(assessmentItem[5]));

                lstAssessmentItem.add(objAssessmentItem);
			}catch (Exception ex) {
				logger.error("QuizDAOImpl::getAssessmentItemList" + ex.getMessage()); 
			}
		}
		return lstAssessmentItem;		
	}
	
	@Transactional
	public AssessmentItem getAssessmentItem (AssessmentItem assessmentItem) throws Exception {
	
		SPCallingParams sparam1 = LCMS_Util.createSPObject("ASSESSMENTITEM_ID", String.valueOf(assessmentItem.getId()) , Integer.class, ParameterMode.IN);
		Object[] assessmentItems =  callStoredProc("SELECT_ASSESSMENTITEM", sparam1).toArray();
		
		AssessmentItem AssessmentItem = new AssessmentItem();		
		Object[] objAssessmentItem;
			
		for (Object obj : assessmentItems) {			
			objAssessmentItem = (Object[]) obj;
			try {	
				AssessmentItem.setId(Integer.parseInt(objAssessmentItem[0].toString()));
				Clob cb = (Clob)objAssessmentItem[1];
				AssessmentItem.setQuestionEM(StringUtil.clobStringConversion(cb));
				AssessmentItem.setQuestionType(objAssessmentItem[2].toString());
                AssessmentItem.setStatus(objAssessmentItem[4].toString());
				
				if(objAssessmentItem[14]!=null)
					AssessmentItem.setDisableRandomizeAnswerChoiceTF(Boolean.valueOf (objAssessmentItem[14].toString()));
				
				AssessmentItem.setASSESSMENTITEM_GUID(objAssessmentItem[9].toString());
			//	AssessmentItem.setQuestion_Id(new Integer (objAssessmentItem[18].toString()));
				
				AssessmentItem.setComplexity(TypeConvertor.AnyToInteger(objAssessmentItem[22]));
			}catch (Exception ex) {
				logger.error("QuizDAOImpl::getAssessmentItem" + ex.getMessage()); 
			}
		}
		return AssessmentItem;		
	}
	
	@Transactional
	@Override
	public List<AssessmentItemAnswer> getAssessmentItemAnswer (AssessmentItem assessmentItem) throws Exception {
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("ASSESSMENTITEM_ID", String.valueOf(assessmentItem.getId()) , Integer.class, ParameterMode.IN);
		Object[] assessmentItemsAnswers =  callStoredProc("SELECT_ASSESSMENTITEM_ASSESSMENTITEMANSWER", sparam1).toArray();
		
		List<AssessmentItemAnswer> lstAssessmentItemAnswer = new ArrayList<AssessmentItemAnswer>();
		
		for (Object obj : assessmentItemsAnswers) {		
			
			try {
					AssessmentItemAnswer assessmentItemAnswer = new AssessmentItemAnswer();
					Object []  objAssessmentItemAnswer = (Object[]) obj;
							
					assessmentItemAnswer.setId(Integer.parseInt(objAssessmentItemAnswer[0].toString()));
					Clob cb = (Clob)objAssessmentItemAnswer[1];
					assessmentItemAnswer.setLabel(StringUtil.clobStringConversion(cb));
					
					cb = (Clob)objAssessmentItemAnswer[2];	
					assessmentItemAnswer.setValue(StringUtil.clobStringConversion(cb));
					
					cb = (Clob)objAssessmentItemAnswer[11];		
					assessmentItemAnswer.setFeedBack(StringUtil.clobStringConversion(cb));
					
					assessmentItemAnswer.setIsCorrectTF((Boolean)objAssessmentItemAnswer[4]);
					
					lstAssessmentItemAnswer.add(assessmentItemAnswer);
			}catch (Exception ex) {
				logger.error("QuizDAOImpl::getAssessmentItemAnswer" + ex.getMessage()); 
			}
		}
		
		return lstAssessmentItemAnswer;
	}

	@Transactional
	@Override
	public AssessmentItem insertAssessmentItem(AssessmentItem assessmentItem)
			throws Exception {
		
		SPCallingParams sparam0 = LCMS_Util.createSPObject("QUESTIONSTEM", String.valueOf(assessmentItem.getQuestionEM()) , String.class, ParameterMode.IN);
		SPCallingParams sparam1 = LCMS_Util.createSPObject("QUESTIONTYPE", String.valueOf(assessmentItem.getQuestionType()) , String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("STATUS", String.valueOf(assessmentItem.getStatus()) , String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("ISANSWERCASESENSITIVETF", String.valueOf(assessmentItem.getIsAnswerCaseSensitiveTF()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("IMAGE_ASSET_ID", "1" , Integer.class, ParameterMode.IN);				
		SPCallingParams sparam5 = LCMS_Util.createSPObject("HIGHVALUELABEL", String.valueOf(assessmentItem.getHighValueLabel()) , String.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("LOWVALUELABEL", String.valueOf(assessmentItem.getLowValueLabel()) , String.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("RATING", String.valueOf(assessmentItem.getRating()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("ASSESSMENTITEM_GUID", UUID.randomUUID().toString().replaceAll("-", "") , String.class, ParameterMode.IN);
		SPCallingParams sparam9 = LCMS_Util.createSPObject("FEEDBACK", assessmentItem.getFeedBack() , String.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(assessmentItem.getContentOwner_Id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("NEWID", String.valueOf(assessmentItem.getId()) , Integer.class, ParameterMode.OUT);
		SPCallingParams sparam12 = LCMS_Util.createSPObject("CreateUserId", String.valueOf(assessmentItem.getCreateUserId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam13 = LCMS_Util.createSPObject("ASSESSMENTITEMTEMPLATE_ID", String.valueOf(assessmentItem.getAssessmentItemTemplate_Id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam14 = LCMS_Util.createSPObject("CORRECTFEEDBACK", assessmentItem.getCorrectFeedback() , String.class, ParameterMode.IN);
		SPCallingParams sparam15 = LCMS_Util.createSPObject("INCORRECTFEEDBACK", assessmentItem.getIncorrectFeedback() , String.class, ParameterMode.IN);
		SPCallingParams sparam16 = LCMS_Util.createSPObject("DISABLERANDOMIZEANSWERCHOICETF", String.valueOf(assessmentItem.getDisableRandomizeAnswerChoiceTF()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam17 = LCMS_Util.createSPObject("FEEBACKTYPE", "AnswerChoice" , String.class, ParameterMode.IN);
		SPCallingParams sparam18 = LCMS_Util.createSPObject("QUESTIONID", String.valueOf(assessmentItem.getQuestionId()) , String.class, ParameterMode.IN);
		SPCallingParams sparam19 = LCMS_Util.createSPObject("REFERENCE", assessmentItem.getReference() , String.class, ParameterMode.IN);
		SPCallingParams sparam20 = LCMS_Util.createSPObject("MAJORCATEGORY", assessmentItem.getMajorCategory() , String.class, ParameterMode.IN);
		SPCallingParams sparam21 = LCMS_Util.createSPObject("MINORCATEGORY", assessmentItem.getMinorCategory(), String.class, ParameterMode.IN);
		SPCallingParams sparam22 = LCMS_Util.createSPObject("TASK", assessmentItem.getTask() , String.class, ParameterMode.IN);
		SPCallingParams sparam23 = LCMS_Util.createSPObject("BLOOMSLEVEL", String.valueOf( assessmentItem.getComplexity()) , Integer.class, ParameterMode.IN);
		
		
		StoredProcedureQuery qr =createQueryParameters("INSERT_ASSESSMENTITEM",sparam0,  sparam1, sparam2,  sparam4, sparam3, sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11,
		          sparam12,  sparam13, sparam14, sparam15, sparam16, sparam17, sparam18, sparam19, sparam20, sparam21, sparam22, sparam23);
		
		qr.execute();

		Integer assementId = (Integer)qr.getOutputParameterValue("NEWID");
		assessmentItem.setId( assementId);
		
		return assessmentItem;
	}

	@Transactional
	@Override
	public AssessmentItemAnswer insertAssessmentItemAnswer(
			AssessmentItemAnswer assessmentItemAnswer) throws Exception {

		SPCallingParams sparam1 = LCMS_Util.createSPObject("ASSESSMENTITEM_ID", String.valueOf(assessmentItemAnswer.getAssessmentItem_Id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("LABEL", String.valueOf(assessmentItemAnswer.getLabel()) , String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("VALUE", String.valueOf(assessmentItemAnswer.getValue()) , String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("ISCORRECTTF", String.valueOf(assessmentItemAnswer.getIsCorrectTF()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("DISPLAYORDER", String.valueOf(assessmentItemAnswer.getDisplayOrder()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("RIGHTITEMTEXT", String.valueOf(assessmentItemAnswer.getRightItemText()) , String.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("RIGHTITEMORDER", String.valueOf(assessmentItemAnswer.getRightItemOrder()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("LEFTITEMTEXT", assessmentItemAnswer.getLeftItemText() , String.class, ParameterMode.IN);
		SPCallingParams sparam9 = LCMS_Util.createSPObject("LEFTITEMORDER", String.valueOf(assessmentItemAnswer.getLeftItemOrder()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("CORRECTORDER", String.valueOf(assessmentItemAnswer.getCorrectOrder()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("ASSESSMENTITEMANSWER_GUID",  UUID.randomUUID().toString().replaceAll("-", "")  , String.class, ParameterMode.IN);
		SPCallingParams sparam12 = LCMS_Util.createSPObject("CreateUserId",  String.valueOf(assessmentItemAnswer.getCreateUserId())  , Integer.class, ParameterMode.IN);
		SPCallingParams sparam13 = LCMS_Util.createSPObject("NEWID", String.valueOf("0") , Integer.class, ParameterMode.OUT);
		SPCallingParams sparam14 = LCMS_Util.createSPObject("FEEDBACK", assessmentItemAnswer.getFeedBack() , String.class, ParameterMode.IN);
		SPCallingParams sparam15 = LCMS_Util.createSPObject("CORRECTFEEDBACK", assessmentItemAnswer.getCorrectFeedBack() , String.class, ParameterMode.IN);
		SPCallingParams sparam16 = LCMS_Util.createSPObject("INCORRECTFEEDBACK", assessmentItemAnswer.getInCorrectFeedBack() , String.class, ParameterMode.IN);
		SPCallingParams sparam17 = LCMS_Util.createSPObject("USEDEFAULTFEEDBACKTF", String.valueOf(assessmentItemAnswer.getUseDefaultTFeedBackTF()), Boolean.class, ParameterMode.IN);
		
		StoredProcedureQuery qr = createQueryParameters("INSERT_ASSESSMENTITEM_ASSESSMENTITEMANSWER", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11,
										sparam12,  sparam13, sparam14, sparam15, sparam16, sparam17);
		
		qr.execute();

		Integer assementId = (Integer)qr.getOutputParameterValue("NEWID");
		assessmentItemAnswer.setId(assementId);
		return assessmentItemAnswer;
	}

	@Transactional
	@Override
	public AssessmentItemBank insertAssessmentItemBank(
			AssessmentItemBank assessmentItemBank) throws Exception {
	
		SPCallingParams sparam1 = LCMS_Util.createSPObject("ASSESSMENTITEMBANK_ID", String.valueOf(assessmentItemBank.getId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("ASSESSMENTITEM_ID", String.valueOf(assessmentItemBank.getAssessmentItem_Id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("SOURCEASSESSMENTITEM_ID", String.valueOf(assessmentItemBank.getSourceAssessmentItem_Id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("SOURCEASSESSMENTITEMBANK_ID", String.valueOf(assessmentItemBank.getSourceAssessmentItemBank_Id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("CreateUserId", String.valueOf(assessmentItemBank.getCreateUserId()) , Integer.class, ParameterMode.IN);
		
		StoredProcedureQuery qr = createQueryParameters("INSERT_ASSESSMENTITEMBANK_ASSESSMENTITEM", sparam1, sparam2, sparam3, sparam4, sparam5);
		qr.execute();
		
		return assessmentItemBank;
	}
	
	
	

	@Transactional
	@Override
	public AssessmentItem updateAssessmentItem(AssessmentItem assessmentItem)
			throws Exception {
		
		SPCallingParams sparam0 = LCMS_Util.createSPObject("ID", String.valueOf(assessmentItem.getId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam1 = LCMS_Util.createSPObject("QUESTIONSTEM", assessmentItem.getQuestionEM() , String.class, ParameterMode.IN);		
		SPCallingParams sparam2 = LCMS_Util.createSPObject("QUESTIONTYPE", assessmentItem.getQuestionType() , String.class, ParameterMode.IN);		
		SPCallingParams sparam3 = LCMS_Util.createSPObject("STATUS", String.valueOf(assessmentItem.getStatus()) , String.class, ParameterMode.IN);		
		SPCallingParams sparam4 = LCMS_Util.createSPObject("ISANSWERCASESENSITIVETF", String.valueOf(assessmentItem.getIsAnswerCaseSensitiveTF()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("HIGHVALUELABEL", assessmentItem.getHighValueLabel() , String.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("LOWVALUELABEL", assessmentItem.getLowValueLabel() , String.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("FEEDBACK", assessmentItem.getFeedBack() , String.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("RATING", String.valueOf(assessmentItem.getRating()) , Integer.class, ParameterMode.IN);		
		SPCallingParams sparam9 = LCMS_Util.createSPObject("IMAGE_ASSET_ID",  "1", Integer.class, ParameterMode.IN);		
		SPCallingParams sparam10 = LCMS_Util.createSPObject("LastUpdateUser", String.valueOf(assessmentItem.getCreateUserId()) , Integer.class, ParameterMode.IN);		
		SPCallingParams sparam11 = LCMS_Util.createSPObject("ASSESSMENTITEMTEMPLATE_ID", String.valueOf(assessmentItem.getAssessmentItemTemplate_Id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam12 = LCMS_Util.createSPObject("CORRECTFEEDBACK", String.valueOf(assessmentItem.getCorrectFeedback()) , String.class, ParameterMode.IN);
		SPCallingParams sparam13 = LCMS_Util.createSPObject("INCORRECTFEEDBACK", String.valueOf(assessmentItem.getIncorrectFeedback()) , String.class, ParameterMode.IN);
		SPCallingParams sparam14 = LCMS_Util.createSPObject("DISABLERANDOMIZEANSWERCHOICETF", String.valueOf(assessmentItem.getDisableRandomizeAnswerChoiceTF()) , Boolean.class, ParameterMode.IN);		
		SPCallingParams sparam15 = LCMS_Util.createSPObject("FEEBACKTYPE", "AnswerChoice" , String.class, ParameterMode.IN);
		SPCallingParams sparam16 = LCMS_Util.createSPObject("QUESTIONID", String.valueOf(assessmentItem.getQuestion_Id()) , String.class, ParameterMode.IN);
		SPCallingParams sparam17 = LCMS_Util.createSPObject("REFERENCE", assessmentItem.getReference() , String.class, ParameterMode.IN);		
		SPCallingParams sparam18 = LCMS_Util.createSPObject("MAJORCATEGORY", assessmentItem.getMajorCategory() , String.class, ParameterMode.IN);
		SPCallingParams sparam19 = LCMS_Util.createSPObject("MINORCATEGORY", assessmentItem.getMinorCategory() , String.class, ParameterMode.IN);
		SPCallingParams sparam20 = LCMS_Util.createSPObject("TASK", assessmentItem.getTask() , String.class, ParameterMode.IN);
		SPCallingParams sparam21 = LCMS_Util.createSPObject("BLOOMSLEVEL", String.valueOf( assessmentItem.getComplexity()) , String.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("UPDATE_ASSESSMENTITEM",sparam0,  sparam1, sparam2,  sparam3, sparam4,  sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11,
		          sparam12,  sparam13, sparam14, sparam15, sparam16, sparam17, sparam18, sparam19, sparam20, sparam21);
		
		qr.execute();
		return assessmentItem;
	}
	@Transactional
	@Override 
	public AssessmentItemAnswer getDetailAssessmentItemAnswer (AssessmentItemAnswer assessmentItemAnswer) throws Exception
	{		
		SPCallingParams sparam0 = LCMS_Util.createSPObject("@AssmentItem_ANSWER", String.valueOf(assessmentItemAnswer.getId()), Integer.class, ParameterMode.IN);		
		Object[] assessmentItemsAnswers =  callStoredProc("LCMS_WEB_GET_ASSESSMENTITEM_ASSESSMENTITEMANSWER",sparam0).toArray();
		
				
		for (Object obj : assessmentItemsAnswers) {		
				try {
						Object []  objAssessmentItemAnswer = (Object[]) obj;
								
						assessmentItemAnswer.setId(Integer.parseInt(objAssessmentItemAnswer[0].toString()));
						
						Clob cb = (Clob)objAssessmentItemAnswer[1];
						assessmentItemAnswer.setLabel(StringUtil.clobStringConversion(cb));
						
						cb = (Clob)objAssessmentItemAnswer[2];	
						assessmentItemAnswer.setValue(StringUtil.clobStringConversion(cb));
						
						cb = (Clob)objAssessmentItemAnswer[3];		
						assessmentItemAnswer.setFeedBack(StringUtil.clobStringConversion(cb));
						
						assessmentItemAnswer.setIsCorrectTF((Boolean)objAssessmentItemAnswer[4]);
						
				}catch (Exception ex) {
					logger.error("QuizDAOImpl::getAssessmentItemAnswer" + ex.getMessage()); 
				}
			}
		
		
		return assessmentItemAnswer;
	}
	
	
	@Transactional
	@Override 
	public AssessmentItemAnswer updateAssessmentItemAnswer (AssessmentItemAnswer assessmentItemAnswer) throws Exception
	{		
		SPCallingParams sparam0 = LCMS_Util.createSPObject("@AssmentItem_ANSWER", String.valueOf(assessmentItemAnswer.getId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam1 = LCMS_Util.createSPObject("@LABEL", assessmentItemAnswer.getLabel(), String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("@VALUE", assessmentItemAnswer.getValue(), String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("@FEEDBACK", assessmentItemAnswer.getFeedBack(), String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("@ISCORRECTTF", String.valueOf(assessmentItemAnswer.getIsCorrectTF()), Boolean.class, ParameterMode.IN);
		
		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATE_ASSESSMENTITEM_ASSESSMENTITEMANSWER",sparam0,  sparam1, sparam2,  sparam3, sparam4);
		
		qr.execute();
		
		return assessmentItemAnswer;
	}
	
	@Transactional
	@Override 
	public AssessmentItemAnswer deleteAssessmentItemAnswer (AssessmentItemAnswer assessmentItemAnswer) throws Exception
	{
		SPCallingParams sparam0 = LCMS_Util.createSPObject("ASSESSMENTITEM_ID", String.valueOf(assessmentItemAnswer.getAssessmentItem_Id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam1 = LCMS_Util.createSPObject("ASSESSMENTITEMANSWER_ID", String.valueOf(assessmentItemAnswer.getId()), Integer.class, ParameterMode.IN);		
		
		StoredProcedureQuery qr = createQueryParameters("DELETE_ASSESSMENTITEM_ASSESSMENTITEMANSWER",sparam0,  sparam1);
		
		qr.execute();
		return assessmentItemAnswer;
	}
	
	
	@Transactional
	@Override 
	public AssessmentItem deleteAssessmentItem (AssessmentItem assessmentItem) throws Exception
	{
		SPCallingParams sparam0 = LCMS_Util.createSPObject("ID", String.valueOf(assessmentItem.getId()) , Integer.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("DELETE_ASSESSMENTITEM",sparam0);
		
		qr.execute();
		return assessmentItem;
	}
	
	@Transactional
	@Override 
	public AssessmentItemBank deleteAssessmentItemBank (AssessmentItemBank assessmentItemBank) throws Exception
	{
		SPCallingParams sparam0 = LCMS_Util.createSPObject("ASSESSMENTITEMBANK_ID", String.valueOf(assessmentItemBank.getId()) , Integer.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("WLCMS_DELETE_ASSESSMENTITEMBANK_ALL",sparam0);
		
		qr.execute();
		return assessmentItemBank;
	}
	
	@Transactional
	@Override
	public boolean disableQuiz (long courseId, int contentId) throws Exception {
		
			//SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);
			SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(contentId) , Integer.class, ParameterMode.IN);
			//SPCallingParams sparam3 = LCMS_Util.createSPObject("USER_ID", "-1" , Integer.class, ParameterMode.IN);
			//SPCallingParams sparam4 = LCMS_Util.createSPObject("EXAMTYPE", "Quiz" , String.class, ParameterMode.IN);
			StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_DELETE_QUIZ_CONFIGURATION",  sparam2);
			qr.execute();
		
		return true;
	}

}

