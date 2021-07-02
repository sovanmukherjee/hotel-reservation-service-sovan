package com.crs.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class GatewayFallback {

	@GetMapping
    public ResponseEntity<String> getAccount() {
       return ResponseEntity.ok().body("Service is not available now. Please try after sometime");
    }
}
