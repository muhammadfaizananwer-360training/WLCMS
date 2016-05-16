package com.softech.ls360.lcms.contentbuilder.utils;

public enum CourseType {

	ONLINE_COURSE(4, "Online"), CLASSROOM_COURSE(5, "Classroom Course"), WEBINAR_COURSE(
			6, "Webinar Course");

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private CourseType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static CourseType getById(int id) {
	    for(CourseType type : values()) {
	        if(type.getId() == id) return type;
	    }
	    return null;
	 }
}
