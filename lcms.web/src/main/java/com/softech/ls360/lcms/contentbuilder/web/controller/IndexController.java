package com.softech.ls360.lcms.contentbuilder.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class IndexController {
	
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		logger.debug("CourseController::index - Start"); 
		
		logger.debug("CourseController::index - End");
		 return "index"; 
	}
	/*
	@RequestMapping(value = "fileuploaderNew",method=RequestMethod.POST)
	 public String handleFile(MultipartHttpServletRequest request)
	 {
	  MultipartFile file = request.getFile("filedata");
	  //some code herehandleFile
	  System.out.println(""+file.getOriginalFilename());
	  return "fileupload/success";
	 }*/
}
