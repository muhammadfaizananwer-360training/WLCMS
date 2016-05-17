package com.softech.ls360.lcms.contentbuilder.dataimport;

import com.softech.common.delegate.Function1WithException;
import com.softech.common.delegate.Function2WithException;
import com.softech.common.event.EventArg;
import com.softech.common.event.ICallbackHandlerWithException;
import com.softech.common.parsing.ExpressionConstant;
import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.MetaData;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.invalidateNode;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.addOnPopulateHandler;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.getNode;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.notifyAllCallbackHandlersWithError;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.notifyCallbackHandlers;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.validateRow;
import java.util.Map;

import com.softech.ls360.lcms.contentbuilder.model.ClassInstructorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import com.softech.ls360.lcms.contentbuilder.model.CourseVO;
import com.softech.ls360.lcms.contentbuilder.model.LocationDTO;
import com.softech.ls360.lcms.contentbuilder.model.SyncClassDTO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.util.StringUtils;

@Component
@Scope("prototype")
public class CRClassParsingSubHndlr implements ITabularParsingSubHandler<SyncClassDTO> {

    private static final Logger logger = LoggerFactory
            .getLogger(CRClassParsingSubHndlr.class);

    private enum ColumnIndexes {
        ACTION,
        COURSE_ID,
        CLASS_NAME,
        NUMBER_OF_SEATS,
        TIME_ZONE,
        ENROLLMENT_CLOSE_DATE,
        LOCATION_NAME,
        INSTRUCTOR_EMAIL
    }

    private static final MetaData[] metaData = new MetaData[]{
        new MetaData("Action", false, null, String.class),
        new MetaData("Course Id", true, null, String.class),
        new MetaData("Class Title", true, null, String.class),
        new MetaData("Number of Seats", false, "^\\d+(\\.0+)?$", String.class, "Invalid Number Of Seats"),
        new MetaData("Time Zone", true, null, String.class),
        new MetaData("Enrollment Close Date", true, ExpressionConstant.SHORT_DATE, Date.class),
        new MetaData("Location name", true, null, String.class),
        new MetaData("Instructor Email", true, ExpressionConstant.EMAIL, String.class, "Invalid Email")
    };

    private CRCourseParsingSubHndlr courseHandler;
    private CRLocationParsingSubHndlr locationHandler;
    private CRInstructorParsingSubHndlr instructorHandler;
    private final Map<String, List<ICallbackHandlerWithException<SyncClassDTO, TabularDataException>>> populateHandlers = new HashMap<>();
    private final List<Function2WithException<SyncClassDTO, Object, Object, TabularDataException>> externalValidators = new ArrayList<>();
    private Function1WithException<SyncClassDTO, Void, TabularDataException> externalPopulator;

    

    public SyncClassDTO getSyncClass(String courseId, String className) {
        CourseVO course = courseHandler.getCourse(courseId);
        SyncClassDTO newCls = new SyncClassDTO();
        newCls.setClassName(className);
        SyncClassDTO cls = getNode(course.getSyncClassesMap(), className, newCls);
        if (cls == newCls) {
            cls.setCourse(course);
        }
        return cls;
    }

    @Override
    public String getHandlerKey(SyncClassDTO syncClass) {
        String courseId = syncClass.getCourse().getCourseId();
        String className = syncClass.getClassName();
        return courseId.toLowerCase() + "::" + className.toLowerCase();
    }
    
    @Override
    public boolean finalize(String tableName) throws TabularDataException {
        if (populateHandlers.size() > 0) {
            notifyAllCallbackHandlersWithError(populateHandlers);
            return false;
        }
        return true;
    }

    @Override
    public boolean handleRow(String tableName, Object[] row, int rowNum) throws TabularDataException {
        return TabularParsingHndlrHelper.handleRow(this, tableName, metaData, row, rowNum, populateHandlers, externalValidators, externalPopulator, new String[]{"child", "add", "update", "delete"});
    }

    @Override
    public boolean addPopulateHandler(SyncClassDTO dto, ICallbackHandlerWithException<SyncClassDTO, TabularDataException> handler) throws TabularDataException {
        return addOnPopulateHandler(dto, getHandlerKey(dto), handler, populateHandlers);
    }

    @Override
    public boolean validateRowData(Object[] row, int rowIndex) throws TabularDataException {
        boolean rowAccepted = validateRow(getName(), metaData, row, rowIndex, (short) 0);
        return rowAccepted;
    }

    @Override
    public boolean validateRowKey(Object[] row, int rowIndex) throws TabularDataException {
        boolean rowAccepted = validateRow(getName(), metaData, row, rowIndex, (short) 0, (short) 2);
        return rowAccepted;
    }

