package com.crs.gateway.filters;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GatewayPreFilter extends AbstractGatewayFilterFactory<GatewayPreFilter.Config> {

	public GatewayPreFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		log.info("inside GatewayPreFilter::apply method");
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			List<String> authorizationHeaders = request.getHeaders().get("Authorization");
			log.info("Authorization header value: {}", authorizationHeaders);
			return chain.filter(exchange.mutate().request(request).build());
		};
	}

	@Getter
	@Setter
	public static class Config {
		private String name;

	}

}
