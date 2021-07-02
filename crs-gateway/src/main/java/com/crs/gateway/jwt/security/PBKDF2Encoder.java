package com.crs.gateway.jwt.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PBKDF2Encoder implements PasswordEncoder {
	private static final String ALGO = "PBKDF2WithHmacSHA512";

	@Value("${security.password.encoder.secret}")
	private String secret;

	@Value("${security.password.encoder.iteration}")
	private Integer iteration;

	@Value("${security.password.encoder.keylength}")
	private Integer keylength;

	@Override
	public String encode(CharSequence key) {
		try {
			byte[] result = SecretKeyFactory.getInstance(ALGO)
					.generateSecret(
							new PBEKeySpec(key.toString().toCharArray(), secret.getBytes(), iteration, keylength))
					.getEncoded();
			return Base64.getEncoder().encodeToString(result);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean matches(CharSequence cs, String string) {
		return encode(cs).equals(string);
	}

}