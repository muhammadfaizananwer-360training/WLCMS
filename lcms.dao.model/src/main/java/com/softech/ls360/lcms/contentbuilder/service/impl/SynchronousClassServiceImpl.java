package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.CourseDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SynchronousClassDAO;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousClass;
import com.softech.ls360.lcms.contentbuilder.model.TimeZone;
import com.softech.ls360.lcms.contentbuilder.service.ISynchronousClassService;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;

public class SynchronousClassServiceImpl implements ISynchronousClassService{
	
	@Autowired
	public SynchronousClassDAO syncClassDAO;
	
	@Autowired
	public CourseDAO courseDAO;

	@Override
	public SynchronousClass saveSynchronousClass (SynchronousClass syncClass){
		SynchronousClass result = syncClassDAO.saveClass(syncClass);
		modifyCourseUpdatedDate(result.getId());
		return result;
	}
	
	@Override
	public List<TimeZone> getAllTimezone (TimeZone timezone){
		return syncClassDAO.getAllTimezone(timezone);
	}

	public SynchronousClassDAO getSyncClassDAO() {
		return syncClassDAO;
	}

	public void setSyncClassDAO(SynchronousClassDAO syncClassDAO) {
		this.syncClassDAO = syncClassDAO;
	}
	
	@Override
	public SynchronousClass getSynchronousClassById (long synchronousClassId){
		return syncClassDAO.getSynchronousClassById(synchronousClassId);
	}
	
	@Override
	public List <SynchronousClass> getSynchronousClassByCourseId (long courseId){
		return syncClassDAO.getSynchronousClassByCourseId(courseId);
	}
	
	@Override
	public List <SynchronousClass> getDeletedandUnDeletedSynchronousClassByCourseId (long courseId){
		return syncClassDAO.getDeletedandUnDeletedSynchronousClassByCourseId(courseId);
	}
	
	@Override
	public SynchronousClass getSynchronousClassWithLocationInfo (long syncClassId){
		return syncClassDAO.getSynchronousClassWithLocationInfo(syncClassId);
	}
	
	@Override
	public TimeZone getTimeZoneById(long id){
		return syncClassDAO.getTimeZoneById(id);
	}
	
	@Override
	public void deleteSession(String commaseparatedIds) {
		ObjectWrapper<Long> classIdWrapper = new ObjectWrapper<Long>();
		syncClassDAO.deleteSession(commaseparatedIds,classIdWrapper);
		modifyCourseUpdatedDate(classIdWrapper.getValue());
	}
	
	@Override
	public int deleteClassroom(long id) {
		// TODO Auto-generated method stub
		int result = syncClassDAO.deleteClassroom(id);
		modifyCourseUpdatedDate(id);
		return result;
	}
	
	private void modifyCourseUpdatedDate(Long classId) {
		CourseDTO course = courseDAO.getById(syncClassDAO.getById(classId).getCourse().getId());
		course.setLastUpdatedDate(new Date());
		courseDAO.updateCourse(course);
	}

}
