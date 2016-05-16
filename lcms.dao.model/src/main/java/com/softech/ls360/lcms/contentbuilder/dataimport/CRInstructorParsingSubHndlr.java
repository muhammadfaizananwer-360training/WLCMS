package com.softech.ls360.lcms.contentbuilder.dataimport;

import com.softech.common.delegate.Function1WithException;
import com.softech.common.delegate.Function2WithException;
import com.softech.common.event.ICallbackHandlerWithException;
import com.softech.common.parsing.ExpressionConstant;
import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.*;
import com.softech.ls360.lcms.contentbuilder.model.ClassInstructorDTO;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.*;

@Component
@Scope("prototype")
public class CRInstructorParsingSubHndlr implements ITabularParsingSubHandler<ClassInstructorDTO> {

    private static final Logger logger = LoggerFactory
            .getLogger(CRInstructorParsingSubHndlr.class);

    private enum ColumnIndexes {
        ACTION,
        EMAIL,
        FIRST_NAME,
        LAST_NAME,
        PHONE,
        BACKGROUND
    }

    private static final MetaData[] metaData = new MetaData[]{
            new MetaData("Action", false, null, String.class),
            new MetaData("Email", true, ExpressionConstant.EMAIL, String.class, "Invalid Email"),
            new MetaData("First Name", true, null, String.class),
            new MetaData("Last Name", true, null, String.class),
            new MetaData("Phone Number", true, null, String.class),
            new MetaData("Background", false, null, String.class),
    };

    private final Map<String, ClassInstructorDTO> instructors = new HashMap<>();
    private final List<Function2WithException<ClassInstructorDTO, Object, Object, TabularDataException>> externalValidators = new ArrayList<>();
    private final Map<String, List<ICallbackHandlerWithException<ClassInstructorDTO, TabularDataException>>> populateHandlers = new HashMap<>();
    private Function1WithException<ClassInstructorDTO, Void, TabularDataException> externalPopulator;

    public ClassInstructorDTO getInstructor(String email) {
        ClassInstructorDTO loc = new ClassInstructorDTO();
        loc.setEmail(email);
        return (ClassInstructorDTO) getNode(instructors, email, loc);

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
    public boolean addRowDataToDTO(Object[] row, int rowNum, ClassInstructorDTO loc) throws TabularDataException {
        loc.setBackground(TypeConvertor.AnyToString(row[ColumnIndexes.BACKGROUND.ordinal()]));
        loc.setEmail(TypeConvertor.AnyToString(row[ColumnIndexes.EMAIL.ordinal()]));
        loc.setFirstName(TypeConvertor.AnyToString(row[ColumnIndexes.FIRST_NAME.ordinal()]));
        loc.setLastName(TypeConvertor.AnyToString(row[ColumnIndexes.LAST_NAME.ordinal()]));
        loc.setPhoneNo(StringUtil.trimSuffix(TypeConvertor.AnyToString(row[ColumnIndexes.PHONE.ordinal()]),".0"));
        return true;
    }

    @Override
    public String getHandlerKey(ClassInstructorDTO loc) {
        return loc.getEmail().toLowerCase();
    }

    @Override
    public ClassInstructorDTO getDtoByRowKey(Object[] row, int rowNum) throws TabularDataException {
        return getInstructor(TypeConvertor.AnyToString(row[ColumnIndexes.EMAIL.ordinal()]));
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
    public void listenParentsToGetPopulated(ClassInstructorDTO loc, int rowNum) throws TabularDataException {
        notifyCallbackHandlers(loc.getEmail().toLowerCase(), loc, populateHandlers);
    }

    @Override
    public boolean addPopulateHandler(ClassInstructorDTO loc, ICallbackHandlerWithException<ClassInstructorDTO, TabularDataException> handler) throws TabularDataException {
        return addOnPopulateHandler(loc, loc.getNodeKey(), handler, populateHandlers);
    }

    @Override
    public String getName() {
        return "Instructor";
    }

    public Map<String, ClassInstructorDTO> getInstructors() {
        return instructors;
    }

    public List<Function2WithException<ClassInstructorDTO, Object, Object, TabularDataException>> getExternalValidators() {
        return externalValidators;
    }

}
