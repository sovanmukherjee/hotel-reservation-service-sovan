package com.crs.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class CRSConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CRSConfigServerApplication.class, args);
	}

}
