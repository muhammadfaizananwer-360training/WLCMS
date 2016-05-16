package com.softech.ls360.lcms.contentbuilder.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.axis.types.UnsignedByte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesLocator;
import com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoapStub;





 

public class AssetUploadHelper {
	
	private final static String serviceEndpoint = LCMSProperties.getLCMSProperty("lcms.webservice.client.asset.endpoint");
	private static Logger logger = LoggerFactory.getLogger(AssetUploadHelper.class);
	
	
	public static int uploadAsset(String localFilePath, String assetType, String fileName, String assetKeywords, String assetDescription,int contentOwnerId,int loggedUserId) throws IOException, ServiceException{
		
		boolean assetUploadSuccess = false;
		int assetId = 0;
		LMSServicesSoapStub service = (LMSServicesSoapStub)new LMSServicesLocator().getLMSServicesSoap(new URL(serviceEndpoint));
	 
		File file = new File(localFilePath);
		int fileSize = (int)file.length();
		int chunkSize = Integer.parseInt(LCMSProperties.getLCMSProperty("lcms.webservice.client.asset.chunkSize"));
				
		String localFileName = file.getName();		
		byte[] readBytes = new byte[fileSize]; 
		
		FileInputStream fis = null;
		UnsignedByte[] usBytes;
		int offset =0;
 
		try  {
			fis = new FileInputStream(file);			
			fis.read(readBytes, 0, fileSize);

			for (int i = 0; i <fileSize ; i+=chunkSize){
				if ((offset + chunkSize) >= fileSize) chunkSize = fileSize - offset ;
				usBytes = getUnsignedBytes(readBytes, offset, chunkSize);
				service.uploadAssetInChunk(localFileName, usBytes , offset);
				offset = i+chunkSize ;
			}
			assetUploadSuccess = true;
		}

		catch (IOException e) {
			logger.error(e.getMessage());
		}

		finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}		
		 
		if (assetUploadSuccess)
			assetId = service.saveAsset(assetType, 0, fileName, assetDescription, assetKeywords, localFileName, 1, "", "", "", "", "", 0, contentOwnerId, 1, true, loggedUserId);
		
		return assetId;
	}

	public static int uploadAssetVSC(String localFilePath, String assetType, String fileName, String assetKeywords, String assetDescription,int scene_id,int contentOwner_id,int loggedUsedId) throws IOException, ServiceException{
		
		boolean assetUploadSuccess = false;
		int assetId = 0;
		LMSServicesSoapStub service = (LMSServicesSoapStub)new LMSServicesLocator().getLMSServicesSoap(new URL(serviceEndpoint));
	 
		File file = new File(localFilePath);
		int fileSize = (int)file.length();
		int chunkSize = Integer.parseInt(LCMSProperties.getLCMSProperty("lcms.webservice.client.asset.chunkSize"));
				
		String localFileName = file.getName();		
		byte[] readBytes = new byte[fileSize]; 
		
		FileInputStream fis = null;
		UnsignedByte[] usBytes;
		int offset =0;
 
		try  {
			fis = new FileInputStream(file);			
			fis.read(readBytes, 0, fileSize);

			for (int i = 0; i <fileSize ; i+=chunkSize){
				if ((offset + chunkSize) >= fileSize) chunkSize = fileSize - offset ;
				usBytes = getUnsignedBytes(readBytes, offset, chunkSize);
				service.uploadAssetInChunk(localFileName, usBytes , offset);
				offset = i+chunkSize ;
			}
			assetUploadSuccess = true;
		}

		catch (IOException e) {
			logger.error(e.getMessage());
		}

		finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}		
		 
		if (assetUploadSuccess)
			assetId = service.saveAsset_VSC(assetType, 0, fileName, assetDescription, assetKeywords, localFileName, contentOwner_id, 1, true, loggedUsedId,scene_id);
		
		return assetId;
	}
	
	
	private static UnsignedByte[] getUnsignedBytes(byte[] bytes, int offset , int length){
		
		UnsignedByte[] usBytes = new UnsignedByte[bytes.length];		
		int f;
		for(int i = offset; i< offset+length;i++){
			f = bytes[i]  & 0xFF;
			
			usBytes[i] = new UnsignedByte(f) ;
		}
		
		return usBytes;
	}
	
}
