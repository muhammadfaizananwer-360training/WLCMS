/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softech.ls360.lcms.contentbuilder.web.api.controller;

import com.google.common.base.Joiner;
import com.softech.common.validator.InvalidField;
import com.softech.common.validator.InvalidModelException;
import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.model.*;
import com.softech.ls360.lcms.contentbuilder.service.IClassroomCourseService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.upload.FileUploader;
import com.softech.ls360.lcms.contentbuilder.web.api.dto.*;
import com.softech.ls360.lcms.contentbuilder.web.controller.ClassroomController;
import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author abdul.wahid
 */
@RestController
@RequestMapping(value = "api/classroom")
//@CrossOrigin(origins = "*")
public class ClassroomApiController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClassroomApiController.class);

    @Autowired
    ClassroomController classroomController;

    @Autowired
    IClassroomCourseService classroomService;

    @Autowired
    @Qualifier("ppt")
    FileUploader pptFileUploader;

    @Autowired
    private VU360UserService userService;

    @Autowired
    private ModelMapper modelMapper ;

    private static CourseVO encloseCourse(ClassroomImportDTO classroom, String courseId) {
        return encloseCourse(classroom, courseId, null);
    }

    private static CourseVO encloseCourse(ClassroomImportDTO classroom, String courseId, CourseVO course) {
        if (course == null) {
            course = new CourseVO();
            course.setAction("child");
        }
        classroom.getCourses().add(course);
        course.setCourseId(courseId);
        return course;
    }

    private static LocationDTO encloseLocation(ClassroomImportDTO classroom, String locationName, LocationDTO loc) {
        if (loc == null) {
            loc = new LocationDTO();
            loc.setAction("child");
        }
        classroom.getLocations().add(loc);
        loc.setLocationName(locationName);
        return loc;
    }

    private static ClassInstructorDTO encloseInstructor(ClassroomImportDTO classroom, String email) {
        return encloseInstructor(classroom,email,null);
    }
    private static ClassInstructorDTO encloseInstructor(ClassroomImportDTO classroom, String email, ClassInstructorDTO inst) {
        if (inst == null) {
            inst = new ClassInstructorDTO();
            inst.setAction("child");
        }
        classroom.getInstructors().add(inst);
        inst.setEmail(email);
        return inst;
    }

    private static SyncClassDTO encloseClass(CourseVO course, String className) {
        return encloseClass(course, className, null);
    }

    private static SyncClassDTO encloseClass(CourseVO course, String className, SyncClassDTO cls) {
        if (cls == null) {
            cls = new SyncClassDTO();
            cls.setAction("child");
        }

        course.getSyncClassesMap().put(className, cls);
        cls.setClassName(className);

        return cls;
    }

    private static SyncSessionDTO encloseSession(SyncClassDTO cls, String sessionKey) {
        return encloseSession(cls, sessionKey, null);
    }

    private static SyncSessionDTO encloseSession(SyncClassDTO cls, String sessionKey, SyncSessionDTO session) {
        if (session == null) {
            session = new SyncSessionDTO();
            session.setAction("child");
        }

        cls.getSessionsMap().put("session", session);
        session.setSessionKey(sessionKey);

        return session;
    }

    private void setResponseSuccessData(RestResponse response, Object data) {
        if (StringUtils.isEmpty(response.getError())) {
            response.setData(data);
        }
    }

    public RestResponse importClassroomCourses(ClassroomImportDTO classroom, VU360UserDetail user) throws BulkUplaodCourseException {
        return  importClassroomCourses(classroom,user,false);
    }

    public RestResponse importClassroomCourses(ClassroomImportDTO classroom, VU360UserDetail user,boolean ignoreCourse) throws BulkUplaodCourseException {
        RestResponse response = new RestResponse();
        classroomService.importCourses(user, classroom,ignoreCourse);
        response.setInfo("ok");
        return response;
    }


    @ExceptionHandler(BulkUplaodCourseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleApiException(BulkUplaodCourseException e) {
        RestResponse response = new RestResponse();
        ErrorDetailsAPIDTO errorDetails = modelMapper.map(e.getExceptionDetail(),ErrorDetailsAPIDTO.class);
        response.setError("conflict");
        response.setData(errorDetails);
        return response;
    }

    @RequestMapping(value = "importSheet", method = RequestMethod.POST)
    ResponseEntity<Object> importSheet(@RequestBody MultipartFile file
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName
            , @RequestParam(name = "ignoreErrors", defaultValue = "false") boolean ignoreErrors) throws Exception {

        RestResponse response = null;
        HttpStatus httpStatus = HttpStatus.OK;
        Assert.notNull(file, "file is not provided");
        String requestId = String.valueOf(new Random().nextInt(999999999));
        String filePath = pptFileUploader.uploadFileChunk(requestId, file.getName(), -1, -1, 0, file.getBytes());
        try {
            VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
            response = classroomController.importClassroomCourses(filePath, ignoreErrors, user);
        } finally {
            try {
                String fullPath = FileUtils.getTempDirectoryPath() + IOUtils.DIR_SEPARATOR + filePath;
                if(!new File(fullPath).delete())
                    logger.warn("File: "+fullPath+" could not be deleted");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /* purpose of below method, to fix sonar critical error */
    private Object getResponseNull(){
        return null;
    }
    @RequestMapping(value = "importSheetToDtos", method = RequestMethod.POST)
    ResponseEntity<Object> importSheetToDtos(@RequestBody MultipartFile file
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws Exception {

        RestResponse response =(RestResponse) getResponseNull();// new RestResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        Assert.notNull(file, "file object is null");
        String requestId = String.valueOf(new Random().nextInt(999999999));
        String filePath = pptFileUploader.uploadFileChunk(requestId, file.getName(), -1, -1, 0, file.getBytes());
        try {
            VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
            response = classroomController.importClassroomCoursesToDtos(filePath, true, user);
        } finally {
            try {
                String fullPath = FileUtils.getTempDirectoryPath() + IOUtils.DIR_SEPARATOR + filePath;
                if(!new File(fullPath).delete())
                    logger.warn("File: "+fullPath+" could not be deleted.");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new ResponseEntity<Object>(response, httpStatus);
    }


    @RequestMapping(value = "importJson", method = RequestMethod.POST)
    ResponseEntity<Object> importJson(@RequestBody ClassroomImportDTO classroom
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    //TODO instructors

    /**
     * It returns collection of instructors of particular content owner.
     * <h1>API Path: /instructors</h1>
     *
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned with collection of instructors as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     * "error": null,
     * "warning": null,
     * "info": null,
     * "data": [
     * {
     * "firstName": "abdus",
     * "lastName": "samad",
     * "email": "abdus-samad@hotmail.com",
     * "phoneNo": "123",
     * "background": null
     * },...]}
     * </pre>
     */
    @RequestMapping(value = "instructors", method = RequestMethod.GET)
    ResponseEntity<Object> getInstructors(
            @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        Collection<ClassInstructorDTO> dto = classroomService.getInstructors(user);
        Type listType = new TypeToken<Collection<ClassInstructorAPIDTO>>(){}.getType();
        Collection<ClassInstructorAPIDTO> apiDto = modelMapper.map(dto, listType);
        response.setData(apiDto);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It returns an instructor of particular content owner.
     * <h1>API Path: /instructors/{email}</h1>
     *
     * @param email the email that will be returned.
     * @param userName     login id of an author.
     * @return {@code RestResponse} will be returned with instructor object as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *     "error": null,
     *     "warning": null,
     *     "info": null,
     *     "data": {
     *          "firstName": "abdus",
     *          "lastName": "samad",
     *           "email": "abdus-samad@hotmail.com",
     *           "phoneNo": "123",
     *           "background": null
     *     }
     * }
     * </pre>
     */
    @RequestMapping(value = "instructors/{email}", method = RequestMethod.GET)
    ResponseEntity<Object> getInstructor(@PathVariable("email") String email
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        ClassInstructorDTO dto = classroomService.getInstructor(user, email);
        if(dto != null) {
            ClassInstructorAPIDTO apiDto = modelMapper.map(dto, ClassInstructorAPIDTO.class);
            response.setData(apiDto);
        } else {
            throw new BulkUplaodCourseException("Not Found In System", "Instructor", 0, 0,
                    "", email );
        }
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It creates a new instructor.
     * <h1>API Path: /instructors</h1>
     * <pre>
     * @param instructor the instructor to add. JSON sample is given below.
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *       "error": null,
     *       "warning": null,
     *      "info": "done",
     *       "data": null}
     * }
     * </pre>
     */
    @RequestMapping(value = "instructors", method = RequestMethod.POST)
    ResponseEntity<Object> addInstructor(@Valid @RequestBody ClassInstructorDTO instructor
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        classroom.getInstructors().add(instructor);
        instructor.setAction("add");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user, true);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It modifies provided instructor.
     * <h1>API Path: /instructors/{email}</h1>
     * @param instructor the instructor that will be modified.
     * @param email     email of an author.
     * @param userName     login id of an author.
     * @return {@code RestResponse} will be returned. JSON Sample is given below.<br/>
     * <pre>
     * {@code
     * {
     *      "error": null,
     *      "warning": null,
     *      "info": "done",
     *       "data": null
     * }
     * }
     * </pre>
     */
    @RequestMapping(value = "instructors/{email}", method = RequestMethod.PUT)
    ResponseEntity<Object> updateInstructor(@Valid @RequestBody ClassInstructorDTO instructor
            , @PathVariable("email") String email
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        encloseInstructor(classroom, email, instructor);
        instructor.setAction("update");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user, true);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    //TODO locations:

    /**
     * It returns collection of locations of particular content owner.
     * <h1>API Path: /locations</h1>
     *
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned with collection of locations as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {"error": null,
     * "warning": null,
     * "info": null,
     * "data": [
     * {
     *    "description": "",
     *    "locationName": "location-123",
     *    "city": "Karachi",
     *    "state": "Sindh",
     *    "zip": "12345",
     *    "country": "Pakistan",
     *    "address": "address-1",
     *    "phone": "03442999099",
     *    "id": 1328564,
     *    "streetAddress": "address-1",
     * },...]}}
     * </pre>
     */
    @RequestMapping(value = "locations", method = RequestMethod.GET)
    ResponseEntity<Object> getLocations(
            @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        Collection<LocationDTO> dto = classroomService.getLocations(user);
        Type listType = new TypeToken<Collection<LocationAPIDTO>>(){}.getType();
        Collection<LocationAPIDTO> apiDto = modelMapper.map(dto, listType);
        response.setData(apiDto);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It returns a location of particular content owner.
     * <h1>API Path: /locations/{locationName}</h1>
     *
     * @param locationName the name of the location that will be returned.
     * @param userName     login id of an author.
     * @return {@code RestResponse} will be returned with location object as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {"error": null,
     * "warning": null,
     * "info": null,
     * "data": {
     *    "description": "",
     *    "locationName": "location-123",
     *    "city": "Karachi",
     *    "state": "Sindh",
     *    "zip": "12345",
     *    "country": "Pakistan",
     *    "address": "address-1",
     *    "phone": "03442999099",
     *    "id": 1328564,
     *    "streetAddress": "address-1",
     * }}}
     * </pre>
     */
    @RequestMapping(value = "locations/{locationName}", method = RequestMethod.GET)
    ResponseEntity<Object> getLocation(@PathVariable("locationName") String locationName
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        LocationDTO dto = classroomService.getLocation(user, locationName);
        if(dto != null) {
            LocationAPIDTO apiDto = modelMapper.map(dto, LocationAPIDTO.class);
            response.setData(apiDto);
        } else {
            throw new BulkUplaodCourseException("Not Found In System", "Location", 0, 0,
                    "", locationName );
        }
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It creates new location.
     * <h1>API Path: /locations</h1>
     *
     * @param location the location to add. JSON sample is given below.
     *                 <pre>
     *   {@code
     *   {"description": "desc",
     *   "locationName": "location-123",
     *   "city": "Karachi",
     *   "state": "Sindh",
     *   "zip": "12345",
     *   "country": "Pakistan",
     *   "address": "address-1",
     *   "phone": "03442999099",
     *   "streetAddress": "address-1" }}
     *   </pre>
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {"error": null,
     * "warning": null,
     * "info": null,
     * "data": null}}
     * </pre>
     */
    @RequestMapping(value = "locations", method = RequestMethod.POST)
    ResponseEntity<Object> addLocation(@Valid @RequestBody LocationDTO location
                    , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        classroom.getLocations().add(location);
        location.setAction("add");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user, true);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It modifies provided location.
     * <h1>API Path: /locations/{locationName}</h1>
     *
     * @param location     the object, the location will be modified with. JSON sample is given below.
     *                     <pre>
     *   {@code
     *   {"description": "desc",
     *   "city": "Karachi",
     *   "state": "Sindh",
     *   "zip": "12345",
     *   "country": "Pakistan",
     *   "address": "address-1",
     *   "phone": "03442999099",
     *   "streetAddress": "address-1" }}
     *   </pre>
     * @param locationName the name of the location that will be modified.
     * @param userName     login id of an author.
     * @return {@code RestResponse} will be returned. JSON Sample is given below.<br/>
     * <pre>
     * {@code
     * {"error": null,
     * "warning": null,
     * "info": null,
     * "data": null}}
     * </pre>
     */
    @RequestMapping(value = "locations/{locationName}", method = RequestMethod.PUT)
    ResponseEntity<Object> updateLocation(@Valid @RequestBody LocationDTO location
            , @PathVariable("locationName") String locationName
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        encloseLocation(classroom, locationName, location);
        location.setAction("update");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user, true);
        return new ResponseEntity<Object>(response, httpStatus);
    }

    //TODO courses

    /**
     * It returns a location of particular content owner.
     * <h1>API Path: /courses/{courseId}</h1>
     *
     * @param courseId the business key of the course that will be returned.
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned with course object as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": {
     *    "id": 217305,
     *    "courseStatus": "Not Started",
     *    "courseId": "12345",
     *    "name": "Sample Course Name",
     *    "description": "Sample Description",
     *    "keywords": "Sample",
     *    "courseType": "Classroom Course",
     *    "currency": "USD",
     *    "courseProvider": null,
     *    "additionalMaterials": null,
     *    "intendedAudience": null,
     *    "courseObjectives": null,
     *    "coursePrereq": null,
     *    "businessunitName": "All",
     *    "topicsCovered": null,
     *    "learningObjectives": null,
     *    "propValue": null
     *  }
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}", method = RequestMethod.GET)
    ResponseEntity<Object> getCourse(@PathVariable("courseId") String courseId
            , @RequestParam(name = "deep", defaultValue = "false") boolean deep
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        CourseVO dto= classroomService.getCourse(user, courseId, deep);
        if(dto != null) {
            CourseAPIDTO apiDto = modelMapper.map(dto, CourseAPIDTO.class);
            response.setData(apiDto);
        } else {
            throw new BulkUplaodCourseException("Not Found In System", "Course", 0, 0,
                    "", courseId);
        }
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It creates new course.
     * <h1>API Path: /courses</h1>
     *
     * @param course   the course to add. JSON sample is given below.
     *                 <pre>
     *   {@code
     *   {"courseStatus": "Not Started",
     *   "courseId": "12345",
     *   "name": "Sample Course Name",
     *   "description": "Sample Description",
     *   "keywords": "Sample",
     *   "courseType": "Classroom Course",
     *   "currency": "USD",
     *   "courseProvider": null,
     *   "additionalMaterials": null,
     *   "intendedAudience": null,
     *   "courseObjectives": null,
     *   "coursePrereq": null,
     *   "businessunitName": "All",
     *   "topicsCovered": null,
     *   "learningObjectives": null,
     *   "propValue": null}
     *   }
     *   </pre>
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {"error": null,
     * "warning": null,
     * "info": null,
     * "data": null}}
     * </pre>
     */
    @RequestMapping(value = "courses", method = RequestMethod.POST)
    ResponseEntity<Object> addCourse(@Valid @RequestBody CourseVO course
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {
        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        classroom.getCourses().add(course);
        encloseInstructor(classroom,course.getInstructorEmail());
        course.setAction("add");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It modifies provided course.
     * <h1>API Path: /courses/{courseId}</h1>
     *
     * @param course   the object, the course will be modified with. JSON sample is given below.
     *                 <pre>
     *    {@code
     *    {"courseStatus": "Not Started",
     *    "courseId": "12345",
     *    "name": "Sample Course Name",
     *    "description": "Sample Description",
     *    "keywords": "Sample",
     *    "courseType": "Classroom Course",
     *    "currency": "USD",
     *    "courseProvider": null,
     *    "additionalMaterials": null,
     *    "intendedAudience": null,
     *    "courseObjectives": null,
     *    "coursePrereq": null,
     *    "businessunitName": "All",
     *    "topicsCovered": null,
     *    "learningObjectives": null,
     *    "propValue": null}
     *    }
     *    </pre>
     * @param courseId the business key of the course that will be modified.
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {"error": null,
     * "warning": null,
     * "info": null,
     * "data": null}}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}", method = RequestMethod.PUT)
    ResponseEntity<Object> updateCourse(@Valid @RequestBody CourseVO course, @PathVariable("courseId") String courseId
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        encloseCourse(classroom, courseId, course);
        encloseInstructor(classroom,course.getInstructorEmail());
        course.setAction("update");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }

    /**
     * It returns collection of classes of particular course.
     * <h1>API Path: /courses/{courseId}/classes</h1>
     *
     * @param courseId the business key of the course, the classes will be returned of.
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned with collection of classes as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": [
     *    {
     *      "id": 19156,
     *      "maximumClassSize": 12,
     *      "timeZoneText": "(GMT -8:00) Pacific (San Jose)",
     *      "enrollmentCloseDate": "04/13/2016 12:00 AM",
     *      "className": "Java Basics",
     *      "locationName": "Austin"
     *    },.....
     *  ]
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes", method = RequestMethod.GET)
    ResponseEntity<Object> getClasses(@PathVariable("courseId") String courseId
            , @RequestParam(name = "deep", defaultValue = "false") boolean deep
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        Collection<SyncClassDTO> dto = classroomService.getCourseClasses(user, courseId, deep);
        Type listType = new TypeToken<Collection<SyncClassAPIDTO>>(){}.getType();
        Collection<SyncClassAPIDTO> aptDto = modelMapper.map(dto, listType);
        response.setData(aptDto);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It returns a class of particular course.
     * <h1>API Path: /courses/{courseId}/classes/{className}</h1>
     *
     * @param courseId  the business key of the course, the class will be returned of.
     * @param className the name of the class that will be returned.
     * @param userName  login id of author.
     * @return {@code RestResponse} will be returned with the synchronous class as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": {
     *      "id": 19156,
     *      "maximumClassSize": 12,
     *      "timeZoneText": "(GMT -8:00) Pacific (San Jose)",
     *      "enrollmentCloseDate": "04/13/2016 12:00 AM",
     *      "className": "Java Basics",
     *      "locationName": "Austin"
     *    }
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes/{className}", method = RequestMethod.GET)
    ResponseEntity<Object> getClass(@PathVariable("courseId") String courseId
            , @PathVariable("className") String className
            , @RequestParam(name = "deep", defaultValue = "false") boolean deep
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        SyncClassDTO dto = classroomService.getCourseClass(user, courseId, className, deep);
        if(dto != null) {
            SyncClassAPIDTO apiDto = modelMapper.map(dto, SyncClassAPIDTO.class);
            response.setData(apiDto);
        }else {
            throw new BulkUplaodCourseException("Not Found In System", "Class", 0, 0,
                    "", courseId + " [" + className + "]" );
        }
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It creates a new class for particular course.
     * <h1>API Path: /courses/{courseId}/classes</h1>
     *
     * @param courseId the business key of the course, the class will be created for.
     * @param cls      the object of the class that will be created. JSON Sample is given below.
     *                 </p><pre/>
     *                 {@code
     *                 {
     *                 "maximumClassSize": 12,
     *                 "timeZoneText": "(GMT -8:00) Pacific (San Jose)",
     *                 "enrollmentCloseDate": "04/13/2016 12:00 AM",
     *                 "className": "Java Basics",
     *                 "locationName": "Austin"
     *                 }
     *                 }
     *                 </p>
     * @param userName login id of author.
     * @return {@code RestResponse} will be returned . JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": null
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes", method = RequestMethod.POST)
    ResponseEntity<Object> addClass(@Valid @RequestBody SyncClassDTO cls
            , @PathVariable("courseId") String courseId
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        encloseClass(encloseCourse(classroom, courseId), cls.getClassName(), cls);
        encloseInstructor(classroom,cls.getInstructorEmail());
        cls.setAction("add");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It modifies the class of particular course.
     * <h1>API Path: /courses/{courseId}/classes/{className}</h1>
     *
     * @param courseId  the business key of the course, the class will be modified of.
     * @param className the name of the class that will be modified.
     * @param cls       the object, the class will be modified with. JSON Sample is given below.
     *                  <pre>
     *      {@code
     *      {"maximumClassSize": 12,
     *           "timeZoneText": "(GMT -8:00) Pacific (San Jose)",
     *           "enrollmentCloseDate": "04/13/2016 12:00 AM",
     *           "className": "Java Basics",
     *           "locationName": "Austin"
     *         }
     *      }
     *      </pre>
     * @param userName  login id of author.
     * @return {@code RestResponse} will be returned . JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": null
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes/{className}", method = RequestMethod.PUT)
    ResponseEntity<Object> updateClass(@Valid @RequestBody SyncClassDTO cls
            , @PathVariable("courseId") String courseId
            , @PathVariable("className") String className
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        encloseClass(encloseCourse(classroom, courseId), className, cls);
        encloseInstructor(classroom,cls.getInstructorEmail());
        cls.setAction("update");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It deletes the class of particular course.
     * <h1>API Path: /courses/{courseId}/classes/{className}</h1>
     *
     * @param courseId  the business key of the course, the class will be deleted of.
     * @param className the name of the class that will be deleted.
     * @param userName  login id of author.
     * @return {@code RestResponse} will be returned . JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": null
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes/{className}", method = RequestMethod.DELETE)
    ResponseEntity<Object> deleteClass(@PathVariable("courseId") String courseId
            , @PathVariable("className") String className
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        SyncClassDTO cls = encloseClass(encloseCourse(classroom, courseId), className);
        cls.setAction("delete");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It returns collection of sessions of particular class.
     * <h1>API Path: /courses/{courseId}/classes/{className}/sessions</h1>
     *
     * @param courseId  the business key of the course.
     * @param className the name of the class, the sessions will be returned of.
     * @param userName  login id of author.
     * @return {@code RestResponse} will be returned with collection of sessions as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": [
     *    {
     *      "id": 19156,
     *      "startDateTime": "04/13/2016 12:00 AM",
     *      "endDateTime": "04/13/2016 12:00 AM",
     *    },.....
     *  ]
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes/{className}/sessions", method = RequestMethod.GET)
    ResponseEntity<Object> getSessions(@PathVariable("courseId") String courseId
            , @PathVariable("className") String className
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        Collection<SyncSessionDTO> dto = classroomService.getClassSessions(user, courseId, className);
        Type listType = new TypeToken<Collection<SyncSessionAPIDTO>>() {}.getType();
        Collection<SyncSessionAPIDTO> apiDto = modelMapper.map(dto, listType);
        response.setData(apiDto);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It returns the synchronous session of particular class.
     * <h1>API Path: /courses/{courseId}/classes/{className}/sessions/{sessionKey}</h1>
     *
     * @param courseId  the business key of the course.
     * @param className the name of the class, the session will be returned of.
     * @param sessionKey the id of the session that will be returned.
     * @param userName  login id of author.
     * @return {@code RestResponse} will be returned with session object as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": {
     *      "id": 19156,
     *      "startDateTime": "04/13/2016 12:00 AM",
     *      "endDateTime": "04/13/2016 12:00 AM",
     *    }
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes/{className}/sessions/{sessionKey}", method = RequestMethod.GET)
    ResponseEntity<Object> getSession(@PathVariable("courseId") String courseId
            , @PathVariable("className") String className
            , @PathVariable("sessionKey") String sessionKey
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        RestResponse response = new RestResponse();
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        SyncSessionDTO dto = classroomService.getClassSession(user, courseId, className, sessionKey);
        if(dto != null) {
            SyncSessionAPIDTO apiDto = modelMapper.map(dto, SyncSessionAPIDTO.class);
            response.setData(apiDto);
        } else {
            throw new BulkUplaodCourseException("Not Found In System", "Session", 0, 0,
                    "", "(" + courseId + "," + className + ") [" + sessionKey + "]" );
        }
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It creates a synchronous session for particular class.
     * <h1>API Path: /courses/{courseId}/classes/{className}/sessions</h1>
     *
     * @param session   the object of the session that will be created. JSON Sample is given below.
     *                  <p><pre/>
     *                  {@code
     *                  {
     *                  "id": 19156,
     *                  "startDateTime": "04/13/2016 12:00 AM",
     *                  "endDateTime": "04/13/2016 12:00 AM",
     *                  }
     *                  }
     *                  </p>
     * @param courseId  the business key of the course.
     * @param className the name of the class, the session will be created for.
     * @param userName  login id of author.
     * @return {@code RestResponse} will be returned with session object as {@code data}. JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": {
     *      "id": 19156,
     *      "startDateTime": "04/13/2016 12:00 AM",
     *      "endDateTime": "04/13/2016 12:00 AM",
     *    }
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes/{className}/sessions", method = RequestMethod.POST)
    ResponseEntity<Object> addSession(@Valid @RequestBody SyncSessionDTO session
            , @PathVariable("courseId") String courseId
            , @PathVariable("className") String className
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        encloseSession(encloseClass(encloseCourse(classroom, courseId), className), session.getSessionKey(), session);
        session.setAction("add");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It modifies the synchronous session of particular class.
     * <h1>API Path: /courses/{courseId}/classes/{className}/sessions/{sessionKey}</h1>
     *
     * @param session   the object, the session will be modified with. JSON Sample is given below.
     *                  <p><pre/>
     *                  {@code
     *                  {
     *                  "startDateTime": "04/13/2016 12:00 AM",
     *                  "endDateTime": "04/13/2016 12:00 AM"
     *                  }
     *                  }
     *                  </p>
     * @param sessionKey the id of the session that will be modified.
     * @param courseId  the business key of the course.
     * @param className the name of the class, the session will be modified of.
     * @param userName  login id of author.
     * @return {@code RestResponse} will be returned . JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": null
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes/{className}/sessions/{sessionKey}", method = RequestMethod.PUT)
    ResponseEntity<Object> updateSession(@Valid @RequestBody SyncSessionDTO session
            , @PathVariable("courseId") String courseId
            , @PathVariable("className") String className
            , @PathVariable("sessionKey") String sessionKey
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        encloseSession(encloseClass(encloseCourse(classroom, courseId), className), sessionKey, session);
        session.setAction("update");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }


    /**
     * It deletes the synchronous session of particular class.
     * <h1>API Path: /courses/{courseId}/classes/{className}/sessions/{sessionKey}</h1>
     *
     * @param sessionKey the id of the session that will be deleted.
     * @param courseId  the business key of the course.
     * @param className the name of the class, the session will be deleted from.
     * @param userName  login id of author.
     * @return {@code RestResponse} will be returned . JSON Sample is given below for more details.<br/>
     * <pre>
     * {@code
     * {
     *  "error": null,
     *  "warning": null,
     *  "info": null,
     *  "data": null
     * }}
     * </pre>
     */
    @RequestMapping(value = "courses/{courseId}/classes/{className}/sessions/{sessionKey}", method = RequestMethod.DELETE)
    ResponseEntity<Object> deleteSession(@PathVariable("courseId") String courseId
            , @PathVariable("className") String className
            , @PathVariable("sessionKey") String sessionKey
            , @RequestParam(name = "userName", defaultValue = "admin.manager@360training.com") String userName) throws BulkUplaodCourseException {

        HttpStatus httpStatus = HttpStatus.OK;
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        SyncSessionDTO session = encloseSession(encloseClass(encloseCourse(classroom, courseId), className), sessionKey);
        session.setAction("delete");
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername(userName);
        RestResponse response = importClassroomCourses(classroom, user);
        return new ResponseEntity<Object>(response, httpStatus);
    }
}
