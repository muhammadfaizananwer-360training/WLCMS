package com.softech.ls360.lcms.contentbuilder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.CourseProviderDAO;
import com.softech.ls360.lcms.contentbuilder.dao.LocationDAO;
import com.softech.ls360.lcms.contentbuilder.model.CourseProvider;
import com.softech.ls360.lcms.contentbuilder.service.ICourseProviderService;

public class CourseProviderServiceImpl implements ICourseProviderService{

	
	@Autowired
	public CourseProviderDAO courseProviderDAO;
	
	@Override
	public CourseProvider save(CourseProvider courseProvider) {
		
		return courseProviderDAO.save(courseProvider); 
	}

	@Override
	public CourseProvider loadProviderbyCourseId(Long courseId) {
		
		return courseProviderDAO.loadProviderbyCourseId(courseId);
		
	}
	
	
	
	
	

}
