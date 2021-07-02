package com.hotel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.springframework.web.client.HttpServerErrorException.ServiceUnavailable;
import org.springframework.web.client.RestTemplate;

import com.hotel.constant.RoomStatus;
import com.hotel.model.Contact;
import com.hotel.model.Hotel;
import com.hotel.model.Room;
import com.hotel.model.RoomCategory;
import com.hotel.model.response.ApiResponseData;
import com.hotel.proxy.ReservationClient;
import com.hotel.proxy.model.Reservation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class IntHotelControllerTest {
	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost";

	private static RestTemplate restTemplate = null;
	@MockBean
	private ReservationClient reservationClient;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setUp() {
		baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/v1/hotels");
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(2,7,2000,'UNPUBLISHED',2,1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test

	 void getHotelsTest() {
		ApiResponseData response = restTemplate.getForObject(baseUrl, ApiResponseData.class);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatus());
		List<Hotel> hotelList = (List<Hotel>) response.getData();
		assertNotNull(hotelList);
		assertEquals(1, hotelList.size());
	}

	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void addHotelTest() {
		Hotel hotel = new Hotel();
		hotel.setName("Hotel A");
		hotel.setDescription("test");
		hotel.setAddress("Pune");

		Contact contact = new Contact();
		contact.setEmail("test@gmail.com");
		contact.setMobile1("9999999999");
		contact.setMobile2("888888888");
		hotel.setContact(contact);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Hotel> postRequest = new HttpEntity<>(hotel, headers);

		ResponseEntity<ApiResponseData> response = restTemplate.postForEntity(baseUrl, postRequest, ApiResponseData.class);
		assertNotNull(response);
		assertEquals(201, response.getStatusCode().value());
		ApiResponseData body = (ApiResponseData)response.getBody();
		assertNotNull(body);
		assertEquals("Hotel Hotel A added successfully", body.getData());
	}


	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void updateHotelTest() {
		Hotel hotel = new Hotel();
		hotel.setName("Hotel A");
		hotel.setDescription("test update");
		hotel.setAddress("Delhi");

		Contact contact = new Contact();
		contact.setEmail("test@gmail.com");
		contact.setMobile1("9999999111");
		contact.setMobile2("888888111");
		hotel.setContact(contact);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Hotel> putRequest = new HttpEntity<>(hotel, headers);
		ResponseEntity<ApiResponseData> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, putRequest,
				ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		ApiResponseData body = response.getBody();
		assertNotNull(body);
		assertEquals("Hotel Hotel A updated successfully", body.getData());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void getHotelByIdTest() {
		ResponseEntity<ApiResponseData> response = restTemplate.getForEntity(baseUrl + "/1", ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		assertNotNull(response.getBody());
	}

	@Sql(statements = {
			"INSERT INTO hotel (id, name,address,description) VALUES (1, 'Hotel A','Pune','')", }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void getHotelByIdWIthEmptyContactTest() {
		ResponseEntity<ApiResponseData> response = restTemplate.getForEntity(baseUrl + "/1", ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		assertNotNull(response.getBody());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void addRoomTest() {
		Room room = new Room();
		room.setPrice(6000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.UNPUBLISHED.toString());

		RoomCategory category = new RoomCategory();
		category.setId(2);
		category.setType("Double Queen");
		room.setCategory(category);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Room> postRequest = new HttpEntity<>(room, headers);

		ResponseEntity<ApiResponseData> response = restTemplate.postForEntity(baseUrl + "/1/room", postRequest, ApiResponseData.class);
		assertNotNull(response);
		assertEquals(201, response.getStatusCodeValue());
		ApiResponseData resp = response.getBody();
		assertNotNull(resp);
		assertEquals("one Double Queen category room added successfully for HotelA", resp.getData());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void addRoomWithEmptyCategoryTest() {
		Room room = new Room();
		room.setPrice(6000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.UNPUBLISHED.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Room> postRequest = new HttpEntity<>(room, headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.postForEntity(baseUrl + "/1/room", postRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void addRoomWithWrongCategoryTest() {
		Room room = new Room();
		room.setPrice(6000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.UNPUBLISHED.toString());

		RoomCategory category = new RoomCategory();
		category.setId(6);
		category.setType("Double Queen");
		room.setCategory(category);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Room> postRequest = new HttpEntity<>(room, headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.postForEntity(baseUrl + "/1/room", postRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void deleteRoomTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity deleteRequest = new HttpEntity<>(headers);
		ResponseEntity<ApiResponseData> response = restTemplate.exchange(baseUrl + "/1/room/1", HttpMethod.DELETE, deleteRequest,
				ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		ApiResponseData resp = response.getBody();
		assertNotNull(resp);
		assertEquals("one King Bed category room deleted from HotelA", resp.getData());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void updateRoomTest() {
		Room room = new Room();
		room.setPrice(4000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.AVAILABLE.toString());

		RoomCategory category = new RoomCategory();
		category.setId(2);
		category.setType("Double Queen");
		room.setCategory(category);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Room> putRequest = new HttpEntity<>(room, headers);

		ResponseEntity<ApiResponseData> response = restTemplate.exchange(baseUrl + "/1/room/1", HttpMethod.PUT, putRequest,
				ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		ApiResponseData resp = response.getBody();
		assertNotNull(resp);
		assertEquals("one Double Queen category room updated successfully for HotelA", resp.getData());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO hotel (id, name,address,description) VALUES (2, 'HotelB','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (2, '9999999999','8888888888','test@gmail.com',2)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,4,2000,'AVAILAVLE',1,1)" ,
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(2,6,6000,'BOOKED',1,1)" ,
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(3,7,2000,'AVAILABLE',1,2)"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void getAvailableRoomHotelTest() {
		List<Reservation> reservations = new ArrayList<>();
		Reservation rev = new Reservation();
		rev.setHotelId(1);
		List<Long> roomIds = new ArrayList<>();
		roomIds.add(2L);
		rev.setRoomIds(roomIds);
		reservations.add(rev);
		ApiResponseData responseData = new ApiResponseData(reservations,HttpStatus.OK,null);
		Mockito.when(reservationClient.getReservedRooms(Mockito.anyString(),Mockito.anyString())).thenReturn(responseData);
		ResponseEntity<ApiResponseData> response = restTemplate.getForEntity(baseUrl + "/10-10-2021/15-10-2021", ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		ApiResponseData apiResponseData = response.getBody();
		assertNotNull(apiResponseData);
		List<Hotel> hotels = (List<Hotel>) apiResponseData.getData();
		assertNotNull(hotels);
		assertEquals(2,hotels.size());
	}
	
	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO hotel (id, name,address,description) VALUES (2, 'HotelB','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (2, '9999999999','8888888888','test@gmail.com',2)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,4,2000,'AVAILAVLE',1,1)" ,
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(2,6,6000,'BOOKED',1,1)" ,
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(3,7,2000,'AVAILABLE',1,2)"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void getAvailableRoomHotelWithServiceUnavailableTest() {
		List<Reservation> reservations = new ArrayList<>();
		Reservation rev = new Reservation();
		rev.setHotelId(1);
		List<Long> roomIds = new ArrayList<>();
		roomIds.add(2L);
		rev.setRoomIds(roomIds);
		reservations.add(rev);
		Mockito.when(reservationClient.getReservedRooms(Mockito.anyString(),Mockito.anyString())).thenThrow(ServiceUnavailable.class);
		ResponseEntity<ApiResponseData> response = restTemplate.getForEntity(baseUrl + "/10-10-2021/15-10-2021", ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		ApiResponseData apiResponseData = response.getBody();
		assertNotNull(apiResponseData);
		List<Hotel> hotels = (List<Hotel>) apiResponseData.getData();
		assertEquals(0,hotels.size());
	}

	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void getHotelsEmpyHotelTest() {
		ApiResponseData response = restTemplate.getForObject(baseUrl, ApiResponseData.class);
		assertNotNull(response);
		List<Hotel> hotelList = (List<Hotel>) response.getData();
		assertEquals(0,hotelList.size());
	}

	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void addHotelWithEmptyDataTest() {
		Hotel hotel = new Hotel();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Hotel> postRequest = new HttpEntity<>(hotel, headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.postForEntity(baseUrl, postRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void updateHotelWrongHotelIdTest() {
		Hotel hotel = new Hotel();
		hotel.setName("Hotel A");
		hotel.setDescription("test update");
		hotel.setAddress("Delhi");

		Contact contact = new Contact();
		contact.setEmail("test@gmail.com");
		contact.setMobile1("9999999111");
		contact.setMobile2("888888111");
		hotel.setContact(contact);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Hotel> putRequest = new HttpEntity<>(hotel, headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.exchange(baseUrl + "/6", HttpMethod.PUT, putRequest, ApiResponseData.class);
		});

		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void getHotelByIdWrongHotelIdTest() {
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.getForEntity(baseUrl + "/6", ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void addRoomWithWrongHotelTest() {
		Room room = new Room();
		room.setPrice(6000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.UNPUBLISHED.toString());

		RoomCategory category = new RoomCategory();
		category.setId(2);
		category.setType("Double Queen");
		room.setCategory(category);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Room> postRequest = new HttpEntity<>(room, headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.postForEntity(baseUrl + "/6/room", postRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void deleteRoomWithWrongHotelTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity deleteRequest = new HttpEntity<>(headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.exchange(baseUrl + "/6/room/1", HttpMethod.DELETE, deleteRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void deleteRoomWithWrongRoomTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity deleteRequest = new HttpEntity<>(headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.exchange(baseUrl + "/1/room/6", HttpMethod.DELETE, deleteRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());

	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void updateRoomWithWrongHotelTest() {
		Room room = new Room();
		room.setPrice(4000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.AVAILABLE.toString());

		RoomCategory category = new RoomCategory();
		category.setId(2);
		category.setType("Double Queen");
		room.setCategory(category);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Room> putRequest = new HttpEntity<>(room, headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.exchange(baseUrl + "/6/room/1", HttpMethod.PUT, putRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void updateRoomWithWrongRoomTest() {
		Room room = new Room();
		room.setPrice(4000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.AVAILABLE.toString());

		RoomCategory category = new RoomCategory();
		category.setId(2);
		category.setType("Double Queen");
		room.setCategory(category);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Room> putRequest = new HttpEntity<>(room, headers);
		HttpClientErrorException assertions = Assertions.assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.exchange(baseUrl + "/1/room/6", HttpMethod.PUT, putRequest, ApiResponseData.class);
		});
		assertEquals(400, assertions.getStatusCode().value());
	}
	
	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void getRoomByIdTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity deleteRequest = new HttpEntity<>(headers);
		ResponseEntity<ApiResponseData> response = restTemplate.exchange(baseUrl + "/1/room/1", HttpMethod.GET, deleteRequest,
				ApiResponseData.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
	}

}
