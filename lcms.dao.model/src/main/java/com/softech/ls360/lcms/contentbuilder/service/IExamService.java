package com.softech.ls360.lcms.contentbuilder.service;

import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.AssessmentItemBank;
import com.softech.ls360.lcms.contentbuilder.model.ExamConfiguration;


public interface IExamService {
	public boolean saveExamConfiguration (ExamConfiguration qz);
	public boolean disableFinalExam (long courseId);
	public ExamConfiguration getExamforEdit (long courseId)throws Exception;
	public List<AssessmentItemBank> getQuestionBankList(long varCourseId) throws Exception;
	boolean saveExamNo_of_questions(int course_id,int no_of_Questions,int bank_id,int loggedUser_Id) throws Exception;
	int getExamNo_of_questions(int bank_id,int course_Id) throws Exception;
}
