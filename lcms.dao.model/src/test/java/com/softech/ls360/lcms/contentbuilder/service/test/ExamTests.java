package com.softech.ls360.lcms.contentbuilder.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import com.softech.ls360.lcms.contentbuilder.model.ExamConfiguration;
import com.softech.ls360.lcms.contentbuilder.service.IExamService;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, SlideServiceTest.class})
@ContextConfiguration(locations = { "classpath:app-context.xml" })
public class ExamTests extends AbstractTestExecutionListener{

	
	final String COURSE_ID = "111633"; 
	final int USER_ID = 1; 
	final int noTargetQuestionsForExam = 60; 
	final String chkrandomizeQuestionsForExam = "on"; 
	final String chkrandomizeAnswersForExam = "1"; 
	final String timePermittedForExam = "60"; 
	final String oboGradeQuestionsForExam = "1"; 
	final String allowReviewaftGradingForExam = "1"; 
	final String scoreRequiredPassForExam = "70"; 
	final String attemptsPermittedExam = "3"; 
	final String actionOnFailtoPassForExam = "1"; 
	
	
	@Autowired
	IExamService examService;
	
	 @Test
	 public void addExam(){
	    	try {
	    		System.out.println("Start....Class ExamTests.....addExam().........");
	    		
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
	    		
	    		if(StringUtil.ifNullReturnEmpty(oboGradeQuestionsForExam).equals("1"))
	    			examConfiguration.setActionOnFailtoPass("Retake Content");
	    		else if(StringUtil.ifNullReturnEmpty(oboGradeQuestionsForExam).equals("2"))	
	    			examConfiguration.setActionOnFailtoPass("Lock Course");
	    		
	    		
	    		examConfiguration.setCourseId(Integer.parseInt(COURSE_ID));
	    		examConfiguration.setUserId(USER_ID);
	    		
	    		
	    		//Assert.assertTrue(examService.saveExamConfiguration(examConfiguration));
	    		
	    		System.out.println("End....Class ExamTests.....addExam().........");
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
	     
	    }
	 
	 
	 
	 
	 @Test
	 public void updateExam(){
	    	try {
	    		System.out.println("Start....Class ExamTests.....updateExam().........");
	    		

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
	    		
	    		if(StringUtil.ifNullReturnEmpty(oboGradeQuestionsForExam).equals("1"))
	    			examConfiguration.setActionOnFailtoPass("Retake Content");
	    		else if(StringUtil.ifNullReturnEmpty(oboGradeQuestionsForExam).equals("2"))	
	    			examConfiguration.setActionOnFailtoPass("Lock Course");
	    		
	    		
	    		//examConfiguration.isAllowReviewaftGrading
	    		
	    		
	    		examConfiguration.setCourseId(Integer.parseInt(COURSE_ID));
	    		examConfiguration.setUserId(USER_ID);
	    		
	    		
	    		//Assert.assertTrue(examService.saveExamConfiguration(examConfiguration));
	    		
	    		System.out.println("End....Class ExamTests.....updateExam().........");
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
	     
	    }
}
