package com.guest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guest.model.Guest;
import com.guest.model.response.ApiResponseData;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "${spring.data.rest.base-path}", produces = APPLICATION_JSON_VALUE)
public interface GuestController {
	/**
	 * Get All Guest
	 * 
	 * @return ResponseEntity
	 */
	@ApiOperation(value = "Get all Guest", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from guest service", response = ApiResponseData.class) })
	@GetMapping
	ResponseEntity<ApiResponseData> getGuests();
	
	@ApiOperation(value = "get guest details by id", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Guest Details", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Guest deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from guest service", response = ApiResponseData.class) })
	@GetMapping("/{guestId}")
	ResponseEntity<ApiResponseData> getGuestById(@PathVariable("guestId") long guestId);


	/**
	 * add Guest
	 * 
	 * @param guest
	 * @return ResponseEntity
	 */
	@ApiOperation(value = "add new Guest", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully added guest", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Guest deatils are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from guest service", response = ApiResponseData.class) })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData> addGuest(@RequestBody Guest guest);

	/**
	 * update Guest
	 * 
	 * @param guest
	 * @param guestId
	 * @return ResponseEntity
	 */
	@ApiOperation(value = "update Guest", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully updated Guest", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Guest deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from guest service", response = ApiResponseData.class) })
	@PutMapping(value = "/{guestId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData> updateGuest(@RequestBody Guest guest, @PathVariable("guestId") long guestId);

	
}
