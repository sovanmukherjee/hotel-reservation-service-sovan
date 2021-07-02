package com.crs.gateway.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crs.gateway.jwt.config.UserConfig;
import com.crs.gateway.jwt.model.User;

import reactor.core.publisher.Mono;

@Service
	
public class UserServiceImpl implements UserService {
    @Autowired
    private UserConfig userConfig;

	@Override
	public Mono<User> getUserByUsername(String username) {
		if (userConfig.getUsers().containsKey(username)) {
            return Mono.just(userConfig.getUsers().get(username));
        } else {
            return Mono.empty();
        }
	}

	

	

}
