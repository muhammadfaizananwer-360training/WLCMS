package com.softech.ls360.lcms.contentbuilder.dataimport;

import com.softech.common.delegate.Function1WithException;
import com.softech.common.delegate.Function2WithException;
import com.softech.common.event.EventArg;
import com.softech.common.event.ICallbackHandlerWithException;
import com.softech.common.parsing.ExpressionConstant;
import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.*;
import com.softech.ls360.lcms.contentbuilder.model.ClassInstructorDTO;
import com.softech.ls360.lcms.contentbuilder.model.CourseProviderDTO;
import com.softech.ls360.lcms.contentbuilder.model.CourseVO;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.*;

@Component
@Scope("prototype")
public class CRCourseParsingSubHndlr implements ITabularParsingSubHandler<CourseVO> {

    private static final Logger logger = LoggerFactory
            .getLogger(CRCourseParsingSubHndlr.class);

    private enum ColumnIndexes {
        ACTION,
        COURSE_NAME,
        COURSE_ID,
        DESCRIPTION,
        PROP_VALUE,
        INDUSTRY_CATEGORY,
        AUDIENCE,
        COURSE_OBJECTIVES,
        TOPICS_COVERED,
        PREREQUISITES,
        ADDITIONAL_INFORMATION,
        INSTRUCTOR_EMAIL,
        AUTHOR_BACKGROUND,
        PROVIDER_NAME,
        PROVIDER_EMAIL,
        PROVIDER_PHONE_NO,
    }

    private static final MetaData[] metaData = new MetaData[]{
        new MetaData("Action", false, null, String.class),
        new MetaData("Course Title", true, null, String.class),
        new MetaData("Course ID", true, null, String.class),
        new MetaData("Description", true, null, String.class),
        new MetaData("About This Course", false, null, String.class),
        new MetaData("Industry Category", true, null, String.class),
        new MetaData("Intended Audience", false, null, String.class),
        new MetaData("Course Objectives", false, null, String.class),
        new MetaData("Topics Covered", false, null, String.class),
        new MetaData("Prerequisites", false, null, String.class),
        new MetaData("Additional Information", false, null, String.class),
        new MetaData("Instructor Email", true, ExpressionConstant.EMAIL, String.class, "Invalid Email"),
        new MetaData("Instructor Background", true, null, String.class),
        new MetaData("Provider Name", true, null, String.class),
        new MetaData("Provider Email", true, ExpressionConstant.EMAIL, String.class, "Invalid Email"),
        new MetaData("Provider Phone", true, null, String.class)
    };

    private final Map<String, CourseVO> courses = new HashMap<>();
    private final Map<String, List<ICallbackHandlerWithException<CourseVO, TabularDataException>>> populateHandlers = new HashMap<>();
    private final List<Function2WithException<CourseVO, Object, Object, TabularDataException>> externalValidators = new ArrayList<>();
    private Function1WithException<CourseVO, Void, TabularDataException> externalPopulator;
    private CRInstructorParsingSubHndlr instructorHandler;
    

    public CourseVO getCourse(String courseId) {
        CourseVO course = new CourseVO();
        course.setCourseId(courseId);
        return getNode(courses, courseId, course);
    }
    
    
    public String getHandlerKey(CourseVO course) {
        return course.getCourseId().toLowerCase();
    }
    
    @Override
    public boolean addPopulateHandler(CourseVO dto, ICallbackHandlerWithException<CourseVO, TabularDataException> handler) throws TabularDataException {
        return addOnPopulateHandler(dto,dto.getNodeKey(), handler, populateHandlers);
    }
    
    @Override
    public boolean validateRowData(Object[] row, int rowIndex) throws TabularDataException {
        boolean rowAccepted = validateRow(getName(), metaData, row, rowIndex, (short) 0);
        return rowAccepted;
    }

    @Override
    public boolean validateRowKey(Object[] row, int rowIndex) throws TabularDataException {
        boolean rowAccepted = validateRow(getName(), metaData, row, rowIndex, (short) 0, (short) 1);
        return rowAccepted;
    }

