package com.softech.ls360.lcms.contentbuilder.dao;


import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.AssessmentItem;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemAnswer;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.Quiz;
import com.softech.ls360.lcms.contentbuilder.model.QuizConfiguration;

public interface QuizDAO extends GenericDAO<Quiz> {

	public QuizConfiguration insertQuizConfiguration (QuizConfiguration qz);
	public QuizConfiguration updateQuizConfiguration (QuizConfiguration qz);
	public List<AssessmentItemBank> getQuizBankListign (ContentObject objContent);
	public List<AssessmentItem> getAssessmentItemList(AssessmentItemBank bank) throws Exception;
	public AssessmentItem getAssessmentItem (AssessmentItem assessmentItem) throws Exception;
	public List<AssessmentItemAnswer> getAssessmentItemAnswer (AssessmentItem assessmentItem) throws Exception;
	
	public AssessmentItem insertAssessmentItem (AssessmentItem assessmentItem)throws Exception;
	public AssessmentItemAnswer insertAssessmentItemAnswer (AssessmentItemAnswer assessmentItemAnswer )throws Exception;
	public AssessmentItemBank 	insertAssessmentItemBank (AssessmentItemBank assessmentItemBank) throws Exception;
	
	public AssessmentItem updateAssessmentItem (AssessmentItem assessmentItem) throws Exception;
	AssessmentItemAnswer deleteAssessmentItemAnswer(AssessmentItemAnswer assessmentItemAnswer) throws Exception;
	AssessmentItem deleteAssessmentItem(AssessmentItem assessmentItem) throws Exception;
	AssessmentItemBank deleteAssessmentItemBank(AssessmentItemBank assessmentItem) throws Exception;
	boolean disableQuiz(long courseId, int contentId) throws Exception;
	boolean saveQuizNo_of_questions(int contentObject_Id,int no_of_Questions,int bank_id,int loggedUser_Id) throws Exception;;
	List<Integer> getQuizNo_of_questions(int contentObject_Id,int bank_id);
	QuizConfiguration getQuizConfiguration(QuizConfiguration qz);
	AssessmentItemAnswer getDetailAssessmentItemAnswer(AssessmentItemAnswer assessmentItemAnswer) throws Exception;
	AssessmentItemAnswer updateAssessmentItemAnswer(AssessmentItemAnswer assessmentItemAnswer) throws Exception;
	
}
