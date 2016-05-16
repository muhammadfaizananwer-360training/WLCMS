package com.softech.ls360.lcms.contentbuilder.service;


import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.SynchronousClass;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousSession;
import com.softech.ls360.lcms.contentbuilder.model.TimeZone;

public interface ISynchronousClassService {

	public SynchronousClass saveSynchronousClass (SynchronousClass syncClass);
	public List<TimeZone> getAllTimezone (TimeZone timezone);
	public SynchronousClass getSynchronousClassById (long synchronousClassId);
	
	public List <SynchronousClass> getSynchronousClassByCourseId (long courseId);
	public List <SynchronousClass> getDeletedandUnDeletedSynchronousClassByCourseId (long courseId);
	
	public SynchronousClass getSynchronousClassWithLocationInfo (long syncClassId);
	public TimeZone getTimeZoneById(long id);
	public void deleteSession(String commaseparatedIds) ;
	public int deleteClassroom(long id);
	//public List<SynchronousSession> getSessionsbyStatus(String status,Long classId);


}
