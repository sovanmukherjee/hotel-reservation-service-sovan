package com.hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.model.Hotel;
import com.hotel.model.Room;
import com.hotel.model.response.ApiResponseData;
import com.hotel.service.HotelService;
/**
 * This controller is used for all Hotel related operations
 * 
 * @author Sovan_Mukherjee
 *
 */
@RestController
public class HotelController implements HotelResource{
	@Autowired
	private HotelService hotelService;
	
	private <T> ApiResponseData<T> buildApiResponse(T data, HttpStatus status) {
        return new ApiResponseData<>(data, status, null);
    }

	/**
	 * get all hotels
	 */
	@Override
	public ResponseEntity<ApiResponseData<List<Hotel>>> getHotels() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(buildApiResponse(hotelService.getHotels(),HttpStatus.OK));
	}

	/**
	 * add new hotel
	 */
	@Override
	public ResponseEntity<ApiResponseData<String>> addHotel(Hotel hotel) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(buildApiResponse(hotelService.addHotel(hotel),HttpStatus.CREATED));
	}

	/**
	 * update existing hotel
	 */
	@Override
	public ResponseEntity<ApiResponseData<String>> updateHotel(Hotel hotel, long hotelId) {
		return ResponseEntity.status(HttpStatus.OK).body(buildApiResponse(hotelService.updateHotel(hotel,hotelId),HttpStatus.OK));
	}

	/**
	 * get existing hotel by hotel id
	 */
	@Override
	public ResponseEntity<ApiResponseData<Hotel>> getHotelById(long hotelId) {
		return ResponseEntity.status(HttpStatus.OK).body(buildApiResponse(hotelService.getHotelById(hotelId),HttpStatus.OK));
	}

	/**
	 * add new room for a hotel
	 */
	@Override
	public ResponseEntity<ApiResponseData<String>> addRoom(Room room, long hotelId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(buildApiResponse(hotelService.addRoom(room,hotelId),HttpStatus.CREATED));

	}

	@Override
	public ResponseEntity<ApiResponseData<String>> deleteRoom(long hotelId, long roomId) {
		return ResponseEntity.status(HttpStatus.OK).body(buildApiResponse(hotelService.deleteRoom(hotelId,roomId),HttpStatus.OK));

	}

	/**
	 * update existing room for a hotel 
	 */
	@Override
	public ResponseEntity<ApiResponseData<String>> updateRoom(Room room, long hotelId, long roomId) {
		return ResponseEntity.status(HttpStatus.OK).body(buildApiResponse(hotelService.updateRoom(room,hotelId,roomId),HttpStatus.OK));

	}

	/**
	 * get all available hotel room between start and end date
	 */
	@Override
	public ResponseEntity<ApiResponseData<List<Hotel>>> getAvailableRoomHotel(String startDate, String endDate) {
		return ResponseEntity.status(HttpStatus.OK).body(buildApiResponse(hotelService.getAvailableRoomHotel(startDate,endDate),HttpStatus.OK));
	}

	/**
	 * get hotel room by room id
	 */
	@Override
	public ResponseEntity<ApiResponseData<Hotel>> getRoomById(long hotelId, long roomId) {
		return ResponseEntity.status(HttpStatus.OK).body(buildApiResponse(hotelService.getRoomById(hotelId,roomId),HttpStatus.OK));

	}

	
	
	

}
