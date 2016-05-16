package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.ExamDAO;
import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ExamConfiguration;
import com.softech.ls360.lcms.contentbuilder.service.IExamService;

public class ExamServiceImpl implements IExamService{

	@Autowired
	private ExamDAO examDAO;

	
	public boolean saveExamConfiguration (ExamConfiguration qz){
		 return examDAO.saveExamConfiguration (qz);
	}
	
	public boolean disableFinalExam (long courseId){
		return examDAO.disableFinalExam (courseId);
	}
	
	public ExamConfiguration getExamforEdit (long courseId)throws Exception{
		return examDAO.getExamforEdit (courseId);
	}
	
	public List<AssessmentItemBank> getQuestionBankList(long varCourseId) throws Exception{
		return examDAO.getQuestionBankList( varCourseId) ;
	}
	public ExamDAO getExamDAO() {
		return examDAO;
	}

	public void setExamDAO(ExamDAO examDAO) {
		this.examDAO = examDAO;
	}
	
	public boolean saveExamNo_of_questions(int course_Id,int no_of_Questions,int bank_id,int loggedUser_Id) throws Exception{
		return examDAO.saveExamNo_of_questions(course_Id, no_of_Questions,bank_id, loggedUser_Id);
	}
	
	public int getExamNo_of_questions(int bank_id,int course_Id) throws Exception{
		return examDAO.getExamNo_of_questions(bank_id, course_Id);
	}		
}
