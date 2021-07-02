package com.reservation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reservation.model.CardDetails;
import com.reservation.model.Payment;
import com.reservation.model.ReservationRequest;
import com.reservation.model.response.ApiResponseData;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "${spring.data.rest.base-path}", produces = APPLICATION_JSON_VALUE)
public interface ReservationController {
	/**
	 * Get All Reservations
	 * 
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "Get ALL Reservations", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from reservation service", response = ApiResponseData.class) })
	@GetMapping
	ResponseEntity<ApiResponseData> getReservations(@RequestHeader Map<String, String> headers);
	
	/**
	 * Allocate Room
	 * 
	 * @param reservation
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS') or hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "allocate room", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully room allocated", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Reservation deatils are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from reservation service", response = ApiResponseData.class) })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData> allocateRoom(@RequestHeader Map<String, String> headers, @RequestBody ReservationRequest reservation);

	/**
	 * get reserved hotel and rooms by start and end date
	 * 
	 * @param guest
	 * @param guestId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "get reserved hotel and rooms by start and end date", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrived allocated rooms", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Guest deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from guest service", response = ApiResponseData.class) })
	@GetMapping(value = "/{startDate}/{endDate}")
	ResponseEntity<ApiResponseData> getReservationHotelRoomsByDate(@RequestHeader Map<String, String> headers, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate);

	
	/**
	 * make payment
	 * 
	 * @param payment
	 * @param reservationId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "make payment", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Payment successfull", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The payment deatils are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from reservation service", response = ApiResponseData.class) })
	@PostMapping(value="/{reservationId}/payment", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData> payment(@RequestHeader Map<String, String> headers,@RequestBody Payment payment, @PathVariable("reservationId") long reservationId);

	/**
	 * add card details
	 * 
	 * @param cardDetails
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "add card details", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Card details added successfull", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The card deatils are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from reservation service", response = ApiResponseData.class) })
	@PostMapping(value="/card", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData> addCardDetails(@RequestHeader Map<String, String> headers,@RequestBody CardDetails cardDetails);

	
	/**
	 * allocate room using axon
	 * 
	 * @param headers
	 * @param reservation
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "allocate room using axon", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully room allocated", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Reservation deatils are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from reservation service", response = ApiResponseData.class) })
	@PostMapping(value="/axon", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData> allocateRoomWithAxonFrmwk(@RequestHeader Map<String, String> headers, @RequestBody ReservationRequest reservation);

	
	/**
	 * Get ALL Reservations using axon framework
	 * 
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "Get ALL Reservations using axon framework", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from reservation service", response = ApiResponseData.class) })
	@GetMapping(value="axon")
	ResponseEntity<ApiResponseData> getReservationsWithAxonFrmwk(@RequestHeader Map<String, String> headers);

	/**
	 * Get ALL Events by reservation no using axon framework
	 * 
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "Get ALL Events by reservation no using axon framework", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from reservation service", response = ApiResponseData.class) })
	@GetMapping(value="/axon/events/{id}")
	ResponseEntity<ApiResponseData> getEventsByIdWithAxonFrmwk(@RequestHeader Map<String, String> headers, @PathVariable("id") String id);

	
	/**
	 * make payment
	 * 
	 * @param payment
	 * @param reservationId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "make payment using axon framework", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Payment successfull", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The payment deatils are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from reservation service", response = ApiResponseData.class) })
	@PostMapping(value="/{reservationNo}/payment/axon", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData> paymentWithAxonFrmwk(@RequestHeader Map<String, String> headers,@RequestBody Payment payment, @PathVariable("reservationNo") String reservationNo);

}
