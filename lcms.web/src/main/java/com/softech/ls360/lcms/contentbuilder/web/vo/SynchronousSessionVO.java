package com.softech.ls360.lcms.contentbuilder.web.vo;

public class SynchronousSessionVO {
	private String id;
	private long syncClassId;
	private String startDate;
	private String startDateDisplay;
	private String startTime;
	private String endDate;
	private String endDateDisplay;
	private String endTime;
	private String sessionKey;
	
	public long getSyncClassId() {
		return syncClassId;
	}
	public void setSyncClassId(long syncClassId) {
		this.syncClassId = syncClassId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartDateDisplay() {
		return startDateDisplay;
	}
	public void setStartDateDisplay(String startDateDisplay) {
		this.startDateDisplay = startDateDisplay;
	}
	public String getEndDateDisplay() {
		return endDateDisplay;
	}
	public void setEndDateDisplay(String endDateDisplay) {
		this.endDateDisplay = endDateDisplay;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
}
