package com.reservation.axon.event;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreatedEvent{
	private EventDetails event;
    private String reservationNo;
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
