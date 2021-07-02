package com.hotel.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hotel.model.Hotel;
import com.hotel.model.Room;
import com.hotel.model.response.ApiResponseData;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//@CrossOrigin(origins = "http://localhost:8098", maxAge = 3600)
@RequestMapping(path = "${spring.data.rest.base-path}", produces = APPLICATION_JSON_VALUE)
public interface HotelResource {
	/**
	 * Get All Hotels
	 * 
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "get All hotels", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service", response = ApiResponseData.class) })
	@GetMapping
	ResponseEntity<ApiResponseData<List<Hotel>>> getHotels();

	/**
	 * add Hotel
	 * 
	 * @param hotel
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "add new hotel", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully added hotel", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Hotel deatils are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service", response = ApiResponseData.class) })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData<String>> addHotel(@RequestBody Hotel hotel);

	/**
	 * add Hotel
	 * 
	 * @param hotel
	 * @param hotelId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "update existing hotel", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully updated hotel", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Hotel deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service", response = ApiResponseData.class) })
	@PutMapping(value = "/{hotelId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData<String>> updateHotel(@RequestBody Hotel hotel, @PathVariable("hotelId") long hotelId);

	/**
	 * get hotel by id
	 * 
	 * @param hotelId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "get hotel details by id", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved hotel", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Hotel deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service", response = ApiResponseData.class) })
	@GetMapping("/{hotelId}")
	ResponseEntity<ApiResponseData<Hotel>> getHotelById(@PathVariable("hotelId") long hotelId);

	/**
	 * add room
	 * 
	 * @param room
	 * @param hotelId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "Add room for a hotel", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully added room for a hotel", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Hotel room deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service", response = ApiResponseData.class) })
	@PostMapping(value = "/{hotelId}/room", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData<String>> addRoom(@RequestBody Room room, @PathVariable("hotelId") long hotelId);

	/**
	 * delete room
	 * 
	 * @param hotelId
	 * @param roomId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CRS')")
	@DeleteMapping(value = "/{hotelId}/room/{roomId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully deleted room for a hotel", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Hotel room deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service") })
	@ApiOperation(value = "delete existing room for a hotel", response = String.class)
	ResponseEntity<ApiResponseData<String>> deleteRoom(@PathVariable("hotelId") long hotelId,
			@PathVariable("roomId") long roomId);

	/**
	 * update toom
	 * 
	 * @param room
	 * @param hotelId
	 * @param roomId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "update existing room for a hotel", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully updated room for a hotel", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Hotel room deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service", response = ApiResponseData.class) })
	@PutMapping(value = "/{hotelId}/room/{roomId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData<String>> updateRoom(@RequestBody Room room, @PathVariable("hotelId") long hotelId,
			@PathVariable("roomId") long roomId);
	
	/**
	 * get room by ID
	 * 
	 * @param hotelId
	 * @param roomId
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "Get room By ID", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved room", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "The Hotel room deatils you have provided are not correct", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service", response = ApiResponseData.class) })
	@GetMapping(value = "/{hotelId}/room/{roomId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApiResponseData<Hotel>> getRoomById(@PathVariable("hotelId") long hotelId,
			@PathVariable("roomId") long roomId);

	/**
	 * Get available Hotel and rooms
	 * 
	 * @param startDate
	 * @param endDate
	 * @return ResponseEntity
	 */
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_CRS')")
	@ApiOperation(value = "get available hotel room between two dates", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved hptel rooms", response = ApiResponseData.class),
			@ApiResponse(code = 401, message = "You are not authorized to access the resource", response = ApiResponseData.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ApiResponseData.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ApiResponseData.class),
			@ApiResponse(code = 400, message = "Date are not correct format. it supports dd-mm-yyyy- format", response = ApiResponseData.class),
			@ApiResponse(code = 500, message = "Internal server issue from hotel service", response = ApiResponseData.class) })
	@GetMapping(value = "/{startDate}/{endDate}")
	ResponseEntity<ApiResponseData<List<Hotel>>> getAvailableRoomHotel(@PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate);
}
