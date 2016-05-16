package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class QuizConfiguration implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	int Id;
	
	int noTargetQuestions;
	boolean randomizeQuestions;
	boolean randomizeAnswers;
	int timeforQuiz;
	String gradeQuestions;
	boolean allowReviewaftGrading;
	int scorePassQuiz;
	int noAttemptsPermitted;
	String actionOnFailtoPass;
	int courseId;
	int contentId;
	int UserId;

	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
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
	public int getTimeforQuiz() {
		return timeforQuiz;
	}
	public void setTimeforQuiz(int timeforQuiz) {
		this.timeforQuiz = timeforQuiz;
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
	public int getScorePassQuiz() {
		return scorePassQuiz;
	}
	public void setScorePassQuiz(int scorePassQuiz) {
		this.scorePassQuiz = scorePassQuiz;
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
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
}
