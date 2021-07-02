package com.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.reservation.model.CardDetails;
import com.reservation.model.Payment;
import com.reservation.model.ReservationRequest;
import com.reservation.model.ReservationResponse;

public interface ReservationService {

	public List<ReservationResponse> getReservations(Map<String, String> headers);

	public String allocateRoom(Map<String, String> headers,ReservationRequest reservation);

	public List<ReservationResponse> getReservationHotelRoomsByDate(Map<String, String> headers,LocalDate startDate, LocalDate endDate);

	public String payment(Map<String, String> headers,Payment payment,long reservationId);

	public String addCardDetails(Map<String, String> headers,CardDetails cardDetails);

}
