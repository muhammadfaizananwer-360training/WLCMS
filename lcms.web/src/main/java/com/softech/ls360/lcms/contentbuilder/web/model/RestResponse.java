package com.softech.ls360.lcms.contentbuilder.web.model;

public class RestResponse {
	private String error;
	private String warning;
	private String info = "ok";
	private Object data;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
		info = null;
	}
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
		info = null;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
