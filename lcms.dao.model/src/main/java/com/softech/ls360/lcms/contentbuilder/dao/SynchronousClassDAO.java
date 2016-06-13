package com.softech.ls360.lcms.contentbuilder.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;







import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.model.Course;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousSession;
import com.softech.ls360.lcms.contentbuilder.model.TimeZone;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousClass;
import java.util.Date;

public interface SynchronousClassDAO extends GenericDAO<SynchronousClass> {

	@Transactional
	public SynchronousClass saveClass(SynchronousClass entity);
	//public CourseDTO getCourseById(long courseId);
	
	@Transactional
	public List<TimeZone> getAllTimezone(TimeZone entity);
	
	
	public SynchronousClass getSynchronousClassById (long synchronousClassId);
	
	public List <SynchronousClass> getSynchronousClassByCourseId (long courseId);
        public SynchronousClass getSynchronousClassByCourseIdAndClassName (long courseId,String className);

	@Transactional
	List<SynchronousClass> getSynchronousClassByCourseIdAndClassNames(long courseId, Collection<String> classNames);

	SynchronousClass getSynchronousClassByOwnerAndCourseAndClassName(Integer ownerId, String courseBusinessKey, String className);
        public SynchronousSession getSynchronousSessionBy(Integer ownerId,String courseBusinessKey, String className,Date startTime,Date endTime);

	@Transactional
	SynchronousSession getSynchronousSessionBy(Integer ownerId, String courseBusinessKey, String className, Long sessionId);

	@Transactional
	List<SynchronousSession> getSyncSessionsByClassIdAndSessionKeys(Long classId, Collection<String> sessionKeys);

	@Transactional
	SynchronousSession getSyncSessionsByClassIdAndSessionKey(Long classId, String sessionKey);

	public List <SynchronousClass> getDeletedandUnDeletedSynchronousClassByCourseId (long courseId);
	
	public SynchronousClass getSynchronousClassWithLocationInfo (long syncClassId);
	public TimeZone getTimeZoneById(long id);
	public void deleteSession(String commaseparatedIds,ObjectWrapper<Long> classIdWrapper);
	public int deleteClassroom(long id);
	public boolean IsInstructorExist(List<Long> instructorIds);
	//public SynchronousSession updateSessions(SynchronousSession session);
	//public List<SynchronousSession> getSessionsbyStatus(String status,Long classId);
	
}
