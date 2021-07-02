package com.reservation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.reservation.ReservationServiceApplication;
import com.reservation.config.MessageConfigValues;
import com.reservation.controller.ReservationController;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ReservationServiceApplicationTests {
	@Autowired
	private MessageConfigValues values;
	@Autowired
	private ReservationController reservationController;

	@BeforeEach
	void setup() {
		RestAssuredMockMvc.standaloneSetup(reservationController);
	}

	@Test
	void contextLoads() {
		ReservationServiceApplication.main(new String[] {});
		assertNotNull(values);
		assertEquals("RC_GENERIC_ERROR", values.getErrorConfig().getGenericErrorCode());
	}

}
