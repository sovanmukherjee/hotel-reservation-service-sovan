package com.hotel.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotel.proxy.model.Reservation;
import com.hotel.service.HotelService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class HotelConsumer {

	@Bean
	public Consumer<Reservation> updateHotelRoomStatus(HotelService hotelService) {
		return reservation -> {
			log.info("Message Received at HotelConsumer::updateHotelRoomStatus");
			hotelService.updateRoomStatus(reservation);
		};
	}
}
