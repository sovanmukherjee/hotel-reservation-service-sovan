package com.hotel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * this is class used to load all messageConfig and errorConfig properties value
 *    
 * @author Sovan_Mukherjee
 *
 */
@Component
@ConfigurationProperties(prefix = "message")
@EnableConfigurationProperties
@Getter@Setter
@NoArgsConstructor
public class MessageConfigValues {
	private ErrorConfig errorConfig;
	private MessageConfig messageConfig;

	@ConfigurationProperties(prefix = "errorConfig")
	@Getter @Setter
	@NoArgsConstructor
	public static class ErrorConfig {
		private String genericErrorCode;
		private String genericErrorMessage;
		private String hotelInfoErrorCode;
		private String hotelInfoErrorMessage;
		private String hotelNotFoundErrorcode;
		private String hotelNotFoundErrorMessage;
	}

	@Getter@Setter
	@NoArgsConstructor
	@ConfigurationProperties(prefix = "messageConfig")
	public static class MessageConfig {
		private String addUpdateHotelMessage;
		private String addRoomMessage;
		private String deleteRoomMessage;
		private String updateRoomMessage;
	}

}
