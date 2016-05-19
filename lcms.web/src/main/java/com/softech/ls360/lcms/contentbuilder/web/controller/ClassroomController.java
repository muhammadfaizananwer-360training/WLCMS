package com.softech.ls360.lcms.contentbuilder.web.controller;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.softech.common.parsing.TabularDataException;
import com.softech.common.parsing.TabularDataExceptionList;
import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Calendar;
import java.util.Comparator;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.ls360.lcms.contentbuilder.service.*;
import com.softech.ls360.lcms.contentbuilder.utils.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.service.impl.ClassroomCourseServiceImpl;
import com.softech.ls360.lcms.contentbuilder.upload.FileUploader;
import com.softech.ls360.lcms.contentbuilder.web.model.ClassroomModel;
import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;
import com.softech.ls360.lcms.contentbuilder.web.vo.SynchronousClassVO;
import com.softech.ls360.lcms.contentbuilder.web.vo.SynchronousSessionVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ClassroomController {

    private static Logger logger = LoggerFactory.getLogger(ClassroomController.class);

    @Autowired
    ISynchronousClassService classService;

    @Autowired
    ICourseService courseService;

    @Autowired
    IClassroomCourseService classroomService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ILocationService locationService;

    @Autowired
    public IClassInstructorService classInstructorService;

    @Autowired
    @Qualifier("ppt")
    FileUploader pptFileUploader;

    @Autowired
    private VU360UserService userService;

    final String COURSE_STATUS = "Active";
    final String CLASS_UNLIMITTED = "Unlimited";
    private List<String> weekDaysList = new ArrayList<String>();
    private static final String EVERYDAY = "EveryDay";
    private static final String EVERYWEEKDAY = "EveryWeekDay";
    SimpleDateFormat formatDateWithTime = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    DateFormat formatonlyDate = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat formatonlyDateDisplay = new SimpleDateFormat("EEE MMM. dd, yyyy");
    SimpleDateFormat formatOnlyTime = new SimpleDateFormat("h:mm a");
    final String expectedPattern = "MM/dd/yyyy hh:mm a";
    private Date classStartDate = new Date();
    private Date classEndDate = new Date();

    @RequestMapping(value = "importClassroomCourses", method = RequestMethod.POST)
    public @ResponseBody
    Object importClassroomCourses(@RequestParam(name = "filePath") String filePath, @RequestParam(name = "ignoreErrors", defaultValue = "false") boolean ignoreErrors) throws Exception {
        VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return importClassroomCourses(filePath, ignoreErrors, user);
    }

    public RestResponse importClassroomCourses(String filePath, boolean ignoreErrors, VU360UserDetail user) throws Exception {
        String fullPath = FileUtils.getTempDirectoryPath() + IOUtils.DIR_SEPARATOR + filePath;

        byte[] fileData = null;
        try (FileInputStream stream = new FileInputStream(fullPath)) {
            fileData = IOConvertor.convertStreamToDataBytes(stream);
        }
        RestResponse response = new RestResponse();
        boolean deleteFile = false;
        try {
            classroomService.importCourses(user, fileData, ignoreErrors);
            deleteFile = true;
        } catch (BulkUplaodCourseException ex) {
            logger.error(ex.getMessage()
                    + "\nSheet Name: " + ex.getTableName()
                    + "\nRow Number: " + ex.getRowNumber()
                    + "\nColumn Number: " + ex.getColumnIndex()
                    + "\nColumn: " + ex.getColumnName()
                    + "\nErrorText: " + ex.getErrorText());
            response.setError("classroomImportPage-error-" + ex.getMessage());
            response.setData(ex.getExceptionDetail());
            deleteFile = true;
        } catch (final TabularDataExceptionList ex) {
            response.setError("classroomImportPage-error-List");
            final Collection<TabularDataException.ExceptionDetail> errorList = Collections2.transform(ex.getErrorList(), new Function<TabularDataException, TabularDataException.ExceptionDetail>() {
                @Override
                public TabularDataException.ExceptionDetail apply(TabularDataException tabularEx) {
                    return tabularEx.getExceptionDetail();
                }
            });
            response.setData(new Object() {
                public Object errors = errorList;
                public Object showstopper = ex.isShowstopper();
            });

            deleteFile = ex.isShowstopper();
        } catch (TabularDataException ex) {
            logger.error(ex.getMessage()
                    + "\nSheet Name: " + ex.getTableName()
                    + "\nRow Number: " + ex.getRowNumber()
                    + "\nColumn Number: " + ex.getColumnIndex()
                    + "\nColumn: " + ex.getColumnName()
                    + "\nErrorText: " + ex.getErrorText());
            response.setError("classroomImportPage-error-" + ex.getMessage());
            response.setData(ex.getExceptionDetail());
            deleteFile = true;
        } catch (Exception e) {
            response.setError("classroomImportPage-error");
            response.setData(e.toString());
            logger.error(e.getMessage(), e);
            deleteFile = true;
        }

        if (deleteFile) {
            if(!new File(fullPath).delete())
                logger.warn("File: "+fullPath+" could not be deleted");
        }

        return response;

    }
    
    public RestResponse importClassroomCoursesToDtos(String filePath, boolean ignoreErrors, VU360UserDetail user) throws IOException {
        String fullPath = FileUtils.getTempDirectoryPath() + IOUtils.DIR_SEPARATOR + filePath;

        byte[] fileData = null;
        try (FileInputStream stream = new FileInputStream(fullPath)) {
            fileData = IOConvertor.convertStreamToDataBytes(stream);
        }
        RestResponse response = new RestResponse();
        try {
            ClassroomImportDTO classroom =  classroomService.importCoursesToDtos(user, fileData, ignoreErrors);
            for(CourseVO course : classroom.getCourses()){
                if(course.getCourseProvider() != null) {
                    course.getCourseProvider().setCourse(null);
                }
                for(SyncClassDTO synClass : course.getSyncClassesMap().values()){
                      synClass.setCourse(null);
                      for( SyncSessionDTO synSession : synClass.getSessions()){
                         synSession.setSyncClass(null);
                      }
                }
            }
            response.setData(classroom);
        } catch (Exception e) {
            response.setError("classroomImportPage-error");
            response.setData(e.toString());
            logger.error(e.getMessage(), e);
        }

        if (!new File(fullPath).delete())
            logger.warn("File: " + fullPath + " could not be deleted");

        return response;

    }
    public RestResponse importClassroomCourses(ClassroomImportDTO classroom, VU360UserDetail user) {
        return  importClassroomCourses(classroom,user,false);
    }
    
    public RestResponse importClassroomCourses(ClassroomImportDTO classroom, VU360UserDetail user,boolean ignoreCourse) {

        RestResponse response = new RestResponse();
        try {
            classroomService.importCourses(user, classroom,ignoreCourse);
            response.setInfo("done");
        } catch (Exception e) {
            response.setError("classroomImportPage-error");
            response.setData(e.toString());
            logger.error(e.getMessage(), e);
        }
        return response;
    }

    @RequestMapping(value = "classroomImportPage", method = RequestMethod.GET)
    public ModelAndView showclassroomImportPage() {
        ModelAndView view = new ModelAndView("classroomImportPage");
        return view;
    }

    @RequestMapping(value = "classroom-classes/action/update", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse updateClass(@RequestBody ClassroomModel.ClassUpdate model) {
        RestResponse response = new RestResponse();
        try {
            SynchronousClass clazz = classService.getSynchronousClassById(model.getId());
            modelMapper.map(model, clazz);

            //set location.
            if (model.getLocId() != clazz.getLocation().getId()) {
                Location location = new Location();
                location.setId(model.getLocId());
                clazz.setLocation(locationService.findLocationById(location.getId()));
                clazz.setUpdateDate(new Date());
                clazz.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
                clazz.setClassInstructorId(model.getClassInsId());
            }

            classService.saveSynchronousClass(clazz);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            response.setError((ex.getMessage() == null) ? "unhandled exception" : ex.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "saveClass", method = RequestMethod.POST)
    public @ResponseBody
    SynchronousClassVO saveClass(HttpServletRequest request, HttpServletResponse response) throws Exception {

        VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String classSize = request.getParameter("classSize");
        String locationId = request.getParameter("classLocation");
        String rdoClassroomlimit = request.getParameter("rdoClassroomlimit");
        String courseId = request.getParameter("varCourseId");
        String classTitle = request.getParameter("classTitle");
        String timeZone = request.getParameter("classTimezome");
        String enrollmentCloseDate = request.getParameter("enrollmentCloseDate");
        String classInstructor = request.getParameter("classInstructor");
        String GUID = UUID.randomUUID().toString().replaceAll("-", "");

        SynchronousClass scForSessAdd = new SynchronousClass();
        Location objLocation = new Location();
        objLocation.setId(Long.parseLong(locationId));
        Location location = locationService.findLocationById(objLocation.getId());
        CourseDTO secondCourse = courseService.getCourseById(TypeConvertor.AnyToLong(courseId));//Long.valueOf(courseId));
        secondCourse.setLastUpdatedDate(new Date());
        secondCourse.setLastUpdateUser(principal.getAuthorId());
        scForSessAdd.setCourse(secondCourse);
        scForSessAdd.setClassInstructorId(TypeConvertor.AnyToLong(classInstructor));
        scForSessAdd.setClassName(classTitle);
        scForSessAdd.setClassStatus(COURSE_STATUS);
        scForSessAdd.setEnrollmentCloseDate(formatDateWithTime.parse(enrollmentCloseDate + " 23:59:59"));
        scForSessAdd.setTimeZoneId(Integer.valueOf(timeZone));
        scForSessAdd.setGuid(GUID);
        if (rdoClassroomlimit != null && rdoClassroomlimit.equals(CLASS_UNLIMITTED)) {
            scForSessAdd.setMaximumClassSize(Long.MAX_VALUE);
        } else {
            scForSessAdd.setMaximumClassSize(Long.valueOf(classSize));
        }

        scForSessAdd.setLocation(location);

        //add instructor info.
        List<SynchronousClass> lstSyncClass = classService.getSynchronousClassByCourseId(Long.parseLong(courseId));
        if (lstSyncClass != null && lstSyncClass.size() > 0) {
            SynchronousClass objSyncClass = lstSyncClass.get(0);
            scForSessAdd.setPresenterFirstName(objSyncClass.getPresenterFirstName());
            scForSessAdd.setPresenterLastName(objSyncClass.getPresenterLastName());
            scForSessAdd.setPresenterEmail(objSyncClass.getPresenterEmail());
            scForSessAdd.setPresenterPhone(objSyncClass.getPresenterPhone());
        }

        SynchronousClass objsClass = classService.saveSynchronousClass(scForSessAdd);
        SynchronousClassVO objClassVO = new SynchronousClassVO();
        objClassVO.setSyncClassId(objsClass.getId());
        return objClassVO;
    }

    @RequestMapping(value = "classroom-classes", method = RequestMethod.GET)
    public ModelAndView classes(@RequestParam("id") Long courseId) throws SQLException {
        ModelAndView view = new ModelAndView("classroom-class");
        List<SynchronousClass> classes = classService.getSynchronousClassByCourseId(courseId);
        CourseDTO course = courseService.getCourseById(TypeConvertor.AnyToLong(courseId));//Long.valueOf(courseId));

        // get locations collection w.r.t contentOwner
        Map<String, Object> queryParams = new HashMap<String, Object>();
        VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        queryParams.put("contentownerId", (int) user.getContentOwnerId());
        List<Location> locationList = locationService.getLocations(queryParams);
        List<ClassInstructor> classInstructors=classInstructorService.findByContentOwnerId(user.getContentOwnerId());

        view.addObject("classes", classes);
        view.addObject("course", course);
        view.addObject("timezones", classService.getAllTimezone(null));
        view.addObject("locations", locationList);
        view.addObject("classInstructors", classInstructors);
        view.addObject("timings", getTimings());
        view.addObject("longMax", Long.MAX_VALUE);

        //for creating new class
        SynchronousClass newClass = new SynchronousClass();
        view.addObject("newClass", newClass);

        return view;
    }

    // send to Schedule page
    @RequestMapping(value = "getSessions", method = RequestMethod.POST)
    public @ResponseBody
    List<SynchronousSessionVO> getSessions(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String classId = request.getParameter("varClassId");
        SynchronousClass lstSyncClass = classService.getSynchronousClassById(TypeConvertor.AnyToLong(classId));//Long.valueOf(classId));
        return this.setScheduleFormFields(lstSyncClass);
    }

    public List<String> getTimings() {
        int interval = 30;
        List<String> timings = new ArrayList<String>();

        for (int i = 0; i < (24 * 60); i += interval) {
            int hour = i / 60;
            int min = i % 60;

            String ampm = "";
            ampm = " AM";
            if (hour == 0) {
                hour = 12;
            } else if (hour > 12) {
                hour = hour - 12;
                ampm = " PM";
            }

            timings.add(String.format("%02d:%02d", hour, min) + ampm);
        }

        return timings;
    }

    public List<SynchronousSessionVO> setScheduleFormFields(SynchronousClass lstSyncClass) {
        SynchronousSessionVO vo = null;
        List<SynchronousSessionVO> lstSessionsVO = new ArrayList<SynchronousSessionVO>();
        Set<SynchronousSession> lstSessions = lstSyncClass.getSyncSession();
        SynchronousSession[] sortedArray = lstSessions.toArray(new SynchronousSession[lstSessions.size()]);
        Arrays.sort(sortedArray, new Comparator<SynchronousSession>() {
            @Override
            public int compare(SynchronousSession o1, SynchronousSession o2) {
                return o1.getStartDateTime().compareTo(o2.getStartDateTime());
            }
        });

        for (SynchronousSession sc1aaa : sortedArray) {
            vo = new SynchronousSessionVO();
            vo.setSessionKey(sc1aaa.getSessionKey());
            vo.setId(sc1aaa.getId().toString());
            if (sc1aaa.getStartDateTime() != null) {
                vo.setStartDate(formatonlyDate.format(sc1aaa.getStartDateTime()));
                vo.setStartDateDisplay(formatonlyDateDisplay.format(sc1aaa.getStartDateTime()));
                vo.setStartTime(formatOnlyTime.format(sc1aaa.getStartDateTime()));
            }
            if (sc1aaa.getEndDateTime() != null) {
                vo.setEndTime(formatOnlyTime.format(sc1aaa.getEndDateTime()));
                vo.setEndDate(formatonlyDate.format(sc1aaa.getEndDateTime()));
                vo.setEndDateDisplay(formatonlyDateDisplay.format(sc1aaa.getEndDateTime()));
            }
            lstSessionsVO.add(vo);
        }

        return lstSessionsVO;
    }

    @RequestMapping(value = "saveSession", method = RequestMethod.POST)
    public @ResponseBody
    String saveSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String classId = request.getParameter("classId");
        String startDate = request.getParameter("sDate");
        String endDate = request.getParameter("endDate");
        String start_time = request.getParameter("stime");
        String end_time = request.getParameter("etime");
        String recurring_pattren = request.getParameter("recurringPattren");
        String daily_session = request.getParameter("dailysession");
        String number_days = request.getParameter("dailysessiondays");//add_sess_number_days
        String strendoccurance = request.getParameter("endDatedays");//
        String strnumber_week_days = request.getParameter("add_sess_number_week_days");
        String str_sunday = request.getParameter("chkdSunday");
        String str_monday = request.getParameter("chkdMonday");
        String str_tuesday = request.getParameter("chkdTuesday");
        String str_wednesday = request.getParameter("chkdWednesday");
        String str_thursday = request.getParameter("chkdThursday");
        String str_friday = request.getParameter("chkdFriday");
        String str_saturday = request.getParameter("chkdSaturday");
        String add_sess_number_week_days = request.getParameter("add_sess_number_week_days");
        String completeEndDate = null;
        String completeStartDate = null;
        Integer session_days = null;
        Integer endoccurance = null;
        Integer number_week_days = null;

        Calendar classstartDateCal = Calendar.getInstance();
        classstartDateCal.setTime(StringToDate(startDate + " " + start_time));
        this.classStartDate = classstartDateCal.getTime();

        if (endDate != null && endDate.length() > 0) {
            Calendar classendDateCal = Calendar.getInstance();

            classendDateCal.setTime(StringToDate(endDate + " " + end_time));
            this.classEndDate = classendDateCal.getTime();
        }

        if (strendoccurance != null) {
            endoccurance = Integer.parseInt(strendoccurance);
        }

        SynchronousClass syncClass = classService.getSynchronousClassById(TypeConvertor.AnyToLong(classId));//Long.valueOf(classId));
        syncClass.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
       // completeStartDate = startDate + " " + start_time;
        //completeEndDate = endDate + " " + end_time;

        if (recurring_pattren.equals("manual")) {
            Set<SynchronousSession> lstSession = generateManualSessions(startDate, endDate, start_time, end_time, syncClass);
            syncClass.setSyncSession(lstSession);
            syncClass.setClassStartDate(this.classStartDate);
            syncClass.setClassEndDate(this.classEndDate);
            classService.saveSynchronousClass(syncClass);
        } else if (recurring_pattren.equals("weekly")) {
            if (add_sess_number_week_days != null) {
                number_week_days = Integer.parseInt(add_sess_number_week_days);
            }

            generateWeekDays(str_sunday, str_monday, str_tuesday, str_wednesday, str_thursday, str_friday, str_saturday);
            Set<SynchronousSession> lstSession = generateWeeklySessions(weekDaysList, startDate, endDate, start_time, end_time, recurring_pattren, endoccurance, number_week_days, syncClass, strnumber_week_days);
            /**
             * it will generate sessions in classSessionList member variable
             */
            if (lstSession != null && lstSession.size() <= 0) {
                return "daterangeerror";
            }
            syncClass.setSyncSession(lstSession);
            syncClass.setClassStartDate(this.classStartDate);
            syncClass.setClassEndDate(this.classEndDate);
            classService.saveSynchronousClass(syncClass);
        } else if (recurring_pattren.equals("daily")) {
            if (number_days != null) {
                session_days = Integer.parseInt(number_days);
            }
            if (strendoccurance != null) {
                endoccurance = Integer.parseInt(strendoccurance);
            }

            generateWeekDaysList(daily_session);
            Set<SynchronousSession> lstSession = generateDailySessions(weekDaysList, startDate, endDate, start_time, end_time, session_days, recurring_pattren, daily_session, endoccurance, syncClass);
            if (lstSession != null && lstSession.size() <= 0) {
                return "daterangeerror";
            }
            syncClass.setSyncSession(lstSession);
            syncClass.setClassStartDate(this.classStartDate);
            syncClass.setClassEndDate(this.classEndDate);

            //generate session keys
            String sessionkeyPrefix  = StringUtil.getRandom(5);
            {
                int i = 0;
                for (SynchronousSession session : lstSession) {
                    String sessionKey = sessionkeyPrefix + "-" + org.apache.commons.lang.StringUtils.leftPad(Integer.valueOf(++i).toString(),2,'0');
                    session.setSessionKey(sessionKey);
                }
            }
            classService.saveSynchronousClass(syncClass);
        }

        return "success";
    }

    @RequestMapping(value = "saveManualSession", method = RequestMethod.POST)
    public @ResponseBody
    SynchronousSessionVO saveManualSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String classId = request.getParameter("classId");
        String startDate = request.getParameter("sDate");
        String endDate = request.getParameter("endDate");
        String start_time = request.getParameter("stime");
        String end_time = request.getParameter("etime");
        String sessionId = request.getParameter("sessionId");
        String sessionKey = request.getParameter("sessionKey");
        SynchronousSessionVO syncVO = new SynchronousSessionVO();
        SynchronousClass syncClass = classService.getSynchronousClassById(Long.parseLong(classId));
        Set<SynchronousSession> lstSession = new HashSet<SynchronousSession>();
        SynchronousSession objSyncSess = new SynchronousSession();

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(StringToDate(startDate + " " + start_time));

        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTime(StringToDate(endDate + " " + end_time));

        objSyncSess.setStartDateTime(startDateCal.getTime());
        objSyncSess.setEndDateTime(endTimeCal.getTime());
        objSyncSess.setSyncClass(syncClass);
        objSyncSess.setSessionKey(sessionKey);
        if (!StringUtils.isEmpty(sessionId)) {
            objSyncSess.setId(TypeConvertor.AnyToLong(sessionId));//Long.valueOf(sessionId));
            objSyncSess.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
            objSyncSess.setUpdateDate(new Date());
        }

        syncClass.setStatus(SynchronousClassSessionStatusEnum.UPDATE.getStatus());
        syncClass.setUpdateDate(new Date());
        lstSession.add(objSyncSess);
        syncClass.setSyncSession(lstSession);
        classService.saveSynchronousClass(syncClass);

        syncVO.setStartDate(startDate);
        syncVO.setStartTime(start_time);
        syncVO.setEndDate(endDate);
        syncVO.setEndTime(end_time);

        return syncVO;
    }

    private Set<SynchronousSession> generateManualSessions(String inputStartDate, String inputEndDate, String inputStartTime, String inputEndTime, SynchronousClass syncClass) {
        SynchronousSession objSession = null;
        Set<SynchronousSession> lstSession = new HashSet<SynchronousSession>();
        LCMS_Util lcmsUtil = new LCMS_Util();
        int numberofdays = 1;

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(StringToDate(inputStartDate + " " + inputStartTime));

        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTime(StringToDate(inputStartDate + " " + inputEndTime));

        /* WLCMS-1874 need increase one day from start day if under calling method condition applies */
        updateEndDatebyCompareDates(startDateCal, endTimeCal);
        /* END */

        Calendar endDateCal = Calendar.getInstance();
        if (inputEndDate != null && inputEndDate.length() > 0) {
            endDateCal.setTime(StringToDate(inputEndDate + " " + inputEndTime));
        }

        if (inputEndDate != null && inputEndDate.length() > 0) {
            endDateCal.setTime(lcmsUtil.getDate(inputEndDate + " " + inputEndTime, expectedPattern));

            for (Calendar startCal = (Calendar) startDateCal.clone(); startCal.compareTo(endDateCal) <= 0; startCal.add(Calendar.DATE, numberofdays)) {
                objSession = new SynchronousSession();
                objSession.setStartDateTime(startCal.getTime());
                objSession.setEndDateTime(endTimeCal.getTime());
                objSession.setSyncClass(syncClass);
                lstSession.add(objSession);
                endTimeCal.add(Calendar.DATE, numberofdays);
            }
        }
        return lstSession;
    }

    private Set<SynchronousSession> generateDailySessions(List<String> inputWeekDaysList, String inputStartDate, String inputEndDate, String inputStartTime, String inputEndTime, int numberofweekdays, String radioDaily, String dailysession, int totalNoOcurrences, SynchronousClass syncClass) {
        SynchronousSession objSession = null;
        Set<SynchronousSession> lstSession = new HashSet<SynchronousSession>();
        int totalNoOcurrencesCount = 0;
        int maxOcurrencesCount = 30;
        int WeekDaysNumber = 0;
        //Calendar startDateCal = Calendar.getInstance();
        //Calendar endDateCal = Calendar.getInstance();
        LCMS_Util lcmsUtil = new LCMS_Util();

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(StringToDate(inputStartDate + " " + inputStartTime));
        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTime(StringToDate(inputStartDate + " " + inputEndTime));
        Calendar endDateCal = Calendar.getInstance();
        /* WLCMS-1874 need increase one day from start day if under calling method condition applies */
        updateEndDatebyCompareDates(startDateCal, endTimeCal);
        /* END */

        if (inputEndDate != null && inputEndDate.length() > 0) {
            endDateCal.setTime(StringToDate(inputEndDate + " " + inputEndTime));
        }

        if (numberofweekdays <= 0) {
            numberofweekdays = 1;
        }

        if (radioDaily.equals("daily")) {
            if (dailysession.equals("EveryDay")) {
                if (inputEndDate != null && inputEndDate.length() > 0) {
                    endDateCal.setTime(lcmsUtil.getDate(inputEndDate + " " + inputEndTime, expectedPattern));
                    //endDateCal.setTime(StringToDate(inputEndDate));
                    for (Calendar startCal = (Calendar) startDateCal.clone(); startCal.compareTo(endDateCal) <= 0; startCal.add(Calendar.DATE, numberofweekdays)) {
                        String weekDays = lcmsUtil.getFormatedDateInfo(startCal.getTime(), "EE");
                        try {
                            for (String weekDay : inputWeekDaysList) {
                                if (weekDay.equalsIgnoreCase(weekDays)) {
                                    totalNoOcurrencesCount++;
                                    objSession = new SynchronousSession();
                                    objSession.setStartDateTime(startCal.getTime());
                                    objSession.setEndDateTime(endTimeCal.getTime());
                                    objSession.setSyncClass(syncClass);
                                    lstSession.add(objSession);
                                }
                            }
                            endTimeCal.add(Calendar.DATE, numberofweekdays);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    for (Calendar startCal = (Calendar) startDateCal.clone(); totalNoOcurrencesCount < totalNoOcurrences; startCal.add(Calendar.DATE, numberofweekdays)) {
                        String weekDays = lcmsUtil.getFormatedDateInfo(startCal.getTime(), "EE");
                        try {
                            for (String weekDay : inputWeekDaysList) {
                                if (weekDay.equalsIgnoreCase(weekDays)) {
                                    totalNoOcurrencesCount++;
                                    objSession = new SynchronousSession();
                                    objSession.setStartDateTime(startCal.getTime());
                                    objSession.setEndDateTime(endTimeCal.getTime());
                                    objSession.setSyncClass(syncClass);
                                    lstSession.add(objSession);

                                    if (totalNoOcurrencesCount <= totalNoOcurrences) {
                                        break;
                                    }
                                }
                            }
                            endTimeCal.add(Calendar.DATE, numberofweekdays);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (dailysession.equals("EveryWeekDay")) {
                if (inputEndDate != null && inputEndDate.length() > 0) {
                    endDateCal.setTime(lcmsUtil.getDate(inputEndDate + " " + inputEndTime, expectedPattern));

                    if (dailysession.equals("EveryWeekDay")) {
                        WeekDaysNumber = 1;
                    }

                    for (Calendar startCal = (Calendar) startDateCal.clone(); startCal.compareTo(endDateCal) <= 0; startCal.add(Calendar.DATE, WeekDaysNumber)) {
                        String weekDays = lcmsUtil.getFormatedDateInfo(startCal.getTime(), "EE");
                        try {
                            for (String weekDay : inputWeekDaysList) {
                                if (weekDay.equalsIgnoreCase(weekDays)) {
                                    totalNoOcurrencesCount++;
                                    objSession = new SynchronousSession();
                                    objSession.setStartDateTime(startCal.getTime());
                                    objSession.setEndDateTime(endTimeCal.getTime());
                                    this.classEndDate = endTimeCal.getTime();
                                    objSession.setSyncClass(syncClass);
                                    lstSession.add(objSession);
                                }
                            }
                            endTimeCal.add(Calendar.DATE, WeekDaysNumber);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    if (dailysession.equals("EveryWeekDay")) {
                        WeekDaysNumber = 1;
                    }

                    for (Calendar startCal = (Calendar) startDateCal.clone(); totalNoOcurrencesCount < totalNoOcurrences; startCal.add(Calendar.DATE, WeekDaysNumber)) {
                        String weekDays = lcmsUtil.getFormatedDateInfo(startCal.getTime(), "EE");
                        try {
                            for (String weekDay : inputWeekDaysList) {
                                if (weekDay.equalsIgnoreCase(weekDays)) {
                                    totalNoOcurrencesCount++;

                                    objSession = new SynchronousSession();
                                    objSession.setStartDateTime(startCal.getTime());
                                    objSession.setEndDateTime(endTimeCal.getTime());
                                    this.classEndDate = endTimeCal.getTime();
                                    objSession.setSyncClass(syncClass);
                                    lstSession.add(objSession);

                                    if (totalNoOcurrencesCount <= totalNoOcurrences) {
                                        break;
                                    }
                                }
                            }
                            endTimeCal.add(Calendar.DATE, WeekDaysNumber);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }
        return lstSession;
    }

    /* WLCMS-1874 need increase one day from start day if below condition applies */
 /* If end date and start date not same in that case below condition will be execute */
 /* suppose user enter start time in PM and end time in AM, end date must be next from start date.*/
    private void updateEndDatebyCompareDates(Calendar startDateCal, Calendar endTimeCal) {

        if (startDateCal.after(endTimeCal)) {
            endTimeCal.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    /* END */
    private Set<SynchronousSession> generateWeeklySessions(List<String> inputWeekDaysList, String inputStartDate, String inputEndDate, String inputStartTime, String inputEndTime, String radioDaily, int numberofweekdays, int WeekDaysNumber, SynchronousClass syncClass, String strNumberWeekDays) {
        SynchronousSession objSession = null;
        int occurrenceWeek = Integer.parseInt(strNumberWeekDays);
        Set<SynchronousSession> lstSession = new HashSet<SynchronousSession>();
        int totalNoOcurrencesCount = 0;
        int maxOcurrencesCount = 30;
        int week_number = 0;
        int week_counter = 0;
        //int WeekDaysNumber = 0;
        //Calendar startDateCal = Calendar.getInstance();
        //Calendar endDateCal = Calendar.getInstance();
        LCMS_Util lcmsUtil = new LCMS_Util();

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(StringToDate(inputStartDate + " " + inputStartTime));
        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTime(StringToDate(inputStartDate + " " + inputEndTime));
        Calendar endDateCal = Calendar.getInstance();

        /* WLCMS-1874 need increase one day from start day if under calling method condition applies */
        updateEndDatebyCompareDates(startDateCal, endTimeCal);
        /* END */

        if (inputEndDate != null && inputEndDate.length() > 0) {
            endDateCal.setTime(StringToDate(inputEndDate + " " + inputEndTime));
        }

        week_number = WeekDaysNumber;

        if (WeekDaysNumber <= 1) {

            WeekDaysNumber = 1;

        } else if (WeekDaysNumber == 2) {
            WeekDaysNumber = 7;
        } else if (WeekDaysNumber > 2) {
            WeekDaysNumber = ((WeekDaysNumber - 1) * 7);
        }

        if (radioDaily.equals("weekly")) {
            if (inputEndDate != null && inputEndDate.length() > 0) {
                endDateCal.setTime(lcmsUtil.getDate(inputEndDate + " " + inputEndTime, expectedPattern));
                //endDateCal.setTime(StringToDate(inputEndDate));
                for (Calendar startCal = (Calendar) startDateCal.clone(); startCal.compareTo(endDateCal) <= 0;) //startCal.add(Calendar.DAY_OF_MONTH, WeekDaysNumber )) 
                {

                    for (int dayCounter = 0; dayCounter < 7; dayCounter++) {
                        String currentDay = lcmsUtil.getFormatedDateInfo(startCal.getTime(), "EE");

                        try {

                            /*if(startCal.compareTo(endDateCal) < 0 && WeekDaysNumber >= 1 ) {
									
									startCal.add(Calendar.DAY_OF_MONTH, 1 );
									if(endTimeCal.before(endDateCal))
										endTimeCal.add(Calendar.DAY_OF_MONTH, 1 );
								}*/
                            if (inputWeekDaysList.contains(currentDay) && startCal.compareTo(endDateCal) < 0 && WeekDaysNumber >= 1) {
                                totalNoOcurrencesCount++;
                                objSession = new SynchronousSession();
                                objSession.setStartDateTime(startCal.getTime());
                                objSession.setEndDateTime(endTimeCal.getTime());
                                objSession.setSyncClass(syncClass);
                                lstSession.add(objSession);
                                startCal.add(Calendar.DAY_OF_MONTH, 1);
                                //if(endTimeCal.before(endDateCal))
                                //endTimeCal.add(Calendar.DAY_OF_MONTH, 1 );
                                updateEndDatebyCompareDates(endDateCal, endTimeCal);
                            } else {
                                startCal.add(Calendar.DAY_OF_MONTH, 1);
                                updateEndDatebyCompareDates(endDateCal, endTimeCal);
                                //break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //log.error(e.getMessage(), e);
                        }
                    }

                    updateStartandEndDatebyNumberofWeekDays(occurrenceWeek, startCal, endTimeCal);

                }

            } else {
                for (Calendar startCal = (Calendar) startDateCal.clone(); totalNoOcurrencesCount < numberofweekdays;) {//startCal.add(Calendar.DATE, WeekDaysNumber )) {
                    for (int dayCounter = 0; dayCounter < 7; dayCounter++) {
                        String weekDays = lcmsUtil.getFormatedDateInfo(startCal.getTime(), "EE");
                        try {
                            //for (String weekDay:inputWeekDaysList) {
                            if (inputWeekDaysList.contains(weekDays) && totalNoOcurrencesCount < numberofweekdays) {
                                totalNoOcurrencesCount++;
                                objSession = new SynchronousSession();
                                objSession.setStartDateTime(startCal.getTime());
                                objSession.setEndDateTime(endTimeCal.getTime());
                                this.classEndDate = endTimeCal.getTime();
                                objSession.setSyncClass(syncClass);
                                lstSession.add(objSession);
                                startCal.add(Calendar.DAY_OF_MONTH, 1);
                                endTimeCal.add(Calendar.DAY_OF_MONTH, 1);
                                //updateEndDatebyCompareDates(endDateCal, endTimeCal);
                            } else if (!inputWeekDaysList.contains(weekDays)) {
                                startCal.add(Calendar.DAY_OF_MONTH, 1);
                                endTimeCal.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            /*if( WeekDaysNumber > 1 ){
										//updateEndDatebyCompareDates(endDateCal, endTimeCal);
										endTimeCal.add(Calendar.DAY_OF_MONTH, 1 );
										startCal.add(Calendar.DAY_OF_MONTH, 1 );
										//weekDays = (String) FormUtil.getInstance().getFormatedDateInfo(startCal.getTime(), "EE");
									}else{
										startCal.add(Calendar.DAY_OF_MONTH, 1 );
										endTimeCal.add(Calendar.DAY_OF_MONTH, 1 );
										//updateEndDatebyCompareDates(endDateCal, endTimeCal);
										break;
									}*/

                            //	}
                            /*if( WeekDaysNumber > 1 ){
							//endTimeCal.add(Calendar.DAY_OF_MONTH, 1 );
							startCal.add(Calendar.DAY_OF_MONTH, 1 );
							updateEndDatebyCompareDates(startDateCal, endTimeCal);
							//weekDays = (String) FormUtil.getInstance().getFormatedDateInfo(startCal.getTime(), "EE");
						}*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    updateStartandEndDatebyNumberofWeekDays(occurrenceWeek, startCal, endTimeCal);
                }
            }

        }

        return lstSession;
    }

    private void updateStartandEndDatebyNumberofWeekDays(int occurrenceWeek, Calendar startCal, Calendar endTimeCal) {
        if (occurrenceWeek > 1) {
            int increase_day_of_month = 7 * occurrenceWeek;
            startCal.add(Calendar.DAY_OF_MONTH, increase_day_of_month);
            endTimeCal.add(Calendar.DAY_OF_MONTH, increase_day_of_month);
        }
        //startCal.add(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH-1 );	
    }

    private void generateWeekDaysList(String radioDaily) {

        weekDaysList.clear();
        if (radioDaily.equalsIgnoreCase(EVERYDAY)) {
            weekDaysList.add("Sun");
            weekDaysList.add("Mon");
            weekDaysList.add("Tue");
            weekDaysList.add("Wed");
            weekDaysList.add("Thu");
            weekDaysList.add("Fri");
            weekDaysList.add("Sat");
        } else if (radioDaily.equalsIgnoreCase(EVERYWEEKDAY)) {
            weekDaysList.add("Mon");
            weekDaysList.add("Tue");
            weekDaysList.add("Wed");
            weekDaysList.add("Thu");
            weekDaysList.add("Fri");
        }
        /**
         * we can use the generateWeeklySessions if just define the weeklist
         * that which days it has to create the sessions
         */
    }

    private void generateWeekDays(String sunday, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday) {

        weekDaysList.clear();
        if (sunday != null && sunday.equalsIgnoreCase("true")) {
            weekDaysList.add("Sun");
        }
        if (monday != null && monday.equalsIgnoreCase("true")) {
            weekDaysList.add("Mon");
        }
        if (tuesday != null && tuesday.equalsIgnoreCase("true")) {
            weekDaysList.add("Tue");
        }
        if (wednesday != null && wednesday.equalsIgnoreCase("true")) {
            weekDaysList.add("Wed");
        }
        if (thursday != null && thursday.equalsIgnoreCase("true")) {
            weekDaysList.add("Thu");
        }
        if (friday != null && friday.equalsIgnoreCase("true")) {
            weekDaysList.add("Fri");
        }
        if (saturday != null && saturday.equalsIgnoreCase("true")) {
            weekDaysList.add("Sat");
        }

        /**
         * we can use the generateWeeklySessions if just define the weeklist
         * that which days it has to create the sessions
         */
    }

    private Date StringToDate(String strdate) {

        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern, Locale.getDefault());
        Date date = null;
        try {
            // (2) give the formatter a String that matches the SimpleDateFormat pattern
            String userInput = strdate;
            date = formatter.parse(userInput);

            // or Locale.CHINESE, Locale.PRC, all work on my machine
        } catch (ParseException e) {
            // execution will come here if the String that is given
            // does not match the expected format.
            e.printStackTrace();
        }
        return date;
    }

    @RequestMapping(value = "deleteSessions", method = RequestMethod.POST)
    public @ResponseBody
    String[] deleteSessions(HttpServletRequest request, HttpServletResponse response) {
        String commasepareteIds = request.getParameter("sessionIds");
        classService.deleteSession(commasepareteIds);
        return commasepareteIds.split(",");
    }

}
