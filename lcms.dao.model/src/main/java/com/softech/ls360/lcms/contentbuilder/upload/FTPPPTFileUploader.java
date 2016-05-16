package com.softech.ls360.lcms.contentbuilder.upload;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.slides.ISlide;
import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import com.aspose.slides.Collections.Generic.List;
import com.softech.ls360.lcms.contentbuilder.utils.IOConvertor;

public class FTPPPTFileUploader extends FTPFileUploader {
	
	
	private static Logger logger = LoggerFactory.getLogger(FTPPPTFileUploader.class);
	static {
		
		License lic = new License();
		try (InputStream is = FTPPPTFileUploader.class.getClassLoader().getResourceAsStream("Aspose.Slides.lic")){
			lic.setLicense(is);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	private String localTempLocation = FileUtils.getTempDirectoryPath();
	
		
	
	@Override
	public String uploadFileChunk(String requestId, String fileName, int currentChunk, int chunks, long chunkSize,
			byte[] data) throws Exception {
		String appendedPath = requestId +  dirSeparator + fileName;
		String filePath = getLocalTempLocation() + dirSeparator + appendedPath;
		File file = new File(filePath);
    	if(!isEnabled()) {
    		return appendedPath;
    	}
       					
		//if it is a first chunk
		if(currentChunk <= 0) {
			//assuming, file already exists. 
			FileUtils.deleteQuietly(file);
			
			//assuming, directory structure is not defined
			file.getParentFile().mkdirs();
		} else {
			//exit if current chunk is not required.
			if(!isChunkRequired(currentChunk, chunks, chunkSize, file.length())){
				logger.warn("ignoring current chunk, file path:" + filePath);
				return appendedPath;
			}
		}
		
		OutputStream out = null;
		try {
			//some time "ftpClient.appendFileStream" returns null stream. so we will try configured number of times.
			//int tryLeft = getMaxRetries();
			out = FileUtils.openOutputStream(file, true);
			out = new BufferedOutputStream(out);
			out.write(data);
			out.flush();
		} finally {
			if(out != null) {
				out.close();
			}
		}
		
	    	
    	return appendedPath;

	}

	@Override
	public Object doPostUploadActivitiy(String requestId, String filePath, String activity) throws Exception {
		String pptPath = filePath;
		File pptFile = new File(getLocalTempLocation() + dirSeparator + pptPath);
		String pptDir = FilenameUtils.getBaseName(pptPath);
		//if whole file has been successfully uploaded
		//get file from server and converted it into images
		Presentation ppt = null;
		int slideNumber = 1;
		List<String> faildSlides = new List<String>();
    	try {
    		
    		ppt = new Presentation(pptFile.getAbsolutePath());
    		   		
     		
    		//add slide images to pptDir
    		logger.info("slides extraction started for file, " + pptPath);
    		for(ISlide slide : ppt.getSlides()){
    			
				//Create a full scale image 
   				byte[] imageData = IOConvertor.convertBufferedImageToDataBytes(slide.getThumbnail(1,1),"jpeg");
				pptPath.lastIndexOf(".");
				super.uploadFileChunk(requestId
						, pptDir + dirSeparator + slideNumber + ".jpg"
						, 0, 1, -1, imageData);
				if(slideNumber%30 == 0){
					logger.info(slideNumber + " slides extracted.");
				}
				slideNumber++;
    			
			}
    		
    		//remove ppt
    		try {
    			pptFile.delete();
    		} catch(Exception ex) {
    			logger.warn("unable to delete file, file path:" 
    					+ getTempLocation() + dirSeparator + pptPath, ex);
    		}
    		
    	}  finally {
			if(ppt != null) {
				ppt.dispose();
			}
		}
    	
    	JSONObject json = new JSONObject();
    	json.put("folderPath",getTempLocation() + requestId + dirSeparator + pptDir);
    	json.put("slides", slideNumber-1);
    	json.put("faildSlides", faildSlides.toArray());
    	return json.toString();
	}

	public String getLocalTempLocation() {
		return localTempLocation;
	}

	public void setLocalTempLocation(String localTempLocation) {
		this.localTempLocation = localTempLocation;
	}
	
}
