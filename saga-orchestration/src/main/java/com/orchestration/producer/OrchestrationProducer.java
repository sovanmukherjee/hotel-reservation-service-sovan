package com.orchestration.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.orchestration.constant.MessageChannel;
import com.orchestration.event.PaymentCreatedEvent;


@Component
public class OrchestrationProducer {
	@Autowired
    private StreamBridge streamBridge;
	
	public <T> void  createPaymentRequest(T reservationCreatedEvent) {
		streamBridge.send(MessageChannel.CREATE_PAYMENT_REQUEST_CH,  MessageBuilder.withPayload(reservationCreatedEvent)
		           //.setHeader("partitionKey", 6 % 4)
		           .build());
	}

	public <T> void updateHotelRoomStatus(T paymentEvent) {
		streamBridge.send(MessageChannel.UPDATE_ROOM_STATUS_CH,  MessageBuilder.withPayload(paymentEvent)
		           //.setHeader("partitionKey", 6 % 4)
		           .build());
	}
}
