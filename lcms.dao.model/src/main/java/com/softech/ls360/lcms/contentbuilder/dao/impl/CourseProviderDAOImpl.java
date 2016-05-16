package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.CourseProviderDAO;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.model.CourseProvider;

public class CourseProviderDAOImpl extends GenericDAOImpl<CourseProvider> implements CourseProviderDAO{

	@Override
	@Transactional
	public CourseProvider save(CourseProvider course) {
		
		try{
			entityManager.merge(course);
		}catch(Exception es){
			es.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional
	public CourseProvider loadProviderbyCourseId(Long courseId) {
		
		 Map<String, Object> queryParams = new HashMap();
		 queryParams.put("courseId", courseId);
		 StringBuilder query = new StringBuilder("from CourseProvider cp where cp.course.id = :courseId");
		 List<CourseProvider> courseProviders = (List)getResultList(query.toString(),queryParams);
		 
		 return (courseProviders==null || courseProviders.size()<=0)?null:(CourseProvider)courseProviders.get(0);
	}
	
	

	
	

}
