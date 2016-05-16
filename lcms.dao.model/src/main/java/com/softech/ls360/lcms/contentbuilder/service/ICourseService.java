package com.softech.ls360.lcms.contentbuilder.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.Course;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.CourseGroup;
import com.softech.ls360.lcms.contentbuilder.model.Lesson;
import com.softech.ls360.lcms.contentbuilder.model.SearchCourseFilter;
import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.model.SlideTemplate;


public interface ICourseService {
	public int addCourse (Course myCourse) throws SQLException;
	public boolean editCourse ();
	//public List<Course> getCourse (String courseName);
	//public List<Course> getCourse (Course myCourse);
	public CourseDTO getCourse(int id) throws SQLException;
	public boolean updateCourse (Course myCourse) throws SQLException;
	public List<CourseDTO> searchCourses(SearchCourseFilter filter) throws SQLException;
	public boolean addScene(String name,long contentOwner_Id,String sceneGuid,long course_Id,long createUser_Id,int asset_Id) throws SQLException;
	public boolean deleteScene(long course_Id) throws SQLException;
	
	// This method is temperary bases to populate courses dropdown.
	// TODO remove this method after build 1.1.1 release
	public List<Course> getCoursesList(int AuthorId) throws SQLException;
	
	public List<ContentObject> getContentObjectList(int courseID) throws SQLException;
	
	public ContentObject addLesson (ContentObject co);
	public List<Slide> getSlidesByContentObject(long varContendOjectId) throws SQLException;
	
	public ContentObject getContentObject(long varContendOjectId) throws Exception;
	public boolean updateContentObject(ContentObject co) throws Exception;
	ContentObject getContentObject2(long varContendOjectId) throws SQLException;
	public boolean deleteLesson (String varLessonId, String courseId,long loginUserId) throws Exception;
	
	public boolean deleteLLO (int id, int contentObjectId )throws Exception;
	public boolean isFinalExamEnabled(int courseID)	throws Exception;
	public List<SlideTemplate> getSlideTemplateID(int contentOwnerId)	throws Exception;
	public Integer getPublishedVersion(String courseId) throws Exception;
	
	public CourseDTO saveCourse (CourseDTO entity);
	public CourseDTO getCourseById(long courseId);
	boolean setCourseDisplayOrder(long courseId, int item_Id, String item_type,
			int direction, long lastModifiedUser);
	public CourseDTO updateCourse (CourseDTO course);
	
	public List<CourseGroup> getCourseGroupByContentOwner (long contentOwnerId, String courseGroupName);
	public boolean updateCourseModifiedDate(long courseId);
	public boolean createLessonFromObject(List<Lesson> lessonList, CourseDTO course) throws SQLException;	
	public int getStreamingTemplateId();
	public boolean updateCourseLastUpdateUserandModifiedDate(long lastUdpateUser,long courseId);
}
