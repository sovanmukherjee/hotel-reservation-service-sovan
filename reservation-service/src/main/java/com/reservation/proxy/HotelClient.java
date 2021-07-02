package com.reservation.proxy;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.reservation.model.response.ApiResponseData;
import com.reservation.proxy.model.hotel.Hotel;

@FeignClient(name = "${hotel.service.name}")
public interface HotelClient {
	
	@GetMapping(value = "/api/v1/hotels/{hotelId}/room/{roomId}")
	ApiResponseData<Hotel> getHotelRoomById(@RequestHeader Map headerMap, @PathVariable("hotelId") long hotelId,@PathVariable("roomId") long roomId);
}
