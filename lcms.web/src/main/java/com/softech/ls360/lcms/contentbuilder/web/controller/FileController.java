package com.softech.ls360.lcms.contentbuilder.web.controller;

import com.softech.ls360.lcms.contentbuilder.upload.FileUploader;
import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
	private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
	FileUploader fileUploader;

	@Autowired
	@Qualifier("fms")
	FileUploader fmsFileUploader;

	@Autowired
	@Qualifier("fms2")
	FileUploader fmsFileUploader2;

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

    		String filePath = null;
			FileDetails fileDetails = new FileDetails();
	    	//upload to fms or fms2 if file type is mp4.
	    	if(fileServer.equalsIgnoreCase("fms")) {
				try {
					filePath= fmsFileUploader.uploadFileChunk(requestId, name, chunk, chunks, chunkSize, file.getBytes());
					logger.info("chunk "+chunk+" uploaded to fms1");
				} catch (Exception e) {
					//if it is not the first chunk of a file, rethrow error as it is the normal expected behavior
					if(chunk != 0) {
						throw e;
					}
					//Now here is the first chunk failed in fms, divert request to fms2
					logger.debug(e.getMessage(), e);
					logger.info("first chunk failed to upload to fms1, diverting request to fms2");

					filePath= fmsFileUploader2.uploadFileChunk(requestId, name, chunk, chunks, chunkSize, file.getBytes());
					//let client code know to upload all further chunks to fms2
					//It is optional variable only set in case of 1st chunk of fms2
					fileDetails.setFileServer("fms2");
				}
			} else if(fileServer.equalsIgnoreCase("fms2")) {
				filePath= fmsFileUploader2.uploadFileChunk(requestId, name, chunk, chunks, chunkSize, file.getBytes());
				logger.info("chunk "+chunk+" uploaded to fms2");
			} else if(fileServer.equalsIgnoreCase("ppt")) {
	    		filePath= pptFileUploader.uploadFileChunk(requestId, name, chunk, chunks, chunkSize, file.getBytes());
	    	} else {
	    		filePath= fileUploader.uploadFileChunk(requestId, name, chunk, chunks, chunkSize, file.getBytes());
	    	}

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
		private String fileServer;

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public String getFileServer() {
			return fileServer;
		}

		public void setFileServer(String fileServer) {
			this.fileServer = fileServer;
		}
	}

}