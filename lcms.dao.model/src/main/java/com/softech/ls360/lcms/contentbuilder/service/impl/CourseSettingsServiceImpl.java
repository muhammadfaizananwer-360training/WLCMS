package com.softech.ls360.lcms.contentbuilder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.CourseSettingsDAO;
import com.softech.ls360.lcms.contentbuilder.model.CourseSettings;
import com.softech.ls360.lcms.contentbuilder.service.ICourseSettingsService;


/**
 * 
 * @author haider.ali
 *
 */
public class CourseSettingsServiceImpl implements ICourseSettingsService{

	@Autowired
	public CourseSettingsDAO courseSettingsDAO;

	
	@Override
	public CourseSettings save(CourseSettings courseSettings) {
		return courseSettingsDAO.save(courseSettings); 
	}

	@Override
	public CourseSettings getCourseSettings(CourseSettings courseSettings) {
		return courseSettingsDAO.getCourseSettings(courseSettings); 
	}

	@Override
	public CourseSettings update(CourseSettings courseSettings) {
		return courseSettingsDAO.update(courseSettings);
	}

	public CourseSettingsDAO getCourseSettingsDAO() {
		return courseSettingsDAO;
	}

	public void setCourseSettingsDAO(CourseSettingsDAO courseSettingsDAO) {
		this.courseSettingsDAO = courseSettingsDAO;
	}



}
