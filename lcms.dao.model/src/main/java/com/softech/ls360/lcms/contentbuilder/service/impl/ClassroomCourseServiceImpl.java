/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softech.ls360.lcms.contentbuilder.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.softech.common.delegate.Function1;
import com.softech.common.delegate.Function1WithException;
import com.softech.common.delegate.Function2;
import com.softech.common.delegate.Function2WithException;
import com.softech.common.mail.MailAsyncManager;
import com.softech.common.parsing.TabularDataException;
import com.softech.common.parsing.TabularDataExceptionList;
import com.softech.common.template.TemplateResolver;
import com.softech.ls360.lcms.contentbuilder.dao.*;
import com.softech.ls360.lcms.contentbuilder.dataimport.*;
import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.model.*;
import com.softech.ls360.lcms.contentbuilder.model.TimeZone;
import com.softech.ls360.lcms.contentbuilder.service.IClassroomCourseService;
import com.softech.ls360.lcms.contentbuilder.utils.*;
import com.softech.ls360.lcms.contentbuilder.utils.WlcmsConstants.DeliveryMethod;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Sheikh Abdul Wahid
 */
@Component
public class ClassroomCourseServiceImpl implements IClassroomCourseService {

    private static Logger logger = LoggerFactory.getLogger(ClassroomCourseServiceImpl.class);

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private LocationDAO locationDAO;

    @Autowired
    ClassInstructorDAO instructorDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    public SynchronousClassDAO syncClassDAO;

    @Autowired
    public CourseProviderDAO courseProviderDAO;

    @Autowired
    public CourseGroupDAO courseGroupDAO;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${course.default.brandingId}")
    private String confDefaultBrandingId;

    @Value("${course.default.coursestatus}")
    private String confDefaultStatus;

    @Value("${course.default.code}")
    private String confDefaultCode;

    @Value("${course.default.currency}")
    private String confDefaultCurrency;

    @Value("${course.default.businessunitId}")
    private String confDefaultBUId;

    @Value("${course.default.businessunitName}")
    private String confDefaultBUName;

    private Map<String, Long> timezoneMap;

    @Autowired
    TemplateResolver templateResolver;

    @Autowired
    MailAsyncManager mailAsyncManager;

    private class SyncSessionValidatorAndPopulator implements Function2WithException<SyncSessionDTO, Object, Object, TabularDataException>, Function1WithException<SyncSessionDTO, Void, TabularDataException> {

        private VU360UserDetail user;
        private Map<String, Map<String, String>> classMapOfSessionsMap = new HashMap<>();

        public SyncSessionValidatorAndPopulator(VU360UserDetail user) {
            this.user = user;
        }

        private Map<String, String> getDefinedSessionKeys(SyncClassDTO clsDto) {
            String courseId = clsDto.getCourse().getCourseId();
            String className = clsDto.getClassName();
            String classKey = courseId.toLowerCase() + "::" + className.toLowerCase();
            Map<String, String> sessionsMap = classMapOfSessionsMap.get(classKey);
            if (sessionsMap == null) {
                sessionsMap = new HashMap<>();
                classMapOfSessionsMap.put(classKey, sessionsMap);
                SynchronousClass syncClass = syncClassDAO.getSynchronousClassByOwnerAndCourseAndClassName((int) user.getContentOwnerId(), clsDto.getCourse().getCourseId(), clsDto.getClassName());
                if (syncClass != null) {
                    for (SynchronousSession session : syncClass.getSyncSession()) {
                        sessionsMap.put(session.getSessionKey().toLowerCase(),session.getSessionKey());
                    }
                }
            }
            return sessionsMap;
        }

        @Override
        public Object apply(SyncSessionDTO session, Object lastValidatorResult) throws TabularDataException {

            //validate unique session
            if (!StringUtils.isEmpty(session.getAction())
                    && session.getAction().equalsIgnoreCase("Add")
                    && getDefinedSessionKeys(session.getSyncClass()).containsKey(session.getKey().toLowerCase())) {
                throw new TabularDataException("Duplicate", null, -1, 0, "Course Id, Class Title, Session", session.getSyncClass().getCourse().getCourseId() + ", " + session.getSyncClass().getClassName() + ", " + session.getKey());
            }

            //validate defined session
            if (!StringUtils.isEmpty(session.getAction())
                    && StringUtil.equalsAny(session.getAction(), true, "delete", "update", "child", "delete-child")
                    && !getDefinedSessionKeys(session.getSyncClass()).containsKey(session.getKey().toLowerCase())) {
                throw new TabularDataException("Not Found In System", null, -1, 0, "Course Id, Class Title, , Session", session.getSyncClass().getCourse().getCourseId() + ", " + session.getSyncClass().getClassName() + ", " + session.getKey());
            }
            return null;
        }

        @Override
        public Void apply(SyncSessionDTO session) throws TabularDataException {
            if (!StringUtils.isEmpty(session.getAction())
                    && StringUtil.equalsAny(session.getAction(), true, "delete", "update", "child", "delete-child")) {
                String key = getDefinedSessionKeys(session.getSyncClass()).get(session.getKey().toLowerCase());
                if (key == null) {
                    throw new TabularDataException("Not Found In System", null, -1, 0, "Course Id, Class Title, , Session", session.getSyncClass().getCourse().getCourseId() + ", " + session.getSyncClass().getClassName() + " ," + session.getKey());
                } else {
                }
            }
            return null;
        }
    }

    private void entityToDTO(ClassInstructor entity, ClassInstructorDTO dto) {
        modelMapper.map(entity, dto);
    }

    private void entityToDTO(Location entity, LocationDTO dto) {
        modelMapper.map(entity, dto);
        if (entity.getAddress() != null) {
            modelMapper.map(entity.getAddress(), dto);
            dto.setZip(entity.getAddress().getZipcode());
        }
    }


    private void entityToDTO(CourseDTO entity, CourseVO dto, boolean deep) {
        modelMapper.map(entity, dto);
        dto.setCourseId(entity.getBussinesskey());

        if (deep) {
            for (SynchronousClass cls : entity.getSyncClass()) {
                SyncClassDTO clsDto = new SyncClassDTO();
                entityToDTO(cls, clsDto, deep);
                dto.getSyncClassesMap().put(clsDto.getId().toString(), clsDto);
            }
            dto.setSyncClasses(dto.getSyncClassesMap().values());
        }
        dto.setSyncClassesMap(null);


        //course provider
        CourseProvider courseProvider = courseProviderDAO.loadProviderbyCourseId(entity.getId());
        if (courseProvider != null) {
            CourseProviderDTO providerDto = new CourseProviderDTO();
            entityToDTO(courseProvider, providerDto);
            providerDto.setInstructorBackground(entity.getAuthorBackground());
            dto.setCourseProvider(providerDto);
        }

        //instructor
        if(entity.getClassInstructorId() != null) {
            ClassInstructor instructor = instructorDAO.getById(entity.getClassInstructorId());
            if(instructor != null) {
                dto.setInstructorEmail(instructor.getEmail());
            }
        }
    }

    private void entityToDTO(CourseProvider entity, CourseProviderDTO dto) {
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNo(entity.getPhoneNo());
    }

