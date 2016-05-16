package com.softech.ls360.lcms.contentbuilder.service;

import com.softech.ls360.lcms.contentbuilder.model.CourseSettings;

/**
 * 
 * @author haider.ali
 *
 */
public interface ICourseSettingsService {
	
	public CourseSettings save(CourseSettings courseSettings) ;
	public CourseSettings update(CourseSettings courseSettings) ;
	public CourseSettings getCourseSettings(CourseSettings courseSettings) ;
	
}
