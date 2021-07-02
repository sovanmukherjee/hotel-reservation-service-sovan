package com.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * this is a Swagger configuration file for API documentation
 * 
 * @author Sovan_Mukherjee
 *
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).enable(true).select()
				.apis(RequestHandlerSelectors.basePackage("com.hotel.controller")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Hotel-Service APIs ").description("MyHotels Reservation System").version("V1").build();
	}
}
