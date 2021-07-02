package com.hotel.exception;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hotel.config.MessageConfigValues;
import com.hotel.model.exception.ErrorResponse;
import com.hotel.model.response.ApiResponseData;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
class HotelServiceExceptionAdviceTest {
	@Autowired
	HotelServiceExceptionAdvice advice;
	@MockBean
	private Tracer tracer;
	@MockBean
	private Span span;
	@MockBean
	private TraceContext context;
	@MockBean
	private MessageConfigValues errorConfig;
	
	private MessageConfigValues.ErrorConfig buildErrorConfig(){
		MessageConfigValues.ErrorConfig config = new MessageConfigValues.ErrorConfig();
		config.setGenericErrorCode("generic-code");
		config.setGenericErrorMessage("message");
		return config;
	}
	
	@Test
	 void handleExceptionTest() {
		Mockito.when(tracer.currentSpan()).thenReturn(span);
		Mockito.when(span.context()).thenReturn(context);
		Mockito.when(context.traceId()).thenReturn("1001");
		Mockito.when(context.spanId()).thenReturn("1002");
		Mockito.when(errorConfig.getErrorConfig()).thenReturn(buildErrorConfig());
		Exception ex = new Exception("test exception");
		ResponseEntity<ApiResponseData<ErrorResponse>> res = advice.handleException(ex);
		assertEquals(500, res.getStatusCodeValue());
	}
}
