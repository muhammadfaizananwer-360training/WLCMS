package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.ExamDAO;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.Exam;
import com.softech.ls360.lcms.contentbuilder.model.ExamConfiguration;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;



public class ExamDAOImpl extends GenericDAOImpl<Exam> implements ExamDAO{

	@Transactional
	@Override
	public boolean saveExamConfiguration(ExamConfiguration qz) {
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(qz.getCourseId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(0) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("USER_ID", String.valueOf(qz.getUserId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("MAXIMUMNOATTEMPT", String.valueOf(qz.getNoAttemptsPermitted()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("NOQUESTION", String.valueOf(qz.getNoTargetQuestions()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("MASTERYSCORE", String.valueOf(qz.getScorePassExam()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("ACTIONTOTAKEAFTERFAILINGMAXATTEMPT", String.valueOf(qz.getActionOnFailtoPass()) , String.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("ENFORCEMAXIMUMTIMELIMIT", String.valueOf(qz.getTimeforExam()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam9 = LCMS_Util.createSPObject("RANDOMIZEANSWERS",  String.valueOf(qz.isRandomizeAnswers()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("RANDOMIZEQUESTION",  String.valueOf(qz.isRandomizeQuestions()), Boolean.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("GRADEQUESTIONS", qz.getGradeQuestions() , String.class, ParameterMode.IN);
		SPCallingParams sparam12 = LCMS_Util.createSPObject("SHOWQUESTIONANSWERSUMMARY", String.valueOf(qz.isAllowReviewaftGrading()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam13 = LCMS_Util.createSPObject("EXAMTYPE", "PostAssessment" , String.class, ParameterMode.IN);
		
		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATECCT_BY_TYPE", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11, sparam12, sparam13);
		qr.execute();
		
		return true;
	}
	
	@Transactional
	@Override
	public boolean disableFinalExam (long courseId){
		
		try{
			SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);
			SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);
			SPCallingParams sparam3 = LCMS_Util.createSPObject("USER_ID", "-1" , Integer.class, ParameterMode.IN);
			SPCallingParams sparam4 = LCMS_Util.createSPObject("EXAMTYPE", "PostAssessment" , String.class, ParameterMode.IN);
			StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATECCT_BY_TYPE_DELETE", sparam1, sparam2, sparam3, sparam4);
			qr.execute();
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	@Override
	@Transactional
	public ExamConfiguration getExamforEdit(long courseId)throws Exception{
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("@COURSE_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);		
		SPCallingParams sparam2 = LCMS_Util.createSPObject("@EXAMTYPE", "PostAssessment" , String.class, ParameterMode.IN);
		Object[] contentObjectRows = callStoredProc("LCMS_WEB_GetCCT_BY_TYPE", sparam1, sparam2).toArray();
		Object[] contentObjectRow;
		ExamConfiguration dto = new ExamConfiguration();
		
		try{
			for (Object ExamConfig : contentObjectRows) {
				contentObjectRow = (Object[]) ExamConfig;
				
				if(contentObjectRow[0]!=null)
					dto.setNoAttemptsPermitted(  Integer.parseInt(  StringUtil.ifNullReturnZero(contentObjectRow[0] )  ));
				
				if(contentObjectRow[1]!=null)
					dto.setNoTargetQuestions(  Integer.parseInt(  StringUtil.ifNullReturnZero( contentObjectRow[1] )  )  );
				
				if(contentObjectRow[2]!=null)
					dto.setScorePassExam(  Integer.parseInt(  StringUtil.ifNullReturnZero(contentObjectRow[2]))  );
				
				dto.setActionOnFailtoPass(   StringUtil.ifNullReturnZero(contentObjectRow[3])  );
				
				if(contentObjectRow[4]!=null)
					dto.setTimeforExam( Integer.parseInt(  StringUtil.ifNullReturnZero(contentObjectRow[4].toString())) );
				
				if(contentObjectRow[5]!=null)
					dto.setRandomizeAnswers(	Boolean.parseBoolean(contentObjectRow[5].toString())	 );
				
				if(contentObjectRow[6]!=null)
					dto.setRandomizeQuestions(Boolean.parseBoolean(contentObjectRow[6].toString()) );
				

				dto.setGradeQuestions(  contentObjectRow[7].toString()  );
				dto.setAllowReviewaftGrading(   Boolean.parseBoolean(contentObjectRow[8].toString())  );
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return dto;
		
	}
	
	@Transactional
	public List<AssessmentItemBank> getQuestionBankList(long varCourseId) throws Exception{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(varCourseId) , Integer.class, ParameterMode.IN);		
		Object[] objAIBRowRowList = callStoredProc("LESSON_SET_EXAM_BANKS_GET", sparam1).toArray();
		Object[] objAIBRow;
		AssessmentItemBank dto ;
		List<AssessmentItemBank> lst = new ArrayList<AssessmentItemBank>();
		try{
			for (Object objAIB : objAIBRowRowList) {
				objAIBRow = (Object[]) objAIB;
				dto = new AssessmentItemBank();
				
				dto.setId(  Integer.parseInt(  StringUtil.ifNullReturnZero(objAIBRow[0].toString())  ));
				dto.setName(  StringUtil.ifNullReturnEmpty(objAIBRow[1].toString()) );
				lst.add (dto);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return lst;
	}
	
	@Transactional
	@Override
	public boolean saveExamNo_of_questions(int course_Id,int no_of_Questions,int bank_Id,int loggedUser_Id) throws Exception{
		try{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(course_Id) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("NO_OF_QUESTION", String.valueOf(no_of_Questions) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("BANK_ID", String.valueOf(bank_Id) , Integer.class, ParameterMode.IN);
		
		SPCallingParams sparam4 = LCMS_Util.createSPObject("USER_ID", String.valueOf(loggedUser_Id) , Integer.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("SAVE_EXAM_NO_OF_QUESTIONS_INFO", sparam1, sparam2, sparam3, sparam4);
		qr.execute();
		}catch(Exception exp){
			System.out.println(exp.toString());
		}
		return true;
	}
	
	
	@Override
	@Transactional
	public int getExamNo_of_questions(int bank_Id,int course_Id){
		int no_of_question = 0;
		try{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(course_Id) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("BANK_ID", String.valueOf(bank_Id) , Integer.class, ParameterMode.IN);

		Object[] QuizNo_of_questions =  callStoredProc("GET_EXAM_NO_OF_QUESTIONS_INFO",sparam1,  sparam2).toArray();
		
		no_of_question = Integer.parseInt(QuizNo_of_questions[0].toString());
		}
		catch(Exception exp)
		{
			System.out.println(exp.toString());
		}
		return no_of_question;
	}	
	
}
