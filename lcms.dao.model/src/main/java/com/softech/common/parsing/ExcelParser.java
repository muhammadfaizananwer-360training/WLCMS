package com.softech.common.parsing;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExcelParser {

    private static final Logger logger = LoggerFactory
            .getLogger(ExcelParser.class);

    private Object getCellValue(Cell cell) {

        Object cellValue = null;

        if (cell == null) {
            return cellValue;
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;

            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;

            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue();
                } else {
                    cellValue = cell.getNumericCellValue();
                }
                break;

            case Cell.CELL_TYPE_BLANK:
                cellValue = "";
                break;

            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;

        }

        return cellValue;
    }

    private static short getRowSize(Row row) {
        short minColIx = row.getFirstCellNum();
        short maxColIx = row.getLastCellNum();
        return (short) ((maxColIx < 0 || minColIx < 0) ? 0 : maxColIx - minColIx + 1);
    }

    private Object[] parseRow(Row row, short rowSize,short tblFirstColIndex,
            short tblLastColIndex) {

        short minColIx = row.getFirstCellNum();
        short maxColIx = row.getLastCellNum();
        
        if(minColIx < tblFirstColIndex){
           minColIx = tblFirstColIndex;
        }
        
        if(maxColIx > tblLastColIndex){
           maxColIx = tblLastColIndex;
        }
        
        Object[] data = new Object[rowSize];
        for (short colIx = minColIx;  colIx <= maxColIx ; colIx++) {
            Cell cell = row.getCell(colIx);
            if (cell != null) {
                data[colIx] = getCellValue(cell);
            }
        }
        return data;
    }

    public void parse(InputStream file, ITabularParsingHandler handler) throws IOException, TabularDataException {
        org.apache.poi.ss.usermodel.Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (InvalidFormatException ex) {
            throw new TabularDataException(ex.getMessage(),0);
        }
        
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
            if (!handler.handlingRequired(sheet.getSheetName())) {
                logger.info("Sheet Skipped: " + sheet.getSheetName());
                continue;
            }

            logger.info("Sheet Name: " + sheet.getSheetName());
            logger.info("Last Row Number: " + sheet.getLastRowNum());
            logger.info("No of Rows: " + sheet.getPhysicalNumberOfRows());
            Iterator<Row> rowIterator = sheet.iterator();
            short rowSize = 0;
            short tblFirstColIndex = 0;
            short tblLastColIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                //ignore empty row otherwise intialize rowSize which is not set earlier.
                short currentRowSize = getRowSize(row);
                if (currentRowSize == 0) {
                    continue;
                } else if (rowSize == 0) {
                    rowSize = currentRowSize;
                    tblFirstColIndex = row.getFirstCellNum();
                    tblLastColIndex = row.getLastCellNum();
                }

                try {
                    if (!handler.handleRow(sheet.getSheetName(), parseRow(row, rowSize,tblFirstColIndex,tblLastColIndex), row.getRowNum())) {
                        break;
                    }
                } catch (TabularDataException ex) {
                    //dont log.
                    throw ex;
                } catch (Exception e) {
                    if (e.getCause() instanceof TabularDataException) {
                        //dont log
                    } else {
                        logger.error(e.getMessage()
                                + "\nSheet Name: " + sheet.getSheetName()
                                + "\nRow Number: " + row.getRowNum()
                        );
                    }
                    throw e;
                }
            }
        }

    }
}
