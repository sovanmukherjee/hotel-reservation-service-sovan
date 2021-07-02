package com.reservation.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreatePaymentCommand {
	@TargetAggregateIdentifier
	private String reservationNo;
	private long id;
	private String paymentBy;
	private double amount;
	private long paymentModeId;
	private long cardStoreId;
	private boolean isFailed;
	private long hotelId;
	private long roomId;
	private String status;
	
	public CreatePaymentCommand(String reservationNo, boolean isFailed,long hotelId,long roomId,String status) {
		this.reservationNo = reservationNo;
		this.isFailed = isFailed;
		this.hotelId = hotelId;
		this.roomId = roomId;
		this.status = status;
	}
}