    private void entityToDTO(SynchronousClass entity, SyncClassDTO dto, boolean deep) {
        modelMapper.map(entity, dto);
        dto.setLocId(entity.getLocation().getId());
        dto.setLocationName(entity.getLocation().getLocationname());

        if (deep) {
            for (SynchronousSession session : entity.getSyncSession()) {
                SyncSessionDTO sessionDto = new SyncSessionDTO();
                entityToDTO(session, sessionDto);
                dto.getSessionsMap().put(sessionDto.getId().toString(), sessionDto);
            }

            dto.setSessions(dto.getSessionsMap().values());
        }

        dto.setSessionsMap(null);

        //time zone.
        TimeZone timezone = syncClassDAO.getTimeZoneById(entity.getTimeZoneId());
        String timeZoneText = "(" + timezone.getCode() + " " + timezone.getHours() + ":" + ((timezone.getMinutes() == 0) ? "00" : timezone.getMinutes()) + ") " + timezone.getZone();
        dto.setTimeZoneText(timeZoneText);

        //instructor
        if(entity.getClassInstructorId() != null) {
            ClassInstructor instructor = instructorDAO.getById(entity.getClassInstructorId());
            if(instructor != null) {
                dto.setInstructorEmail(instructor.getEmail());
            }
        }
    }

    private void entityToDTO(SynchronousSession entity, SyncSessionDTO dto) {
        modelMapper.map(entity, dto);
    }

    private void validateCourses(VU360UserDetail user, ClassroomImportDTO classroom, boolean ignoreCourse) throws BulkUplaodCourseException {

        Collection<CourseVO> courses = classroom.getCourses();
        Collection<LocationDTO> locations = classroom.getLocations();
        Collection<ClassInstructorDTO> instructors = classroom.getInstructors();

        // ============ Courses =======
        Collection<CourseVO> newCourses = CollectionHelper.filterCollection(courses, new Function1<CourseVO, CourseVO>() {
            @Override
            public CourseVO apply(CourseVO course) {
                if (!StringUtils.isEmpty(course.getAction()) && course.getAction().equalsIgnoreCase("add")) {
                    return course;
                }
                return null;
            }
        });

        Collection<CourseVO> existingCourses = CollectionHelper.filterCollection(courses, new Function1<CourseVO, CourseVO>() {
            @Override
            public CourseVO apply(CourseVO course) {
                if (!StringUtils.isEmpty(course.getAction()) && StringUtil.equalsAny(course.getAction(), true, "delete-child", "update", "child", "delete")) {
                    return course;
                }
                return null;
            }
        });

        //validate courses 
        if (!ignoreCourse && newCourses.isEmpty() && existingCourses.isEmpty()) {
            throw new BulkUplaodCourseException("NoRow", "Course", 0, 0,
                    "", "");
        }

        validateUniqueCourses(user, newCourses);
        validateDefinedCourses(user, existingCourses);

        // ============ Locations =======
        Collection<LocationDTO> existingLocations = CollectionHelper.filterCollection(locations, new Function1<LocationDTO, LocationDTO>() {
            @Override
            public LocationDTO apply(LocationDTO loc) {
                if (StringUtils.isEmpty(loc.getAction()) || StringUtil.equalsAny(loc.getAction(), true, "update", "delete", "child")) {
                    return loc;
                }
                return null;
            }
        });

        Collection<LocationDTO> newLocations = CollectionHelper.filterCollection(locations, new Function1<LocationDTO, LocationDTO>() {
            @Override
            public LocationDTO apply(LocationDTO loc) {
                if (!StringUtils.isEmpty(loc.getAction()) && loc.getAction().equalsIgnoreCase("add")) {
                    return loc;
                }
                return null;
            }
        });

        validateDefinedLocations(user, existingLocations);
        validateUniqueLocations(user, newLocations);


        // ============ Instructors =======
        Collection<ClassInstructorDTO> existingInstructors = CollectionHelper.filterCollection(instructors, new Function1<ClassInstructorDTO, ClassInstructorDTO>() {
            @Override
            public ClassInstructorDTO apply(ClassInstructorDTO loc) {
                if (StringUtils.isEmpty(loc.getAction()) || StringUtil.equalsAny(loc.getAction(), true, "update", "delete", "child")) {
                    return loc;
                }
                return null;
            }
        });

        Collection<ClassInstructorDTO> newInstructors = CollectionHelper.filterCollection(instructors, new Function1<ClassInstructorDTO, ClassInstructorDTO>() {
            @Override
            public ClassInstructorDTO apply(ClassInstructorDTO loc) {
                if (!StringUtils.isEmpty(loc.getAction()) && loc.getAction().equalsIgnoreCase("add")) {
                    return loc;
                }
                return null;
            }
        });

        validateDefinedInstructors(user, existingInstructors);
        validateUniqueInstructors(user, newInstructors);
        

        // ============ Classes =======
        for (CourseVO courseDto : existingCourses) {
            CourseDTO course = courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), courseDto.getCourseId());

            Collection<SyncClassDTO> existingSyncClasses = CollectionHelper.filterCollection(courseDto.getSyncClasses(), new Function1<SyncClassDTO, SyncClassDTO>() {
                @Override
                public SyncClassDTO apply(SyncClassDTO cls) {
                    if (!StringUtils.isEmpty(cls.getAction()) && StringUtil.equalsAny(cls.getAction(), true, "update", "delete", "child")) {
                        return cls;
                    }
                    return null;
                }
            });

            Collection<SyncClassDTO> newSyncClasses = CollectionHelper.filterCollection(courseDto.getSyncClasses(), new Function1<SyncClassDTO, SyncClassDTO>() {
                @Override
                public SyncClassDTO apply(SyncClassDTO cls) {
                    if (!StringUtils.isEmpty(cls.getAction()) && cls.getAction().equalsIgnoreCase("add")) {
                        return cls;
                    }
                    return null;
                }
            });

            validateDefinedClasses(course, existingSyncClasses);
            validateUniqueClasses(course , newSyncClasses);

