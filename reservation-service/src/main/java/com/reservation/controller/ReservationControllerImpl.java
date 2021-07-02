package com.reservation.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.reservation.axon.service.ReservationCommandService;
import com.reservation.axon.service.ReservationQueryService;
import com.reservation.model.CardDetails;
import com.reservation.model.Payment;
import com.reservation.model.ReservationRequest;
import com.reservation.model.ReservationResponse;
import com.reservation.model.response.ApiResponseData;
import com.reservation.service.ReservationService;

import lombok.extern.slf4j.Slf4j;

/**
 * This controller is used for all Reservation related operations
 * 
 * @author Sovan_Mukherjee
 *
 */
@Slf4j
@RestController
public class ReservationControllerImpl implements ReservationController {
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private ReservationCommandService reservationCommandService;
	@Autowired
	private ReservationQueryService reservationQueryService;

	private <T> ApiResponseData<T> buildApiResponse(T data, HttpStatus status) {
		return new ApiResponseData<>(data, status, null);
	}

	/**
	 * get all Reservations
	 */
	@Override
	public ResponseEntity<ApiResponseData> getReservations(Map<String, String> headers) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(buildApiResponse(reservationService.getReservations(headers), HttpStatus.OK));
	}

	/**
	 * get reservation hotel and room ids by date
	 */
	@Override
	public ResponseEntity<ApiResponseData> getReservationHotelRoomsByDate(Map<String, String> headers,String startDate, String endDate) {
		log.info("start date:: "+startDate);
		log.info("end date:: "+endDate);
		for(String key : headers.keySet()) {
			log.info("Header:: "+key +":"+headers.get(key));
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(buildApiResponse(reservationService.getReservationHotelRoomsByDate(headers,LocalDate.parse(startDate), LocalDate.parse(endDate)), HttpStatus.OK));
	}

	/**
	 * allocate Room
	 */
	@Override
	public ResponseEntity<ApiResponseData> allocateRoom(Map<String, String> headers,ReservationRequest reservation) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(buildApiResponse(reservationService.allocateRoom(headers,reservation), HttpStatus.CREATED));
	}

	/**
	 * make payment
	 */
	@Override
	public ResponseEntity<ApiResponseData> payment(Map<String, String> headers,Payment payment, long reservationId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(buildApiResponse(reservationService.payment(headers,payment, reservationId), HttpStatus.OK));
	}

	/**
	 * add Card Details
	 */
	@Override
	public ResponseEntity<ApiResponseData> addCardDetails(Map<String, String> headers,CardDetails cardDetails) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(buildApiResponse(reservationService.addCardDetails(headers,cardDetails), HttpStatus.OK));
	}

	
	/**
	 * ****************************************************
	 * ****************************************************
	 * below APIs are using CQRS and EventSourcing using AXON
	 * ****************************************************
	 */
	
	/**
	 * allocate HotelRoom using AXON
	 */
	@Override
	public ResponseEntity<ApiResponseData> allocateRoomWithAxonFrmwk(Map<String, String> headers,
			ReservationRequest reservation) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(buildApiResponse(reservationCommandService.bookHotelRoom(headers,reservation), HttpStatus.CREATED));
	}
	
	/**
	 * make payment using Axon
	 */
	@Override
	public ResponseEntity<ApiResponseData> paymentWithAxonFrmwk(Map<String, String> headers, Payment payment,
			String reservationNo) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(buildApiResponse(reservationCommandService.payment(headers,payment, reservationNo), HttpStatus.OK));
	}

	/**
	 * get All reservation using Axon
	 */
	@Override
	public ResponseEntity<ApiResponseData> getReservationsWithAxonFrmwk(Map<String, String> headers) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(buildApiResponse(reservationQueryService.getReservations(headers), HttpStatus.OK));
	}

	/**
	 * get All events by reservation no using AXON
	 */
	@Override
	public ResponseEntity<ApiResponseData> getEventsByIdWithAxonFrmwk(Map<String, String> headers, String id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(buildApiResponse(reservationQueryService.getEventsById(id), HttpStatus.OK));

	}

	

}
