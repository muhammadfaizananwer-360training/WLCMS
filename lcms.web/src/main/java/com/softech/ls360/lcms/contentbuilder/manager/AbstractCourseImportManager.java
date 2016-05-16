package com.softech.ls360.lcms.contentbuilder.manager;

import java.io.IOException;
import java.util.List;

import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.interfaces.ICourseParser;
import com.softech.ls360.lcms.contentbuilder.model.Lesson;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;

public abstract class AbstractCourseImportManager {

	protected ICourseParser courseParser;
	protected String fileName;
	protected List<Lesson> lessonList;
	protected int courseId;
	
	//@Autowired
	ICourseService courseService;
	
	
	public ICourseParser getCourseParser() {
		return courseParser;
	}

	public void setCourseParser(ICourseParser courseParser) {
		this.courseParser = courseParser;
	}

	protected AbstractCourseImportManager(String fileName, ICourseParser parser, ICourseService service, int courseId	) {
		this.fileName = fileName;
		this.courseParser = parser;
		this.courseService = service;
		this.courseId = courseId;
	}
	
	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	public abstract boolean importCourse() throws IOException, BulkUplaodCourseException;
	public abstract boolean createLesson();
	public abstract boolean createSlide();
}
