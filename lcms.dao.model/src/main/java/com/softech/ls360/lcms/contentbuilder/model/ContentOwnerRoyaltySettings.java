package com.softech.ls360.lcms.contentbuilder.model;


public class ContentOwnerRoyaltySettings {
	
	private String userName;
	private String firstName;
	private String lastName;
	private String onlineRoyaltyPercentage;
	private String classroomRoyaltyPercentage;
	private String webinarRoyaltyPercentage;
	private int contentOwnerId;
	
	public ContentOwnerRoyaltySettings(){
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getContentOwnerId() {
		return contentOwnerId;
	}

	public void setContentOwnerId(int contentOwnerId) {
		this.contentOwnerId = contentOwnerId;
	}

	public String getOnlineRoyaltyPercentage() {
		return onlineRoyaltyPercentage;
	}

	public void setOnlineRoyaltyPercentage(String onlineRoyaltyPercentage) {
		this.onlineRoyaltyPercentage = onlineRoyaltyPercentage;
	}

	public String getClassroomRoyaltyPercentage() {
		return classroomRoyaltyPercentage;
	}

	public void setClassroomRoyaltyPercentage(String classroomRoyaltyPercentage) {
		this.classroomRoyaltyPercentage = classroomRoyaltyPercentage;
	}

	public String getWebinarRoyaltyPercentage() {
		return webinarRoyaltyPercentage;
	}

	public void setWebinarRoyaltyPercentage(String webinarRoyaltyPercentage) {
		this.webinarRoyaltyPercentage = webinarRoyaltyPercentage;
	}
}