    @Override
    public boolean addRowDataToDTO(Object[] row, int rowNum, SyncClassDTO syncClass) throws TabularDataException {
        syncClass.setLocationName(locationHandler.getLocation(TypeConvertor.AnyToString(row[ColumnIndexes.LOCATION_NAME.ordinal()])).getLocationName());
        syncClass.setInstructorEmail(instructorHandler.getInstructor(TypeConvertor.AnyToString(row[ColumnIndexes.INSTRUCTOR_EMAIL.ordinal()])).getEmail());
        syncClass.setMaximumClassSize(TypeConvertor.AnyToLong(row[ColumnIndexes.NUMBER_OF_SEATS.ordinal()]));
        syncClass.setTimeZoneText(TypeConvertor.AnyToString(row[ColumnIndexes.TIME_ZONE.ordinal()]));
        try {
            syncClass.setEnrollmentCloseDate(TypeConvertor.AnyToDate(row[ColumnIndexes.ENROLLMENT_CLOSE_DATE.ordinal()]));
        } catch (ParseException ex) {
            throw new TabularDataException("Invalid Date", getName(), rowNum, ColumnIndexes.ENROLLMENT_CLOSE_DATE.ordinal(), metaData[ColumnIndexes.ENROLLMENT_CLOSE_DATE.ordinal()].getText(), TypeConvertor.AnyToString(row[ColumnIndexes.ENROLLMENT_CLOSE_DATE.ordinal()]));
        }

        //Date cannot be a past date
        if (syncClass.getEnrollmentCloseDate().getTime() <= new Date().getTime()) {
            throw new TabularDataException("Past Date", getName(), rowNum, ColumnIndexes.ENROLLMENT_CLOSE_DATE.ordinal(), metaData[ColumnIndexes.ENROLLMENT_CLOSE_DATE.ordinal()].getText(), TypeConvertor.DateTimeToString(syncClass.getEnrollmentCloseDate()));
        }

        return true;
    }

    @Override
    public SyncClassDTO getDtoByRowKey(Object[] row, int rowNum) throws TabularDataException {
        return getSyncClass(TypeConvertor.AnyToString(row[ColumnIndexes.COURSE_ID.ordinal()]), TypeConvertor.AnyToString(row[ColumnIndexes.CLASS_NAME.ordinal()]));
    }

    @Override
    public void listenParentsToGetPopulated(final SyncClassDTO syncClass, final int rowNum) throws TabularDataException {
        final String handlerKey = getHandlerKey(syncClass);
        // for course
        courseHandler.addPopulateHandler(syncClass.getCourse(), new ICallbackHandlerWithException<CourseVO, TabularDataException>() {
            @Override
            public void handle(EventArg<CourseVO> arg) throws TabularDataException {
                if (StringUtils.isEmpty(syncClass.getLocationName())) {
                    notifyCallbackHandlers(handlerKey, syncClass, populateHandlers);
                } else {
                    // for location
                    locationHandler.addPopulateHandler(locationHandler.getLocation(syncClass.getLocationName()), new ICallbackHandlerWithException<LocationDTO, TabularDataException>() {
                        @Override
                        public void handle(EventArg<LocationDTO> arg) throws TabularDataException {
                            if (StringUtils.isEmpty(syncClass.getInstructorEmail())) {
                                notifyCallbackHandlers(handlerKey, syncClass, populateHandlers);
                            } else {
                                //for instructor
                                instructorHandler.addPopulateHandler(instructorHandler.getInstructor(syncClass.getInstructorEmail()), new ICallbackHandlerWithException<ClassInstructorDTO, TabularDataException>() {
                                    @Override
                                    public void handle(EventArg<ClassInstructorDTO> arg) throws TabularDataException {
                                        notifyCallbackHandlers(handlerKey, syncClass, populateHandlers);
                                    }

                                    @Override
                                    public void error(Exception ex, EventArg<ClassInstructorDTO> arg) throws TabularDataException {
                                        invalidateNode(syncClass);
                                        populateHandlers.remove(handlerKey);
                                        throw new TabularDataException("Instructor Not Provided", getName(), rowNum, ColumnIndexes.INSTRUCTOR_EMAIL.ordinal(), metaData[ColumnIndexes.INSTRUCTOR_EMAIL.ordinal()].getText(), syncClass.getInstructorEmail());
                                    }
                                });
                            }
                        }

                        @Override
                        public void error(Exception ex, EventArg<LocationDTO> arg) throws TabularDataException {
                            invalidateNode(syncClass);
                            populateHandlers.remove(handlerKey);
                            throw new TabularDataException("Location Not Provided", getName(), rowNum, ColumnIndexes.LOCATION_NAME.ordinal(), metaData[ColumnIndexes.LOCATION_NAME.ordinal()].getText(), syncClass.getLocationName());
                        }
                    });
                }
            }

            @Override
            public void error(Exception ex, EventArg<CourseVO> arg) throws TabularDataException {
                invalidateNode(syncClass);
                populateHandlers.remove(handlerKey);
                throw new TabularDataException("Course Not Provided", getName(), rowNum, ColumnIndexes.COURSE_ID.ordinal(), metaData[ColumnIndexes.COURSE_ID.ordinal()].getText(), syncClass.getCourse().getCourseId());
            }
        });
    }

    @Override
    public String getName() {
        return "Class";
    }

    public CRCourseParsingSubHndlr getCourseHandler() {
        return courseHandler;
    }

    public void setCourseHandler(CRCourseParsingSubHndlr courseHandler) {
        this.courseHandler = courseHandler;
    }

    public CRLocationParsingSubHndlr getLocationHandler() {
        return locationHandler;
    }

    public void setLocationHandler(CRLocationParsingSubHndlr locationHandler) {
        this.locationHandler = locationHandler;
    }

    public List<Function2WithException<SyncClassDTO, Object, Object, TabularDataException>> getExternalValidators() {
        return externalValidators;
    }

    public Function1WithException<SyncClassDTO, Void, TabularDataException> getExternalPopulator() {
        return externalPopulator;
    }

    public void setExternalPopulator(Function1WithException<SyncClassDTO, Void, TabularDataException> externalPopulator) {
        this.externalPopulator = externalPopulator;
    }

    public CRInstructorParsingSubHndlr getInstructorHandler() {
        return instructorHandler;
    }

    public void setInstructorHandler(CRInstructorParsingSubHndlr instructorHandler) {
        this.instructorHandler = instructorHandler;
    }
}
