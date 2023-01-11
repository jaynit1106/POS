package com.increff.pos.util;

import java.text.DecimalFormat;

public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}
	
	public static String convertMrp(double val) {
		DecimalFormat df = new DecimalFormat("0.00");
	    String mrpFormated = df.format(val);
	    return mrpFormated;
	}

}
