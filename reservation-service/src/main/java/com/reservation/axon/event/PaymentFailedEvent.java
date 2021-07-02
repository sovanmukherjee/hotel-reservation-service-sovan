package com.reservation.axon.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEvent {
	private EventDetails event;
	private String reservationNo;
	private long id;
	private String paymentBy;
	private double amount;
	private long paymentModeId;
	private long cardStoreId;
	private long hotelId;
	private long roomId;
	private String status;
}