    @Override
    public void listenParentsToGetPopulated(final CourseVO course, final int rowNum) throws TabularDataException {
        final String handlerKey = course.getNodeKey().toLowerCase();
        if (StringUtils.isEmpty(course.getInstructorEmail())) {
            notifyCallbackHandlers(handlerKey, course, populateHandlers);
        } else {
            instructorHandler.addPopulateHandler(instructorHandler.getInstructor(course.getInstructorEmail()), new ICallbackHandlerWithException<ClassInstructorDTO, TabularDataException>() {
                @Override
                public void handle(EventArg<ClassInstructorDTO> arg) throws TabularDataException {
                    notifyCallbackHandlers(handlerKey, course, populateHandlers);
                }

                @Override
                public void error(Exception ex, EventArg<ClassInstructorDTO> arg) throws TabularDataException {
                    invalidateNode(course);
                    populateHandlers.remove(handlerKey);
                    throw new TabularDataException("Instructor Not Provided", getName(), rowNum, ColumnIndexes.INSTRUCTOR_EMAIL.ordinal(), metaData[ColumnIndexes.INSTRUCTOR_EMAIL.ordinal()].getText(), course.getInstructorEmail());
                }
            });
        }
    }

    @Override
    public CourseVO getDtoByRowKey(Object[] row, int rowNum) throws TabularDataException {
        return getCourse(TypeConvertor.AnyToString(row[ColumnIndexes.COURSE_ID.ordinal()]));
    }

    @Override
    public boolean addRowDataToDTO(Object[] row, int rowNum, CourseVO course) throws TabularDataException {
        CourseProviderDTO courseProvider = new CourseProviderDTO();
        course.setCourseProvider(courseProvider);
        courseProvider.setCourse(course);

        course.setName(TypeConvertor.AnyToString(row[ColumnIndexes.COURSE_NAME.ordinal()]));
        course.setCourseId(TypeConvertor.AnyToString(row[ColumnIndexes.COURSE_ID.ordinal()]));
        course.setDescription(TypeConvertor.AnyToString(row[ColumnIndexes.DESCRIPTION.ordinal()]));
        course.setIntendedAudience(TypeConvertor.AnyToString(row[ColumnIndexes.AUDIENCE.ordinal()]));
        course.setLearningObjectives(TypeConvertor.AnyToString(row[ColumnIndexes.COURSE_OBJECTIVES.ordinal()]));
        course.setTopicsCovered(TypeConvertor.AnyToString(row[ColumnIndexes.TOPICS_COVERED.ordinal()]));
        course.setCoursePrereq(TypeConvertor.AnyToString(row[ColumnIndexes.PREREQUISITES.ordinal()]));
        course.setAdditionalMaterials(TypeConvertor.AnyToString(row[ColumnIndexes.ADDITIONAL_INFORMATION.ordinal()]));
        course.setBusinessunitName(TypeConvertor.AnyToString(row[ColumnIndexes.INDUSTRY_CATEGORY.ordinal()]));
        course.setPropValue(TypeConvertor.AnyToString(row[ColumnIndexes.PROP_VALUE.ordinal()]));
        course.setInstructorEmail(instructorHandler.getInstructor(TypeConvertor.AnyToString(row[ColumnIndexes.INSTRUCTOR_EMAIL.ordinal()])).getEmail());
        courseProvider.setInstructorBackground(TypeConvertor.AnyToString(row[ColumnIndexes.AUTHOR_BACKGROUND.ordinal()]));
        courseProvider.setName(TypeConvertor.AnyToString(row[ColumnIndexes.PROVIDER_NAME.ordinal()]));
        courseProvider.setEmail(TypeConvertor.AnyToString(row[ColumnIndexes.PROVIDER_EMAIL.ordinal()]));
        courseProvider.setPhoneNo(StringUtil.trimSuffix(TypeConvertor.AnyToString(row[ColumnIndexes.PROVIDER_PHONE_NO.ordinal()]),".0"));

        return true;
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
        return TabularParsingHndlrHelper.handleRow(this, tableName, metaData, row, rowNum, populateHandlers, externalValidators, externalPopulator
                , new String[]{"child","add","update"});
    }

    @Override
    public String getName() {
        return "Course";
    }

    public Map<String, CourseVO> getCourses() {
        return courses;
    }

    public List<Function2WithException<CourseVO, Object, Object, TabularDataException>> getExternalValidators() {
        return externalValidators;
    }

    public Function1WithException<CourseVO, Void, TabularDataException> getExternalPopulator() {
        return externalPopulator;
    }

    public void setExternalPopulator(Function1WithException<CourseVO, Void, TabularDataException> externalPopulator) {
        this.externalPopulator = externalPopulator;
    }

    public CRInstructorParsingSubHndlr getInstructorHandler() {
        return instructorHandler;
    }

    public void setInstructorHandler(CRInstructorParsingSubHndlr instructorHandler) {
        this.instructorHandler = instructorHandler;
    }
}
