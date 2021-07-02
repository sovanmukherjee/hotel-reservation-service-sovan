package com.reservation.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ReservationRequest {
	private long id;
	@NotBlank(message = "Guest Id is mandatory")
	private long guestId;
	@NotBlank(message = "Hotel Id is mandatory")
	private long hotelId;
	@NotBlank(message = "Room Id is mandatory")
	private long roomId;
	@NotBlank(message = "CheckInDate is mandatory")
	private LocalDate checkInDate;
	@NotBlank(message = "CheckOutDate is mandatory")
	private LocalDate checkOutDate;
	@NotBlank(message = "TotalAmount is mandatory")
	private double totalAmount;
	private double totalDepositedAmount;
	private String comment;
	private String status;

	
	
	

}
