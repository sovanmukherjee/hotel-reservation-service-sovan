package com.reservation.proxy.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

import com.reservation.proxy.AuthClient;
import com.reservation.proxy.model.AuthRequest;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

	public static final String AUTHORIZATION_STRING = "Authorization";

	@Value("${security.appId}")
	private String appId;
	@Value("${security.pass}")
	private String password;

	private final Tracer tracer;
	private final AuthClient authClient;

	FeignClientInterceptor(Tracer tracer, AuthClient authClient) {
		this.tracer = tracer;
		this.authClient = authClient;
	}

	@Override
	public void apply(RequestTemplate template) {
		if (!template.url().contains("/token")) {
			var authResponse = authClient.getToken(new AuthRequest(appId, password));
			var currentSpan = this.tracer.currentSpan();
			if (null != currentSpan) {
				template.header("X-B3-Traceid", currentSpan.context().traceId());
				template.header("traceid", currentSpan.context().traceId());
				template.header(AUTHORIZATION_STRING, authResponse.getToken());
			}
		}
	}

}
