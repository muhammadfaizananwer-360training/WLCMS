package com.softech.ls360.lcms.contentbuilder.manager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.utils.WlcmsConstants;

@Component
public class OnlineCourseExportManager {
	
	ICourseService courseService;
	
	private static Logger logger = LoggerFactory
			.getLogger(OnlineCourseExportManager.class);
	
	public OnlineCourseExportManager(){
		//super(null, null, null, 0);
	}
	
	public OnlineCourseExportManager(ICourseService courseService){
		this.courseService = courseService;
		//super(fileName, parser, courseService, courseId);
	}

	
	public List<Object> exportCourse(int courseId) throws IOException, BulkUplaodCourseException, SQLException {
	 
		List<?> contentObjectList = courseService
				.getContentObjectList(courseId);

		List<Object> dataList = new ArrayList<Object>();
		
		dataList.add(new Object[]{"Content Type","Title","Slide Template"});
		
		for(int i = 0; i < contentObjectList.size(); i++){
			
			Object[] arrayObject   = (Object[])contentObjectList.get(i);
			dataList.add(new Object[]{"Lesson", arrayObject[2],"Not Applicable (Lessons Only)"});
			List<Slide> lstSlide = courseService.getSlidesByContentObject((Integer)arrayObject[0]);
			for(Slide slide : lstSlide){
				dataList.add(new Object[]{"Slide", slide.getName(), getSlideTemplateName(slide.getTemplateID())});
			}
		}
		
		return dataList;
	}
	
	private Object getSlideTemplateName(long templateID) {
		
		String templateName;
		switch((int)templateID){
			case WlcmsConstants.VISUAL_LEFT_CONSTATNT:
				templateName =  "Visual Left";
			break;
			case WlcmsConstants.VISUAL_RIGHT_CONSTATNT:
				templateName =  "Visual Right";
				break;
			case WlcmsConstants.VISUAL_TOP_CONSTATNT:
				templateName =  "Visual Top";
				break;
			case WlcmsConstants.VISUAL_BOTTOM_CONSTATNT:
				templateName =  "Visual Bottom";
				break;
			case WlcmsConstants.VIDEO_STRAMING_CONSTATNT:
				templateName =  "Streaming Video";
				break;
			default:
				templateName =  "Visual Left";
		}
		return templateName;
	}

	public boolean createLesson() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createSlide() {
		// TODO Auto-generated method stub
		return false;
	}

}
