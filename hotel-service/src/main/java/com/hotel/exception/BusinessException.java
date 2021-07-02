package com.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BusinessException extends HotelServiceException {
	
	private static final long serialVersionUID = 1L;
	private static final HttpStatus HTTP_RESPONSE_CODE = HttpStatus.BAD_REQUEST;
	/**
	 * Instantiation a new business exception
	 * 
	 * @param errorSubCode
	 * @param errorMessage
	 */
	public BusinessException(String errorSubCode, String errorMessage) {
		super(HTTP_RESPONSE_CODE, errorSubCode, errorMessage);
	}

}
