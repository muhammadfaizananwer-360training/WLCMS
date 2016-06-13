package com.softech.ls360.lcms.contentbuilder.service.test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.softech.ls360.lcms.contentbuilder.service.IAssetService;
import com.softech.ls360.lcms.contentbuilder.test.AbstractLcmsTest;
import com.softech.ls360.lcms.contentbuilder.upload.FileUploader;
import com.softech.ls360.lcms.contentbuilder.utils.IOConvertor;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;

public class AssetServiceTest extends AbstractLcmsTest {
	
	@Autowired
	IAssetService assetService;
	
    @Autowired
	FileUploader fileUploader;
    
	@Autowired
	@Qualifier("fms")
	FileUploader fmsFileUploader;
	
	@Autowired
	@Qualifier("ppt")
	FileUploader pptFileUploader;
	
	
	@Value("${code.lcms.assets.URL}")
	String assetServerUrl;
	
	@Test()
	public void uploadAsset() throws IOException, Exception {
		String testFilePath = "images/Koala.jpg";
		String requestId = "testing/" + ((int) (Math.random() * 100000));
		
		byte[] inputFileData = null;
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(testFilePath)){
			assertNotNull("classpath:" + testFilePath + " file not available for testing.",is);
			inputFileData = IOConvertor.convertStreamToDataBytes(is);
		}
		
		String tempLocation = fileUploader.uploadFileChunk(requestId, FilenameUtils.getName(testFilePath), 0, 0, 0,inputFileData);
		assertNotEmpty("file was not copied to temp location on FTP server",tempLocation);
		
		
		Long assetId = assetService.addAsset(requestId, 
				getUser().getUsername(), getUser().getContentId(), getUser().getAuthorId(), 
				FilenameUtils.getBaseName(testFilePath) + "-Test-" + new Date(), 
				tempLocation, 
				"Image", "", "", new ObjectWrapper<Long>());
		
		assertNotNull("Asset creation or confirmation is failed",assetId);
		
		String assetPersistenceLocation = assetService.getAssetLocation(assetId.intValue());
		assertNotEmpty("Asset persistence location not found",assetPersistenceLocation);
		
	}
}
