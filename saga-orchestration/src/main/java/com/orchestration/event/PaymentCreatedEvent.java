package com.orchestration.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PaymentCreatedEvent{
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
