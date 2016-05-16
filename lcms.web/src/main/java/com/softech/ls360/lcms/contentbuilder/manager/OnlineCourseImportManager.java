package com.softech.ls360.lcms.contentbuilder.manager;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.interfaces.ICourseParser;
import com.softech.ls360.lcms.contentbuilder.model.Course;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.Lesson;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;

@Component
public class OnlineCourseImportManager extends AbstractCourseImportManager {
	
	private static Logger logger = LoggerFactory
			.getLogger(OnlineCourseImportManager.class);
	
	public OnlineCourseImportManager(){
		super(null, null, null, 0);
	}
	
	public OnlineCourseImportManager(String fileName, ICourseParser parser, ICourseService courseService,  int courseId){
		
		super(fileName, parser, courseService, courseId);
	}

	@Override
	public boolean importCourse() throws IOException, BulkUplaodCourseException {
	
		CourseDTO currentCourse = courseService.getCourseById(this.courseId);
		int streamingtemplateId = courseService.getStreamingTemplateId();
		boolean success = false;
			lessonList = (List<Lesson>) this.courseParser.parseCourse(fileName, streamingtemplateId);
			try{
				success = this.courseService.createLessonFromObject(lessonList, currentCourse);
			}catch(Exception e){
				throw new BulkUplaodCourseException("System error, data import failed. Please retry.", -1);
			}
		
		return success;
	}

	@Override
	public boolean createLesson() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createSlide() {
		// TODO Auto-generated method stub
		return false;
	}

}
