package com.hotel.exception;

import org.springframework.http.HttpStatus;

import com.hotel.model.exception.ErrorResponse;

/**
 * this is generic exception class build error object
 * 
 * @author Sovan_Mukherjee
 *
 */
public class HotelServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final ErrorResponse errorResponse = new ErrorResponse();
	private void buildErrorResponse(final String errorCode, final String errorSubCode, final String errorMessage) {
		errorResponse.setErrorCode(errorCode);
		errorResponse.setErrorSubCode(errorSubCode);
		errorResponse.setErrorMessage(errorMessage);
	}

	
	public HotelServiceException(final HttpStatus httpStatus, final String errorSubCode, final String errorMessage) {
		buildErrorResponse(String.valueOf(httpStatus.value()) , errorSubCode, errorMessage);
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
}
