package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.sql.Clob;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.CourseSettingsDAO;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.CourseSettings;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;

/**
 * 
 * @author haider.ali
 *
 */
public class CourseSettingsDAOImpl extends GenericDAOImpl<CourseSettings> implements CourseSettingsDAO{

	
	private static Logger logger = Logger.getLogger(CourseSettingsDAOImpl.class);

	/**
	 * get Course Setting by Course ID
	 */
	@Transactional
	@Override
	public CourseSettings getCourseSettings(CourseSettings courseSettings) {

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseSettings.getCourse_Id()) , String.class, ParameterMode.IN);		
		Object[] courseSetting = callStoredProc(CORUSE_SETTING_PROCEDURE, sparam1).toArray();

		try {
			
			for (Object settings : courseSetting) {

				Object[] settingRow = (Object[]) settings;
				
				courseSettings.setCourseConfiguration_Id(Long.parseLong(StringUtil.ifNullReturnZero(settingRow[0])));
				courseSettings.setPassAllQuizzes( (Boolean) settingRow[1] );
				courseSettings.setAttemptTheExam( (Boolean) settingRow[2]);
				courseSettings.setPassTheExam( (Boolean) settingRow[3]);		
				courseSettings.setAgreeWithSpecifiedText( (Boolean) settingRow[4]);
				
				Clob cText = (Clob) settingRow[5];
				courseSettings.setSpecifiedText( StringUtil.clobStringConversion(cText));
				
				
				Clob courseName = (Clob) settingRow[6];
				courseSettings.setCourseName( StringUtil.clobStringConversion(courseName));
				
				courseSettings.setBusinessKey( (String) settingRow[7]);
                
				courseSettings.setDisplaySlidesTOC((Boolean) settingRow[8]);
				
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return courseSettings;
	}


	/**
	 * get Course Setting by Course ID
	 */
	@Transactional
	@Override
	public CourseSettings update(CourseSettings courseSettings) {
		
		SPCallingParams sparam0 = LCMS_Util.createSPObject("COURSECONFIGURATION_ID", stringVal(courseSettings.getCourseConfiguration_Id()), String.class, ParameterMode.IN);
   		SPCallingParams sparam1 = LCMS_Util.createSPObject("COMPLETION_QUIZMASTERY", stringVal(courseSettings.getPassAllQuizzes()), String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("COMPLETION_POSTASSESSMENTATTEMPTED", stringVal(courseSettings.getAttemptTheExam()), String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("COMPLETION_POSTASSESSMENTMASTERY", stringVal(courseSettings.getPassTheExam()), String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("EMBEDDED_ACKNOWLEDGMENT_ENABLEDTF", stringVal(courseSettings.getAgreeWithSpecifiedText()), String.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("EMBEDDED_ACKNOWLEDGMENT_TEXT", stringVal(courseSettings.getSpecifiedText()), String.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("PLAYER_SHOW_DISPLAY_SLIDES_TOC", stringVal(courseSettings.getDisplaySlidesTOC()), String.class, ParameterMode.IN);
		
		StoredProcedureQuery storedProcedureQuery = createQueryParameters("LCMS_WEB_UPDATE_COURSESETTINGS", sparam0, sparam1, sparam2, sparam3, sparam4, sparam5, sparam6);
		storedProcedureQuery.execute();

		return courseSettings;
	}
	
	/**
	 * 
	 * @param ob
	 * @return
	 */
	private static String stringVal(Object ob){
		if(ob!=null)
			return String.valueOf(ob);
		return "";
				
	}
}
