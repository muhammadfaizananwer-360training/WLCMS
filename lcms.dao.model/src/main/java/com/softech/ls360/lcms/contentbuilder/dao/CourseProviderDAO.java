package com.softech.ls360.lcms.contentbuilder.dao;

import com.softech.ls360.lcms.contentbuilder.model.CourseProvider;

public interface CourseProviderDAO  extends GenericDAO<CourseProvider> {
	
	public CourseProvider save(CourseProvider course);
	public CourseProvider loadProviderbyCourseId(Long courseId);
}
