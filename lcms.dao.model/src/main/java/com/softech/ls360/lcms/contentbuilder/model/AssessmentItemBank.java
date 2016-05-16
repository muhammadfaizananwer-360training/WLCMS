package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

import javax.persistence.Id;



public class AssessmentItemBank  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	int Id;
	String Name;
	int contentOwnerId;
	int CreateUserId;
	int AssessmentItem_Id;
	int SourceAssessmentItem_Id;
	int SourceAssessmentItemBank_Id;
	


	
	public int getAssessmentItem_Id() {
		return AssessmentItem_Id;
	}
	public void setAssessmentItem_Id(int assessmentItem_Id) {
		AssessmentItem_Id = assessmentItem_Id;
	}
	public int getSourceAssessmentItem_Id() {
		return SourceAssessmentItem_Id;
	}
	public void setSourceAssessmentItem_Id(int sourceAssessmentItem_Id) {
		SourceAssessmentItem_Id = sourceAssessmentItem_Id;
	}
	public int getSourceAssessmentItemBank_Id() {
		return SourceAssessmentItemBank_Id;
	}
	public void setSourceAssessmentItemBank_Id(int sourceAssessmentItemBand_Id) {
		SourceAssessmentItemBank_Id = sourceAssessmentItemBand_Id;
	}
	public int getCreateUserId() {
		return CreateUserId;
	}
	public void setCreateUserId(int createUserId) {
		CreateUserId = createUserId;
	}
		
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getContentOwnerId() {
		return contentOwnerId;
	}
	public void setContentOwnerId(int contentOwnerId) {
		this.contentOwnerId = contentOwnerId;
	}
	
}
