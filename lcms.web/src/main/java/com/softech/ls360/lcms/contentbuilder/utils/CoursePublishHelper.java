package com.softech.ls360.lcms.contentbuilder.utils;

import java.io.IOException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesLocator;
import com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoapStub;

public class CoursePublishHelper {
	private final static String serviceEndpoint = LCMSProperties.getLCMSProperty("lcms.webservice.client.asset.endpoint");
	
	public static boolean publishCourse(long courseId, String publishMethod, long loginAuthorId) throws IOException, ServiceException{
		LMSServicesSoapStub service = (LMSServicesSoapStub)new LMSServicesLocator().getLMSServicesSoap(new URL(serviceEndpoint));
		
		boolean success = service.publishCourseForLCMS_Web_QuickBuild(courseId, publishMethod, loginAuthorId);
				
		return success;
	}
}
