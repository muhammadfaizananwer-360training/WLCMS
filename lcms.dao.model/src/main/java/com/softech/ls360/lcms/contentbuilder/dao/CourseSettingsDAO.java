package com.softech.ls360.lcms.contentbuilder.dao;

import com.softech.ls360.lcms.contentbuilder.model.CourseSettings;


/**
 * 
 * @author haider.ali
 *
 */
public interface CourseSettingsDAO {

	
	public static final String CORUSE_SETTING_PROCEDURE = "[LCMS_WEB_SELECT_COURSESETTINGS]";
	public CourseSettings  save(CourseSettings courseSettings) ;
	public CourseSettings  update(CourseSettings courseSettings) ;
	public CourseSettings  getCourseSettings(CourseSettings courseSettings) ;

}
