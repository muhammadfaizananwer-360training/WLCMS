package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

import javax.persistence.Id;

public class AssessmentItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	int Id;
	String QuestionEM;
	String QuestionType;
	String ASSESSMENTITEM_GUID;
	int Question_Id;
	int Sequence;
	int Complexity;				//  @BLOOMSLEVEL VARCHAR(200) = NULL
	int Course_Id;

	String Status;
	Boolean IsAnswerCaseSensitiveTF;
	int Image_Asset_Id;
	String HighValueLabel;
	String LowValueLabel;
	int Rating;
	String AssessmentItem_GUID;
	String FeedBack;
	int ContentOwner_Id;
	int CreateUserId;
	int AssessmentItemTemplate_Id;
	String CorrectFeedback;
	String IncorrectFeedback;
	Boolean DisableRandomizeAnswerChoiceTF;
	String FeedBackType;
	String QuestionId;
	String Reference;
	String MinorCategory;
	String MajorCategory;
	String Task;	
	
	public AssessmentItem () {
		
		this.Status ="Active";
		this.IsAnswerCaseSensitiveTF = false;
		this.Image_Asset_Id = 0;
		this.HighValueLabel = "cccc";
		this.LowValueLabel  = "";
		this.Rating = 0;
		//AssessmentItem_GUID;
		this.FeedBack = "";		
		this.AssessmentItemTemplate_Id = 1;
		this.CorrectFeedback ="";
		this.IncorrectFeedback ="";
		this.DisableRandomizeAnswerChoiceTF = false;
		this.FeedBackType ="";
		this.QuestionId ="";
		this.Reference  ="";
		this.MinorCategory ="";
		this.MajorCategory ="";
		this.Task = "";	
	}
	
	
	public int getCourse_Id() {
		return Course_Id;
	}


	public void setCourse_Id(int course_Id) {
		Course_Id = course_Id;
	}
	public int getId() {
		return Id;
	}
	public int getComplexity() {
		return Complexity;
	}
	public void setComplexity(int complexity) {
		Complexity = complexity;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Boolean getIsAnswerCaseSensitiveTF() {
		return IsAnswerCaseSensitiveTF;
	}
	public void setIsAnswerCaseSensitiveTF(Boolean isAnswerCaseSensitiveTF) {
		IsAnswerCaseSensitiveTF = isAnswerCaseSensitiveTF;
	}
	public int getImage_Asset_Id() {
		return Image_Asset_Id;
	}
	public void setImage_Asset_Id(int image_Asset_Id) {
		Image_Asset_Id = image_Asset_Id;
	}
	public String getHighValueLabel() {
		return HighValueLabel;
	}
	public void setHighValueLabel(String highValueLabel) {
		HighValueLabel = highValueLabel;
	}
	public String getLowValueLabel() {
		return LowValueLabel;
	}
	public void setLowValueLabel(String lowValueLabel) {
		LowValueLabel = lowValueLabel;
	}
	public int getRating() {
		return Rating;
	}
	public void setRating(int rating) {
		Rating = rating;
	}
	public String getAssessmentItem_GUID() {
		return AssessmentItem_GUID;
	}
	public void setAssessmentItem_GUID(String assessmentItem_GUID) {
		AssessmentItem_GUID = assessmentItem_GUID;
	}
	public String getFeedBack() {
		return FeedBack;
	}
	public void setFeedBack(String feedBack) {
		FeedBack = feedBack;
	}
	public int getContentOwner_Id() {
		return ContentOwner_Id;
	}
	public void setContentOwner_Id(int contentOwner_Id) {
		ContentOwner_Id = contentOwner_Id;
	}
	public int getCreateUserId() {
		return CreateUserId;
	}
	public void setCreateUserId(int createUserId) {
		CreateUserId = createUserId;
	}
	public int getAssessmentItemTemplate_Id() {
		return AssessmentItemTemplate_Id;
	}
	public void setAssessmentItemTemplate_Id(int assessmentItemTemplate_Id) {
		AssessmentItemTemplate_Id = assessmentItemTemplate_Id;
	}
	public String getCorrectFeedback() {
		return CorrectFeedback;
	}
	public void setCorrectFeedback(String correctFeedback) {
		CorrectFeedback = correctFeedback;
	}
	public String getIncorrectFeedback() {
		return IncorrectFeedback;
	}
	public void setIncorrectFeedback(String incorrectFeedback) {
		IncorrectFeedback = incorrectFeedback;
	}
	public Boolean getDisableRandomizeAnswerChoiceTF() {
		return DisableRandomizeAnswerChoiceTF;
	}
	public void setDisableRandomizeAnswerChoiceTF(
			Boolean disableRandomizeAnswerChoiceTF) {
		DisableRandomizeAnswerChoiceTF = disableRandomizeAnswerChoiceTF;
	}
	public String getFeedBackType() {
		return FeedBackType;
	}
	public void setFeedBackType(String feedBackType) {
		FeedBackType = feedBackType;
	}
	public String getQuestionId() {
		return QuestionId;
	}
	public void setQuestionId(String questionId) {
		QuestionId = questionId;
	}
	public String getReference() {
		return Reference;
	}
	public void setReference(String reference) {
		Reference = reference;
	}
	public String getMinorCategory() {
		return MinorCategory;
	}
	public void setMinorCategory(String minorCategory) {
		MinorCategory = minorCategory;
	}
	public String getMajorCategory() {
		return MajorCategory;
	}
	public void setMajorCategory(String majorCategory) {
		MajorCategory = majorCategory;
	}
	public String getTask() {
		return Task;
	}
	public void setTask(String task) {
		Task = task;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getQuestionEM() {
		return QuestionEM;
	}
	public void setQuestionEM(String qUESTIONSTEM) {
		QuestionEM = qUESTIONSTEM;
	}
	public String getQuestionType() {
		return QuestionType;
	}
	public void setQuestionType(String qUESTIONTYPE) {
		QuestionType = qUESTIONTYPE;
	}
	public String getASSESSMENTITEM_GUID() {
		return ASSESSMENTITEM_GUID;
	}
	public void setASSESSMENTITEM_GUID(String aSSESSMENTITEM_GUID) {
		ASSESSMENTITEM_GUID = aSSESSMENTITEM_GUID;
	}
	public int getQuestion_Id() {
		return Question_Id;
	}
	public void setQuestion_Id(int question_Id) {
		Question_Id = question_Id;
	}
	public int getSequence() {
		return Sequence;
	}
	public void setSequence(int sequence) {
		Sequence = sequence;
	}


}
