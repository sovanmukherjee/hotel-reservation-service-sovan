package com.crs.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GatewayPostFilter extends AbstractGatewayFilterFactory<GatewayPostFilter.Config>{

	
	public GatewayPostFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		log.info("inside GatewayPostFilter::apply method...");
		
		return(exchange, chain)->{
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				ServerHttpResponse response = exchange.getResponse();
				HttpHeaders headers = response.getHeaders();
				headers.forEach((k,v)->{
					log.info(k + " : " + v);
				});
			}));
		};
	}
	
	@Getter @Setter
	public static class Config {
		private String name;
		
	}
}