            // ============ Sessions =======
            for (SyncClassDTO cls : existingSyncClasses) {

                Collection<SyncSessionDTO> existingSessions = CollectionHelper.filterCollection(cls.getSessions(), new Function1<SyncSessionDTO, SyncSessionDTO>() {
                    @Override
                    public SyncSessionDTO apply(SyncSessionDTO loc) {
                        if (!StringUtils.isEmpty(loc.getAction()) && StringUtil.equalsAny(loc.getAction(), true, "update", "delete", "child")) {
                            return loc;
                        }
                        return null;
                    }
                });

                Collection<SyncSessionDTO> newSessions = CollectionHelper.filterCollection(cls.getSessions(), new Function1<SyncSessionDTO, SyncSessionDTO>() {
                    @Override
                    public SyncSessionDTO apply(SyncSessionDTO loc) {
                        if (!StringUtils.isEmpty(loc.getAction()) && loc.getAction().equalsIgnoreCase("add")) {
                            return loc;
                        }
                        return null;
                    }
                });

                SynchronousClass dbClass = syncClassDAO.getSynchronousClassByCourseIdAndClassName(course.getId(), cls.getClassName());
                Set<SynchronousSession> dbDefinedSessions = dbClass.getSyncSession();
                validateDefinedSessions(dbClass, existingSessions);
                validateUniqueSessions(dbClass, newSessions);


                //Session start date with close date.
                for(SyncSessionDTO session : cls.getSessions()) {
                    if (!StringUtils.isEmpty(session.getAction()) && StringUtil.equalsAny(session.getAction(), true, "update", "add")) {
                         if(dbClass.getEnrollmentCloseDate().getTime() > session.getStartDateTime().getTime()) {
                             throw new BulkUplaodCourseException("Start Date Before Enrollment Close Date", "Session", 0, 0,
                                     "", "enrollment date: " + TypeConvertor.DateToString(dbClass.getEnrollmentCloseDate()));
                         }
                    }
                }

            }



        }

        //validate timezone
        prepareTimezoneMap();
        validateTimezone(courses);

    }

    @Transactional
    private void importCoursesWithoutValidation(VU360UserDetail user, ClassroomImportDTO classroom,boolean ignoreCourse) throws BulkUplaodCourseException {
        Collection<CourseVO> courses = classroom.getCourses();
        Collection<LocationDTO> locations = classroom.getLocations();
        Collection<ClassInstructorDTO> instructors = classroom.getInstructors();

        //validate courses list
        Collection<CourseVO> validCourses = CollectionHelper.filterCollection(courses, new Function1<CourseVO, CourseVO>() {
            @Override
            public CourseVO apply(CourseVO course) {
                if (!StringUtils.isEmpty(course.getAction()) && !course.isError() && isDirty(course)) {
                    return course;
                }
                return null;
            }

            private boolean isDirty(CourseVO course) {
                List<SyncClassDTO> classesWithChildAction = new ArrayList<>();
                if (StringUtil.equalsAny(course.getAction(), true, "add", "update", "delete", "delete-child")) {
                    return true;
                } else if (course.getAction().equalsIgnoreCase("child")) {
                    for (SyncClassDTO cls : course.getSyncClasses()) {
                        if (StringUtils.isEmpty(cls.getAction())) {
                            continue;
                        } else if (StringUtil.equalsAny(cls.getAction(), true, "add", "update", "delete", "delete-child")) {
                            return true;
                        } else if (cls.getAction().equalsIgnoreCase("child")) {
                            classesWithChildAction.add(cls);
                        }
                    }

                    for (SyncClassDTO cls : classesWithChildAction) {
                        for (SyncSessionDTO session : cls.getSessions()) {
                            if (StringUtil.equalsAny(session.getAction(), true, "add", "update", "delete")) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });

        if (!ignoreCourse && validCourses.isEmpty()) {
            throw new BulkUplaodCourseException("NoRow", "Course", 0, 0,
                    "", "");
        }

        //add or update locations
        Collection<LocationDTO> validLocations = CollectionHelper.filterCollection(locations, new Function1<LocationDTO, LocationDTO>() {
            @Override
            public LocationDTO apply(LocationDTO loc) {
                if (!StringUtils.isEmpty(loc.getAction()) && StringUtil.equalsAny(loc.getAction(), true, "add", "update","delete")) {
                    return loc;
                }
                return null;
            }
        });
        for (LocationDTO locDto : validLocations) {
            if(StringUtil.equalsAny(locDto.getAction(), true, "add", "update")) {
                Location loc = addOrUpdateLocation(user, locDto);

                //set loc id to dto
                locDto.setId(loc.getId());
            }
        }


        //add or update instructors
        Collection<ClassInstructorDTO> validInstructors = CollectionHelper.filterCollection(instructors, new Function1<ClassInstructorDTO, ClassInstructorDTO>() {
            @Override
            public ClassInstructorDTO apply(ClassInstructorDTO loc) {
                if (!StringUtils.isEmpty(loc.getAction()) && StringUtil.equalsAny(loc.getAction(), true, "add", "update","delete")) {
                    return loc;
                }
                return null;
            }
        });
        for (ClassInstructorDTO instDto : validInstructors) {
            if(StringUtil.equalsAny(instDto.getAction(), true, "add", "update")) {
                ClassInstructor inst = addOrUpdateClassInstructor(user, instDto);

                //set loc id to dto
                instDto.setId(inst.getId());
            }
        }

        //get course group.
        CourseGroup courseGroup = getCourseGroup(user);

        //add or update courses
        for (CourseVO courseDTO : validCourses) {
            CourseDTO course;
            CourseProviderDTO courseProviderDTO = null;
            if (StringUtil.equalsAny(courseDTO.getAction(), true, "add", "update")) {
                course = addOrUpdateCourse(user,courseGroup,courseDTO);

                //set course id to dto
                courseDTO.setId(course.getId());

                //add or update provider
                if (courseDTO.getCourseProvider() != null) {
                    courseProviderDTO = courseDTO.getCourseProvider();
                    addOrUpdateCourseProvider(user, courseDTO, course);
                }
            } else {
                course = courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), courseDTO.getCourseId());

                //mark as dirty so can appear on top in course search list.
                course.setLastUpdatedDate(new Date());
                course = courseDAO.updateCourse(course);
            }

            //delete classes
            if (courseDTO.getAction().equalsIgnoreCase("delete-child")) {
                for (SynchronousClass syncClass : syncClassDAO.getSynchronousClassByCourseId(course.getId())) {
                    syncClassDAO.deleteClassroom(syncClass.getId());
                }

                //now move to next course.
                continue;
            }

            //add or update classes
            Collection<SyncClassDTO> validClasses = CollectionHelper.filterCollection(courseDTO.getSyncClasses(), new Function1<SyncClassDTO, SyncClassDTO>() {
                @Override
                public SyncClassDTO apply(SyncClassDTO syncClass) {
                    if (!StringUtils.isEmpty(syncClass.getAction()) && isDirty(syncClass)) {
                        return syncClass;
                    }
                    return null;
                }

                private boolean isDirty(SyncClassDTO cls) {
                    if (StringUtil.equalsAny(cls.getAction(), true, "add", "update", "delete", "delete-child")) {
                        return true;
                    } else if (cls.getAction().equalsIgnoreCase("child")) {
                        for (SyncSessionDTO session : cls.getSessions()) {
                            if (StringUtil.equalsAny(session.getAction(), true, "add", "update", "delete")) {
                                return true;
                            }
                        }
                    }

                    return false;
                }
            });

            for (SyncClassDTO syncClassDTO : validClasses) {
                SynchronousClass syncClass;
                if (StringUtil.equalsAny(syncClassDTO.getAction(), true, "add", "update")) {
                    syncClass = addOrUpdateSyncClass(user, course, syncClassDTO, courseProviderDTO);
                } else {
                    syncClass = syncClassDAO.getSynchronousClassByCourseIdAndClassName(course.getId(), syncClassDTO.getClassName());

                    //mark as dirty so can be considered while republishing course.
                    syncClass.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
                    syncClass = syncClassDAO.update(syncClass);

                }

                //set sync class id to dto
                syncClassDTO.setId(syncClass.getId());

                if (syncClassDTO.getAction().equalsIgnoreCase("delete")) {
                    syncClassDAO.deleteClassroom(syncClass.getId());

                    //now move to next class.
                    continue;
                }

                //add or delete Sessions
                for (SyncSessionDTO syncSessionDTO : syncClassDTO.getSessions()) {
                    if (StringUtil.equalsAny(syncSessionDTO.getAction(),true,"add","update")) {
                        SynchronousSession syncSession = addOrUpdateSyncSession(user, syncClass, syncSessionDTO);
                        syncSessionDTO.setId(syncSession.getId());
                    } else if (syncSessionDTO.getAction().equalsIgnoreCase("delete")) {
                        SynchronousSession syncSession = syncClassDAO.getSyncSessionsByClassIdAndSessionKey(syncClass.getId(),syncSessionDTO.getKey());
                        syncClassDAO.deleteSession(syncSession.getId().toString(), new ObjectWrapper<Long>());
                    }
                }
            }

        }

    }

    private void addLocationValidators(final VU360UserDetail user, CRLocationParsingSubHndlr locationSubHandler) {
        locationSubHandler.getExternalValidators().add(new Function2WithException<LocationDTO, Object, Object, TabularDataException>() {
            @Override
            public Object apply(LocationDTO loc, Object lastValidatorResult) throws TabularDataException {
                //validate defined location
                if (!StringUtils.isEmpty(loc.getAction()) && StringUtil.equalsAny(loc.getAction(), true, "delete", "update", "child", "delete-child")
                        && locationDAO.getLocationByOwnerIdAndName((int) user.getContentOwnerId(), loc.getLocationName()) == null) {
                    throw new TabularDataException("Not Found In System", null, -1, 0, "Location Name", loc.getLocationName());
                }

                //validate unique locations
                if (!StringUtils.isEmpty(loc.getAction()) && loc.getAction().equalsIgnoreCase("Add") && locationDAO.getLocationByOwnerIdAndName((int) user.getContentOwnerId(), loc.getLocationName()) != null) {
                    throw new TabularDataException("Duplicate", null, -1, 0, "Location Name", loc.getLocationName());
                }
                return null;
            }
        });
    }

    private void addInstructorValidators(final VU360UserDetail user, CRInstructorParsingSubHndlr instructorSubHandler) {
        instructorSubHandler.getExternalValidators().add(new Function2WithException<ClassInstructorDTO, Object, Object, TabularDataException>() {
            @Override
            public Object apply(ClassInstructorDTO inst, Object lastValidatorResult) throws TabularDataException {
                //validate defined instructor
                if (!StringUtils.isEmpty(inst.getAction()) && StringUtil.equalsAny(inst.getAction(), true, "delete", "update", "child", "delete-child")
                        && instructorDAO.findByContentOwnerIdAndEmail(user.getContentOwnerId(), inst.getEmail()) == null
                        ) {
                    throw new TabularDataException("Not Found In System", null, -1, 0, "Instructor Email", inst.getEmail());
                }

                //validate unique instructor
                if (!StringUtils.isEmpty(inst.getAction()) && inst.getAction().equalsIgnoreCase("Add")
                        && instructorDAO.findByContentOwnerIdAndEmail(user.getContentOwnerId(), inst.getEmail()) != null
                        ) {
                    throw new TabularDataException("Duplicate", null, -1, 0, "Instructor Email", inst.getEmail());
                }
                return null;
            }
        });
    }

    private void addCourseValidators(final VU360UserDetail user, CRCourseParsingSubHndlr courseSubHandler) {
        courseSubHandler.getExternalValidators().add(new Function2WithException<CourseVO, Object, Object, TabularDataException>() {
            @Override
            public Object apply(CourseVO course, Object lastValidatorResult) throws TabularDataException {
                //validate unique courses
                if (!StringUtils.isEmpty(course.getAction())
                        && course.getAction().equalsIgnoreCase("Add")
                        && courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), course.getCourseId()) != null) {
                    throw new TabularDataException("Duplicate", null, -1, 0, "Course ID", course.getCourseId());
                }

                //validate defined courses
                if (!StringUtils.isEmpty(course.getAction())
                        && StringUtil.equalsAny(course.getAction(), true, "delete", "update", "child", "delete-child")
                        && courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), course.getCourseId()) == null) {
                    throw new TabularDataException("Not Found In System", null, -1, 0, "Course ID", course.getCourseId());
                }
                return null;
            }
        });
    }

    private void setSyncClassPopulator(final VU360UserDetail user, CRClassParsingSubHndlr classSubHandler) {
        classSubHandler.setExternalPopulator(new Function1WithException<SyncClassDTO, Void, TabularDataException>() {
                                                 @Override
                                                 public Void apply(SyncClassDTO clsDto) throws TabularDataException {
                                                     if (!StringUtils.isEmpty(clsDto.getAction())
                                                             && StringUtil.equalsAny(clsDto.getAction(), true, "delete", "child", "delete-child")) {
                                                         SynchronousClass syncClass = syncClassDAO.getSynchronousClassByOwnerAndCourseAndClassName((int) user.getContentOwnerId(), clsDto.getCourse().getCourseId(), clsDto.getClassName());
                                                         if (syncClass == null) {
                                                             throw new TabularDataException("Not Found In System", null, -1, 0, "Course Id, Class Title", clsDto.getCourse().getCourseId() + ", " + clsDto.getClassName());
                                                         } else {
                                                             modelMapper.map(syncClass, clsDto);
                                                         }
                                                     }
                                                     return null;
                                                 }
                                             }
        );
    }

    private void addSyncClassValidators(final VU360UserDetail user, CRClassParsingSubHndlr classSubHandler) {

        //validate time zone
        classSubHandler.getExternalValidators().add(new Function2WithException<SyncClassDTO, Object, Object, TabularDataException>() {
            @Override
            public Object apply(SyncClassDTO syncClass, Object lastValidatorResult) throws TabularDataException {
                if (!StringUtils.isEmpty(syncClass.getAction()) && StringUtil.equalsAny(syncClass.getAction(), true, "add", "update") && !timezoneMap.containsKey(syncClass.getTimeZoneText().toLowerCase().trim())) {
                    throw new TabularDataException("Invalid Value", "Class", -1, 0,
                            "Time Zone", syncClass.getTimeZoneText());
                }
                return null;
            }
        });

        //validate unique and defined classes 
        classSubHandler.getExternalValidators().add(new Function2WithException<SyncClassDTO, Object, Object, TabularDataException>() {
            @Override
            public Object apply(SyncClassDTO cls, Object lastValidatorResult) throws TabularDataException {

                //validate unique classes
                if (!StringUtils.isEmpty(cls.getAction())
                        && cls.getAction().equalsIgnoreCase("Add")
                        && syncClassDAO.getSynchronousClassByOwnerAndCourseAndClassName((int) user.getContentOwnerId(), cls.getCourse().getCourseId(), cls.getClassName()) != null) {
                    throw new TabularDataException("Duplicate", null, -1, 0, "Course Id, Class Title", cls.getCourse().getCourseId() + ", " + cls.getClassName());
                }

                //validate defined classes
                if (!StringUtils.isEmpty(cls.getAction())
                        && StringUtil.equalsAny(cls.getAction(), true, "delete", "update", "child", "delete-child")
                        && !StringUtils.isEmpty(cls.getCourse().getAction()) && StringUtil.equalsAny(cls.getCourse().getAction(), true, "update", "child")
                        && syncClassDAO.getSynchronousClassByOwnerAndCourseAndClassName((int) user.getContentOwnerId(), cls.getCourse().getCourseId(), cls.getClassName()) == null) {
                    throw new TabularDataException("Not Found In System", null, -1, 0, "Course Id, Class Title", cls.getCourse().getCourseId() + ", " + cls.getClassName());
                }

                return null;
            }
        });

    }

    @Override
    @Transactional
    public void importCourses(VU360UserDetail user, ClassroomImportDTO classroom,boolean ignoreCourse) throws BulkUplaodCourseException {
        validateCourses(user, classroom,ignoreCourse);
        importCoursesWithoutValidation(user, classroom,ignoreCourse);
    }

    @Transactional
    @Override
    public CourseVO getCourse(VU360UserDetail user, String businessKey, boolean deep) {
        CourseVO dto = null;
        CourseDTO course = courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), businessKey);
        if (course != null) {
            dto = new CourseVO();
            entityToDTO(course, dto, deep);
        }
        return dto;
    }

    @Transactional
    @Override
    public Collection<SyncClassDTO> getCourseClasses(VU360UserDetail user, String businessKey, boolean deep) throws BulkUplaodCourseException {
        //validate data
        CourseVO courseDto = new CourseVO();
        courseDto.setCourseId(businessKey);
        validateDefinedCourses(user,Arrays.asList(courseDto));

        Collection<SyncClassDTO> classes = new ArrayList<>();
        CourseDTO course = courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), businessKey);
        if (course != null) {
            for (SynchronousClass cls : course.getSyncClass()) {
                SyncClassDTO clsDto = new SyncClassDTO();
                entityToDTO(cls, clsDto, deep);
                classes.add(clsDto);
            }
        }
        return classes;
    }

    @Transactional
    @Override
    public SyncClassDTO getCourseClass(VU360UserDetail user, String businessKey, String className, boolean deep) throws BulkUplaodCourseException {

        //validate data
        CourseVO courseDto = new CourseVO();
        courseDto.setCourseId(businessKey);
        validateDefinedCourses(user,Arrays.asList(courseDto));

        SyncClassDTO clsDto = null;
        SynchronousClass cls = syncClassDAO.getSynchronousClassByOwnerAndCourseAndClassName((int) user.getContentOwnerId(), businessKey, className);
        if (cls != null) {
            clsDto = new SyncClassDTO();
            entityToDTO(cls, clsDto, deep);
        }
        return clsDto;
    }

    @Transactional
    @Override
    public Collection<SyncSessionDTO> getClassSessions(VU360UserDetail user, String businessKey, String className) throws BulkUplaodCourseException {

        //validate data
        CourseVO courseDto = new CourseVO();
        courseDto.setCourseId(businessKey);
        validateDefinedCourses(user,Arrays.asList(courseDto));

        CourseDTO course = courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), businessKey);
        SyncClassDTO classDto = new SyncClassDTO();
        classDto.setCourse(courseDto);
        classDto.setClassName(className);
        validateDefinedClasses(course,Arrays.asList(classDto));


        Collection<SyncSessionDTO> sessions = new ArrayList<>();
        SynchronousClass cls = syncClassDAO.getSynchronousClassByOwnerAndCourseAndClassName((int) user.getContentOwnerId(), businessKey, className);
        if (cls != null) {
            for (SynchronousSession session : cls.getSyncSession()) {
                SyncSessionDTO sessionDto = new SyncSessionDTO();
                entityToDTO(session, sessionDto);
                sessions.add(sessionDto);
            }
        }
        return sessions;
    }

    @Transactional
    @Override
    public SyncSessionDTO getClassSession(VU360UserDetail user, String businessKey, String className, String sessionKey) throws BulkUplaodCourseException {

        //validate data
        CourseVO courseDto = new CourseVO();
        courseDto.setCourseId(businessKey);
        validateDefinedCourses(user,Arrays.asList(courseDto));

        CourseDTO course = courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), businessKey);
        SyncClassDTO classDto = new SyncClassDTO();
        classDto.setCourse(courseDto);
        classDto.setClassName(className);
        validateDefinedClasses(course,Arrays.asList(classDto));

        SyncSessionDTO sessionDto = null;
        SynchronousClass syncClass = syncClassDAO.getSynchronousClassByOwnerAndCourseAndClassName((int) user.getContentOwnerId(), businessKey, className);

        if(syncClass != null) {
            SynchronousSession session = syncClassDAO.getSyncSessionsByClassIdAndSessionKey(syncClass.getId(), sessionKey);
            if (session != null) {
                sessionDto = new SyncSessionDTO();
                entityToDTO(session, sessionDto);
            }
        }
        return sessionDto;
    }

    @Transactional
    @Override
    public Collection<ClassInstructorDTO> getInstructors(VU360UserDetail user) {
        Collection<ClassInstructorDTO> instDtos = new ArrayList<>();
        Collection<ClassInstructor> insts = instructorDAO.findByContentOwnerId(user.getContentOwnerId());
        if (insts != null) {
            for (ClassInstructor inst : insts) {
                ClassInstructorDTO instDto = new ClassInstructorDTO();
                entityToDTO(inst, instDto);
                instDtos.add(instDto);
            }
        }
        return instDtos;
    }

    @Transactional
    @Override
    public ClassInstructorDTO getInstructor(VU360UserDetail user, String email) {
        ClassInstructorDTO instDto = null;
        ClassInstructor inst = instructorDAO.findByContentOwnerIdAndEmail(user.getContentOwnerId(), email);
        if (inst != null) {
            instDto = new ClassInstructorDTO();
            entityToDTO(inst, instDto);
        }
        return instDto;
    }

    @Transactional
    @Override
    public Collection<LocationDTO> getLocations(VU360UserDetail user) {
        Collection<LocationDTO> locDtos = new ArrayList<>();
        Collection<Location> locs = locationDAO.getLocationsByOwnerId((int) user.getContentOwnerId());
        if (locs != null) {
            for (Location loc : locs) {
                LocationDTO locDto = new LocationDTO();
                entityToDTO(loc, locDto);
                locDtos.add(locDto);
            }
        }
        return locDtos;
    }

    @Transactional
    @Override
    public LocationDTO getLocation(VU360UserDetail user, String locationName) {
        LocationDTO locDto = null;
        Location loc = locationDAO.getLocationByOwnerIdAndName((int) user.getContentOwnerId(), locationName);
        if (loc != null) {
            locDto = new LocationDTO();
            entityToDTO(loc, locDto);
        }
        return locDto;
    }

    @Override
    public ClassroomImportDTO importCourses(VU360UserDetail user, byte[] fileData, boolean ignoreErrors) throws IOException, TabularDataException {

        ClassroomImportDTO classroom = importCoursesToDtos(user, fileData, ignoreErrors);

        //import DTOs
        try {
            importCoursesWithoutValidation(user, classroom,false);
        } finally {
            sendClassroomErrorEmail(user, classroom.getErrorList(), fileData);
        }

        return classroom;
    }

    @Override
    public ClassroomImportDTO importCoursesToDtos(VU360UserDetail user, byte[] fileData, boolean ignoreErrors) throws TabularDataException, IOException {
        ClassroomParsingHndlr classroomHandler = appContext.getBean(ClassroomParsingHndlr.class);
        CRCourseParsingSubHndlr courseSubHandler = appContext.getBean(CRCourseParsingSubHndlr.class);
        CRClassParsingSubHndlr classSubHandler = appContext.getBean(CRClassParsingSubHndlr.class);
        CRSessionParsingSubHndlr sessionSubHandler = appContext.getBean(CRSessionParsingSubHndlr.class);
        CRLocationParsingSubHndlr locationSubHandler = appContext.getBean(CRLocationParsingSubHndlr.class);
        CRInstructorParsingSubHndlr instructorSubHandler = appContext.getBean(CRInstructorParsingSubHndlr.class);

        //--initialization
        classroomHandler.addSubHandlers(classSubHandler, courseSubHandler, sessionSubHandler, locationSubHandler,instructorSubHandler);

        //session ini
        sessionSubHandler.setClassHandler(classSubHandler);

        //class ini
        classSubHandler.setCourseHandler(courseSubHandler);
        classSubHandler.setLocationHandler(locationSubHandler);
        classSubHandler.setInstructorHandler(instructorSubHandler);

        //course ini
        courseSubHandler.setInstructorHandler(instructorSubHandler);

        //--add validators
        addCourseValidators(user, courseSubHandler);
        addLocationValidators(user, locationSubHandler);
        addInstructorValidators(user, instructorSubHandler);
        addSyncClassValidators(user, classSubHandler);

        SyncSessionValidatorAndPopulator sessionValidatorAndPopulator = new SyncSessionValidatorAndPopulator(user);
        sessionSubHandler.getExternalValidators().add(sessionValidatorAndPopulator);

        //set populators
        setSyncClassPopulator(user, classSubHandler);
        sessionSubHandler.setExternalPopulator(sessionValidatorAndPopulator);

        //prepareTimezoneMap
        prepareTimezoneMap();

        //parse course.
        Collection<TabularDataException.ExceptionDetail> errorList = null;
        try {
            classroomHandler.parseCourse(new ByteArrayInputStream(fileData));
        } catch (TabularDataException ex) {
            if (ex instanceof TabularDataExceptionList) {
                errorList = Collections2.transform(((TabularDataExceptionList) ex).getErrorList(), new Function<TabularDataException, TabularDataException.ExceptionDetail>() {
                    @Override
                    public TabularDataException.ExceptionDetail apply(TabularDataException tabularEx) {
                        TabularDataException.ExceptionDetail detail = tabularEx.getExceptionDetail();
                        return new TabularDataException.ExceptionDetail("classroomImportPage-error-" + detail.getMessage().replace(" ", "."), detail.getRowNum(), detail.isShowstopper(), detail.getColumnIndex(), detail.getColumnName(), detail.getTableName(), detail.getErrorText());
                    }
                });
            }
            if (!ignoreErrors || ex.isShowstopper()) {
                throw ex;
            }
        }
        ClassroomImportDTO classroom = new ClassroomImportDTO();
        classroom.setCourses(courseSubHandler.getCourses().values());
        classroom.setLocations(locationSubHandler.getLocations().values());
        classroom.setInstructors(instructorSubHandler.getInstructors().values());
        classroom.setErrorList(errorList);
        return classroom;
    }

    private void sendClassroomErrorEmail(VU360UserDetail user, Collection<TabularDataException.ExceptionDetail> errorList, byte[] fileData) {

        if (!StringUtils.isEmpty(user.getEmailAddress())) {
            if (errorList == null) {
                errorList = new ArrayList<>();
            }
            Map model = new HashMap();
            model.put("user", user);
            model.put("errors", errorList);
            String msgBody = templateResolver.resolveTemplate(user, "classImportResult.vm", model);
            String subject = "Class Import Result. " + errorList.size() + " error(s) found.";
            mailAsyncManager.send(new String[]{user.getEmailAddress()}, null, "support@360training.com",
                    "360training Member Service", subject, msgBody, new byte[][]{fileData}, new String[]{"Classroom Import Worksheet.xls"});

        }
    }

    private void prepareTimezoneMap() {
        if (timezoneMap == null) {
            List<TimeZone> timezones = syncClassDAO.getAllTimezone(null);
            timezoneMap = new HashMap<>();
            for (TimeZone timezone : timezones) {
                String key = "(" + timezone.getCode() + " " + timezone.getHours() + ":" + ((timezone.getMinutes() == 0) ? "00" : timezone.getMinutes()) + ") " + timezone.getZone();
                timezoneMap.put(key.toLowerCase(), timezone.getId());
            }
        }
    }

    private void validateTimezone(Collection<CourseVO> courses) throws BulkUplaodCourseException {
        for (CourseVO courseDTO : courses) {
            for (SyncClassDTO syncClassDTO : courseDTO.getSyncClasses()) {
                if (StringUtil.equalsAny(syncClassDTO.getAction(), true, "update", "add")
                        && !timezoneMap.containsKey(syncClassDTO.getTimeZoneText().toLowerCase().trim())) {
                    throw new BulkUplaodCourseException("Invalid Value", "Class", 0, 0,
                            "Time Zone", syncClassDTO.getTimeZoneText());
                }
            }

        }
    }

    private Location addOrUpdateLocation(VU360UserDetail user, LocationDTO dto) {
        Location location = locationDAO.getLocationByOwnerIdAndName((int) user.getContentOwnerId(), dto.getLocationName());
        boolean update = false;
        if (location != null) {
            update = true;
        } else {
            location = new Location();
            location.setEnabledtf("1");
            location.setContentownerId((int) user.getContentOwnerId());
            location.setAddress(new Address());
        }
        modelMapper.map(dto, location);
        modelMapper.map(dto, location.getAddress());
        location.getAddress().setZipcode(location.getZip());

        if (update) {
            locationDAO.updateLocation(location);
        } else {
            locationDAO.addLocation(location);
        }

        return location;
    }

    private ClassInstructor addOrUpdateClassInstructor(VU360UserDetail user, ClassInstructorDTO dto) {
        ClassInstructor inst = instructorDAO.findByContentOwnerIdAndEmail(user.getContentOwnerId(), dto.getEmail());

        boolean update = false;
        if (inst != null) {
            update = true;
        } else {
            inst = new ClassInstructor();
            inst.setContentOwnerId(user.getContentOwnerId());
            inst.setCreatedUser(user.getVu360UserID());
        }
        modelMapper.map(dto, inst);

        if (update) {
            inst = instructorDAO.update(inst);
        } else {
            inst = instructorDAO.save(inst);
        }

        return inst;
    }


    private CourseGroup getCourseGroup(VU360UserDetail user) {
        List<CourseGroup> courseGroups = courseDAO.getCourseGroupByContentOwner(
                user.getContentOwnerId(),
                WlcmsConstants.COURSE_GROUP_CLASSROOM);
        if (courseGroups.size() > 0) {
            return courseGroups.get(0);
        } else {
            CourseGroup cg = new CourseGroup();
            cg.setName(WlcmsConstants.COURSE_GROUP_CLASSROOM);
            cg.setDescription(WlcmsConstants.COURSE_GROUP_CLASSROOM);
            cg.setKeyword(WlcmsConstants.COURSE_GROUP_CLASSROOM);
            cg = courseGroupDAO.save(cg);
            return cg;
        }

    }

    private CourseDTO addOrUpdateCourse(VU360UserDetail user, CourseGroup courseGroup, CourseVO dto) {
        try {
            CourseDTO course = courseDAO.getCourseByOwnerIdAndBusinessKey((int) user.getContentOwnerId(), dto.getCourseId());
            boolean update = false;
            if (course != null) {
                update = true;
            } else {
                course = new CourseDTO();
                course.setBrandingId(Integer.valueOf(confDefaultBrandingId));
                course.setCeus(new BigDecimal(0));
                course.setBussinesskey(dto.getCourseId());
                course.setKeywords(dto.getCourseId());
                course.setProductPrice(BigDecimal.ONE);
                course.setCurrency(confDefaultCurrency);
                course.setBusinessunitId(Integer.valueOf(confDefaultBUId));
                course.setCreatedDate(new Date());
                course.setBusinessunitName(confDefaultBUName);
                course.setGuid(UUID.randomUUID().toString().replaceAll("-", ""));
                course.setContentownerId((int) user.getContentOwnerId());
                course.setDeliveryMethodId(DeliveryMethod.Classroom.getDeliveryMethod());
                course.setCourseType(CourseType.CLASSROOM_COURSE.getName());
                course.setCreateUserId(user.getAuthorId());
                course.setLanguage_id(1);
                course.setCode(confDefaultCode);
                course.setCourseGroup(new HashSet<CourseGroup>());
                course.getCourseGroup().add(courseGroup);
                course.setSource(user.getContentOwnerId() == 1 ? "360Training" : "eLM");
            }
            course.setLastUpdatedDate(new Date());
            modelMapper.map(dto, course);

            if (dto.getCourseProvider() != null && dto.getCourseProvider().getInstructorBackground() != null) {
                course.setAuthorBackground(dto.getCourseProvider().getInstructorBackground());
            }

            if (!StringUtils.isEmpty(dto.getInstructorEmail())) {
                course.setClassInstructorId(instructorDAO.findByContentOwnerIdAndEmail(user.getContentOwnerId(), dto.getInstructorEmail()).getId());
            }

            if (update) {
                course = courseDAO.updateCourse(course);
            } else {
                course = courseDAO.saveCourse(course);
            }

            return course;
        } catch(Exception ex) {
            logger.error("Course: " + dto.getCourseId());
            throw ex;
        }
    }

    private CourseProvider addOrUpdateCourseProvider(VU360UserDetail user, CourseVO courseDTO, CourseDTO course) {
        CourseProvider courseProvider = courseProviderDAO.loadProviderbyCourseId(course.getId());
        boolean update = false;
        if (courseProvider != null) {
            update = true;
        } else {
            courseProvider = new CourseProvider();
            courseProvider.setCourse(course);
        }
        modelMapper.map(courseDTO.getCourseProvider(), courseProvider);
        
        if (update) {
            courseProvider = courseProviderDAO.update(courseProvider);
        } else {
            courseProvider = courseProviderDAO.save(courseProvider);
        }

        return courseProvider;
    }

    private SynchronousClass addOrUpdateSyncClass(VU360UserDetail user, CourseDTO course, SyncClassDTO dto, CourseProviderDTO courseProvider) {
        SynchronousClass syncClass = syncClassDAO.getSynchronousClassByCourseIdAndClassName(course.getId(), dto.getClassName());
        boolean update = false;
        if (syncClass != null) {
            update = true;
            syncClass.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
        } else {
            syncClass = new SynchronousClass();
            syncClass.setCourse(course);
            syncClass.setGuid(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        syncClass.setLocation(locationDAO.getLocationByOwnerIdAndName((int) user.getContentOwnerId(), dto.getLocationName()));

        if(!StringUtils.isEmpty(dto.getInstructorEmail())) {
            syncClass.setClassInstructorId(instructorDAO.findByContentOwnerIdAndEmail(user.getContentOwnerId(), dto.getInstructorEmail()).getId());
        }

        syncClass.setTimeZoneId(timezoneMap.get(dto.getTimeZoneText().toLowerCase().trim()).intValue());
        syncClass.setClassStatus("Active");
        modelMapper.map(dto, syncClass);

        if (update) {
            syncClass = syncClassDAO.update(syncClass);
        } else {
            //some values, which are populated from the database directly are not getting populated.
            syncClass = syncClassDAO.saveClass(syncClass);

            //so populating them explicitly.
            syncClass = syncClassDAO.getById(syncClass.getId());
        }
        return syncClass;

    }


    private SynchronousSession addOrUpdateSyncSession(VU360UserDetail user, SynchronousClass syncClass, SyncSessionDTO dto) {

        SynchronousSession syncSession = syncClassDAO.getSyncSessionsByClassIdAndSessionKey(syncClass.getId(),dto.getKey());
        boolean update = false;
        if (syncSession != null) {
            update = true;
            syncSession.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
        } else {
            syncSession  = new SynchronousSession();
            syncSession.setSyncClass(syncClass);
            syncSession.setSessionKey(dto.getKey());
        }


        syncSession.setStartDateTime(dto.getStartDateTime());
        syncSession.setEndDateTime(dto.getEndDateTime());
        syncSession.setUpdateDate(new Date());

        Set<SynchronousSession> sessionSet = new HashSet<>();
        syncClass.setSyncSession(sessionSet);
        sessionSet.add(syncSession);

        syncClassDAO.saveClass(syncClass);
        return syncSession;
    }

    private void validateDefinedLocations(VU360UserDetail user, Collection<LocationDTO> locations) throws BulkUplaodCourseException {
        //validate locations are defined in database.
        if (locations.isEmpty()) {
            return;
        }

        Collection<String> locationNames = Collections2.transform(locations, new Function<LocationDTO, String>() {
            @Override
            public String apply(LocationDTO loc) {
                return loc.getLocationName();
            }
        });

        List<Location> dbDefinedLocs = locationDAO.getLocationsByOwnerIdAndNames((int) user.getContentOwnerId(), locationNames);
        Collection<String> notDefinedLocNames = CollectionHelper.getMissingItems(locationNames, dbDefinedLocs, new Function2<String, Location, Boolean>() {
            @Override
            public Boolean apply(String locName, Location loc) {
                return loc.getLocationname().trim().equalsIgnoreCase(locName.trim());
            }
        });

        if (notDefinedLocNames.size() > 0) {
            throw new BulkUplaodCourseException("Not Found In System", "Location", 0, 0,
                    "", Joiner.on(", ").join(notDefinedLocNames));
        }
    }

    private void validateUniqueCourses(final VU360UserDetail user, Collection<CourseVO> courses) throws BulkUplaodCourseException {
        //validate coruses are defined in database.
        if (courses.isEmpty()) {
            return;
        }

        Collection<String> businessKeys = Collections2.transform(courses, new Function<CourseVO, String>() {
            @Override
            public String apply(CourseVO course) {
                return course.getCourseId();
            }
        });

        List<CourseDTO> dbDefinedCourses = courseDAO.getCoursesByOwnerIdAndBusinessKeys((int) user.getContentOwnerId(), businessKeys);
        Collection<String> duplicateCourses = CollectionHelper.getFoundItems(businessKeys, dbDefinedCourses, new Function2<String, CourseDTO, String>() {

            @Override
            public String apply(String key, CourseDTO course) {
                if (key.equalsIgnoreCase(course.getBussinesskey().trim())) {
                    return course.getBussinesskey();
                }
                return null;
            }
        });

        if (duplicateCourses.size() > 0) {
            throw new BulkUplaodCourseException("Duplicate", "Course", 0, 0,
                    "", Joiner.on(", ").join(duplicateCourses));
        }
    }

    private void validateDefinedCourses(final VU360UserDetail user, Collection<CourseVO> courses) throws BulkUplaodCourseException {
        if (courses.isEmpty()) {
            return;
        }

        Collection<String> businessKeys = Collections2.transform(courses, new Function<CourseVO, String>() {
            @Override
            public String apply(CourseVO course) {
                return course.getCourseId();
            }
        });

        List<CourseDTO> dbDefinedCourses = courseDAO.getCoursesByOwnerIdAndBusinessKeys((int) user.getContentOwnerId(), businessKeys);
        Collection<String> missingCourses = CollectionHelper.getMissingItems(businessKeys, dbDefinedCourses, new Function2<String, CourseDTO, Boolean>() {
            @Override
            public Boolean apply(String key, CourseDTO course) {
                return key.equalsIgnoreCase(course.getBussinesskey().trim());
            }
        });

        if (missingCourses.size() > 0) {
            throw new BulkUplaodCourseException("Not Found In System", "Course", 0, 0,
                    "", Joiner.on(", ").join(missingCourses));
        }
    }

    private void validateUniqueClasses(CourseDTO course, Collection<SyncClassDTO> classes) throws BulkUplaodCourseException {
        //validate whether locations are defined in database.
        if (classes.isEmpty()) {
            return;
        }

        Collection<String> classNames = Collections2.transform(classes, new Function<SyncClassDTO, String>() {
            @Override
            public String apply(SyncClassDTO cls) {
                return cls.getClassName();
            }
        });

        Collection<SynchronousClass> dbDefinedClasses = syncClassDAO.getSynchronousClassByCourseIdAndClassNames(course.getId(), classNames);


        Collection<String> duplicateClasses = CollectionHelper.getFoundItems(classNames, dbDefinedClasses, new Function2<String, SynchronousClass, String>() {

            @Override
            public String apply(String className, SynchronousClass cls) {
                if (className.trim().equalsIgnoreCase(cls.getClassName().trim())) {
                    return className;
                }
                return null;
            }
        });

        if (duplicateClasses.size() > 0) {
            throw new BulkUplaodCourseException("Duplicate", "Class", 0, 0,
                    "", course.getBussinesskey() + " [" + Joiner.on(", ").join(duplicateClasses) + "]");
        }
    }

    private void validateDefinedClasses(CourseDTO course, Collection<SyncClassDTO> classes) throws BulkUplaodCourseException {

        //validate whether locations are defined in database.
        if (classes.isEmpty()) {
            return;
        }

        Collection<String> classNames = Collections2.transform(classes, new Function<SyncClassDTO, String>() {
            @Override
            public String apply(SyncClassDTO cls) {
                return cls.getClassName();
            }
        });

        Collection<SynchronousClass> dbDefinedClasses = syncClassDAO.getSynchronousClassByCourseIdAndClassNames(course.getId(), classNames);


        Collection<String> missigClasses = CollectionHelper.getMissingItems(classNames, dbDefinedClasses, new Function2<String, SynchronousClass, Boolean>() {
            @Override
            public Boolean apply(String className, SynchronousClass cls) {
                return className.trim().equalsIgnoreCase(cls.getClassName().trim());
            }
        });

        if (missigClasses.size() > 0) {
            throw new BulkUplaodCourseException("Not Found In System", "Class", 0, 0,
                    "", course.getBussinesskey() + " [" + Joiner.on(", ").join(missigClasses) + "]");
        }
    }

    private void validateUniqueLocations(VU360UserDetail user, Collection<LocationDTO> locations) throws BulkUplaodCourseException {
        //validate whether locations are defined in database.
        if (locations.isEmpty()) {
            return;
        }

        Collection<String> locationNames = Collections2.transform(locations, new Function<LocationDTO, String>() {
            @Override
            public String apply(LocationDTO loc) {
                return loc.getLocationName();
            }
        });

        List<Location> dbDefinedLocs = locationDAO.getLocationsByOwnerIdAndNames((int) user.getContentOwnerId(), locationNames);
        Collection<String> duplicateLocs = CollectionHelper.getFoundItems(locationNames, dbDefinedLocs, new Function2<String, Location, String>() {

            @Override
            public String apply(String key, Location loc) {
                if (key.equalsIgnoreCase(loc.getLocationname().trim())) {
                    return loc.getLocationname();
                }
                return null;
            }
        });

        if (duplicateLocs.size() > 0) {
            throw new BulkUplaodCourseException("Duplicate", "Location", 0, 0,
                    "", Joiner.on(", ").join(duplicateLocs));
        }
    }

    private void validateUniqueInstructors(VU360UserDetail user, Collection<ClassInstructorDTO> instructors) throws BulkUplaodCourseException {
        //validate whether instructors are defined in database.
        if (instructors.isEmpty()) {
            return;
        }

        Collection<String> instructorEmails = Collections2.transform(instructors, new Function<ClassInstructorDTO, String>() {
            @Override
            public String apply(ClassInstructorDTO inst) {
                return inst.getEmail();
            }
        });

        List<ClassInstructor> dbDefinedInstructors = instructorDAO.findByContentOwnerIdAndEmails(user.getContentOwnerId(), instructorEmails);
        Collection<String> duplicateInstructors = CollectionHelper.getFoundItems(instructorEmails, dbDefinedInstructors, new Function2<String, ClassInstructor, String>() {

            @Override
            public String apply(String key, ClassInstructor inst) {
                if (key.equalsIgnoreCase(inst.getEmail().trim())) {
                    return inst.getEmail();
                }
                return null;
            }
        });

        if (duplicateInstructors.size() > 0) {
            throw new BulkUplaodCourseException("Duplicate", "Instructor", 0, 0,
                    "", Joiner.on(", ").join(duplicateInstructors));
        }
    }

    private void validateDefinedInstructors(VU360UserDetail user, Collection<ClassInstructorDTO> instructors) throws BulkUplaodCourseException {
        //validate instructors are defined in database.
        if (instructors.isEmpty()) {
            return;
        }

        Collection<String> instructorEmails = Collections2.transform(instructors, new Function<ClassInstructorDTO, String>() {
            @Override
            public String apply(ClassInstructorDTO loc) {
                return loc.getEmail();
            }
        });

        List<ClassInstructor> dbDefinedinstructors = instructorDAO.findByContentOwnerIdAndEmails(user.getContentOwnerId(), instructorEmails);
        Collection<String> notDefinedLocNames = CollectionHelper.getMissingItems(instructorEmails, dbDefinedinstructors, new Function2<String, ClassInstructor, Boolean>() {
            @Override
            public Boolean apply(String email, ClassInstructor loc) {
                return loc.getEmail().trim().equalsIgnoreCase(email.trim());
            }
        });

        if (notDefinedLocNames.size() > 0) {
            throw new BulkUplaodCourseException("Not Found In System", "Classinstructor", 0, 0,
                    "", Joiner.on(", ").join(notDefinedLocNames));
        }
    }


    //validate whether locations are defined in database.
    private void validateUniqueSessions(SynchronousClass cls, Collection<SyncSessionDTO> sessions) throws BulkUplaodCourseException {
        if (sessions.isEmpty()) {
            return;
        }

        Collection<String> sessionKeys = Collections2.transform(sessions, new Function<SyncSessionDTO, String>() {
            @Override
            public String apply(SyncSessionDTO sessionDto) {
                return sessionDto.getKey();
            }
        });

        Collection<SynchronousSession> dbDefinedSessions = syncClassDAO.getSyncSessionsByClassIdAndSessionKeys(cls.getId(), sessionKeys);


        Collection<String> duplicateSessions = CollectionHelper.getFoundItems(sessionKeys, dbDefinedSessions, new Function2<String, SynchronousSession, String>() {

            @Override
            public String apply(String sessionKey, SynchronousSession session) {
                if (sessionKey.equalsIgnoreCase(session.getSessionKey())) {
                    return sessionKey;
                }
                return null;
            }
        });

        if (duplicateSessions.size() > 0) {
            throw new BulkUplaodCourseException("Duplicate", "Session", 0, 0,
                    "", "(" + cls.getCourse().getBussinesskey() + "," + cls.getClassName() + ") " + "[" + Joiner.on(", ").join(duplicateSessions) + "]");
        }
    }

    private void validateDefinedSessions(SynchronousClass cls, Collection<SyncSessionDTO> sessions) throws BulkUplaodCourseException {
        //validate whether sessions are defined in database.
        if (sessions.isEmpty()) {
            return;
        }

        Collection<String> sessionKeys = Collections2.transform(sessions, new Function<SyncSessionDTO, String>() {
            @Override
            public String apply(SyncSessionDTO sessionDto) {
                return sessionDto.getKey();
            }
        });

        Collection<SynchronousSession> dbDefinedSessions = syncClassDAO.getSyncSessionsByClassIdAndSessionKeys(cls.getId(), sessionKeys);


        Collection<String> missingSessions = CollectionHelper.getMissingItems(sessionKeys, dbDefinedSessions, new Function2<String, SynchronousSession, Boolean>() {
            @Override
            public Boolean apply(String key, SynchronousSession session) {
                return key.equalsIgnoreCase(session.getSessionKey());
            }
        });

        if (missingSessions.size() > 0) {
            throw new BulkUplaodCourseException("Not Found In System", "Session", 0, 0,
                    "", "(" + cls.getCourse().getBussinesskey() + "," + cls.getClassName() + ") " + "[" + Joiner.on(", ").join(missingSessions) + "]");
        }
    }

}
