package com.softech.common.validator;

/**
 * Created by abdul.wahid on 5/20/2016.
 */
public class InvalidField {
    private String fieldName;
    private String errorMessage;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
