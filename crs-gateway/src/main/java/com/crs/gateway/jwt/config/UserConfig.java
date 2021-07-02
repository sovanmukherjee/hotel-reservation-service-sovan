package com.crs.gateway.jwt.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.crs.gateway.jwt.model.User;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties
@Component
@Getter  @Setter
public class UserConfig {
	private Map<String, User> users;
}
