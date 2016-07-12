package com.softech.common.parsing;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public class ExpressionConstant {
    public static final String EMAIL = "^([\\w.%+-]+@[\\w-]+(\\.[a-zA-Z]{2,})+)?$";
    public static final String SHORT_DATE = "^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/(19|20|21)\\d\\d$";
    public static final String TIME = "^([0-9]|[01][0-9]|2[0-3]):([0-5][0-9]) ([Aa][Mm]|[Pp][Mm])$";
}
