package com.hotel.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.hotel.config.MessageConfigValues;
import com.hotel.model.Contact;
import com.hotel.model.Hotel;
import com.hotel.model.Room;
import com.hotel.service.HotelService;

@WebMvcTest(HotelController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class HotelControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private Tracer tracer;
	@MockBean
	private HotelService hotelService;
	@MockBean
	private MessageConfigValues errorConfigValues;

	@Test
	void getHotelsTest() throws Exception {
		List<Hotel> list = new ArrayList<>();
		Hotel hotel = new Hotel();
		hotel.setName("Hotel A");
		Contact contact = new Contact();
		contact.setEmail("test@gmail.com");
		hotel.setContact(contact);
		list.add(hotel);
		
		Mockito.when(hotelService.getHotels()).thenReturn(list);
		this.mockMvc.perform(get("/v1/hotels"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Hotel A")))
		.andExpect(content().string(containsString("test@gmail.com")));
	}
	
	@Test
	void addHotelTest() throws Exception {
		Mockito.when(hotelService.addHotel(Mockito.any(Hotel.class))).thenReturn("success");
		this.mockMvc.perform(post("/v1/hotels")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \"hotel A\"}")
				)
		.andExpect(status().isCreated())
		.andExpect(content().string(containsString("success")));
	}
	
	@Test
	void updateHotelTest() throws Exception {
		Mockito.when(hotelService.updateHotel(Mockito.any(Hotel.class),Mockito.anyLong())).thenReturn("success");
		this.mockMvc.perform(put("/v1/hotels/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \"hotel A\"}")
				)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("success")));
	}
	
	@Test
	void getHotelByIdTest() throws Exception {
		Hotel hotel = new Hotel();
		hotel.setName("HotelA");
		Mockito.when(hotelService.getHotelById(Mockito.anyLong())).thenReturn(hotel);
		this.mockMvc.perform(get("/v1/hotels/1")
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("HotelA")));
	}
	
	@Test
	void addRoomTest() throws Exception {
		Mockito.when(hotelService.addRoom(Mockito.any(Room.class),Mockito.anyLong())).thenReturn("success");
		this.mockMvc.perform(post("/v1/hotels/1/room")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"roomFloorNo\": 6}")
				)
		.andExpect(status().isCreated())
		.andExpect(content().string(containsString("success")));
	}
	
	@Test
	void deleteRoomTest() throws Exception {
		Mockito.when(hotelService.deleteRoom(Mockito.anyLong(),Mockito.anyLong())).thenReturn("success");
		this.mockMvc.perform(delete("/v1/hotels/1/room/1")
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("success")));
	}
	
	@Test
	void updateRoomTest() throws Exception {
		Mockito.when(hotelService.updateRoom(Mockito.any(Room.class),Mockito.anyLong(),Mockito.anyLong())).thenReturn("success");
		this.mockMvc.perform(put("/v1/hotels/1/room/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"roomFloorNo\": 6}")
				)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("success")));
	}
	
	@Test
	void getAvailableRoomHotelTest() throws Exception {
		List<Hotel> list = new ArrayList<>();
		Hotel hotel = new Hotel();
		hotel.setName("HotelA");
		list.add(hotel);
		Mockito.when(hotelService.getAvailableRoomHotel(Mockito.any(String.class),Mockito.anyString())).thenReturn(list);
		this.mockMvc.perform(get("/v1/hotels/10-10-2021/16-10-2021")
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("HotelA")));
	}
}
