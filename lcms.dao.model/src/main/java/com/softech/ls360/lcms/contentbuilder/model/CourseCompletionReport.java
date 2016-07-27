package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class CourseCompletionReport implements Serializable{

	@Id
	int CourseConfigurationId;
	boolean quizEnabled;
	boolean preAssessmentEnabled;
	boolean postAssesssmentEnabled;
	boolean atleastOneScenePerCO;
	boolean contentObjectPresent;
	boolean courseExpired;
	boolean publishStatus;
	int courseId;
	String courseName;
	String bussinessKey;
	boolean contentOwnerStoreFront;
	private int contentOwnerId;
	private int courseType;

	boolean quizMastery;
	boolean examMastery;
	Integer quizQuestionCount;
	Integer examQuestionCount;
	private boolean agreeWithSpecifiedTextEnabled;
	private String agreeWithSpecifiedText;

	private boolean completionPostAssessmentAttempted;
	private boolean isSetCourseExpiration;
	// webinar fields
	boolean IsScheduleSet;
	boolean IsPresenterSet;
	boolean IsMeetingSet;
	boolean IsClassRoomSet;
	boolean IsAuthorInfoProvided;
	Double lowestPrice;
	Double offerPrice;
	boolean publishedOnSF;
	String lastPublishDate;
	Integer examQuestionToAskCount;


	public CourseCompletionReport(int courseConfigurationId,
								  boolean quizEnabled, boolean preAssessmentEnabled,
								  boolean postAssesssmentEnabled, boolean atleastOneScenePerCO,
								  boolean contentObjectPresent,boolean contentOwnerStoreFront) {
		super();
		CourseConfigurationId = courseConfigurationId;
		this.quizEnabled = quizEnabled;
		this.preAssessmentEnabled = preAssessmentEnabled;
		this.postAssesssmentEnabled = postAssesssmentEnabled;
		this.atleastOneScenePerCO = atleastOneScenePerCO;
		this.contentObjectPresent = contentObjectPresent;
		this.contentOwnerStoreFront = contentOwnerStoreFront;
	}

	public CourseCompletionReport() {
		// TODO Auto-generated constructor stub
	}

	public int getCourseConfigurationId() {
		return CourseConfigurationId;
	}
	public void setCourseConfigurationId(int courseConfigurationId) {
		CourseConfigurationId = courseConfigurationId;
	}
	public boolean isQuizEnabled() {
		return quizEnabled;
	}
	public void setQuizEnabled(boolean quizEnabled) {
		this.quizEnabled = quizEnabled;
	}
	public boolean isPreAssessmentEnabled() {
		return preAssessmentEnabled;
	}
	public void setPreAssessmentEnabled(boolean preAssessmentEnabled) {
		this.preAssessmentEnabled = preAssessmentEnabled;
	}
	public boolean isPostAssesssmentEnabled() {
		return postAssesssmentEnabled;
	}
	public void setPostAssesssmentEnabled(boolean postAssesssmentEnabled) {
		this.postAssesssmentEnabled = postAssesssmentEnabled;
	}
	public boolean isAtleastOneScenePerCO() {
		return atleastOneScenePerCO;
	}
	public void setAtleastOneScenePerCO(boolean atleastOneScenePerCO) {
		this.atleastOneScenePerCO = atleastOneScenePerCO;
	}
	public boolean isContentObjectPresent() {
		return contentObjectPresent;
	}
	public void setContentObjectPresent(boolean contentObjectPresent) {
		this.contentObjectPresent = contentObjectPresent;
	}



	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public boolean isCourseExpired() {
		return courseExpired;
	}

	public void setCourseExpired(boolean courseExpired) {
		this.courseExpired = courseExpired;
	}

	public boolean isPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(boolean publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getBussinessKey() {
		return bussinessKey;
	}

	public void setBussinessKey(String bussinessKey) {
		this.bussinessKey = bussinessKey;
	}

	public boolean isContentOwnerStoreFront() {
		return contentOwnerStoreFront;
	}

	public void setContentOwnerStoreFront(boolean contentOwnerStoreFront) {
		this.contentOwnerStoreFront = contentOwnerStoreFront;
	}

	public int getContentOwnerId() {
		return contentOwnerId;
	}

	public void setContentOwnerId(int contentOwnerId) {
		this.contentOwnerId = contentOwnerId;
	}

	public boolean isQuizMastery() {
		return quizMastery;
	}

	public void setQuizMastery(boolean quizMastery) {
		this.quizMastery = quizMastery;
	}

	public boolean isExamMastery() {
		return examMastery;
	}

	public void setExamMastery(boolean examMastery) {
		this.examMastery = examMastery;
	}

	public Integer getQuizQuestionCount() {
		return quizQuestionCount;
	}

	public void setQuizQuestionCount(Integer quizQuestionCount) {
		this.quizQuestionCount = quizQuestionCount;
	}

	public Integer getExamQuestionCount() {
		return examQuestionCount;
	}

	public void setExamQuestionCount(Integer examQuestionCount) {
		this.examQuestionCount = examQuestionCount;
	}

	public boolean isAgreeWithSpecifiedTextEnabled() {
		return agreeWithSpecifiedTextEnabled;
	}

	public void setAgreeWithSpecifiedTextEnabled(
			boolean agreeWithSpecifiedTextEnabled) {
		this.agreeWithSpecifiedTextEnabled = agreeWithSpecifiedTextEnabled;
	}

	public String getAgreeWithSpecifiedText() {
		return agreeWithSpecifiedText;
	}

	public void setAgreeWithSpecifiedText(String agreeWithSpecifiedText) {
		this.agreeWithSpecifiedText = agreeWithSpecifiedText;
	}

	public boolean isIsScheduleSet() {
		return IsScheduleSet;
	}

	public void setIsScheduleSet(boolean isScheduleSet) {
		IsScheduleSet = isScheduleSet;
	}



	public boolean isIsMeetingSet() {
		return IsMeetingSet;
	}

	public void setIsMeetingSet(boolean isMeetingSet) {
		IsMeetingSet = isMeetingSet;
	}

	public boolean isIsPresenterSet() {
		return IsPresenterSet;
	}

	public void setIsPresenterSet(boolean isPresenterSet) {
		IsPresenterSet = isPresenterSet;
	}

	public boolean isIsClassRoomSet() {
		return IsClassRoomSet;
	}

	public void setIsClassRoomSet(boolean isClassRoomSet) {
		IsClassRoomSet = isClassRoomSet;
	}

	public boolean isIsAuthorInfoProvided() {
		return IsAuthorInfoProvided;
	}

	public void setAuthorInfoProvided(boolean isAuthorInfoProvided) {
		this.IsAuthorInfoProvided = isAuthorInfoProvided;
	}

	public boolean isCompletionPostAssessmentAttempted() {
		return completionPostAssessmentAttempted;
	}

	public void setCompletionPostAssessmentAttempted(
			boolean completionPostAssessmentAttempted) {
		this.completionPostAssessmentAttempted = completionPostAssessmentAttempted;
	}

	public boolean isSetCourseExpiration() {
		return isSetCourseExpiration;
	}

	public void setSetCourseExpiration(boolean isSetCourseExpiration) {
		this.isSetCourseExpiration = isSetCourseExpiration;
	}

	public Integer getExamQuestionToAskCount() {
		return examQuestionToAskCount;
	}

	public void setExamQuestionToAskCount(Integer examQuestionToAskCount) {
		this.examQuestionToAskCount = examQuestionToAskCount;
	}

	public int getCourseType() {
		return courseType;
	}

	public void setCourseType(int courseType) {
		this.courseType = courseType;
	}

	public String getLastPublishDate() {
		return lastPublishDate;
	}

	public void setLastPublishDate(String lastPublishDate) {
		this.lastPublishDate = lastPublishDate;
	}

	public Double getLowestPrice() {
		return lowestPrice;
	}


	public boolean isPublishedOnSF() {
		return publishedOnSF;
	}

	public void setPublishedOnSF(boolean publishedOnSF) {
		this.publishedOnSF = publishedOnSF;
	}


	public void setLowestPrice(Double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public Double getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(Double offerPrice) {
		this.offerPrice = offerPrice;
	}
}