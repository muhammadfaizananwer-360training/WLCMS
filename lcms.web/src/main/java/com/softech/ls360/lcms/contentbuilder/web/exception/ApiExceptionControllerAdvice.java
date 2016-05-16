package com.softech.ls360.lcms.contentbuilder.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionControllerAdvice {

    private static Logger logger = LoggerFactory.getLogger(ApiExceptionControllerAdvice.class);
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestResponse handleApiException(Exception e) {
        RestResponse response = new RestResponse();
        response.setError(e.toString());
        logger.error("Unhandled API Exception", e);
        return response;
    }
        
}
