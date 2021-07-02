package com.crs.gateway.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class AuthenticationExceptionAdvice {

	private <T> void logErrorMessage(String className, Object errorMessage, T t) {
		log.error(new StringBuilder().append("Error Cause: ").append(className).append("| Error Message: ")
				.append(errorMessage).toString(), t);
	}

	
	/**
	 * Bad credentials exception handler
	 * 
	 * @param Exception
	 * @return ResponseEntity
	 */
	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
		logErrorMessage(e.getClass().getSimpleName(), e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Authentication failed");
	}
	
	
	/**
	 * generic exception handler
	 * 
	 * @param Exception
	 * @return ResponseEntity
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		logErrorMessage(e.getClass().getSimpleName(), e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("There was an error while processing your request. Please try again.");
	}

}
