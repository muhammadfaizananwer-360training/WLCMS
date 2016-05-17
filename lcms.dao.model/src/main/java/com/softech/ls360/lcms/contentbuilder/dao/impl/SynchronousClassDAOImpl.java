package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SynchronousClassDAO;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousClass;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousSession;
import com.softech.ls360.lcms.contentbuilder.model.TimeZone;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;
import com.softech.ls360.lcms.contentbuilder.utils.SynchronousClassSessionStatusEnum;

public class SynchronousClassDAOImpl extends GenericDAOImpl<SynchronousClass> implements SynchronousClassDAO{
	
	@Override
	@Transactional
	public SynchronousClass saveClass(SynchronousClass syncClass)
	{
			syncClass = super.save(syncClass);
			return syncClass;
		
	}
	
	@Override
	@Transactional
	public List<TimeZone> getAllTimezone(TimeZone entity)
	{
		Query query = entityManager.createNamedQuery("Timezone.getAllTimeZone",TimeZone.class);
		
	//	query.setParameter("VU360USER_ID", 200616);
		List<TimeZone> resultList = query.getResultList();
		return CollectionUtils.isEmpty(resultList) ? null : resultList;
	}
	
	
	@Override
	@Transactional
	public SynchronousClass getSynchronousClassById (long synchronousClassId){
		// TODO Auto-generated method stub
		SynchronousClass synchronousClass = super.getById(synchronousClassId);
		return synchronousClass;
	}
	
	@Override
	@Transactional
	public List <SynchronousClass> getSynchronousClassByCourseId (long courseId){
		
		Query query = entityManager.createQuery(" SELECT sc FROM SynchronousClass sc   "
												+ " WHERE sc.course.id=:COURSE_ID and sc.deleted=true");
		
		query.setParameter("COURSE_ID", courseId);
		List <SynchronousClass> syncCourse = (List<SynchronousClass>)query.getResultList();
		
		return syncCourse;
		
	}
        
    @Override
    @Transactional
    public SynchronousClass getSynchronousClassByCourseIdAndClassName(long courseId, String className) {
        Query query = entityManager.createQuery(" SELECT sc FROM SynchronousClass sc   "
                + " WHERE sc.course.id=:COURSE_ID and sc.className =:className and sc.deleted=true");

        query.setParameter("COURSE_ID", courseId);
        query.setParameter("className", className);
        List<SynchronousClass> classes = (List<SynchronousClass>) query.getResultList();

        if (!classes.isEmpty()) {
            return classes.get(0);
        }

        return null;

    }
    
    
    @Override
    @Transactional
    public SynchronousClass getSynchronousClassByOwnerAndCourseAndClassName(Integer ownerId,String courseBusinessKey, String className) {
        Query query = entityManager.createQuery(" SELECT sc FROM SynchronousClass sc   "
                + " WHERE sc.course.bussinesskey=:courseBusinessKey and sc.course.contentownerId=:ownerId and sc.className =:className and sc.deleted=true");

        query.setParameter("ownerId", ownerId);
        query.setParameter("courseBusinessKey", courseBusinessKey);
        query.setParameter("ownerId", ownerId);
        query.setParameter("className", className);
        List<SynchronousClass> classes = (List<SynchronousClass>) query.getResultList();

        if (!classes.isEmpty()) {
            return classes.get(0);
        }

        return null;

    }
    
    
    @Override
    @Transactional
    public SynchronousSession getSynchronousSessionBy(Integer ownerId,String courseBusinessKey, String className,Date startTime,Date endTime) {
        Query query = entityManager.createQuery(" SELECT ss FROM SynchronousSession ss   "
                + " WHERE ss.syncClass.course.bussinesskey=:courseBusinessKey and ss.syncClass.course.contentownerId=:ownerId and ss.syncClass.className =:className" 
                + " and ss.startDateTime = :startTime and ss.endDateTime = :endTime"
                + " and ss.syncClass.deleted=true");

        query.setParameter("ownerId", ownerId);
        query.setParameter("courseBusinessKey", courseBusinessKey);
        query.setParameter("ownerId", ownerId);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        query.setParameter("className", className);
        List<SynchronousSession> sessions = (List<SynchronousSession>) query.getResultList();

        if (!sessions.isEmpty()) {
            return sessions.get(0);
        }

        return null;

    }

