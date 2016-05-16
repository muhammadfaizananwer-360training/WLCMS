package com.softech.ls360.lcms.contentbuilder.upload;

import java.io.IOException;
import java.net.SocketException;

import com.softech.ls360.lcms.contentbuilder.upload.FTPFileUploader.FileDetail;
import com.softech.ls360.lcms.contentbuilder.utils.Delegate;

public interface FileUploader {
	public String uploadFileChunk(String requestId,String fileName, int currentChunk, int chunks, long chunkSize, byte[] data) throws Exception;
	public FileDetail confirmFile(String requestId,String fileName,String permanentRelativePath) throws Exception;
	public Object doPostUploadActivitiy(String requestId,String filePath, String activity) throws Exception;
	public boolean tempFileExists(String filePath) throws Exception;
	boolean deleteFile(String filePath) throws Exception;
}
