package com.softech.ls360.lcms.contentbuilder.utils;


public class UserFeature{	
	
	public UserFeature(){
		
	}

	public static  Integer courseEditing = 1;
	public static  Integer assetEditing = 2;
	public static  Integer assessmentEditing = 3;
	public static  Integer regulatoryEditing = 5;
	public static  Integer userManagement = 6;
	public static  Integer publishingToMasterCatlog = 7;
	public static  Integer publishingToLegacySystem = 8;
	public static  Integer publishingToLMS = 9;
	public static  Integer readOnlyMode = 10;
	public static  Integer courseConfiguration = 11;
	public static  Integer allowCopyContent = 13;
	public static  Integer courseConfigurationTemplate = 15;
	public static  Integer allowMoveContent = 16;
	public static  Integer makeOffers = 17;
	public static  Integer acceptedOffers = 18;
	public static  Integer courseClone = 19;
	public static  Integer publishingMetaData = 20;
	public static  Integer allowHTML5Template = 22;
	public static  Integer npsCourseRating = 25;
	public static  Integer contentOwnerRoyaltySettings = 26;
	public static  Integer userPermission = 27;
	public static  Integer bulkCourseImportPermssion = 28;
	public static  Integer viewWLCMSReportPermssion = 29;
	

	public static Integer getUserPermission() {
		return userPermission;
	}
	public static void setUserPermission(Integer userPermission) {
		UserFeature.userPermission = userPermission;
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
	public static Integer getBulkCourseImportPermssion() {
		return bulkCourseImportPermssion;
	}
	public static void setBulkCourseImportPermssion(
			Integer bulkCourseImportPermssion) {
		UserFeature.bulkCourseImportPermssion = bulkCourseImportPermssion;
	}
	public static Integer getViewWLCMSReportPermssion() {
		return viewWLCMSReportPermssion;
	}
	public static void setViewWLCMSReportPermssion(Integer viewWLCMSReportPermssion) {
		UserFeature.viewWLCMSReportPermssion = viewWLCMSReportPermssion;
	}
}
