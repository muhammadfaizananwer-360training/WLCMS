package com.softech.ls360.lcms.contentbuilder.model;

public enum QUICK_BUILD_COURSETYPE {
	POWER_POINT_BASED (1), DOCUMENT_BASED (2), VIDEO_BASED (3);
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private int value;
	
	private QUICK_BUILD_COURSETYPE(int value) {
        this.value = value;
	}
}
