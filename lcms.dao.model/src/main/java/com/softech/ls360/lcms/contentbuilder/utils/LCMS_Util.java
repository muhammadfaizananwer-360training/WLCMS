package com.softech.ls360.lcms.contentbuilder.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.ParameterMode;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;

public class LCMS_Util{

    public static final String dateFormatString="MM/dd/yyyy";
	private static final ThreadLocal<DateFormat> formatter = new ThreadLocal<DateFormat>(){

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(dateFormatString);
        }



	}; //SimpleDateFormat("MM/dd/yyyy");
	
	public static SPCallingParams createSPObject (String param,	String value, Class<?>  ValueType,	ParameterMode parameterMode)
	{
		SPCallingParams obj = new SPCallingParams ();
		obj.setParam(param);
		obj.setValue(value);
		obj.setValueType(ValueType);
		obj.setParameterMode(parameterMode);
		return obj;
	}
	
	public static Timestamp parseStringToTimestamp(String dateString, String format) throws ParseException{

	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	            format);

	    java.util.Date parsedTimeStamp = dateFormat.parse(dateString);

	    Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
	    System.out.println("timestamp:" + timestamp);
	    
	    return timestamp;

	}
	
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (pattern == null || pattern.equals("")) {
			pattern = "MMM d, yyyy";
		}
		SimpleDateFormat sdf = getSimpleDateFormatWith(pattern);
		return sdf.format(date);
	}
	
	protected static SimpleDateFormat getSimpleDateFormatWith(String pattern) {
		SimpleDateFormat result = null;
		result = new SimpleDateFormat(pattern);
		return result;
	}
	
	
	public static Date getDateObject(String strDate){
		Date date = null;
		try {
			
			date = formatter.get().parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public Date getDate(String inputDate, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date outputDate = null;
		try {
			outputDate = sdf.parse(inputDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputDate;
	}
	
	public String getFormatedDateInfo(Date date, String patern) {
		SimpleDateFormat sdf = new SimpleDateFormat(patern);
		String formatedDateInfo = "";
		try {
			formatedDateInfo = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return formatedDateInfo;
	}
}
