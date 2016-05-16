package com.softech.ls360.lcms.contentbuilder.utils;

public enum SynchronousClassSessionStatusEnum {
	
	
	UPDATE("U"),DELETE("C"),EXPIRED("D"),ADD("A");
	String status;
	
	
	private SynchronousClassSessionStatusEnum(String status){
		this.status = status;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
