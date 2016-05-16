package com.softech.ls360.lcms.contentbuilder.factory;

import com.softech.ls360.lcms.contentbuilder.manager.AbstractCourseImportManager;
import com.softech.ls360.lcms.contentbuilder.manager.ExcelCourseParser;
import com.softech.ls360.lcms.contentbuilder.manager.OnlineCourseImportManager;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.utils.CourseType;

public class CourseImportManagerFactory {
	
	private static AbstractCourseImportManager instance = null;
	
	public static AbstractCourseImportManager getCourseImportManager(String fileName, int courseTypeId, ICourseService courseService, int courseId){
		
		CourseType courseType = CourseType.getById(courseTypeId);
		
		switch(courseType){
			case ONLINE_COURSE:
//				instance = (AbstractCourseImportManager) context.getBean("OnlineCourseImportManager.class");
//				instance.setCourseParser(new ExcelCourseParser());
				instance =  new OnlineCourseImportManager(fileName, new ExcelCourseParser(), courseService, courseId);
				break;
			case CLASSROOM_COURSE:
				//courseImportManager = new ClassroomCourseImportManager(fileName, new ExcelCourseParser());
				break;
			case WEBINAR_COURSE:
				//courseImportManager = new WebinarCourseImportManager(fileName, new ExcelCourseParser());
				break;
			default:
		}
		
		return instance;
	}
	
	public CourseImportManagerFactory(){
		
	}
}
