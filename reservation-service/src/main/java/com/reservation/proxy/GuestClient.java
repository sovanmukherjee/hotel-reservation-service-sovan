package com.reservation.proxy;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.reservation.model.response.ApiResponseData;
import com.reservation.proxy.model.guest.Guest;

@FeignClient(name = "${guest.service.name}")
public interface GuestClient {
	@GetMapping(value = "/api/v1/guests/{guestId}")
	public ApiResponseData<Guest> getGuestById(@RequestHeader Map headerMap,@PathVariable("guestId") long guestId);


}
