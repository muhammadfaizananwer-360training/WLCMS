package com.softech.ls360.lcms.contentbuilder.utils;

import org.springframework.stereotype.Component;

@Component
public class UserFeature{	
	
	public UserFeature(){
		
	}

	private  Integer npsCourseRating = 25;
	private  Integer contentOwnerRoyaltySettings = 26;
	private  Integer userPermission = 27;
	private  Integer bulkCourseImportPermssion = 28;
	private  Integer viewWLCMSReportPermssion = 29;
	

	public Integer getUserPermission() {
		return userPermission;
	}
	public void setUserPermission(Integer userPermission) {
		this.userPermission = userPermission;
	}
	
	public Integer getNpsCourseRating() {
		return npsCourseRating;
	}
	public void setNpsCourseRating(Integer npsCourseRating) {
		this.npsCourseRating = npsCourseRating;
	}
	public Integer getContentOwnerRoyaltySettings() {
		return contentOwnerRoyaltySettings;
	}
	public void setContentOwnerRoyaltySettings(Integer contentOwnerRoyaltySettings) {
		this.contentOwnerRoyaltySettings = contentOwnerRoyaltySettings;
	}
	public Integer getBulkCourseImportPermssion() {
		return bulkCourseImportPermssion;
	}
	public void setBulkCourseImportPermssion(
			Integer bulkCourseImportPermssion) {
		this.bulkCourseImportPermssion = bulkCourseImportPermssion;
	}
	public Integer getViewWLCMSReportPermssion() {
		return viewWLCMSReportPermssion;
	}
	public void setViewWLCMSReportPermssion(Integer viewWLCMSReportPermssion) {
		this.viewWLCMSReportPermssion = viewWLCMSReportPermssion;
	}
}
