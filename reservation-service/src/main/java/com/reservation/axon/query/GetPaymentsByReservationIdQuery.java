package com.reservation.axon.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPaymentsByReservationIdQuery {
	private long reservationId;
	
}
