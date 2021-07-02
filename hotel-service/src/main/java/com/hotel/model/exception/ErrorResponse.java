package com.hotel.model.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
/**
 * this class contains error response related information
 * 
 * @author Sovan_Mukherjee
 *
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
private String errorCode;
private String errorMessage;
private String errorSubCode;
private String traceId;
}
