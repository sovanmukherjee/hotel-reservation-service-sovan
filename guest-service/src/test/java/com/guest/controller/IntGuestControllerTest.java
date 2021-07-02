package com.guest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.guest.model.Guest;
import com.guest.model.response.ApiResponseData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IntGuestControllerTest {

	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost";

	private static RestTemplate restTemplate = null;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setUp() {
		baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/v1/guests");
	}

	@Sql(statements = {
			"INSERT INTO guest (id, firstName,lastName,dob,address,mobile,email) VALUES (1, 'Sumit','Jain','1992-10-16','address','9999999999','test@Gmail.com')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM guest" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test

	void getGuestsTest() {
		ApiResponseData response = restTemplate.getForObject(baseUrl, ApiResponseData.class);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatus());
		List<Guest> guestList = (List<Guest>) response.getData();
		assertNotNull(guestList);
		assertEquals(1, guestList.size());
	}

	@Sql(statements = {
			"INSERT INTO guest (id, firstName,lastName,dob,address,mobile,email) VALUES (1, 'Sumit','Jain','1992-10-16','address','9999999999','test@Gmail.com')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM guest" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	void getGuestByIdTest() {
		ResponseEntity<ApiResponseData> response = restTemplate.getForEntity(baseUrl + "/1", ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		assertNotNull(response.getBody());
	}

	@Sql(statements = {
			"INSERT INTO guest (id, firstName,lastName,dob,address,mobile,email) VALUES (1, 'Sumit','Jain','1992-10-16','address','9999999999','test@Gmail.com')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM guest" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	void getGuestByIdExceptionTest() {

		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.getForEntity(baseUrl + "/2", ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "DELETE FROM guest" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	void addGuest() {
		Guest guest = new Guest();
		guest.setFirstName("first name");
		guest.setLastName("last name");
		guest.setDob(LocalDate.parse("2019-03-29"));
		guest.setEmail("test@gmail.com");
		guest.setMobile("9999999999");
		guest.setAddress("address");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Guest> postRequest = new HttpEntity<>(guest, headers);

		ResponseEntity<ApiResponseData> response = restTemplate.postForEntity(baseUrl, postRequest,
				ApiResponseData.class);
		assertNotNull(response);
		assertEquals(201, response.getStatusCode().value());
		ApiResponseData body = (ApiResponseData) response.getBody();
		assertNotNull(body);
		assertEquals("Guest first name added successfully", body.getData());
	}

	@Sql(statements = {
			"INSERT INTO guest (id, firstName,lastName,dob,address,mobile,email) VALUES (1, 'Sumit','Jain','1992-10-16','address','9999999999','test@Gmail.com')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM guest" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	void updateGuestTest() {
		Guest guest = new Guest();
		guest.setFirstName("first name A");
		guest.setLastName("last name update");
		guest.setDob(LocalDate.parse("2019-03-29"));
		guest.setEmail("testupdate@gmail.com");
		guest.setMobile("9999999999");
		guest.setAddress("addressupdate");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Guest> putRequest = new HttpEntity<>(guest, headers);
		ResponseEntity<ApiResponseData> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, putRequest,
				ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		ApiResponseData body = response.getBody();
		assertNotNull(body);
		assertEquals("Guest first name A updated successfully", body.getData());
	}

	@Sql(statements = {
			"INSERT INTO guest (id, firstName,lastName,dob,address,mobile,email) VALUES (1, 'Sumit','Jain','1992-10-16','address','9999999999','test@Gmail.com')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM guest" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	void updateGuestValidationTest() {
		Guest guest = new Guest();
		guest.setFirstName("first name A");
		guest.setLastName("last name update");
		guest.setDob(LocalDate.parse("2019-03-29"));
		guest.setMobile("9999999999");
		guest.setAddress("addressupdate");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Guest> putRequest = new HttpEntity<>(guest, headers);
		
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, putRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

}
