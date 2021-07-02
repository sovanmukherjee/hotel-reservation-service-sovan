package com.reservation.axon.query;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReservationQuery {
	private UUID aggregateId;
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
