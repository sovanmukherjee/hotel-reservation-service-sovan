package com.orchestration.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orchestration.event.PaymentCreatedEvent;
import com.orchestration.event.ReservationCreatedEvent;
import com.orchestration.producer.OrchestrationProducer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OrchestrationConsumer {
	
	@Bean
	public Consumer<ReservationCreatedEvent> reservationCreatedEvent(OrchestrationProducer orchestrationProducer) {
		return reservation -> {
			log.info("Reservation Request Event Received at OrchestrationConsumer::updateHotelRoomStatus");
			log.info("Registration No: {}", reservation.getReservationNo());
			log.info("Event Type: {}", reservation.getEvent().getEventType());
			log.info("Registration Status: {}", reservation.getStatus());
			orchestrationProducer.updateHotelRoomStatus(reservation);
			orchestrationProducer.createPaymentRequest(reservation);
		};
	}
	
	@Bean
	public Consumer<PaymentCreatedEvent> paymentCreatedEvent(OrchestrationProducer orchestrationProducer) {
		return paymentEvent -> {
			log.info("Payment Created Event Received at OrchestrationConsumer::paymentCreatedEvent");
			log.info("Registration No: {}", paymentEvent.getReservationNo());
			log.info("Event Type: {}", paymentEvent.getEvent().getEventType());
			log.info("Amount: {}", paymentEvent.getAmount());
			if("PaymentFailedEvent".equalsIgnoreCase(paymentEvent.getEvent().getEventType())){
				paymentEvent.setStatus("AVAILABLE");
				orchestrationProducer.updateHotelRoomStatus(paymentEvent);
			}
			
		};
	}
	
	
	
	
}