	@Override
	@Transactional
	public SynchronousSession getSynchronousSessionBy(Integer ownerId,String courseBusinessKey, String className,Long sessionId) {
		Query query = entityManager.createQuery(" SELECT ss FROM SynchronousSession ss   "
				+ " WHERE ss.syncClass.course.bussinesskey=:courseBusinessKey and ss.syncClass.course.contentownerId=:ownerId and ss.syncClass.className =:className"
				+ " and ss.id = :sessionId"
				+ " and ss.syncClass.deleted=true");

		query.setParameter("ownerId", ownerId);
		query.setParameter("courseBusinessKey", courseBusinessKey);
		query.setParameter("ownerId", ownerId);
		query.setParameter("sessionId", sessionId);
		query.setParameter("className", className);
		List<SynchronousSession> sessions = (List<SynchronousSession>) query.getResultList();

		if (!sessions.isEmpty()) {
			return sessions.get(0);
		}

		return null;

	}
	
	@Override
	@Transactional
	public List <SynchronousClass> getDeletedandUnDeletedSynchronousClassByCourseId (long courseId){
		
		Query query = entityManager.createQuery(" SELECT sc FROM SynchronousClass sc   "
				+ " WHERE sc.course.id=:COURSE_ID ");

		query.setParameter("COURSE_ID", courseId);
		List <SynchronousClass> syncCourse = (List<SynchronousClass>)query.getResultList();

		return syncCourse;
	}
	
	@Override
	@Transactional
	public SynchronousClass getSynchronousClassWithLocationInfo (long syncClassId){
		Query query = entityManager.createQuery(" SELECT sc FROM SynchronousClass sc  join sc.location l "
												+ " WHERE sc.id=:CLASS_ID");
		
		query.setParameter("CLASS_ID", syncClassId);
		SynchronousClass syncCourse =null;
		try{
			syncCourse = (SynchronousClass)query.getSingleResult();
		}catch (NoResultException nre){
			//Ignore this because as per logic this is ok!
		}
		//syncCourse.setLocation(syncCourse.getLocation());
		return syncCourse;
		
	}

	
	@Override
	@Transactional
	public TimeZone getTimeZoneById(long id){
	
		Query query = entityManager.createNamedQuery("Timezone.getTimeZoneById", TimeZone.class);
		query.setParameter("ID", id);
		
		TimeZone timeZone = (TimeZone)query.getSingleResult();
		return timeZone;
	}

