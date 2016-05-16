package com.softech.ls360.lcms.contentbuilder.dataimport;

import com.softech.common.parsing.ExcelParser;
import com.softech.common.parsing.ITabularParsingHandler;
import com.softech.common.parsing.TabularDataException;
import com.softech.common.parsing.TabularDataExceptionList;
import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class ClassroomParsingHndlr implements ITabularParsingHandler {

    private static final Logger logger = LoggerFactory
            .getLogger(ClassroomParsingHndlr.class);

    private final Map<String, ITabularParsingSubHandler> subHandlers = new HashMap<>();
    private ITabularParsingSubHandler currentSubHandler;
    private final TabularDataExceptionList exceptionList = new TabularDataExceptionList();

    @Autowired
    private ExcelParser tabularParser;

    public void addSubHandler(ITabularParsingSubHandler handler) {
        subHandlers.put(handler.getName().toLowerCase(), handler);
    }

    public void addSubHandlers(ITabularParsingSubHandler... handlers) {
        for (ITabularParsingSubHandler handler : handlers) {
            addSubHandler(handler);
        }
    }

    public void parseCourse(String fileName) throws Exception {
        try (FileInputStream file = new FileInputStream(new File(fileName))) {
            parseCourse(file);
        }
    }

    public void parseCourse(InputStream stream) throws IOException, TabularDataException {
        
        //parsing
        tabularParser.parse(stream, this);
                
        //finalizing
        for (Map.Entry key : subHandlers.entrySet()) {
            ITabularParsingSubHandler handler = subHandlers.get(key.getKey().toString());
            try {
                handler.finalize(key.getKey().toString());
            } catch (TabularDataException ex) {
                exceptionList.addException(ex);
            }
        }
        if (exceptionList.hasError()) {
            throw exceptionList;
        }
    }

    @Override
    public boolean handlingRequired(String tableName) {
        currentSubHandler = subHandlers.get(tableName.toLowerCase());
        return (currentSubHandler != null && !exceptionList.isShowstopper());
    }

    @Override
    public boolean handleRow(String tableName, Object[] row, int rowNum) throws TabularDataException {
        boolean continued = true;
        try {
            continued = currentSubHandler.handleRow(tableName, row, rowNum);
        } catch (TabularDataException ex) {
            exceptionList.addException(ex);
        } catch (Exception ex) {
            exceptionList.addException(new TabularDataException("Unknown", tableName, rowNum, 0, "", ex.getMessage()));
            logger.error(ex.getMessage(), ex);
        }

        return continued && !exceptionList.isShowstopper();
    }

}
