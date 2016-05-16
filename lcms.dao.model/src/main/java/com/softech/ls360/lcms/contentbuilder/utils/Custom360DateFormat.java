package com.softech.ls360.lcms.contentbuilder.utils;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public class Custom360DateFormat extends DateFormat {
	DateFormat dateFormat;
	DateFormat dateTimeFormat;
	
	
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	
	public DateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(DateFormat dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		return dateTimeFormat.format(date, toAppendTo, fieldPosition);
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		try {
			Date date = dateTimeFormat.parse(source,pos);
			if(date == null){
				throw new Exception(source);
			}
			return date;
		} catch (Exception ex) {
			return dateFormat.parse(source, pos);
		}
	}

	
	@Override
	public Object clone() {
		Custom360DateFormat format = new Custom360DateFormat();
		format.setDateFormat((DateFormat) dateFormat.clone());
		format.setDateTimeFormat((DateFormat) dateTimeFormat.clone());
		return format;
	}
	
	
	@Override
	public int hashCode() {
		return dateFormat.hashCode();
	}

	
}
