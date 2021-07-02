package com.crs.gateway.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crs.gateway.jwt.model.AuthRequest;
import com.crs.gateway.jwt.model.AuthResponse;
import com.crs.gateway.jwt.security.JwtUtil;
import com.crs.gateway.jwt.security.PBKDF2Encoder;
import com.crs.gateway.jwt.service.UserService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/v1")
public class AuthenticationController {

	@Autowired
	private UserService userService;
	@Autowired
	private PBKDF2Encoder pwdencoder;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping
	@RequestMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<? extends Object>> authenticate(@RequestBody AuthRequest request) {
		return userService.getUserByUsername(request.getUsername()).map((user) -> {
            if (pwdencoder.encode(request.getPassword()).equals(user.getPassword())) {
                return ResponseEntity.ok().body(new AuthResponse(jwtUtil.generateToken(user)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed"));
    }
		
	

}
