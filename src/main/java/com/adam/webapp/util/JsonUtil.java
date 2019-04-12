package com.adam.webapp.util;

public class JsonUtil {

	public static String makeJsonBasicAuthResponse(String key, String value) {
		return "{\"" + key + "\":\"" + value + "\"}";
	}
	
}
