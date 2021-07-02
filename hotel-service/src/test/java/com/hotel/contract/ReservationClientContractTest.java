package com.hotel.contract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import com.hotel.model.response.ApiResponseData;
import com.hotel.proxy.ReservationClient;
import com.hotel.proxy.model.Reservation;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"reservation.service.name=reservation-service" })
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(ids = "com.reservation:reservation-service:+:stubs:6565", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ReservationClientContractTest {

	@Autowired
	private ReservationClient reservationClient;


		 @Test
		 void getReservedRoomsClientContractTest() {
			 ApiResponseData<List<Reservation>> response = reservationClient.getReservedRooms("2021-06-21", "2021-06-22");
				assertNotNull(response);
				assertEquals(200, response.getStatus().value());
				List<Reservation> reservationList = response.getData();
				assertNotNull(reservationList);
				assertEquals(1, reservationList.get(0).getHotelId());
				assertNotNull(reservationList.get(0).getRoomIds());
				assertEquals(3, reservationList.get(0).getRoomIds().size());
				
		 }
		 
		 
}
