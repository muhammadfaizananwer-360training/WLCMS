package com.softech.ls360.lcms.contentbuilder.model;


/**
 *
 * @author haider.ali
 *
 */
public class CourseSettings {

	protected Long id;
	protected Long course_Id;
	protected Long courseConfiguration_Id;
	protected Boolean passAllQuizzes;
	protected Boolean attemptTheExam;
	protected Boolean passTheExam;
	protected Boolean agreeWithSpecifiedText;
	protected String specifiedText;
	protected String courseName;
	protected String businessKey;
	protected Boolean displaySlidesTOC;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCourse_Id() {
		return course_Id;
	}
	public void setCourse_Id(Long course_Id) {
		this.course_Id = course_Id;
	}
	public Boolean getPassAllQuizzes() {
		return passAllQuizzes;
	}
	public void setPassAllQuizzes(Boolean passAllQuizzes) {
		this.passAllQuizzes = passAllQuizzes;
	}
	public Boolean getAttemptTheExam() {
		return attemptTheExam;
	}
	public void setAttemptTheExam(Boolean attemptTheExam) {
		this.attemptTheExam = attemptTheExam;
	}
	public Boolean getPassTheExam() {
		return passTheExam;
	}
	public void setPassTheExam(Boolean passTheExam) {
		this.passTheExam = passTheExam;
	}
	public Boolean getAgreeWithSpecifiedText() {
		return agreeWithSpecifiedText;
	}
	public void setAgreeWithSpecifiedText(Boolean agreeWithSpecifiedText) {
		this.agreeWithSpecifiedText = agreeWithSpecifiedText;
	}
	public String getSpecifiedText() {
		return specifiedText;
	}
	public void setSpecifiedText(String specifiedText) {
		this.specifiedText = specifiedText;
	}
	public Long getCourseConfiguration_Id() {
		return courseConfiguration_Id;
	}
	public void setCourseConfiguration_Id(Long courseConfiguration_Id) {
		this.courseConfiguration_Id = courseConfiguration_Id;
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

	public Boolean getDisplaySlidesTOC() {
		return displaySlidesTOC;
	}
	public void setDisplaySlidesTOC(Boolean displaySlidesTOC) {
		this.displaySlidesTOC = displaySlidesTOC;
	}

}
