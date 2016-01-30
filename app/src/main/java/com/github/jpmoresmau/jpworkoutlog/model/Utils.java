package com.github.jpmoresmau.jpworkoutlog.model;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Various utility functions
 * @author jpmoresmau
 */
public class Utils {

    /**
     * format wiehgt according to default locale
     * @param d
     * @return
     */
    public static String formatWeight(Double d){
        if (d==null){
            return "";
        }
        return NumberFormat.getNumberInstance().format(d);
    }

    /**
     * parse weight according to default locale
     * @param s
     * @return
     */
    public static Double parseWeight(String s){
        if (s==null || s.length()==0){
            return 0.0;
        }
        try {
            return NumberFormat.getNumberInstance().parse(s).doubleValue();
        } catch (ParseException pe){
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException nfe){
                return 0.0;
            }
        }
    }
}
