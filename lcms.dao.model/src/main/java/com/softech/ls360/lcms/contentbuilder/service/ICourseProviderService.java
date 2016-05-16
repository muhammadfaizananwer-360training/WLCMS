package com.softech.ls360.lcms.contentbuilder.service;

import com.softech.ls360.lcms.contentbuilder.model.CourseProvider;

public interface ICourseProviderService {
	
	public CourseProvider save(CourseProvider courseProvider);
	public CourseProvider loadProviderbyCourseId(Long courseId);

}
