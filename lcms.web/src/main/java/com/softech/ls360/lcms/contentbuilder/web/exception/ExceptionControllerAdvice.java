package com.softech.ls360.lcms.contentbuilder.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.web.controller.CourseController;


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
}
