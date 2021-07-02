package com.reservation.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.reservation.config.MessageConfigValues;
import com.reservation.proxy.model.guest.Guest;
import com.reservation.service.ReservationService;


@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ReservationService guestService;
	@MockBean
	private Tracer tracer;
	@MockBean
	private MessageConfigValues errorConfigValues;
	
	@Test
	void getGuestsTest() throws Exception {
		
	}
	
	
}
