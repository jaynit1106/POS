package com.increff.employee.util;

import org.apache.commons.lang3.RandomStringUtils;

public class BarcodeUtil {
	public static String generateBarcode() {
		return RandomStringUtils.random( 8, true, true);
	}

}
