package com.crs.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.factory.FallbackHeadersGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerResilience4JFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

@Configuration
public class CircuitBreakerConfig {

//	@Bean
//	public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(CircuitBreakerRegistry circuitBreakerRegistry) {
//		 var timeLimiterRegistry = TimeLimiterRegistry.ofDefaults();
//		var reactiveResilience4JCircuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory(circuitBreakerRegistry,timeLimiterRegistry);
//	    reactiveResilience4JCircuitBreakerFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);
//	    return reactiveResilience4JCircuitBreakerFactory;
//	}
//	
//	@Bean
//	public Customizer defaultCustomizer() {
//	   return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//	      .circuitBreakerConfig(CircuitBreakerConfig.custom()
//	         .slidingWindowSize(5)
//	         .permittedNumberOfCallsInHalfOpenState(5)
//	         .failureRateThreshold(50.0F)
//	         .waitDurationInOpenState(Duration.ofMillis(30))
//	         .build())
//	      .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(200)).build()).build());
//	}
	
	@Bean
    public FallbackHeadersGatewayFilterFactory fallbackHeadersGatewayFilterFactory() {
        return new FallbackHeadersGatewayFilterFactory();
    }

    @Bean
    public SpringCloudCircuitBreakerFilterFactory resilience4JCircuitBreakerFactory(
            ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory,
            ObjectProvider<DispatcherHandler> dispatcherHandlers) {
        return new SpringCloudCircuitBreakerResilience4JFilterFactory(reactiveCircuitBreakerFactory, dispatcherHandlers);
    }
}
