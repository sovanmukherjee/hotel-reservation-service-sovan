package com.guest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guest.model.Guest;
import com.guest.model.response.ApiResponseData;
import com.guest.service.GuestService;

/**
 * This controller is used for all Guest related operations
 * 
 * @author Sovan_Mukherjee
 *
 */
@RestController
public class GuestControllerImpl implements GuestController{
	@Autowired
	private GuestService guestService;
	
	private <T> ApiResponseData buildApiResponse(T data, HttpStatus status) {
        return new ApiResponseData(data, status, null);
    }

	/**
	 * get all guests
	 */
	@Override
	public ResponseEntity<ApiResponseData> getGuests() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(buildApiResponse(guestService.getGuests(),HttpStatus.OK));
	}
	
	/**
	 * get Guest details by id
	 */
	@Override
	public ResponseEntity<ApiResponseData> getGuestById(long guestId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(buildApiResponse(guestService.getGuestById(guestId),HttpStatus.OK));
	}


	/**
	 * add new Guest
	 */
	@Override
	public ResponseEntity<ApiResponseData> addGuest(@Valid Guest guest) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(buildApiResponse(guestService.addGuest(guest),HttpStatus.CREATED));
	}

	@Override
	public ResponseEntity<ApiResponseData> updateGuest(@Valid Guest guest, long guestId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(buildApiResponse(guestService.updateGuest(guest,guestId),HttpStatus.OK));
	}

	
	
	

}
