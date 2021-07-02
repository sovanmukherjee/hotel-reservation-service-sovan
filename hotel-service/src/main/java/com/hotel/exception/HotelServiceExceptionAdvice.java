package com.hotel.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hotel.config.MessageConfigValues;
import com.hotel.model.exception.ErrorResponse;
import com.hotel.model.response.ApiResponseData;

import lombok.extern.slf4j.Slf4j;

/**
 * this class intercept all exception and return ErrorRespomse object with
 * ApiResponseData response
 * 
 * @author Sovan_Mukherjee
 *
 */
@Slf4j
@ControllerAdvice
public class HotelServiceExceptionAdvice {
	@Autowired
	private Tracer tracer;
	@Autowired
	private MessageConfigValues errorConfig;

	private ApiResponseData<ErrorResponse> buildApiResponse(ErrorResponse errorData, HttpStatus status) {
		return new ApiResponseData<>(null, status, errorData);
	}

	private <T> void logErrorMessage(String className, String errorMessage, T t) {
		log.error(new StringBuilder().append("Error Cause: ").append(className).append("| Error Message: ")
				.append(errorMessage).toString(), t);
	}

	private String getTraceId() {
		var span = tracer.currentSpan();
		TraceContext context = null != span? span.context():null;
		return String.format("%s | %s", null !=context?context.traceId():null, null !=context?context.spanId():null);
	}

	/**
	 * BusinessException handler
	 *  
	 * @param BusinessException
	 * @return ResponseEntity
	 */
	@ExceptionHandler(value = BusinessException.class)
	public ResponseEntity<ApiResponseData<ErrorResponse>> handleBusinessException(BusinessException be) {
		var errorResponse = be.getErrorResponse();
		errorResponse.setTraceId(getTraceId());
		logErrorMessage(be.getClass().getSimpleName(), errorResponse.getErrorMessage(), be);
		return ResponseEntity.badRequest().body(buildApiResponse(errorResponse, HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<ApiResponseData<ErrorResponse>> accessDeniedException(AccessDeniedException e) {
		logErrorMessage(e.getClass().getSimpleName(), e.getMessage(), e);
		var errorResponse = new ErrorResponse();
		errorResponse.setTraceId(getTraceId());
		errorResponse.setErrorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
		errorResponse.setErrorMessage("Unauthorized Access");
		errorResponse.setErrorSubCode("UNAUTHORIZED_ACCESS");
		errorResponse.setTraceId(getTraceId());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(buildApiResponse(errorResponse, HttpStatus.UNAUTHORIZED));
	}

	/**
	 * generic exception handler
	 * 
	 * @param Exception
	 * @return ResponseEntity
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ApiResponseData<ErrorResponse>> handleException(Exception e) {
		logErrorMessage(e.getClass().getSimpleName(), e.getMessage(), e);
		var errorResponse = new ErrorResponse();
		errorResponse.setTraceId(getTraceId());
		errorResponse.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		errorResponse.setErrorMessage(errorConfig.getErrorConfig().getGenericErrorMessage());
		errorResponse.setErrorSubCode(errorConfig.getErrorConfig().getGenericErrorCode());
		errorResponse.setTraceId(getTraceId());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(buildApiResponse(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
	}
	
	
	

}