	@Override
	@Transactional
	public void deleteSession(String commaseparatedIds,ObjectWrapper<Long> classIdWrapper) {
		
		/*
		SynchronousClass obj = new SynchronousClass();
		SynchronousSession synchronousSession = new SynchronousSession();
		
		String[] arr = commaseparatedIds.split(",");
		long sessionId = Long.parseLong(arr[0]);
		Query query = entityManager.createQuery("select syncClass.id from SynchronousSession where id=:sessionId");
		query.setParameter("sessionId", sessionId);
		long classId = (Long)query.getResultList().get(0);
		obj= getSynchronousClassById(classId);
		//obj = getSynchronousClassById(classId);
		obj.setUpdateDate(new Date());
		obj.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
		//List<SynchronousSession> sessionList = new ArrayList<SynchronousSession>(obj.getSyncSession());
		Set<SynchronousSession> sessionList = new HashSet<SynchronousSession> (); 
		for(int i=0; i<arr.length; i++){
			 synchronousSession.setId(new Long(arr[i]));
			 synchronousSession.setStatus(SynchronousClassSessionStatusEnum.DELETE.getStatus());
			 synchronousSession.setUpdateDate(new Date());
			 synchronousSession.setSyncClass(obj);
			 sessionList.add(synchronousSession);
			 synchronousSession=null;
			 synchronousSession = new SynchronousSession();
		}
		obj.setSyncSession(sessionList);
		saveClass(obj);*/
		
		String[] arr = commaseparatedIds.split(",");
		List<Long> ids= new ArrayList<Long>();
		for(int i=0; i<arr.length; i++)
			ids.add(Long.parseLong(arr[i]));
		long sessionId = Long.parseLong(arr[0]);
		Query queryClassId = entityManager.createQuery("select syncClass.id from SynchronousSession where id=:sessionId");
		queryClassId.setParameter("sessionId", sessionId);
		long classId = (Long)queryClassId.getResultList().get(0);
		classIdWrapper.setValue(classId);
		
		
		Query queryUpdateSession = entityManager.createQuery("update SynchronousSession set status=:delete,updateDate=:updateDate where id in :commaseparatedIds");
		queryUpdateSession.setParameter("commaseparatedIds", ids);
		queryUpdateSession.setParameter("updateDate", new Date());
		queryUpdateSession.setParameter("delete", SynchronousClassSessionStatusEnum.DELETE.getStatus());
		queryUpdateSession.executeUpdate();
		
		Query queryClass = entityManager.createQuery("update SynchronousClass set status=:update,updateDate=:updateDate where id in :classId");
		queryClass.setParameter("classId", classId);
		queryClass.setParameter("updateDate", new Date());
		queryClass.setParameter("update", SynchronousClassSessionStatusEnum.UPDATE.getStatus());
		queryClass.executeUpdate();
	}
	@Override
	@Transactional
	public int deleteClassroom(long id) {
		// TODO Auto-generated method stub
		//SynchronousClass obj = new SynchronousClass();
		SynchronousClass obj = getSynchronousClassById(id);
		obj.setDeleted(false);
		obj.setUpdateDate(new Date());
		obj.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
		List<SynchronousSession> sessionList = new ArrayList<SynchronousSession>(obj.getSyncSession());
		for(SynchronousSession  synchronousSession : sessionList){
				 synchronousSession.setStatus(SynchronousClassSessionStatusEnum.DELETE.getStatus());
				 synchronousSession.setUpdateDate(new Date());
				 //synchronousSession.setUpdateDate(new Date());
		}
		 
		 saveClass(obj);
		
		//Query synchronousIds =  entityManager.createQuery("from SynchronousSession where syncClass.id in :id");
		/*synchronousIds.setParameter("id", id);
		List<SynchronousSession> sessionList = synchronousIds.getResultList(); 
		if(sessionList != null && sessionList.size() >0){
			List<Long> sessionIds=new ArrayList();
			for(SynchronousSession synchronousSession : sessionList){
				sessionIds.add(synchronousSession.getId());
			}
			
			Query synSessionDeleteQuery = entityManager.createQuery("delete from SynchronousSession where id in :params");
			synSessionDeleteQuery.setParameter("params", sessionIds);
			synSessionDeleteQuery.executeUpdate();
		}
		Query synClassDeleteQuery = entityManager.createQuery("delete from SynchronousClass where id in :id");
		synClassDeleteQuery.setParameter("id", id);
		synClassDeleteQuery.executeUpdate();*/
		
		
		
		return 1;
		
		
	}
	@Override
	public boolean IsInstructorExist(List<Long> instructorIds) {
		Query query = entityManager.createNativeQuery("select id from SynchronousClass where CLASSINSTRUCTOR_ID in (:instructorIds)");
		query.setParameter("instructorIds", instructorIds);
		List lst = query.getResultList();

		return lst.size()>0?true:false;
	}
	
	
	
	/*@Override
	public List<SynchronousSession> getSessionbyStatusandClassId(String status,Long classId){
		
		Map<String,Object> sessionMap = new HashMap();
		sessionMap.put("status", status);
		sessionMap.put("syncClass", status);
		
		SynchronousSession synchronousSession2 = entityManager.
		
		
		
	}*/
}
