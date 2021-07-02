package com.reservation.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.reservation.axon.event.PaymentCreatedEvent;
import com.reservation.axon.event.PaymentFailedEvent;
import com.reservation.axon.event.ReservationCreatedEvent;
import com.reservation.constants.MessageChannel;

@Service
public class ReservationProducer {

	@Autowired
    private StreamBridge streamBridge;
	
	public void sendToOrchestratorOrderCreated(ReservationCreatedEvent reservationCreatedEvent) {
		streamBridge.send(MessageChannel.RESERVATION_CREATE_REQUEST_CH,  MessageBuilder.withPayload(reservationCreatedEvent)
		           //.setHeader("partitionKey", 6 % 4)
		           .build());
		
	}

	public void sendToOrchestratorPaymentCreatedEvent(PaymentCreatedEvent event) {
		streamBridge.send(MessageChannel.PAYMENT_CREATED_EVENT_CH,  MessageBuilder.withPayload(event)
		           //.setHeader("partitionKey", 6 % 4)
		           .build());
	}

	public void sendToOrchestratorPaymentFailedEvent(PaymentFailedEvent event) {
		streamBridge.send(MessageChannel.PAYMENT_CREATED_EVENT_CH,  MessageBuilder.withPayload(event)
		           //.setHeader("partitionKey", 6 % 4)
		           .build());
	}
}
