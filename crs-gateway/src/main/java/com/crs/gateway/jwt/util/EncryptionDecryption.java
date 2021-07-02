package com.crs.gateway.jwt.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class EncryptionDecryption {

//	public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
//		System.out.println("encrypt:: "+ encrypt("admin"));
//		//fPPYmiylbeQUAQVuyhPSoH9iU/Kr9TsZh6iiLodpqbk=
//	}
	
	
	public static String encrypt(CharSequence key) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                .generateSecret(new PBEKeySpec(key.toString().toCharArray(), "passwordEncodeSecret".getBytes(), 65536, 256))
                .getEncoded();
		return Base64.getEncoder().encodeToString(result);
		
	}
	
	

}
