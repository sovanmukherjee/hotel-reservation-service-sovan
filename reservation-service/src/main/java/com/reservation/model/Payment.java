package com.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	private long id;
	private String paymentBy;
	private double amount;
	private PaymentMode mode;
	private long cardStoreId;
}
