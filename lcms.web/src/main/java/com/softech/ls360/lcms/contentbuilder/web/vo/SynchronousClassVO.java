package com.softech.ls360.lcms.contentbuilder.web.vo;

import javax.persistence.Column;


public class SynchronousClassVO {

	private long syncClassId;
	private String timeZone;
	private String enrollmentCloseDate;
	private String courseStartDate;
	private String courseStartTime;
	private String courseEndTime;
	private String presenterFirstName;	
	private String presenterLastName;	
	private String presenterEmail;
	private String presenterPhone;
	private String meetingURL;		
	private String meetingPassCode;	
	private String  dialInNumber;
	private String  additionalInformation;
	private String  webinarServiceProvider;
	private String  courseStatus;
	private String meetingId;
	private String fullName;
	private String emailAddress;
	private Long courseProviderId;
	private String providerPhoneNo;
	// 0 mean setup form data is not save in DB, 1 mean saved
	private String  classroomSetupPageData = "0"; 
	
	public String getPresenterPhone() {
		return presenterPhone;
	}
	public void setPresenterPhone(String presenterPhone) {
		this.presenterPhone = presenterPhone;
	}
	private long maximumClassSize;
	
	private String classSize;
	private String location;
	private String address;
	private String city;
	private String zipPostal;
	private String country;
	private String state;
	private String phone;
	private String description;
	
	public long getSyncClassId() {
		return syncClassId;
	}
	public void setSyncClassId(long syncClassId) {
		this.syncClassId = syncClassId;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getEnrollmentCloseDate() {
		return enrollmentCloseDate;
	}
	public void setEnrollmentCloseDate(String enrollmentCloseDate) {
		this.enrollmentCloseDate = enrollmentCloseDate;
	}
	public String getCourseStartDate() {
		return courseStartDate;
	}
	public void setCourseStartDate(String courseStartDate) {
		this.courseStartDate = courseStartDate;
	}
	public String getCourseStartTime() {
		return courseStartTime;
	}
	public void setCourseStartTime(String courseStartTime) {
		this.courseStartTime = courseStartTime;
	}
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	public String getPresenterFirstName() {
		return presenterFirstName;
	}
	public void setPresenterFirstName(String presenterFirstName) {
		this.presenterFirstName = presenterFirstName;
	}
	public String getPresenterLastName() {
		return presenterLastName;
	}
	public void setPresenterLastName(String presenterLastName) {
		this.presenterLastName = presenterLastName;
	}
	public String getPresenterEmail() {
		return presenterEmail;
	}
	public void setPresenterEmail(String presenterEmail) {
		this.presenterEmail = presenterEmail;
	}
	public long getMaximumClassSize() {
		return maximumClassSize;
	}
	public void setMaximumClassSize(long maximumClassSize) {
		this.maximumClassSize = maximumClassSize;
	}
	public String getClassSize() {
		return classSize;
	}
	public void setClassSize(String classSize) {
		this.classSize = classSize;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZipPostal() {
		return zipPostal;
	}
	public void setZipPostal(String zipPostal) {
		this.zipPostal = zipPostal;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getMeetingURL() {
		return meetingURL;
	}

	public void setMeetingURL(String meetingURL) {
		this.meetingURL = meetingURL;
	}
	
	
	public String getMeetingPassCode() {
		return meetingPassCode;
	}

	public void setMeetingPassCode(String meetingPassCode) {
		this.meetingPassCode = meetingPassCode;
	}

	
	public String getDialInNumber() {
		return dialInNumber;
	}

	public void setDialInNumber(String dialInNumber) {
		this.dialInNumber = dialInNumber;
	}

	

	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public Long getCourseProviderId() {
		return courseProviderId;
	}
	public void setCourseProviderId(Long courseProviderId) {
		this.courseProviderId = courseProviderId;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	
	public String getWebinarServiceProvider() {
		return webinarServiceProvider;
	}

	public void setWebinarServiceProvider(String webinarServiceProvider) {
		this.webinarServiceProvider = webinarServiceProvider;
	}
	public String getClassroomSetupPageData() {
		return classroomSetupPageData;
	}
	public void setClassroomSetupPageData(String classroomSetupPageData) {
		this.classroomSetupPageData = classroomSetupPageData;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getProviderPhoneNo() {
		return providerPhoneNo;
	}
	public void setProviderPhoneNo(String providerPhoneNo) {
		this.providerPhoneNo = providerPhoneNo;
	}
	
	
}
