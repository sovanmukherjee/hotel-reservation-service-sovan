package com.guest.model.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.guest.model.exception.ErrorResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * this ApiResponseData contains - response message details
 * 
 * @author Sovan_Mukherjee
 *
 */
@Getter @AllArgsConstructor @NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiResponseData {
	private Object data;
	private HttpStatus status;
	private ErrorResponse errorResponse;

}
