package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.softech.ls360.lcms.contentbuilder.upload.FTPFileUploader;
import com.softech.ls360.lcms.contentbuilder.upload.FileUploader;
import com.softech.ls360.lcms.contentbuilder.utils.FileMeta;
import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;
 
@Controller
public class FileController {
	private static Logger logger = LoggerFactory.getLogger(FileController.class);
	
    @Autowired
	FileUploader fileUploader;
    
	@Autowired
	@Qualifier("fms")
	FileUploader fmsFileUploader;
	
	@Autowired
	@Qualifier("ppt")
	FileUploader pptFileUploader;
    
    @ResponseBody
    @RequestMapping(value="upload", method = RequestMethod.POST)
    ResponseEntity<Object> upload(@RequestBody MultipartFile file,@RequestParam String requestId
    		, @RequestParam String fileServer
    		, @RequestParam String name, @RequestParam(required=false, defaultValue="-1") int chunks
    		, @RequestParam(required=false, defaultValue="-1") int chunk
    		, @RequestParam(required=false, defaultValue="0") int chunkSize) throws Throwable {
    	RestResponse response = new RestResponse();
    	HttpStatus httpStatus = HttpStatus.OK;
    	try {
	    	
    		String filePath;
    		
	    	//upload to FMS if file type is mp4.
	    	if(fileServer.equalsIgnoreCase("fms")) {
	    		filePath= fmsFileUploader.uploadFileChunk(requestId, name, chunk, chunks, chunkSize, file.getBytes());
	    	} else if(fileServer.equalsIgnoreCase("ppt")) {
	    		filePath= pptFileUploader.uploadFileChunk(requestId, name, chunk, chunks, chunkSize, file.getBytes());
	    	} else {
	    		filePath= fileUploader.uploadFileChunk(requestId, name, chunk, chunks, chunkSize, file.getBytes());
	    	}
	    	
	    	FileDetails fileDetails = new FileDetails();
	    	fileDetails.setFilePath(filePath);
	    	response.setData(fileDetails);
    	} catch(Exception ex) {
    		response.setError(ex.getMessage());
    		httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    		logger.error(ex.getMessage(), ex);
    	}
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
    	return new ResponseEntity<Object>(response,headers,httpStatus);
    	
    }
    
    @ResponseBody
    @RequestMapping(value="doPostUploadActivites", method = RequestMethod.POST)
    ResponseEntity<Object> doPostUploadActivites(@RequestParam String requestId
    		, @RequestParam String fileServer
    		, @RequestParam String activity
    		, @RequestParam String filePath) throws Throwable {
    	RestResponse response = new RestResponse();
    	HttpStatus httpStatus = HttpStatus.OK;
    	try {
	    	
    		FileUploader uploader = null;
	    	//upload to FMS if file type is mp4.
	    	if(fileServer.equalsIgnoreCase("fms")) {
	    		uploader= fmsFileUploader;
	    	} else if(fileServer.equalsIgnoreCase("ppt")) {
	    		uploader= pptFileUploader;
	    	} else {
	    		uploader= fileUploader;
	    	}
	    	response.setData(uploader.doPostUploadActivitiy(requestId, filePath, activity));
    	} catch(Exception ex) {
    		response.setError(ex.getMessage());
    		httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    		logger.error(ex.getMessage(), ex);
    	}
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
    	return new ResponseEntity<Object>(response,headers,httpStatus);
    	
    }
    
    
            
    
        
    public static class FileDetails {
    	private String filePath;

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
    }

}