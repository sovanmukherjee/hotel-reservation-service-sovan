package com.guest.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.guest.config.MessageConfigValues;
import com.guest.model.exception.ErrorResponse;
import com.guest.model.response.ApiResponseData;

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
public class GuestServiceExceptionAdvice {
	@Autowired
	private Tracer tracer;
	@Autowired
	private MessageConfigValues errorConfig;

	private ApiResponseData buildApiResponse(ErrorResponse errorData, HttpStatus status) {
		return new ApiResponseData(null, status, errorData);
	}

	private <T> void logErrorMessage(String className, Object errorMessage, T t) {
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
	public ResponseEntity<ApiResponseData> handleBusinessException(BusinessException be) {
		var errorResponse = be.getErrorResponse();
		errorResponse.setTraceId(getTraceId());
		logErrorMessage(be.getClass().getSimpleName(), errorResponse.getErrorMessage(), be);
		return ResponseEntity.badRequest().body(buildApiResponse(errorResponse, HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseData> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach(k -> {
	        String fieldName = ((FieldError) k).getField();
	        String errorMessage = k.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    
	    var errorResponse = new ErrorResponse();
		errorResponse.setTraceId(getTraceId());
		errorResponse.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		errorResponse.setErrorMessage(errors);
		errorResponse.setErrorSubCode(errorConfig.getErrorConfig().getInputValidationErrorCode());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(buildApiResponse(errorResponse, HttpStatus.BAD_REQUEST));
	}

	/**
	 * generic exception handler
	 * 
	 * @param Exception
	 * @return ResponseEntity
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ApiResponseData> handleException(Exception e) {
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
