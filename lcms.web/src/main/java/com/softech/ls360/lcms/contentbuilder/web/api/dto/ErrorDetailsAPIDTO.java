package com.softech.ls360.lcms.contentbuilder.web.api.dto;

/**
 * Created by abdul.wahid on 5/24/2016.
 */
public class ErrorDetailsAPIDTO {
    private String resourceName;
    private String errorValue;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getErrorValue() {
        return errorValue;
    }

    public void setErrorValue(String errorValue) {
        this.errorValue = errorValue;
    }
}
