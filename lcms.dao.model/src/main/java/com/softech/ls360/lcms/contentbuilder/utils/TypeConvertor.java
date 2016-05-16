package com.softech.ls360.lcms.contentbuilder.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

    public class TypeConvertor {
    static final String dateFormatString = "MM/dd/yyyy";
    static final String dateTimeFormatString = "MM/dd/yyyy hh:mm a";
    static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(dateFormatString);
        }
    };
    //static final DateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    static final ThreadLocal<DateFormat> dateTimeFormat = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(dateTimeFormatString);
        }
    };
    static final long millisecondsInADay = 24 * 60 * 60 * 1000;
    static final long calendarFirstDayTime = 5 * 60 * 60 * 1000;
    static final Calendar timeComplianceCalendar;

    static {
        timeComplianceCalendar = Calendar.getInstance();
        timeComplianceCalendar.set(2015, 6, 6, 6, 6, 6);
        timeComplianceCalendar.set(Calendar.MILLISECOND, 1);
        try {
            timeComplianceCalendar.setTime(dateTimeFormat.get().parse(dateTimeFormat.get().format(timeComplianceCalendar.getTime())));
        } catch (ParseException ex) {
        }
    }

    public static String DateTimeToString(Date date) {
        String str = null;
        if (date != null) {
            return dateTimeFormat.get().format(date);
        }
        return str;

    }

    public static String DateToString(Date date) {
        String str = null;
        if (date != null) {
            return dateFormat.get().format(date);
        }
        return str;
    }

    public static String AnyToString(Object obj) {
        if (obj == null || obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Date) {
            Date date = (Date) obj;
            if (hasTimePart(date)) {
                return DateTimeToString(date);
            } else {
                return DateToString(date);
            }
        } else {
            return obj.toString();
        }
    }

    private static boolean hasTimePart(Date date) {
        Calendar clndr = Calendar.getInstance();
        clndr.setTime(date);
        return ((clndr.get(Calendar.HOUR) > 0 && timeComplianceCalendar.get(Calendar.HOUR) > 0)
                || (clndr.get(Calendar.MINUTE) > 0 && timeComplianceCalendar.get(Calendar.MINUTE) > 0)
                || (clndr.get(Calendar.SECOND) > 0 && timeComplianceCalendar.get(Calendar.SECOND) > 0)
                || (clndr.get(Calendar.MILLISECOND) > 0 && timeComplianceCalendar.get(Calendar.MILLISECOND) > 0));
    }

    public static Integer AnyToInteger(Object obj) {
        Double val = AnyToDouble(obj);
        if (val != null) {
            return val.intValue();
        }
        return null;
    }

    public static Long AnyToLong(Object obj) {
        Double val = AnyToDouble(obj);
        if (val != null) {
            return val.longValue();
        }
        return null;
    }

    public static Short AnyToShort(Object obj) {
        Double val = AnyToDouble(obj);
        if (val != null) {
            return val.shortValue();
        }
        return null;
    }

    public static Float AnyToFloat(Object obj) {
        Double val = AnyToDouble(obj);
        if (val != null) {
            return val.floatValue();
        }
        return null;
    }

    public static Double AnyToDouble(Object obj) {
        if (obj == null || obj instanceof Double) {
            return (Double) obj;
        } else if (StringUtils.isEmpty(obj.toString())) {
            return null;
        } else {
            return new Double(obj.toString());
        }
    }

    public static Boolean AnyToBoolean(Object obj) {
        if (obj == null || obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (StringUtils.isEmpty(obj.toString())) {
            return null;
        } else {
            return Boolean.valueOf(obj.toString());
        }
    }



    public static Date AnyToDate(Object obj) throws ParseException {
        if (obj == null || obj instanceof Date) {
            return (Date) obj;
        } else {
            try {
                return dateTimeFormat.get().parse(obj.toString());
            } catch (Exception ex) {
                return dateFormat.get().parse(obj.toString());
            }
        }
    }
}
