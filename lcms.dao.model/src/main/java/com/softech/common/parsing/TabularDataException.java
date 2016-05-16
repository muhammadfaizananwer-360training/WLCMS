package com.softech.common.parsing;

import org.springframework.util.StringUtils;

public class TabularDataException extends Exception {

    public static class ExceptionDetail {

        private int rowNum;
        private int columnIndex;
        private String columnName;
        private String tableName;
        private String errorText;
        private String message;
        private boolean showstopper;
        
        public ExceptionDetail() {
        }
        
        public ExceptionDetail(String message, int rowNum, boolean showstopper) {
            this.rowNum = rowNum;
            this.message = message;
            this.showstopper = showstopper;
        }

        public ExceptionDetail(String message,int rowNum, boolean showstopper, int columnIndex, String columnName, String tableName, String errorText) {
            this.rowNum = rowNum;
            this.columnIndex = columnIndex;
            this.columnName = columnName;
            this.tableName = tableName;
            this.errorText = errorText;
            this.message = message;
            this.showstopper = showstopper;
        }

        public int getRowNum() {
            return rowNum;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getTableName() {
            return tableName;
        }

        public String getErrorText() {
            return errorText;
        }

        public String getMessage() {
            return message;
        }

        public boolean isShowstopper() {
            return showstopper;
        }
        
    }

    private static final long serialVersionUID = 1L;
    private ExceptionDetail exceptionDetail;
    public TabularDataException() {
        exceptionDetail = new ExceptionDetail();
    }

    public TabularDataException(String errorMessage, int rowAt) {
        this(errorMessage, rowAt, false);
    }
    public TabularDataException(String errorMessage, int rowAt, boolean showstopper) {
        super(errorMessage);
        exceptionDetail = new ExceptionDetail(errorMessage,rowAt,showstopper);
    }

    public TabularDataException(String message, String tableName, int rowNum, int columnIndex,
            String columnName, String errorText) {
        this(message, tableName, rowNum, false, columnIndex, columnName, errorText);
    }
    public TabularDataException(String message, String tableName, int rowNum, boolean showstopper, int columnIndex,
            String columnName, String errorText) {
        super(message);
        exceptionDetail = new ExceptionDetail(message,rowNum,showstopper,columnIndex, columnName, tableName, errorText);
    }
    
    public void definePosition(String tableName, int rowNum, int columnIndex,
        String columnName) {
        if(exceptionDetail.getRowNum() < 0) {
            exceptionDetail = new ExceptionDetail(exceptionDetail.getMessage(), rowNum, exceptionDetail.isShowstopper(), columnIndex, columnName, tableName, exceptionDetail.getErrorText());
        } 
    }

    public int getRowNumber() {
        return exceptionDetail.rowNum;
    }

    public int getColumnIndex() {
        return exceptionDetail.columnIndex;
    }

    public String getColumnName() {
        return exceptionDetail.columnName;
    }

    public String getTableName() {
        return exceptionDetail.tableName;
    }

    public String getErrorText() {
        return exceptionDetail.errorText;
    }
    
    public boolean isShowstopper() {
        return exceptionDetail.showstopper;
    }

    public ExceptionDetail getExceptionDetail() {
        return exceptionDetail;
    }
}
