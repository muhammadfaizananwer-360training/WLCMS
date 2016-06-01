package com.softech.ls360.lcms.contentbuilder.exception;

import com.softech.common.parsing.TabularDataException;

public class BulkUplaodCourseException extends TabularDataException{

	private static final long serialVersionUID = 1L;

	public BulkUplaodCourseException() {
		
	}

	public BulkUplaodCourseException(String errorMessage, int rowAt) {
		super(errorMessage, rowAt);
	}

	public BulkUplaodCourseException(String message, String errorOnTable, int errorAtRow, int errorAtColumnIndex,
			String errorAtColumnText, String errorText) {
		super(message, errorOnTable, errorAtRow, errorAtColumnIndex, errorAtColumnText, errorText);
	}
}
