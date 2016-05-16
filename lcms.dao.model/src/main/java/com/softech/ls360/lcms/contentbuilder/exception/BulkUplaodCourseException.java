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
	
	public BulkUplaodCourseException(TabularDataException tabularException) {
		this(tabularException.getMessage(), tabularException.getTableName(), tabularException.getRowNumber()
				, tabularException.getColumnIndex(), tabularException.getColumnName()
				, tabularException.getErrorText());
	}

    @Override
    public String toString() {
        return super.toString() + " [" + getErrorText() + "]";
    }
        
        

	
}
