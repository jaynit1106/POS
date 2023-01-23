package com.increff.pos.util;

import com.increff.pos.service.ApiException;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Objects;

public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static <T> void normalise(T form, Class<T> type) throws ApiException {
		Field[] fields = type.getDeclaredFields();

		for(Field field: fields) {

			if(field.getType().getSimpleName().equals("String")){
				field.setAccessible(true);
				try{
					if(Objects.nonNull(field.get(form))){
						field.set(form,(field.get(form).toString()).toLowerCase());
					}
				} catch(IllegalAccessException e){
					throw new ApiException("Error normalising form");
				}
			}
		}
	}

	public static String convertMrp(double val) {
		DecimalFormat df = new DecimalFormat("0.00");
	    String mrpFormated = df.format(val);
	    return mrpFormated;
	}

}
