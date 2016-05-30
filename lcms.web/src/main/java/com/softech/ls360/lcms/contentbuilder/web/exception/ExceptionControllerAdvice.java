package com.softech.ls360.lcms.contentbuilder.web.exception;

import com.softech.common.validator.InvalidField;
import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.web.controller.CourseController;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ExceptionControllerAdvice {

	private static Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
	
	@ExceptionHandler(Exception.class)
    public ModelAndView handleUnhandledException(Exception e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("name", e.getClass().getSimpleName());
        mav.addObject("message", e.getMessage());
        logger.error("Unhandled Exception", e);
        return mav;
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
        BindingResult result = e.getBindingResult();
        List<InvalidField> invalidFields = new ArrayList<>();

        for (FieldError error : result.getFieldErrors()) {
            InvalidField invalidField = new InvalidField();
            invalidField.setFieldName(error.getField());
            invalidField.setErrorMessage(error.getDefaultMessage());
            invalidFields.add(invalidField);
        }
        RestResponse response = new RestResponse();
        response.setError("invalid-data");
        response.setData(invalidFields);
        return response;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleApiException(HttpMessageNotReadableException e) {
        List<InvalidField> invalidFields = new ArrayList<>();
        RestResponse response = new RestResponse();
        response.setError("corrupt-data");
        response.setData("unable to parse data");
        return response;
    }
}
