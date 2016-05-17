
package com.softech.ls360.lcms.contentbuilder.service;

import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.model.*;

import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public interface IClassroomCourseService {
    CourseVO getCourse(VU360UserDetail user, String businessKey, boolean deep);

    Collection<SyncClassDTO> getCourseClasses(VU360UserDetail user, String businessKey, boolean deep);

    SyncClassDTO getCourseClass(VU360UserDetail user, String businessKey, String className, boolean deep);

    Collection<SyncSessionDTO> getClassSessions(VU360UserDetail user, String businessKey, String className);

    SyncSessionDTO getClassSession(VU360UserDetail user, String businessKey, String className, String sessionKey);

    Collection<ClassInstructorDTO> getInstructors(VU360UserDetail user);

    ClassInstructorDTO getInstructor(VU360UserDetail user, String email);

    Collection<LocationDTO> getLocations(VU360UserDetail user);

    LocationDTO getLocation(VU360UserDetail user, String locationName);

    ClassroomImportDTO importCourses(VU360UserDetail user, byte[] fileData, boolean ignoreErrors) throws IOException, TabularDataException;
    void importCourses(VU360UserDetail user, ClassroomImportDTO classroom, boolean ignoreCourse) throws BulkUplaodCourseException;
    ClassroomImportDTO importCoursesToDtos(VU360UserDetail user, byte[] fileData, boolean ignoreErrors) throws IOException, TabularDataException;
}
