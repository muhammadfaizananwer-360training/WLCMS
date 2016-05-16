package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.QuizDAO;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItem;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemAnswer;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.QuizConfiguration;
import com.softech.ls360.lcms.contentbuilder.service.IQuizService;

public class QuizServiceImpl implements IQuizService {
	
	@Autowired
	private QuizDAO quizDAO;

	public QuizDAO getQuizDAO() {
		return quizDAO;
	}

	public void setQuizDAO(QuizDAO quizDAO) {
		this.quizDAO = quizDAO;
	}

	@Override
	public QuizConfiguration insertQuizConfiguration(QuizConfiguration qz) {		
		return quizDAO.insertQuizConfiguration(qz);
	}
	
	@Override
	public QuizConfiguration updateQuizConfiguration(QuizConfiguration qz) {		
		return quizDAO.updateQuizConfiguration(qz);
	}
	
	@Override
	public QuizConfiguration getQuizConfiguration (QuizConfiguration qz){
		return quizDAO.getQuizConfiguration(qz);
	}

	@Override
	public List<AssessmentItemBank> getQuizBankListign(ContentObject objContent) {

		return quizDAO.getQuizBankListign(objContent);
	}

	@Override
	public List<AssessmentItem> getAssessmentItemList(AssessmentItemBank bank) throws Exception {
		return quizDAO.getAssessmentItemList(bank);
	}

	@Override
	public AssessmentItem getAssessmentItem(AssessmentItem assessmentItem) throws Exception {
 		return quizDAO.getAssessmentItem(assessmentItem);
	}

	@Override
	public List<AssessmentItemAnswer> getAssessmentItemAnswer(AssessmentItem assessmentItem) throws Exception {
		return quizDAO.getAssessmentItemAnswer(assessmentItem);
	}

	@Override
	public AssessmentItem insertAssessmentItem(AssessmentItem assessmentItem, List<AssessmentItemAnswer >lstAssessmentItemAnswer)
			throws Exception {
		
		 assessmentItem =  quizDAO.insertAssessmentItem(assessmentItem);
		
		 for (AssessmentItemAnswer objAssessmentAnswerItem : lstAssessmentItemAnswer) {
			 objAssessmentAnswerItem.setAssessmentItem_Id(assessmentItem.getId());
			 quizDAO.insertAssessmentItemAnswer(objAssessmentAnswerItem);
		 }
		return assessmentItem;
	}
	
	@Override
	public List<AssessmentItemAnswer > insertAssessmentItemAnswer(List<AssessmentItemAnswer >lstAssessmentItemAnswer)throws Exception {
		
		 for (AssessmentItemAnswer objAssessmentAnswerItem : lstAssessmentItemAnswer) {
				 quizDAO.insertAssessmentItemAnswer(objAssessmentAnswerItem);
		 }		 
		return lstAssessmentItemAnswer;
	}
	
	@Override
	public Boolean associateAssessmentItemBank (int assessmentItemId,  int assessmentId, int createUserId) throws Exception {
		
		
		AssessmentItemBank assessmentItemBank = new AssessmentItemBank ();
		 
		 assessmentItemBank.setAssessmentItem_Id(assessmentItemId);
		 assessmentItemBank.setCreateUserId(createUserId);
		 assessmentItemBank.setId(assessmentId);
		 quizDAO.insertAssessmentItemBank(assessmentItemBank);
		 
		return true;
	}
	
	@Override
	public AssessmentItem updateAssessmentItem(AssessmentItem assessmentItem) throws Exception {
			
			quizDAO.updateAssessmentItem(assessmentItem);
			return assessmentItem;
	}
	
	@Override
	public boolean saveQuizNo_of_questions(int contentObject_Id,int no_of_Questions,int bank_id,int loggedUser_Id) throws Exception{
		return quizDAO.saveQuizNo_of_questions(contentObject_Id, no_of_Questions,bank_id, loggedUser_Id);
	}
	
	@Override
	public int getQuizNo_of_questions(int contentObject_Id,int bank_id) throws Exception{
		int no_of_Questions = 0 ;
		no_of_Questions = quizDAO.getQuizNo_of_questions(contentObject_Id, bank_id).get(0);
		return no_of_Questions;
	}	

	@Override
	public AssessmentItemAnswer deleteAssessmentItemAnswer(AssessmentItemAnswer assessmentItemAnswer) throws Exception {
		
		quizDAO.deleteAssessmentItemAnswer(assessmentItemAnswer);
		return assessmentItemAnswer;
	}
	
	@Override
	public AssessmentItem deleteAssessmentItem(AssessmentItem assessmentItem) throws Exception {
		
		quizDAO.deleteAssessmentItem(assessmentItem);
		return assessmentItem;
	}
	
	@Override
	public AssessmentItemBank deleteAssessmentItem(AssessmentItemBank assessmentBankItem) throws Exception {
		
		quizDAO.deleteAssessmentItemBank(assessmentBankItem);
		return assessmentBankItem;
	}

	@Override
	public boolean disableQuiz (long courseId, int contentId) throws Exception {

		quizDAO.disableQuiz(courseId,  contentId);
		return true;
	}
	
	@Override
	public AssessmentItemAnswer updateAssessmentItemAnswer (AssessmentItemAnswer assessmentItemAnswer) throws Exception {
		return quizDAO.updateAssessmentItemAnswer(assessmentItemAnswer);
	}
	
	@Override 
	public AssessmentItemAnswer getDetailAssessmentItemAnswer (AssessmentItemAnswer assessmentItemAnswer) throws Exception 	{
		return quizDAO. getDetailAssessmentItemAnswer (assessmentItemAnswer);
	}
	
	
	
}

