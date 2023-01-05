package com.increff.pos.util;

import java.time.Instant;

public class TimestampUtil {
	public static String getTimestamp() {
		return Instant.now().toString();
	}
}
