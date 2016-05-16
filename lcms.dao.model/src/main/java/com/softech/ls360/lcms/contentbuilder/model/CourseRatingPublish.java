package com.softech.ls360.lcms.contentbuilder.model;

public class CourseRatingPublish {

	private short RatingCourse;
	private short RatingShopping;
	private short RatingLearningTech;
	private short RatingCS;
	private String UserReviewText;
	private String CourseGuid;
	private String LearnerFirstName;
	private String LearnerCity;
	private String LearnerState;
	private String RatingTimeStamp;
	private int CourseId;
	private int EnrollmentId;
	public short getRatingCourse() {
		return RatingCourse;
	}
	public void setRatingCourse(short ratingCourse) {
		RatingCourse = ratingCourse;
	}
	public String getUserReviewText() {
		return UserReviewText;
	}
	public void setUserReviewText(String userReviewText) {
		UserReviewText = userReviewText;
	}
	public String getCourseGuid() {
		return CourseGuid;
	}
	public void setCourseGuid(String courseGuid) {
		CourseGuid = courseGuid;
	}
	public String getLearnerFirstName() {
		return LearnerFirstName;
	}
	public void setLearnerFirstName(String learnerFirstName) {
		LearnerFirstName = learnerFirstName;
	}
	public String getLearnerCity() {
		return LearnerCity;
	}
	public void setLearnerCity(String learnerCity) {
		LearnerCity = learnerCity;
	}
	public String getLearnerState() {
		return LearnerState;
	}
	public void setLearnerState(String learnerState) {
		LearnerState = learnerState;
	}
	public String getRatingTimeStamp() {
		return RatingTimeStamp;
	}
	public void setRatingTimeStamp(String ratingTimeStamp) {
		RatingTimeStamp = ratingTimeStamp;
	}
	public int getCourseId() {
		return CourseId;
	}
	public void setCourseId(int courseId) {
		CourseId = courseId;
	}
	public int getEnrollmentId() {
		return EnrollmentId;
	}
	public void setEnrollmentId(int enrollmentId) {
		EnrollmentId = enrollmentId;
	}
	public short getRatingShopping() {
		return RatingShopping;
	}
	public void setRatingShopping(short ratingShopping) {
		RatingShopping = ratingShopping;
	}
	public short getRatingLearningTech() {
		return RatingLearningTech;
	}
	public void setRatingLearningTech(short ratingLearningTech) {
		RatingLearningTech = ratingLearningTech;
	}
	public short getRatingCS() {
		return RatingCS;
	}
	public void setRatingCS(short ratingCS) {
		RatingCS = ratingCS;
	}
}
