package com.reservation.axon.service;

import java.util.List;
import java.util.Map;

import com.reservation.model.ReservationResponse;

public interface ReservationQueryService {

	List<ReservationResponse> getReservations(Map<String, String> headers);
	List<Object> getEventsById(String id);
}
