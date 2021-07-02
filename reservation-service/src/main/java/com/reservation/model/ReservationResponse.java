package com.reservation.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.reservation.proxy.model.guest.Guest;
import com.reservation.proxy.model.hotel.Hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor@Builder
@JsonInclude(Include.NON_NULL)
public class ReservationResponse {
	private long id;
	private Guest guest;
	private Hotel hotel;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private double totalAmount;
	private double totalDepositedAmount;
	private String comment;
	private List<Payment> payment;
	private String status;
	
	private long hotelId;
	private List<Long> roomIds;
	
}
