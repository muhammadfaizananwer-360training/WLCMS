package com.softech.ls360.lcms.contentbuilder.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CourseAvailability implements java.io.Serializable {

	private static final long serialVersionUID = 4399549077058319833L;
	
	@Id
	private long id;
	private String industry = null;
	private String courseGroupName = null;
	private String courseGroupId = null;
	private String courseExpiration = null;
	private String learnerAccessToCourse = null;
	private Timestamp courseExpirationTimestamp = null;
	private String courseName = null;
	private String businessKey = null;
	
	//flags
	Boolean eligibleForMobileTablet = false;
	Boolean eligibleForSubscription = false;
	
	Boolean eligibleForVARresale = false;
	Boolean eligibleForDistrThruSCORM = false;
	Boolean eligibleForDistrThruAICC = false;
	
	Boolean requireReportToRegulator = false;
	Boolean requireShippableItems = false;
	Boolean isThirdpartyCourse = false;
	private String updatedBy = null;

	public CourseAvailability() {

	}

	public CourseAvailability(long id, String industry, String courseGroupName,
			String courseExpiration, String LearnerAccessToCourse) {
		super();
		this.id = id;
		this.industry = industry;
		this.courseGroupName = courseGroupName;
		this.courseExpiration = courseExpiration;
		this.learnerAccessToCourse = LearnerAccessToCourse;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getCourseExpiration() {
		return courseExpiration;
	}

	public void setCourseExpiration(String courseExpiration) {
		this.courseExpiration = courseExpiration;
	}

	public String getLearnerAccessToCourse() {
		return learnerAccessToCourse;
	}

	public void setLearnerAccessToCourse(String learnerAccessToCourse) {
		this.learnerAccessToCourse = learnerAccessToCourse;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCourseGroupName() {
		return courseGroupName;
	}

	public void setCourseGroupName(String courseGroupName) {
		this.courseGroupName = courseGroupName;
	}

	public String getCourseGroupId() {
		return courseGroupId;
	}

	public void setCourseGroupId(String courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	public Boolean getEligibleForMobileTablet() {
		return eligibleForMobileTablet;
	}

	public void setEligibleForMobileTablet(Boolean eligibleForMobileTablet) {
		this.eligibleForMobileTablet = eligibleForMobileTablet;
	}

	public Boolean getEligibleForSubscription() {
		return eligibleForSubscription;
	}

	public void setEligibleForSubscription(Boolean eligibleForSubscription) {
		this.eligibleForSubscription = eligibleForSubscription;
	}

	public Boolean getEligibleForVARresale() {
		return eligibleForVARresale;
	}

	public void setEligibleForVARresale(Boolean eligibleForVARresale) {
		this.eligibleForVARresale = eligibleForVARresale;
	}

	public Boolean getEligibleForDistrThruSCORM() {
		return eligibleForDistrThruSCORM;
	}

	public void setEligibleForDistrThruSCORM(Boolean eligibleForDistrThruSCORM) {
		this.eligibleForDistrThruSCORM = eligibleForDistrThruSCORM;
	}

	public Boolean getEligibleForDistrThruAICC() {
		return eligibleForDistrThruAICC;
	}

	public void setEligibleForDistrThruAICC(Boolean eligibleForDistrThruAICC) {
		this.eligibleForDistrThruAICC = eligibleForDistrThruAICC;
	}

	public Boolean getRequireReportToRegulator() {
		return requireReportToRegulator;
	}

	public void setRequireReportToRegulator(Boolean requireReportToRegulator) {
		this.requireReportToRegulator = requireReportToRegulator;
	}

	public Boolean getRequireShippableItems() {
		return requireShippableItems;
	}

	public void setRequireShippableItems(Boolean requireShippableItems) {
		this.requireShippableItems = requireShippableItems;
	}

	public Boolean getIsThirdpartyCourse() {
		return isThirdpartyCourse;
	}

	public void setIsThirdpartyCourse(Boolean isThirdpartyCourse) {
		this.isThirdpartyCourse = isThirdpartyCourse;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getCourseExpirationTimestamp() {
		return courseExpirationTimestamp;
	}

	public void setCourseExpirationTimestamp(Timestamp courseExpirationTimestamp) {
		this.courseExpirationTimestamp = courseExpirationTimestamp;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}  
	
}
