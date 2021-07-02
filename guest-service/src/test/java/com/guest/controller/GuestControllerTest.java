package com.guest.controller;

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

import com.guest.config.MessageConfigValues;
import com.guest.model.Guest;
import com.guest.service.GuestService;


@WebMvcTest(GuestController.class)
class GuestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private GuestService guestService;
	@MockBean
	private Tracer tracer;
	@MockBean
	private MessageConfigValues errorConfigValues;
	
	@Test
	void getGuestsTest() throws Exception {
		List<Guest> list = new ArrayList<>();
		Guest guest = new Guest();
		guest.setFirstName("Amit");
		guest.setLastName("last name");
		guest.setDob(LocalDate.parse("2019-03-29"));
		guest.setEmail("test@gmail.com");
		guest.setMobile("9999999999");
		guest.setAddress("address");
		list.add(guest);
		
		Mockito.when(guestService.getGuests()).thenReturn(list);
		this.mockMvc.perform(get("/v1/guests"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Amit")))
		.andExpect(content().string(containsString("9999999999")));
	}
	
	@Test
	void addGuestTest() throws Exception {
		Mockito.when(guestService.addGuest(Mockito.any(Guest.class))).thenReturn("success");
		this.mockMvc.perform(post("/v1/guests")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"firstName\": \"Amit\",\"mobile\": \"9999\",\"email\": \"test@gmail.com\"}")
				)
		.andExpect(status().isCreated())
		.andExpect(content().string(containsString("success")));
	}
	
	@Test
	void updateGuestTest() throws Exception {
		Mockito.when(guestService.updateGuest(Mockito.any(Guest.class),Mockito.anyLong())).thenReturn("success");
		this.mockMvc.perform(put("/v1/guests/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"firstName\": \"Amit\",\"mobile\": \"9999\",\"email\": \"test@gmail.com\"}")
				)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("success")));
	}
	
	@Test
	void getGuestByIdTest() throws Exception {
		Guest guest = new Guest();
		guest.setFirstName("Amit");
		Mockito.when(guestService.getGuestById(Mockito.anyLong())).thenReturn(guest);
		this.mockMvc.perform(get("/v1/guests/1")
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Amit")));
	}
	
}
