package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

public class AssessmentItemAnswer  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@javax.persistence.Id
	int Id;
	String Value;
	String Label;
	String FeedBack;
	int AssessmentItem_Id;
	Boolean IsCorrectTF;
	int DisplayOrder ;
	String RightItemText;
	int RightItemOrder;
	String LeftItemText;
	int LeftItemOrder;
	int CorrectOrder;
	String AssessmentItemAnswer_GUID;
	int CreateUserId;
	String CorrectFeedBack;
	String InCorrectFeedBack;
	Boolean UseDefaultTFeedBackTF;
	
	public AssessmentItemAnswer ()
	{
		this.DisplayOrder = 0;
		this.LeftItemOrder = 0;
		this.LeftItemText= "";
		this.RightItemText = "";
		this.CorrectFeedBack= "";
		this.InCorrectFeedBack = "";
		this.FeedBack ="";
	}

	public int getAssessmentItem_Id() {
		return AssessmentItem_Id;
	}
	public void setAssessmentItem_Id(int assessmentItem_Id) {
		AssessmentItem_Id = assessmentItem_Id;
	}
	public Boolean getIsCorrectTF() {
		return IsCorrectTF;
	}
	public void setIsCorrectTF(Boolean isCorrectTF) {
		IsCorrectTF = isCorrectTF;
	}
	public int getDisplayOrder() {
		return DisplayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		DisplayOrder = displayOrder;
	}
	public String getRightItemText() {
		return RightItemText;
	}
	public void setRightItemText(String rightItemText) {
		RightItemText = rightItemText;
	}
	public int getRightItemOrder() {
		return RightItemOrder;
	}
	public void setRightItemOrder(int rightItemOrder) {
		RightItemOrder = rightItemOrder;
	}
	public String getLeftItemText() {
		return LeftItemText;
	}
	public void setLeftItemText(String leftItemText) {
		LeftItemText = leftItemText;
	}
	public int getCorrectOrder() {
		return CorrectOrder;
	}
	public void setCorrectOrder(int correctOrder) {
		CorrectOrder = correctOrder;
	}
	public String getAssessmentItemAnswer_GUID() {
		return AssessmentItemAnswer_GUID;
	}
	public void setAssessmentItemAnswer_GUID(String assessmentItemAnswer_GUID) {
		AssessmentItemAnswer_GUID = assessmentItemAnswer_GUID;
	}
	public int getCreateUserId() {
		return CreateUserId;
	}
	public void setCreateUserId(int createUserId) {
		CreateUserId = createUserId;
	}
	public String getCorrectFeedBack() {
		return CorrectFeedBack;
	}
	public void setCorrectFeedBack(String correctFeedBack) {
		CorrectFeedBack = correctFeedBack;
	}
	public String getInCorrectFeedBack() {
		return InCorrectFeedBack;
	}
	public void setInCorrectFeedBack(String inCorrectFeedBack) {
		InCorrectFeedBack = inCorrectFeedBack;
	}
	public Boolean getUseDefaultTFeedBackTF() {
		return UseDefaultTFeedBackTF;
	}
	public void setUseDefaultTFeedBackTF(Boolean useDefaultTFeedBackTF) {
		UseDefaultTFeedBackTF = useDefaultTFeedBackTF;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	public String getFeedBack() {
		return FeedBack;
	}
	public void setFeedBack(String feedBack) {
		FeedBack = feedBack;
	}
	public int getLeftItemOrder() {
		return LeftItemOrder;
	}
	public void setLeftItemOrder(int leftItemOrder) {
		LeftItemOrder = leftItemOrder;
	}
	
	
}
