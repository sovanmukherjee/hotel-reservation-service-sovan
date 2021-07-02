package com.reservation.config;


import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reservation.axon.event.ReservationCreatedEvent;
import com.reservation.axon.service.ReservationCommandService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class PaymentConsumer {

	@Bean
	public Consumer<ReservationCreatedEvent> createPaymentRequest(ReservationCommandService reservationCommandService) {
		return reservation -> {
			log.info("Payment Request Event Received at PaymentConsumer::createPaymentRequest");
			log.info("Registration No: {}", reservation.getReservationNo());
			log.info("Registration Status: {}", reservation.getStatus());
			reservationCommandService.paymentInitiate(reservation);
		};
	}
}
