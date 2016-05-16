package com.softech.ls360.lcms.contentbuilder.manager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelDataWriter extends AbstractExcelView{

	public XSSFWorkbook writeData(List<Object> dataList) throws IOException{
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Signup Author Data");
        sheet.setDefaultColumnWidth(18);
        
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        
        CellStyle styleBold = workbook.createCellStyle();
        styleBold.setFont(font);        
        styleBold.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleBold.setFillPattern(CellStyle.FINE_DOTS);
        styleBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);             
        styleBold.setBorderRight(HSSFCellStyle.BORDER_THIN);            
        styleBold.setBorderTop(HSSFCellStyle.BORDER_THIN);              
        styleBold.setBorderBottom(HSSFCellStyle.BORDER_THIN);   
        styleBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styleBold.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styleBold.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        styleBold.setRightBorderColor(IndexedColors.BLACK.getIndex());
        

        
        CellStyle style = workbook.createCellStyle();              
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);             
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);            
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);              
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);   
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(false);
        int rowCount = 0;
        
        for(Object object : dataList){
        	Object[] dataArray = (Object[])object;
        	
        	int columnCount = 0;
        	Row row = sheet.createRow(rowCount);
        	 for(Object rowCellItem : dataArray){
        		 
        		 Cell cell = row.createCell(columnCount++);
        		 if(rowCount==0){
           			 cell.setCellStyle(styleBold);           			
           		 }
        		 else {cell.setCellStyle(style);}
        		 
				if (rowCellItem instanceof String) {
					cell.setCellValue((String) rowCellItem);
				} else if (rowCellItem instanceof Integer) {
					cell.setCellValue((Integer) rowCellItem);
				}
				
				
				//TODO:: add more datatypes hadling
        	 }
        	 rowCount++;
        } 
        
        return workbook;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<Object> dataList = (List<Object>)model.get("data");
        HSSFSheet sheet = workbook.createSheet("CourseStructure");
        sheet.setDefaultColumnWidth(12);
        
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int rowCount = 0;
        
        for(Object object : dataList){
        	Object[] dataArray = (Object[])object;
        	
        	int columnCount = 0;
        	// Row row = sheet.createRow(rowCount++);
        	 for(Object rowCellItem : dataArray){
        		 
        		 HSSFCell cell = getCell(sheet, rowCount, columnCount++);
        		 cell.setCellValue((String)rowCellItem);
        		 cell.setCellStyle(style);
        	     setText(cell, (String)rowCellItem);
 
        	 }
        	 rowCount++;
        }
	}
}
