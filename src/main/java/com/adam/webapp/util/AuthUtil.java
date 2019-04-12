package com.adam.webapp.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class AuthUtil {
	
	public static String issueToken(ArrayList<String> authTokens) {
		SecureRandom secureRandom = new SecureRandom();
        String authToken = new BigInteger(130, secureRandom).toString(32);
    	if(authTokens != null && authTokens.contains(authToken))
    		issueToken(authTokens); // recursive call for case when auth token already exists in database
    	return authToken;
	}
	
	public static String hashStringValue(String stringVal) {
		return BCrypt.hashpw(stringVal, BCrypt.gensalt());
	}
	
	public static boolean isHashVerified(String value, String hash) {
		return BCrypt.checkpw(value, hash);
	}
	
}
