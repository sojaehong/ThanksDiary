package com.ssostudio.thanksdiary.utility;

import java.text.DecimalFormat;

public class FormatChangeManager {

    public static String intToCommaFormat(long count){
        DecimalFormat format = new DecimalFormat("###,###");
        String s = format.format(count);
        return s;
    }

    public static String floatToCommaFormat(float count){
        DecimalFormat format = new DecimalFormat("#.##");
        String s = format.format(count);
        return s;
    }

}
