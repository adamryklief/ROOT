package com.adam.webapp.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {
	
	public static byte[] getDecryptedBytes(byte[] encryptedBytes, String secretKey)
            throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashedSecretKey = digest.digest(secretKey.getBytes(StandardCharsets.ISO_8859_1));
		SecretKeySpec secretKeySpec = new SecretKeySpec(hashedSecretKey, "AES");
		Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return decryptedBytes;
    }

}
