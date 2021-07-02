package com.hotel.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hotel.proxy.model.AuthRequest;
import com.hotel.proxy.model.AuthResponse;

@FeignClient(name = "${gateway.service.name}")
public interface AuthClient {

		@GetMapping(value = "/auth/v1/token")
		AuthResponse getToken(@RequestBody AuthRequest authRequest);
}
