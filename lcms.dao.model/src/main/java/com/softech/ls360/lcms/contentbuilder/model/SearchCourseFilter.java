package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

public class SearchCourseFilter implements Serializable{

	private static final long serialVersionUID = -5175335948065016579L;
	
	private String loginUserId;
	private String searchCriteria;
	private String loginAuthorId;
	private String contentOwnerId;
	
	public String getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	public String getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	public String getLoginAuthorId() {
		return loginAuthorId;
	}
	public void setLoginAuthorId(String loginAuthorId) {
		this.loginAuthorId = loginAuthorId;
	}
	public String getContentOwnerId() {
		return contentOwnerId;
	}
	public void setContentOwnerId(String contentOwnerId) {
		this.contentOwnerId = contentOwnerId;
	}
	 
}