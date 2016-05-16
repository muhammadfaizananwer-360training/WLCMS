package com.softech.ls360.lcms.contentbuilder.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity
public class ContentObject {
	
	@Transient 
	Integer CourseID;
	@Transient 
	String CO_GUID;
	@Transient 
	Integer NEW_ID;
	@Transient
	Integer contentOwner_Id;
	@Transient
	Integer CreateUserId;
	
	@Transient
	List<LearningObject> lstContentObject;
	
	public List<LearningObject> getLstContentObject() {
		return lstContentObject;
	}

	public void setLstContentObject(List<LearningObject> lstContentObject) {
		this.lstContentObject = lstContentObject;
	}
	@Id
	Integer ID;
	String ContentObjectType;
	String Name;
	String Description;
	@Transient
	String LearningObjective;
	
	@Transient
	String LearningObjectiveId;
	
	public String getLearningObjectiveId() {
		return LearningObjectiveId;
	}

	public void setLearningObjectiveId(String learningObjectiveId) {
		LearningObjectiveId = learningObjectiveId;
	}

	public String getLearningObjective() {
		return LearningObjective;
	}

	public void setLearningObjective(String learningObjective) {
		LearningObjective = learningObjective;
	}
	@Transient
	Integer PARENT_CO_ID;
	@Transient
	String CONTENTOBJECTTYPE;
	@Transient
	Integer ALLOWQUIZTF;
	@Transient
	Integer SHOWINOUTLINETF;
	@Transient
	Integer ROOTNODETF;
	@Transient
	Integer MAXQUIZQUESTIONSTOASK;
	@Transient
	Integer OVERRIDEMAXQUIZQUESTIONSTOASK;
	@Transient
	Long LastUpdateUser;
	@Transient
	Integer DEMOABLE;
	
	private int allowQuiz;
	
	public ContentObject()
	{
		
		
		
	}
	
	public ContentObject(Integer iD, String contentObjectType, String name,
			String description, String learningObjective) {
		
		super();
		ID = iD;
		ContentObjectType = contentObjectType;
		Name = name;
		Description = description;
		LearningObjective = learningObjective; 
	}
	
	
	public ContentObject(Integer iD, String contentObjectType, String name,
			String description, int allowQuiz) {
		
		super();
		ID = iD;
		ContentObjectType = contentObjectType;
		Name = name;
		Description = description;
		this.allowQuiz = allowQuiz;
	}
	
	public ContentObject(Integer iD, String contentObjectType, String name,
			String description, Integer parentContentObjectId, Integer isAllowQuiz, 
			Integer isShowInOutLine, Integer isRootNode, 
			Integer maxQuizQuestionStoask,Integer overRideMaxQuizQuestionStoask, Long lastUpdateUser, Integer demoable) {
		
		super();
		ID = iD;
		ContentObjectType = contentObjectType;
		Name = name;
		Description = description;
		
		PARENT_CO_ID = parentContentObjectId;
		
		ALLOWQUIZTF=isAllowQuiz;
		SHOWINOUTLINETF=isShowInOutLine;
		ROOTNODETF=isRootNode;
		MAXQUIZQUESTIONSTOASK=maxQuizQuestionStoask;
		OVERRIDEMAXQUIZQUESTIONSTOASK=overRideMaxQuizQuestionStoask;
		LastUpdateUser = lastUpdateUser;
		DEMOABLE =demoable;
	}
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getContentObjectType() {
		return ContentObjectType;
	}
	public void setContentObjectType(String contentObjectType) {
		ContentObjectType = contentObjectType;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	public Integer getCourseID() {
		return CourseID;
	}

	public void setCourseID(Integer courseID) {
		CourseID = courseID;
	}

	public String getCO_GUID() {
		return CO_GUID;
	}

	public void setCO_GUID(String cO_GUID) {
		CO_GUID = cO_GUID;
	}

	public Integer getNEW_ID() {
		return NEW_ID;
	}

	public void setNEW_ID(Integer nEW_ID) {
		NEW_ID = nEW_ID;
	}

	public Integer getContentOwner_Id() {
		return contentOwner_Id;
	}

	public void setContentOwner_Id(Integer contentOwner_Id) {
		this.contentOwner_Id = contentOwner_Id;
	}

	public Integer getCreateUserId() {
		return CreateUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		CreateUserId = createUserId;
	}

	public Integer getPARENT_CO_ID() {
		return PARENT_CO_ID;
	}

	public void setPARENT_CO_ID(Integer pARENT_CO_ID) {
		PARENT_CO_ID = pARENT_CO_ID;
	}

	public String getCONTENTOBJECTTYPE() {
		return CONTENTOBJECTTYPE;
	}

	public void setCONTENTOBJECTTYPE(String cONTENTOBJECTTYPE) {
		CONTENTOBJECTTYPE = cONTENTOBJECTTYPE;
	}

	public Integer getALLOWQUIZTF() {
		return ALLOWQUIZTF;
	}

	public void setALLOWQUIZTF(Integer aLLOWQUIZTF) {
		ALLOWQUIZTF = aLLOWQUIZTF;
	}

	public Integer getSHOWINOUTLINETF() {
		return SHOWINOUTLINETF;
	}

	public void setSHOWINOUTLINETF(Integer sHOWINOUTLINETF) {
		SHOWINOUTLINETF = sHOWINOUTLINETF;
	}

	public Integer getROOTNODETF() {
		return ROOTNODETF;
	}

	public void setROOTNODETF(Integer rOOTNODETF) {
		ROOTNODETF = rOOTNODETF;
	}

	public Integer getMAXQUIZQUESTIONSTOASK() {
		return MAXQUIZQUESTIONSTOASK;
	}

	public void setMAXQUIZQUESTIONSTOASK(Integer mAXQUIZQUESTIONSTOASK) {
		MAXQUIZQUESTIONSTOASK = mAXQUIZQUESTIONSTOASK;
	}

	public Integer getOVERRIDEMAXQUIZQUESTIONSTOASK() {
		return OVERRIDEMAXQUIZQUESTIONSTOASK;
	}

	public void setOVERRIDEMAXQUIZQUESTIONSTOASK(
			Integer oVERRIDEMAXQUIZQUESTIONSTOASK) {
		OVERRIDEMAXQUIZQUESTIONSTOASK = oVERRIDEMAXQUIZQUESTIONSTOASK;
	}

	public Long getLastUpdateUser() {
		return LastUpdateUser;
	}

	public void setLastUpdateUser(Long lastUpdateUser) {
		LastUpdateUser = lastUpdateUser;
	}

	public Integer getDEMOABLE() {
		return DEMOABLE;
	}

	public void setDEMOABLE(Integer demoable) {
		DEMOABLE = demoable;
	}

	public int getAllowQuiz() {
		return allowQuiz;
	}

	public void setAllowQuiz(int allowQuiz) {
		this.allowQuiz = allowQuiz;
	}

	
	
}
