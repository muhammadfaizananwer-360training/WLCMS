package com.softech.ls360.lcms.contentbuilder.web.api.dto;

/**
 * Created by abdul.wahid on 5/24/2016.
 */
public class ErrorDetailsAPIDTO {
    private String tableName;
    private String errorText;
    private String message;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
