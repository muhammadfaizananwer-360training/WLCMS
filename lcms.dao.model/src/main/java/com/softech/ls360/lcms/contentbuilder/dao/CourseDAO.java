package com.softech.ls360.lcms.contentbuilder.dao;

 
import java.sql.SQLException;
import java.util.List;


import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.Course;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.CourseDisplayOrder;
import com.softech.ls360.lcms.contentbuilder.model.CourseGroup;
import com.softech.ls360.lcms.contentbuilder.model.LearningObject;
import com.softech.ls360.lcms.contentbuilder.model.SearchCourseFilter;
import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.model.SlideTemplate;
import java.util.Collection;

public interface CourseDAO extends GenericDAO<CourseDTO> {
	
	public CourseDTO getCourseByID (int id) throws SQLException;
	public List<CourseDTO> searchCourses (SearchCourseFilter filter) throws SQLException;
	public boolean updateCourse(Course thisCourse) throws SQLException;
	public int addCourse(Course thisCourse) throws SQLException;
	public boolean addScene(String name,long contentOwner_Id,String sceneGuid,long course_Id,long createUser_Id,int asset_Id) throws SQLException;
	public boolean deleteScene(long course_Id) throws SQLException;
	
	// This method is temperary bases to populate courses dropdown.
		// TODO remove this method after build 1.1.1 release
	public List<Course> getCoursesList(int AuthorId) throws SQLException;
	
	
	public List<ContentObject> getContentObjectList(int CourseID);	
	public ContentObject addLesson (ContentObject co);
	public List<Slide> getSlidesByContentObject(long varContendOjectId) throws SQLException;
	public Object[] getContentObject(long varContendOjectId) throws SQLException;
	
	public boolean updateContentObject(ContentObject co) throws SQLException;
	
	ContentObject getContentObject2(long varContendOjectId) throws SQLException;
	
	public boolean deleteLesson (String varLessonId, String courseId,long loginUserId) throws Exception;
	
	List<LearningObject> getContentLearningObject (int contentID) throws SQLException;
	
	public boolean deleteLLO (int id, int contentObjectId) throws Exception;
	public boolean isFinalExamEnabled(int courseID)	throws Exception;
	public List<SlideTemplate> getSlideTemplateID(int contentOwnerId)	throws Exception;
	public Integer getPublishedVersion(String courseId) throws Exception;
	
	public CourseDTO saveCourse(CourseDTO entity);
	public CourseDTO getCourseById(long courseId);
	public List<CourseDisplayOrder> getCourseDisplay(long courseId) throws Exception;
	//public boolean UpdateCourseDisplayOrder(CourseDisplayOrder courseDisplayOrder,long courseId,long lastUpdateUser) throws Exception;
	boolean UpdateCourseDisplayOrder(CourseDisplayOrder courseDisplayOrder,
			long course_Id, long lastUpdateUser) throws Exception;
	
	public CourseDTO updateCourse (CourseDTO course);
	public int addRecentCourse(long courseId);
	
	public List<CourseGroup> getCourseGroupByContentOwner (long contentOwnerId, String courseGroupName);
	public boolean updateCourseModifiedDate(long courseId);	
        public List<CourseDTO> getCoursesByOwnerIdAndBusinessKey(Integer ownerId, String businessKey);
        public CourseDTO getCourseByOwnerIdAndBusinessKey(Integer ownerId, String businessKey);
        public List<CourseDTO> getCoursesByOwnerIdAndBusinessKeys(Integer ownerId, Collection<String> businessKeys);
   	public int getStreamingTemplateId();
   	public boolean updateCourseLastUpdateUserandModifiedDate(long lastUpdateUser,long courseId);
}
