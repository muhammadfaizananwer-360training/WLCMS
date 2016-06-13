package com.softech.ls360.lcms.contentbuilder.utils;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.sql.Clob;
import java.util.Random;

import org.springframework.util.StringUtils;

public class StringUtil {

    public static String clobStringConversion(Clob clb) throws Exception {
        if (clb == null) {
            return "";
        }

        StringBuffer str = new StringBuffer();
        String strng;
            BufferedReader bufferRead = new BufferedReader(clb.getCharacterStream());

            while ((strng = bufferRead.readLine()) != null) {
                str.append(strng);
            }
            bufferRead.close();

            return str.toString();
    }

    public static String ifNullReturnEmpty(Object clb) throws Exception {
        if (clb != null) {
            return clb.toString();
        } else {
            return "";
        }
    }

    public static String ifNullReturnZero(Object clb) throws Exception {
        if (clb != null) {
            return clb.toString();
        } else {
            return "0";
        }
    }

    public static Boolean isNumber(String val) {
        if (val != null) {
            try {
                Double.parseDouble(val);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return false;
    }

    public static boolean equalsAny(String str1, boolean ignoreCase, String... strs2) {
        if (!StringUtils.isEmpty(str1)) {
            for (String str2 : strs2) {
                boolean result;
                if (ignoreCase) {
                    result = str1.equalsIgnoreCase(str2);
                } else {
                    result = str1.equals(str2);
                }

                if (result) {
                    return result;
                }
            }
        }
        return false;
    }


    public static String trimSuffix(String str, String suffix) {
        if(StringUtils.endsWithIgnoreCase(str,suffix)){
            return str.substring(0,str.length() - suffix.length());
        }
        return str;
    }

    public static String getRandom(int length) {
        Random random = new Random();
        return new BigInteger(length * 5, random).toString(32).toUpperCase(); //five bit representation of a number.
    }

}
