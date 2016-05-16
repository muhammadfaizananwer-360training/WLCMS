package com.softech.common.parsing;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class TabularDataExceptionList extends TabularDataException {

    private static final long serialVersionUID = 1L;
    private final List<TabularDataException> exceptionList = new ArrayList<>();
    private int notDefinedPositionCount = 0;
    private boolean showstopper;
    public TabularDataExceptionList() {

    }

    private static int getNotDefinedPositionCountOfException(TabularDataException ex) {
        if (ex instanceof TabularDataExceptionList) {
            return ((TabularDataExceptionList) ex).getNotDefinedPositionCount();
        } else if (ex.getRowNumber() < 0) {
            return 1;
        }

        return 0;
    }

    public void addException(TabularDataException ex) {
        showstopper |= ex.isShowstopper();
        notDefinedPositionCount += getNotDefinedPositionCountOfException(ex);
        exceptionList.add(ex);
    }

    public boolean hasError() {
        return !exceptionList.isEmpty();
    }

    @Override
    public void definePosition(String tableName, int rowNum, int columnIndex,
            String columnName) {
        if (notDefinedPositionCount > 0) {
            notDefinedPositionCount = 0;
            for (TabularDataException ex : exceptionList) {
                ex.definePosition(tableName, rowNum, columnIndex, columnName);
            }
        }
    }

    public List<TabularDataException> getErrorList() {
        List<TabularDataException> errors = new ArrayList<>();
        for (TabularDataException ex : exceptionList) {
            if (ex instanceof TabularDataExceptionList) {
                errors.addAll(((TabularDataExceptionList) ex).getErrorList());
            } else {
                errors.add(ex);
            }
        }
        return errors;
    }

    int getNotDefinedPositionCount() {
        return notDefinedPositionCount;
    }
    
    public boolean isShowstopper() {
        return showstopper;
    }
    
    

}
