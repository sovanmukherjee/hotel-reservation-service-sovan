package com.reservation.axon.command;

import java.time.LocalDate;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateReservationCommand {

	@TargetAggregateIdentifier
	private String reservationNo;
	private long id;
	private long guestId;
	private long hotelId;
	private long roomId;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private double totalAmount;
	private double totalDepositedAmount;
	private String comment;
	private String status;
}
