package com.hotel.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hotel.model.response.ApiResponseData;
import com.hotel.proxy.model.Reservation;

/**
 * this interface is used to call reservation-service APIs to get reservation information 
 * 
 * @author Sovan_Mukherjee
 *
 */
@FeignClient(name = "${reservation.service.name}")
public interface ReservationClient {

	@GetMapping(value = "/api/v1/reservations/{startDate}/{endDate}")
	ApiResponseData<List<Reservation>> getReservedRooms(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate);
}
