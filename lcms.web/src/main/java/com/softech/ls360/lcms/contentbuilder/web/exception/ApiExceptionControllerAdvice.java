package com.softech.ls360.lcms.contentbuilder.web.exception;

import com.softech.common.validator.InvalidField;
import com.softech.common.validator.InvalidModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionControllerAdvice {
    @Autowired
    private ExceptionControllerAdvice exceptionAdvise;
    private static Logger logger = LoggerFactory.getLogger(ApiExceptionControllerAdvice.class);
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestResponse handleApiException(Exception e) {
        RestResponse response = new RestResponse();
        try {
            throw e;
        } catch (InvalidModelException ex) {

        } catch (Exception ex) {
            response.setError(e.toString());
            logger.error("Unhandled API Exception", e);
        }

        return response;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestResponse handleApiException(HttpRequestMethodNotSupportedException e) {
        RestResponse response = new RestResponse();
        response.setError("not-found");
        response.setData("mathod & URL not supported");
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleApiException(MethodArgumentNotValidException e) {
       return exceptionAdvise.handleApiException(e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleApiException(HttpMessageNotReadableException e) {
        return exceptionAdvise.handleApiException(e);
    }
}
