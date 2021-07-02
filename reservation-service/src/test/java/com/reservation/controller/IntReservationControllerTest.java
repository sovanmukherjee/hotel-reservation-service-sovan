package com.reservation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import com.reservation.model.CardDetails;
import com.reservation.model.Payment;
import com.reservation.model.PaymentMode;
import com.reservation.model.ReservationRequest;
import com.reservation.model.ReservationResponse;
import com.reservation.model.response.ApiResponseData;
import com.reservation.proxy.GuestClient;
import com.reservation.proxy.HotelClient;
import com.reservation.proxy.model.guest.Guest;
import com.reservation.proxy.model.hotel.Hotel;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntReservationControllerTest {

	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost";

	private static RestTemplate restTemplate = null;

	@MockBean
	private GuestClient guestClient;
	@MockBean
	private HotelClient hotelClient;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setUp() {
		baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/v1/reservations");
	}

	@Sql(statements = {
			"INSERT INTO reservation (id, guestId,hotelId,roomId,checkInDate,checkOutDate,totalAmount,totalDepositedAmount,comment,status) VALUES (1, 1,1,1,'2021-06-21','2021-06-22',2000,1000,'comments','BOOKED')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM reservation" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test

	void getReservationsTest() {
		ApiResponseData<Guest> guestRes = new ApiResponseData<>(new Guest(), HttpStatus.OK, null);
		ApiResponseData<Hotel> hotelRes = new ApiResponseData<>(new Hotel(), HttpStatus.OK, null);
		Mockito.when(guestClient.getGuestById(Mockito.anyMap(), Mockito.anyLong())).thenReturn(guestRes);
		Mockito.when(hotelClient.getHotelRoomById(Mockito.anyMap(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(hotelRes);
		ApiResponseData response = restTemplate.getForObject(baseUrl, ApiResponseData.class);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatus());
		List<ReservationResponse> reservationList = (List<ReservationResponse>) response.getData();
		assertNotNull(reservationList);
		assertEquals(1, reservationList.size());
	}

	@Sql(statements = { "DELETE FROM reservation" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	void allocateRoomTest() {
		ApiResponseData<Guest> guestRes = new ApiResponseData<>(new Guest(), HttpStatus.OK, null);
		ApiResponseData<Hotel> hotelRes = new ApiResponseData<>(new Hotel(), HttpStatus.OK, null);
		Mockito.when(guestClient.getGuestById(Mockito.anyMap(), Mockito.anyLong())).thenReturn(guestRes);
		Mockito.when(hotelClient.getHotelRoomById(Mockito.anyMap(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(hotelRes);
		ReservationRequest reservationRequest = new ReservationRequest(1, 1, 1, 1, LocalDate.parse("2021-06-21"),
				LocalDate.parse("2021-06-22"), 2000, 1000, "comments", "BOOKED");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ReservationRequest> postRequest = new HttpEntity<>(reservationRequest, headers);
		ResponseEntity<ApiResponseData> response = restTemplate.postForEntity(baseUrl, postRequest,
				ApiResponseData.class);

		assertNotNull(response);
		assertEquals(201, response.getStatusCode().value());
		assertEquals("Successfully room allocated. Reservation Id: 1", response.getBody().getData());
	}
	
	@Sql(statements = {
			"INSERT INTO reservation (id, guestId,hotelId,roomId,checkInDate,checkOutDate,totalAmount,totalDepositedAmount,comment,status) VALUES (1, 1,1,1,'2021-06-21','2021-06-22',2000,1000,'comments','BOOKED')",
			"INSERT INTO payment_mode (id, type) VALUES(1,'CreditCard')",
			"INSERT INTO card_store(id,creditCardNo,creditCardName,creditCardExpiry,guestId) VALUES(1,'9999999999999999','Test Name', '2023-10',1)"
			 }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM payment","DELETE FROM reservation","DELETE from card_store","DELETE from payment_mode" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	void paymentTest() {
		ApiResponseData<Guest> guestRes = new ApiResponseData<>(new Guest(), HttpStatus.OK, null);
		ApiResponseData<Hotel> hotelRes = new ApiResponseData<>(new Hotel(), HttpStatus.OK, null);
		Mockito.when(guestClient.getGuestById(Mockito.anyMap(), Mockito.anyLong())).thenReturn(guestRes);
		Mockito.when(hotelClient.getHotelRoomById(Mockito.anyMap(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(hotelRes);
		
		Payment payment = new Payment(1,"Suman K",2000, new PaymentMode(1, "CreditCard"), 1L);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Payment> postRequest = new HttpEntity<>(payment, headers);

		ResponseEntity<ApiResponseData> response = restTemplate.postForEntity(baseUrl+"/1/payment",postRequest, ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Payment Successful for reservation id 1", response.getBody().getData());
	}
	
	@Test
	void addCardDetailsTest() {
		ApiResponseData<Guest> guestRes = new ApiResponseData<>(new Guest(), HttpStatus.OK, null);
		Mockito.when(guestClient.getGuestById(Mockito.anyMap(), Mockito.anyLong())).thenReturn(guestRes);
		
		CardDetails card = new CardDetails(1,"9999999999999999","Test Name", "2023-10",1);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<CardDetails> postRequest = new HttpEntity<>(card, headers);

		ResponseEntity<ApiResponseData> response = restTemplate.postForEntity(baseUrl+"/card",postRequest, ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Card details stored successfully for guest id 1", response.getBody().getData());
	}

	
}
