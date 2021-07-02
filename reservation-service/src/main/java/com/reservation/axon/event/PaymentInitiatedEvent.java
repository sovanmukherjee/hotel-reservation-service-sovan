package com.reservation.axon.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiatedEvent {
	private EventDetails event;
	private String reservationNo;
	private String status;
}
