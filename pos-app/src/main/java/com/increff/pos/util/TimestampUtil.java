package com.increff.pos.util;

import java.time.Instant;

public class TimestampUtil {
	public static String getTimestamp() {
		String ts = Instant.now().toString();
		ts=ts.replace('T', ' ');
		ts=ts.replace('Z',' ');
		ts = ts.substring(0, 16);
		return ts;
	}
}
