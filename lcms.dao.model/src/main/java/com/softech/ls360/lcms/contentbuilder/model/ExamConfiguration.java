package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

public class ExamConfiguration implements Serializable{

	private static final long serialVersionUID = 1L;
	@javax.persistence.Id
	int Id;
	
	int courseId;
	int UserId;
	int noTargetQuestions;
	boolean randomizeQuestions;
	boolean randomizeAnswers;
	int timeforExam;
	String gradeQuestions;
	boolean allowReviewaftGrading;
	int scorePassExam;
	int noAttemptsPermitted;
	String actionOnFailtoPass;
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public int getNoTargetQuestions() {
		return noTargetQuestions;
	}
	public void setNoTargetQuestions(int noTargetQuestions) {
		this.noTargetQuestions = noTargetQuestions;
	}
	public boolean isRandomizeQuestions() {
		return randomizeQuestions;
	}
	public void setRandomizeQuestions(boolean randomizeQuestions) {
		this.randomizeQuestions = randomizeQuestions;
	}
	public boolean isRandomizeAnswers() {
		return randomizeAnswers;
	}
	public void setRandomizeAnswers(boolean randomizeAnswers) {
		this.randomizeAnswers = randomizeAnswers;
	}
	public int getTimeforExam() {
		return timeforExam;
	}
	public void setTimeforExam(int timeforExam) {
		this.timeforExam = timeforExam;
	}
	public String getGradeQuestions() {
		return gradeQuestions;
	}
	public void setGradeQuestions(String gradeQuestions) {
		this.gradeQuestions = gradeQuestions;
	}
	public boolean isAllowReviewaftGrading() {
		return allowReviewaftGrading;
	}
	public void setAllowReviewaftGrading(boolean allowReviewaftGrading) {
		this.allowReviewaftGrading = allowReviewaftGrading;
	}
	public int getScorePassExam() {
		return scorePassExam;
	}
	public void setScorePassExam(int scorePassExam) {
		this.scorePassExam = scorePassExam;
	}
	public int getNoAttemptsPermitted() {
		return noAttemptsPermitted;
	}
	public void setNoAttemptsPermitted(int noAttemptsPermitted) {
		this.noAttemptsPermitted = noAttemptsPermitted;
	}
	public String getActionOnFailtoPass() {
		return actionOnFailtoPass;
	}
	public void setActionOnFailtoPass(String actionOnFailtoPass) {
		this.actionOnFailtoPass = actionOnFailtoPass;
	}
	
}
