package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;


public class SearchCourseDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5175335948065016579L;
	
    private long id;
    
    private String courseGUID = null;
    
    private String courseStatus = null;
    
    private String courseId = null;//bussinessKey
    
    private String name = null;
    
    private String courseType = null;
    
    private long createdByAuthorID;
    
    private String creationDate = null;
    
    private String lastModifiedDate = null;
    
    private String courseRatingStr = null;
        
    private int courseRating;
    
    private int courseRatingCount;
    private String coursePublishStatus;
    
    private long totalRows;
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCourseGUID() {
		return courseGUID;
	}

	public void setCourseGUID(String courseGUID) {
		this.courseGUID = courseGUID;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public long getCreatedByAuthorID() {
		return createdByAuthorID;
	}

	public void setCreatedByAuthorID(Long createdByAuthorID) {
		this.createdByAuthorID = createdByAuthorID;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getCourseRatingStr() {
		return courseRatingStr;
	}

	public void setCourseRatingStr(String courseRating) {
		this.courseRatingStr = courseRating;
	}

	public int getCourseRating() {
		return courseRating;
	}

	public void setCourseRating(int courseRating) {
		this.courseRating = courseRating;
	}

	public int getCourseRatingCount() {
		return courseRatingCount;
	}

	public void setCourseRatingCount(int courseRatingCount) {
		this.courseRatingCount = courseRatingCount;
	}

	public String getCoursePublishStatus() {
		return coursePublishStatus;
	}

	public void setCoursePublishStatus(String coursePublishStatus) {
		this.coursePublishStatus = coursePublishStatus;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	 public String toString() {
	        return "{\"Id\":\""+this.id+"\",\"Title\":\""+this.name+"\",\"CourseID\":\""+this.courseId+"\",\"Type\":\""+this.courseType+"\",\"Status\":\""+this.courseStatus+"\",\"CreationDate\":\""+this.creationDate+"\",\"LastModifiedDate\":\""+this.lastModifiedDate+"\",\"CourseRating\":\""+this.courseRatingStr+"\",\"CoursePublishStatus\":\""+this.coursePublishStatus+"\"}";
	 }
}