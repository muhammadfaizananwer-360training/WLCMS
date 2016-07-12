package com.softech.ls360.lcms.contentbuilder.upload;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;

public class FTPFileUploader implements FileUploader {
	public static class FileDetail {
		private String filePath;
		private long sizeInBytes;

		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public long getSizeInBytes() {
			return sizeInBytes;
		}
		public void setSizeInBytes(long sizeInBytes) {
			this.sizeInBytes = sizeInBytes;
		}


	}
	private String tempLocation;
	private String permanentLocation;
	private String ftpServer;
	private String userName;
	private String password;
	private Integer maxRetries;
	private boolean enabled;
	private static Logger logger = LoggerFactory.getLogger(FTPFileUploader.class);
	protected static char dirSeparator = '/';
	@Override
	public String uploadFileChunk(String requestId,String fileName, int currentChunk, int chunks, long chunkSize, byte[] data) throws Exception {
		String filePath = getTempLocation() + requestId +  dirSeparator + fileName;
    	if(!enabled) {
    		return filePath;
    	}

    	FTPClient ftpClient = null;
    	try {
    		ftpClient = connectFTP();

			//if it is a first chunk
			if(currentChunk <= 0) {
				//assuming, file already exists.
				ftpClient.deleteFile(filePath);

				//assuming, directory structure is not defined
				createDirectory(ftpClient,filePath, true);
			} else {
				//exit if current chunk is not required.
				if(!isChunkRequired(currentChunk, chunks, chunkSize, getFileSize(ftpClient, filePath))){
					logger.warn("ignoring current chunk, file path:" + filePath);
					return filePath;
				}
			}

			OutputStream out = null;
			try {
				//some time "ftpClient.appendFileStream" returns null stream. so we will try configured number of times.
				int tryLeft = getMaxRetries();
				while(true) {
					out = ftpClient.appendFileStream(filePath);
					tryLeft--;
					if(out == null && tryLeft>0 ) {
						logger.warn("Unable to append file"
							+ "\n File Path:" + filePath
							+ "\nFTP Error Msg:" + ftpClient.getReplyString());
						Thread.sleep(200);
					} else {
						break;
					}
				}

				if(out != null) {
					out = new BufferedOutputStream(out);
					out.write(data);
					out.flush();
				} else {
					//last try with input stream
					InputStream is = new ByteArrayInputStream(data);

					//appendFileStream is observed much faster that appendFile, So we kept appendFileStream as first priority
					if(!ftpClient.appendFile(filePath,is)){
						throw new IOException("Unable to copy file to server because of unkonwn issue.\nFTP Error Msg:" + ftpClient.getReplyString());
					}
				}
			} finally {
				if(out != null) {
					out.close();
				}
			}


		} finally {
			try {
				if(ftpClient != null) {
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		}

    	return filePath;
	}

	@Override
	public FileDetail confirmFile(String requestId,String fileRelativePath,String permanentRelativePath) throws Exception {
		String permanetPath = getPermanentLocation() + permanentRelativePath;
		String tempPath = fileRelativePath;

		FileDetail fileDetail = new FileDetail();
		fileDetail.setFilePath(permanetPath);

		if(!enabled) {
    		return fileDetail;
    	}

		FTPClient ftpClient = null;
    	try {
    		ftpClient = connectFTP();
			createDirectory(ftpClient,permanetPath, true);

			//move file location.
			if(!ftpClient.rename(tempPath, permanetPath)) {
				throw new IOException("Unable to move file to permanent location because of unkonwn issue."
						+ "\nTemp Path:" + tempPath
						+ "\nPermanent Path:" + permanetPath
						+ "\nFTP Error Msg:" + ftpClient.getReplyString());
			}
			fileDetail.setSizeInBytes(getFileSize(ftpClient, permanetPath));
		} finally {
			try {
				if(ftpClient != null) {
					ftpClient.disconnect();
				}
			} catch (IOException e) {}
		}



		return fileDetail;

	}

	@Override
	public boolean tempFileExists(String filePath) throws Exception {
		FTPClient ftpClient = null;
    	try {
    		ftpClient = connectFTP();
    		return fileExists(ftpClient, filePath);
    	} finally {
			try {
				if(ftpClient != null) {
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		}
	}


	@Override
	public boolean deleteFile(String filePath) throws Exception {
		FTPClient ftpClient = null;
    	try {
    		ftpClient = connectFTP();
    		if(!ftpClient.deleteFile(filePath)){
    			logger.warn("Unable to delete file"
						+ "\n File Path:" + filePath
						+ "\nFTP Error Msg:" + ftpClient.getReplyString());
    			return false;
    		}else {
    			return true;
    		}
    	} finally {
			try {
				if(ftpClient != null) {
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		}
	}

	public FileDetail getFileDetail(String filePath) throws Exception {
		FTPClient ftpClient = null;
		FileDetail fileDetail = new FileDetail();
		fileDetail.setFilePath(filePath);
    	try {
    		ftpClient = connectFTP();
    		fileDetail.setSizeInBytes(getFileSize(ftpClient, filePath));
    		return fileDetail;
    	} finally {
			try {
				if(ftpClient != null) {
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		}
	}

	protected static boolean isChunkRequired(int currentChunk, int chunks, long chunkSize, long fileSize) {
		if(currentChunk * chunkSize >= fileSize) {
			return true;
		} else {
			logger.warn("acutal file size:" + fileSize + " bytes"
					+ "/n file should not be largert than of," + currentChunk * chunkSize + " bytes");
			return false;
		}
	}

	protected static long getFileSize(FTPClient ftpClient,String filePath) throws Exception {
		long fileSize = 0;
	    FTPFile[] files = ftpClient.listFiles(filePath);
	    if (files.length == 1 && files[0].isFile()) {
	        fileSize = files[0].getSize();
	    }
	    return fileSize;
	}

	protected static boolean fileExists(FTPClient ftpClient,String filePath) throws Exception {
	    FTPFile[] files = ftpClient.listFiles(filePath);
	    return files.length == 1 && files[0].isFile();
	}

	protected static void createDirectory(FTPClient ftpClient,String filePath,boolean hasFileName) throws SocketException, IOException {
		String[] pathParts = filePath.split("[/\\\\]");

		StringBuilder constructedPath = new StringBuilder();

		//for avoiding last part if it has a file name.
		int loopSize = pathParts.length - ((hasFileName)? 1: 0);
		String fwSlash = "";

		for(int i=0; i<loopSize; i++) {
			String pathPart = pathParts[i];
			if(pathPart.trim().isEmpty()) {
				continue;
			}
			constructedPath.append(fwSlash + pathPart);
			ftpClient.makeDirectory(constructedPath.toString());
			fwSlash = String.valueOf(dirSeparator);
		}
	}

	protected FTPClient connectFTP() throws Exception {
		FTPClient ftpClient = new FTPClient();

		ftpClient.connect(getFtpServer());
		if(!ftpClient.login(getUserName(), getPassword())){
			throw new Exception("Unable to login\nFTP Error Msg:" + ftpClient.getReplyString());
		}

		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


		//Application from linux application server doesn't work on Active mode.
		ftpClient.enterLocalPassiveMode();

		return ftpClient;
	}

	public String getTempLocation() {
		return tempLocation;
	}

	public void setTempLocation(String tempLocation) {
		this.tempLocation = tempLocation;
	}

	public String getPermanentLocation() {
		return permanentLocation;
	}

	public void setPermanentLocation(String permanentLocation) {
		this.permanentLocation = permanentLocation;
	}

	public String getFtpServer() {
		return ftpServer;
	}

	public void setFtpServer(String ftpServer) {
		this.ftpServer = ftpServer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public Object doPostUploadActivitiy(String requestId, String filePath, String activity) throws Exception {
		//sub class override this method.
		return null;
	}

}
