package com.softech.ls360.lcms.contentbuilder.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LCMSDateUtils {

	public static int getTwoDigitYearValue()
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = sdf.format(Calendar.getInstance().getTime());
		
		
		   //  String.format("%0"+5+"d", Integer.parseInt(formattedDate));
				
		return Integer.parseInt(formattedDate);
	}
	
	
	public static String addLeadingZeroes(int size, long value)
	{
	    return String.format("%0"+size+"d", value);
	}
}
