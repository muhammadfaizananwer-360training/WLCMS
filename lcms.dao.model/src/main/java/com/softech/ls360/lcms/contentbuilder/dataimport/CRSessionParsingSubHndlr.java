package com.softech.ls360.lcms.contentbuilder.dataimport;

import com.softech.common.delegate.Function1WithException;
import com.softech.common.delegate.Function2WithException;
import com.softech.common.event.EventArg;
import com.softech.common.event.ICallbackHandlerWithException;
import com.softech.common.parsing.ExpressionConstant;
import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.MetaData;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.validateRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import com.softech.ls360.lcms.contentbuilder.model.SyncClassDTO;
import com.softech.ls360.lcms.contentbuilder.model.SyncSessionDTO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class CRSessionParsingSubHndlr implements ITabularParsingSubHandler<SyncSessionDTO> {

    private static final Logger logger = LoggerFactory
            .getLogger(CRSessionParsingSubHndlr.class);

    private enum ColumnIndexes {
        ACTION,
        COURSE_ID,
        CLASS_NAME,
        SESSION_START_DATE,
        SESSION_END_DATE,
        SESSION_START_TIME,
        SESSION_END_TIME
    }

    private static final MetaData[] metaData = new MetaData[]{
        new MetaData("Action", false, null, String.class),
        new MetaData("Course ID", true, null, String.class),
        new MetaData("Class Title", true, null, String.class),
        new MetaData("Session Start Date", true, ExpressionConstant.SHORT_DATE, Date.class),
        new MetaData("Session End Date", true, ExpressionConstant.SHORT_DATE, Date.class),
        new MetaData("Session Start Time", true, ExpressionConstant.TIME, String.class, "Invalid Time"),
        new MetaData("Session End Time", true, ExpressionConstant.TIME, String.class, "Invalid Time")
    };

    private Object[] header = null;

    private CRClassParsingSubHndlr classHandler;
    private final List<Function2WithException<SyncSessionDTO, Object, Object, TabularDataException>> externalValidators = new ArrayList<>();
    private final Map<String, List<ICallbackHandlerWithException<SyncSessionDTO, TabularDataException>>> populateHandlers = new HashMap<>();
    private Function1WithException<SyncSessionDTO, Void, TabularDataException> externalPopulator;

    private void validateOnDTOReady(SyncSessionDTO syncSession, Integer rowIndex) throws TabularDataException {
        //start date must be after closing date
        if (syncSession.getSyncClass().getEnrollmentCloseDate().getTime() > syncSession.getStartDateTime().getTime()) {
            syncSession.setAction(null);
            throw new TabularDataException("Start Date Before Close Date", getName(), rowIndex, ColumnIndexes.SESSION_START_DATE.ordinal(), metaData[ColumnIndexes.SESSION_START_DATE.ordinal()].getText(), TypeConvertor.DateTimeToString(syncSession.getStartDateTime()));
        }
    }
    
     public String getHandlerKey(SyncSessionDTO session) {
        String courseId = session.getSyncClass().getCourse().getCourseId();
        String className = session.getSyncClass().getClassName();
        return courseId.toLowerCase() + "::" + className.toLowerCase();
    }

    @Override
    public boolean finalize(String tableName) {
        return true;
    }

    @Override
    public boolean handleRow(String tableName, Object[] row, int rowNum) throws TabularDataException {
        return TabularParsingHndlrHelper.handleRow(this, tableName, metaData, row, rowNum, populateHandlers, externalValidators, externalPopulator
                , new String[]{"add","delete"});
    }

    @Override
    public boolean addRowDataToDTO(Object[] row, int rowNum, SyncSessionDTO syncSession) throws TabularDataException {
        if (syncSession.getStartDateTime().getTime() >= syncSession.getEndDateTime().getTime()) {
            throw new TabularDataException("Start Date After End Date", getName(), rowNum, ColumnIndexes.SESSION_END_DATE.ordinal(), metaData[ColumnIndexes.SESSION_END_DATE.ordinal()].getText() + ", " + metaData[ColumnIndexes.SESSION_END_TIME.ordinal()].getText(), TypeConvertor.DateTimeToString(syncSession.getEndDateTime()));
        }
        return true;
    }

    @Override
    public SyncSessionDTO getDtoByRowKey(Object[] row, int rowNum) throws TabularDataException {
        SyncSessionDTO syncSession = new SyncSessionDTO();
        SyncClassDTO syncClass = classHandler.getSyncClass(TypeConvertor.AnyToString(row[ColumnIndexes.COURSE_ID.ordinal()]), TypeConvertor.AnyToString(row[ColumnIndexes.CLASS_NAME.ordinal()]));
        syncSession.setSyncClass(syncClass);

        String strStartDate = TypeConvertor.AnyToString(row[ColumnIndexes.SESSION_START_DATE.ordinal()])
                + " " + TypeConvertor.AnyToString(row[ColumnIndexes.SESSION_START_TIME.ordinal()]);
        try {
            syncSession.setStartDateTime(TypeConvertor.AnyToDate(strStartDate));
        } catch (ParseException ex) {
            throw new TabularDataException("Invalid Date", getName(), rowNum, ColumnIndexes.SESSION_START_DATE.ordinal(), metaData[ColumnIndexes.SESSION_START_DATE.ordinal()].getText() + ", " + metaData[ColumnIndexes.SESSION_START_TIME.ordinal()].getText(), strStartDate);
        }

        String strEndDate = TypeConvertor.AnyToString(row[ColumnIndexes.SESSION_END_DATE.ordinal()])
                + " " + TypeConvertor.AnyToString(row[ColumnIndexes.SESSION_END_TIME.ordinal()]);
        try {
            syncSession.setEndDateTime(TypeConvertor.AnyToDate(strEndDate));
        } catch (ParseException ex) {
            throw new TabularDataException("Invalid Date", getName(), rowNum, ColumnIndexes.SESSION_END_DATE.ordinal(), metaData[ColumnIndexes.SESSION_END_DATE.ordinal()].getText() + ", " + metaData[ColumnIndexes.SESSION_END_TIME.ordinal()].getText(), strEndDate);
        }

        syncClass.getSessionsMap().put(syncSession.getKey().toLowerCase(), syncSession);
        return syncSession;
    }

    @Override
    public boolean validateRowData(Object[] row, int rowNum) throws TabularDataException {
        boolean rowAccepted = validateRow(getName(), metaData, row, rowNum, (short) 0);
        return rowAccepted;
    }

    @Override
    public boolean validateRowKey(Object[] row, int rowNum) throws TabularDataException {
        boolean rowAccepted = validateRow(getName(), metaData, row, rowNum, (short) 0);
        return rowAccepted;
    }

    @Override
    public void listenParentsToGetPopulated(final SyncSessionDTO syncSession, final int rowNum) throws TabularDataException {
        classHandler.addPopulateHandler(syncSession.getSyncClass(), new ICallbackHandlerWithException<SyncClassDTO, TabularDataException>() {
            @Override
            public void handle(EventArg<SyncClassDTO> arg) throws TabularDataException {
                validateOnDTOReady(syncSession, rowNum);
            }

            @Override
            public void error(Exception ex, EventArg<SyncClassDTO> arg) throws TabularDataException {
                throw new TabularDataException("Class Not Provided", getName(), rowNum, ColumnIndexes.CLASS_NAME.ordinal(), metaData[ColumnIndexes.COURSE_ID.ordinal()].getText() + "," + metaData[ColumnIndexes.CLASS_NAME.ordinal()].getText(), syncSession.getSyncClass().getCourse().getCourseId() + "," + syncSession.getSyncClass().getClassName());
            }
        });
    }

    @Override
    public boolean addPopulateHandler(SyncSessionDTO dto, ICallbackHandlerWithException<SyncSessionDTO, TabularDataException> handler) throws TabularDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return "Session";
    }

    public CRClassParsingSubHndlr getClassHandler() {
        return classHandler;
    }

    public void setClassHandler(CRClassParsingSubHndlr classHandler) {
        this.classHandler = classHandler;
    }

    public List<Function2WithException<SyncSessionDTO, Object, Object, TabularDataException>> getExternalValidators() {
        return externalValidators;
    }

    public Function1WithException<SyncSessionDTO, Void, TabularDataException> getExternalPopulator() {
        return externalPopulator;
    }

    public void setExternalPopulator(Function1WithException<SyncSessionDTO, Void, TabularDataException> externalPopulator) {
        this.externalPopulator = externalPopulator;
    }
    
    
}
