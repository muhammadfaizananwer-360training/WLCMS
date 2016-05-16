package com.softech.ls360.lcms.contentbuilder.web.model;

public class BulkUploadCourseModel {

	private int errorAtLineNo = -1;
	private int totalRows = -1;
	private String message = "";
	private int currentRowIndex = -1;
	private boolean success = false;
	
	public int getErrorAtLineNo() {
		return errorAtLineNo;
	}
	public void setErrorAtLineNo(int errorAtLineNo) {
		this.errorAtLineNo = errorAtLineNo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	public int getCurrentRowIndex() {
		return currentRowIndex;
	}
	public void setCurrentRowIndex(int currentRowIndex) {
		this.currentRowIndex = currentRowIndex;
	}
}
