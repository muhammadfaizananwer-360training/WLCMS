package com.softech.ls360.lcms.contentbuilder.service;

import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.AssessmentItem;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemAnswer;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.QuizConfiguration;

public interface IQuizService {
	public QuizConfiguration insertQuizConfiguration (QuizConfiguration qz);
	public QuizConfiguration updateQuizConfiguration (QuizConfiguration qz);
	public List<AssessmentItemBank> getQuizBankListign (ContentObject objContent);
	public List<AssessmentItem> getAssessmentItemList(AssessmentItemBank bank) throws Exception;
	public AssessmentItem getAssessmentItem (AssessmentItem assessmentItem) throws Exception;
	public List<AssessmentItemAnswer> getAssessmentItemAnswer (AssessmentItem assessmentItem) throws Exception;
	AssessmentItem insertAssessmentItem(AssessmentItem assessmentItem , List<AssessmentItemAnswer >lstAssessmentItemAnswer)	throws Exception;
	Boolean associateAssessmentItemBank(int assessmentItemId, int assessmentId, int createUserId) throws Exception;
	AssessmentItem updateAssessmentItem(AssessmentItem assessmentItem)			throws Exception;
	public AssessmentItemAnswer deleteAssessmentItemAnswer (AssessmentItemAnswer assessmentItemAnswer) throws Exception;
	List<AssessmentItemAnswer> insertAssessmentItemAnswer(List<AssessmentItemAnswer> lstAssessmentItemAnswer)throws Exception;
	AssessmentItem deleteAssessmentItem(AssessmentItem assessmentItem)	throws Exception;
	AssessmentItemBank deleteAssessmentItem(AssessmentItemBank assessmentBankItem) throws Exception;
	boolean disableQuiz(long courseId, int contentId) throws Exception;
	boolean saveQuizNo_of_questions(int contentObject_Id,int no_of_Questions,int bank_id,int loggedUser_Id) throws Exception;
	int getQuizNo_of_questions(int contentObject_Id,int bank_id) throws Exception;
	QuizConfiguration getQuizConfiguration(QuizConfiguration qz);
	AssessmentItemAnswer updateAssessmentItemAnswer(AssessmentItemAnswer assessmentItemAnswer) throws Exception;
	AssessmentItemAnswer getDetailAssessmentItemAnswer(AssessmentItemAnswer assessmentItemAnswer) throws Exception;

}
