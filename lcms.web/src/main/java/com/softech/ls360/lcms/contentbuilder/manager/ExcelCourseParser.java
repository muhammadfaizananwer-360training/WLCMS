package com.softech.ls360.lcms.contentbuilder.manager;

import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.interfaces.ICourseParser;
import com.softech.ls360.lcms.contentbuilder.model.Course;
import com.softech.ls360.lcms.contentbuilder.model.Lesson;
import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import com.softech.ls360.lcms.contentbuilder.utils.WlcmsConstants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelCourseParser implements ICourseParser{

	private static Logger logger = LoggerFactory
			.getLogger(ExcelCourseParser.class);
	
	private static int COURSE_TITLE_COLUMN_INDEX = 3;
	private static int OBJECT_CONTENT_TYPE_COLUMN_INDEX = 0;
	private static long DEFAULT_DURATION = 0;
	private static String HEADER_TEXT_AT_COLUMN_0 = "Content Type";
	private static String HEADER_TEXT_AT_COLUMN_1 = "Title";
	private static String HEADER_TEXT_AT_COLUMN_2 = "Slide Template";
	private static String HEADER_TEXT_AT_COLUMN_3 = "Slide Duration (In Seconds)";
	
	
	void openFile(){
		
	}
	
	private Slide populateSlideData(int rowNum, Row row, int streamingtemplateId) throws Throwable {

		Slide slide = new Slide();
		String slideName = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX + 1));
		
		String duration = StringUtil.trimSuffix(TypeConvertor.AnyToString(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX + 3)),".0");
		Integer intDuration = TypeConvertor.AnyToInteger(duration);
		if(intDuration != null){
			if (intDuration > 99999){
				throw new Throwable(" Slide Duration (In Seconds) cannot exceed five digits.");
			} else if(intDuration < 0){
				throw new Throwable(" Slide Duration (In Seconds) cannot be negative.");
			} else {
				slide.setDuration(intDuration);
			}
		}
		else {
			slide.setDuration(DEFAULT_DURATION);
		}			
		
		if(slideName == null || slideName.length() > 1000){
			throw new Throwable();
		}
		
		slide.setName(slideName);
		slide.setTemplateName("");
		
		String sceneTemplate = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX + 2));
		if(sceneTemplate.equalsIgnoreCase("Visual Left")){
			slide.setTemplateID(WlcmsConstants.VISUAL_LEFT_CONSTATNT);
		}else if(sceneTemplate.equalsIgnoreCase("Visual Right")){
			slide.setTemplateID(WlcmsConstants.VISUAL_RIGHT_CONSTATNT);
		}else if(sceneTemplate.equalsIgnoreCase("Visual TOP")){
			slide.setTemplateID(WlcmsConstants.VISUAL_TOP_CONSTATNT);
		}else if(sceneTemplate.equalsIgnoreCase("Visual Bottom")){
			slide.setTemplateID(WlcmsConstants.VISUAL_BOTTOM_CONSTATNT);
		}else if(sceneTemplate.equalsIgnoreCase("Streaming Video")){
			slide.setTemplateID(streamingtemplateId);
		}else{
			throw new Throwable();
		}
		
		
		
		return slide;
	}

	private Lesson populateLessonObject(int rowNum, Row row) throws BulkUplaodCourseException {
		
		Lesson lesson = new Lesson();
		String title = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX + 1));
		if(title == null || title.length() > 255){
			throw new BulkUplaodCourseException("Batch file is configured incorrectly. Error occurred on row [" + ++rowNum + "]. Please edit the file and try again.", rowNum);
		}
		lesson.setTitle(title);
		return lesson;
	}

	private Object getCellValue(Cell cell) {
		
		if(cell == null){
			return cell;
		}
		
	    switch (cell.getCellType()) {
	    case Cell.CELL_TYPE_STRING:
	    	return cell.getStringCellValue();
	 
	    case Cell.CELL_TYPE_BOOLEAN:
	    	return cell.getBooleanCellValue();
	 
	    case Cell.CELL_TYPE_NUMERIC:
	    	return cell.getNumericCellValue();
	    }
	    return null;
	}

	@Override
	public List<Lesson> parseCourse(String fileName, int streamingtemplateId) throws IOException, BulkUplaodCourseException {
		
		FileInputStream file = null;
		boolean foundHeader = false;
		List<Lesson> lessonList = new ArrayList<Lesson>();
		Integer currentRowIndex = 0;
		try{
		
			file = new FileInputStream(new File(fileName));
			
			 org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(file); //new XSSFWorkbook(file);
			
			//Course course = null;
			for(int i = 0; i < 1 /*workbook.getNumberOfSheets()*/; i++){
				
				org.apache.poi.ss.usermodel.Sheet  sheet = workbook.getSheetAt(i);
				
				logger.info("===Sheet Index======:" + i);
				Row lastRow = determindLastRow(sheet);
				int lastRowNo = lastRow.getRowNum();
				logger.info("===Sheet Last row no from function ======:" + lastRow.getRowNum());
				logger.info("===Sheet Last row no ======:" + sheet.getLastRowNum());
				logger.debug("===Sheet Last row no physically ======:" + sheet.getPhysicalNumberOfRows());
				
				Iterator<Row> rowIterator = sheet.iterator();
				Lesson lesson = null;
				while(rowIterator.hasNext()){
					
					Row row = rowIterator.next();
					logger.info("===Row Index======:" + row.getRowNum());
					currentRowIndex = row.getRowNum();
					if(row.getRowNum() <= OBJECT_CONTENT_TYPE_COLUMN_INDEX){
						String headerText0 = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX));
						String headerText1 = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX + 1));
						String headerText2 = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX + 2));
						String headerText3 = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX + 3));
						if(headerText0.equals(HEADER_TEXT_AT_COLUMN_0) && headerText1.equals(HEADER_TEXT_AT_COLUMN_1) && headerText2.equals(HEADER_TEXT_AT_COLUMN_2) && headerText3.equals(HEADER_TEXT_AT_COLUMN_3)){
							foundHeader = true;
							continue;
						}else{
							throw new  BulkUplaodCourseException("Batch file is configured incorrectly. Error occurred on row [" + currentRowIndex + "]. Please edit the file and try again.", currentRowIndex);
						}
						
					}
					
						String objectType = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX));
						if(objectType == null){
							//check perhaps file contents are ended
							if(row.getRowNum() <= lastRowNo){
								throw new  BulkUplaodCourseException("Batch file is configured incorrectly. Error occurred on row [" +  (currentRowIndex + 1) + "]. Please edit the file and try again.", currentRowIndex);
							}else{
								break;
							}
						}
						
						if(objectType.equals("Lesson")){
							//course.addLesson(lesson);
							lesson = populateLessonObject(row.getRowNum(), row);
							lessonList.add(lesson);
						}else if(objectType.equals("Slide")){
							Slide slide = populateSlideData(row.getRowNum(), row, streamingtemplateId);
							lesson.addSlide(slide);
						}else{
							throw new  BulkUplaodCourseException("Batch file is configured incorrectly. Error occurred on row [" + (currentRowIndex + 1) + "]. Please edit the file and try again.", currentRowIndex);
						}
					//}
				}
			}
		}
		catch(Throwable t){
			//t.getMessage() has null value in case of NullPointerException
			String exMsg = t.getMessage() == null ? "" : t.getMessage();
			logger.error("=== ExCEL COURSE PARSING ERROR::" + exMsg);
			String errMsg = ""; 
			errMsg = exMsg.startsWith(" ") ? "Error occurred on row [" + (currentRowIndex + 1) + "]." + exMsg +" Please edit the file and try again.":"Batch file is configured incorrectly. Error occurred on row [" + (currentRowIndex + 1) + "]. Please edit the file and try again." ;
			throw new BulkUplaodCourseException(errMsg, currentRowIndex);
		}
		finally{
			if(file != null){
				file.close();
			}
		}
		
		if(lessonList.size() == 0 && foundHeader){
			throw new BulkUplaodCourseException("Batch file is configured incorrectly. Error occurred on row [2]. Please edit the file and try again.", currentRowIndex);
		}
		return lessonList;
	}

	private Row determindLastRow(Sheet sheet) {
		int lastRow = sheet.getPhysicalNumberOfRows();
		for(int i = lastRow - 1; i >= 0; i--){
			Row row = sheet.getRow(i);
			String objectValue = (String)getCellValue(row.getCell(OBJECT_CONTENT_TYPE_COLUMN_INDEX));
			if(objectValue == null){
				continue;
			}else{
				return row;
			}
		}
		return null;
	}

	public Course populateCourseData(Row courseRow) {
		
		Course course = new Course();
		course.setName((String)getCellValue(courseRow.getCell(COURSE_TITLE_COLUMN_INDEX)));
		course.setDescription((String)getCellValue(courseRow.getCell(COURSE_TITLE_COLUMN_INDEX + 1)));
		course.setLanguage_id(1);
		course.setKeywords(course.getName());
		String duration = "";

		try {
			if (duration != null && !duration.equals(""))
				course.setCeus(new BigDecimal(duration));
			else
				course.setCeus(new BigDecimal(0));
		} catch (Exception ex) {

			logger.debug("Error whole creating while casting duration field into decimal:: Online Course Exception "
					+ ex.getMessage());

		}
		return course;
	}

}
