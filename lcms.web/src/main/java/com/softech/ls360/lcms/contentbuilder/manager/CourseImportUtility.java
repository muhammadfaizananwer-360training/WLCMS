package com.softech.ls360.lcms.contentbuilder.manager;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.factory.CourseImportManagerFactory;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;

@Component
public class CourseImportUtility {
	
	private static Logger logger = LoggerFactory
			.getLogger(CourseImportUtility.class);
	private static char dirSeparator = '/';
	
	public CourseImportUtility(){
		
	}

	public static void main(String[] args){
		
	}
	
	public void processCourseImport(String fileName, int courseTypeId, ICourseService courseService, int courseId) throws IOException, BulkUplaodCourseException{
		//fileName = "D://Original.xlsx";
		fileName = FileUtils.getTempDirectoryPath() + dirSeparator + fileName;
		AbstractCourseImportManager courseImportManager = CourseImportManagerFactory.getCourseImportManager(fileName, courseTypeId, courseService, courseId);
		courseImportManager.importCourse();
	}
}
