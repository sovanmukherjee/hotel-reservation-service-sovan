package com.reservation.axon.service;

import java.util.Map;

import com.reservation.axon.event.ReservationCreatedEvent;
import com.reservation.model.Payment;
import com.reservation.model.ReservationRequest;

public interface ReservationCommandService {
	public String bookHotelRoom(Map<String, String> headers,ReservationRequest reservation);

	public String payment(Map<String, String> headers, Payment payment, String reservationNo);

	public void paymentInitiate(ReservationCreatedEvent reservation);
}
