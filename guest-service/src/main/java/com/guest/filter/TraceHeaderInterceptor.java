package com.guest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class TraceHeaderInterceptor extends GenericFilterBean {
	private final Tracer tracer;

	TraceHeaderInterceptor(Tracer tracer) {
	        this.tracer = tracer;
	    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 var currentSpan = this.tracer.currentSpan();
	        if (currentSpan == null) {
	            chain.doFilter(request, response);
	            return;
	        }
	        ((HttpServletResponse) response).addHeader("X-B3-Traceid",
	                currentSpan.context().traceId());
	        // we can also add some custom tags
	        currentSpan.tag("custom", "tag");
	        chain.doFilter(request, response);

	}

}
