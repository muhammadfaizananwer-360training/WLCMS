package com.softech.ls360.lcms.contentbuilder.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller public class FileUploadController {

	
	
	@RequestMapping(value = "FileController",method=RequestMethod.GET)
	 public String handleFile(ModelMap model)
	 {
	  System.out.println("FileUploadTester GET");
	  return "FileController";
	 }
	/*
	
	 @RequestMapping(method=RequestMethod.POST)
	 public String handleFile3(MultipartHttpServletRequest request)
	 {
	  MultipartFile file = request.getFile("filedata");
	  //some code here
	  System.out.println(""+file.getOriginalFilename());
	  return "fileupload/success";
	 }
	 */
	 
	 @RequestMapping(value = "upload1", method = RequestMethod.POST)
	 public String processUpload(@RequestParam MultipartFile file,
	   ModelMap modelMap, HttpServletRequest request) throws Exception {
	 
	  //log.debug("========= upload file:" + file.getOriginalFilename());
		 System.out.println(""+file.getOriginalFilename());
	                 //you can do something with the uploaded file
	  //UploadedFile uploaded = uploadService.save(file);
	 
	  //modelMap.addAttribute("file", uploaded);
	 
	  return "FileUploadTester";
	 }	 
	} 