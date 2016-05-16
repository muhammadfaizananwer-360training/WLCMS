package com.softech.ls360.lcms.contentbuilder.dao;

import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ExamConfiguration;



public interface ExamDAO {
	public boolean saveExamConfiguration (ExamConfiguration qz);
	public boolean disableFinalExam (long courseId);
	public ExamConfiguration getExamforEdit (long courseId) throws Exception;
	public List<AssessmentItemBank> getQuestionBankList(long varCourseId) throws Exception;
	boolean saveExamNo_of_questions(int course_Id,int no_of_Questions,int bank_Id,int loggedUser_Id) throws Exception;
	public int getExamNo_of_questions(int bank_Id,int course_Id) throws Exception;
}
