package com.bjss.pricebasket.util;

public class ValidateUtil {

	public static void validateNotNull(Object arg, String argName)
			throws IllegalArgumentException {
		if (arg == null) {
			throw new IllegalArgumentException(String.format(
					"[%s] should not be null", argName));
		}
	}

}
