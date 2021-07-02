package com.crs.gateway.jwt.service;

import com.crs.gateway.jwt.model.User;

import reactor.core.publisher.Mono;

public interface UserService {

	Mono<User> getUserByUsername(String username);

}
