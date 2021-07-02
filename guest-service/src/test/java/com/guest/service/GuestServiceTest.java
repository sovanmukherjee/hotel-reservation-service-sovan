package com.guest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.guest.config.MessageConfigValues;
import com.guest.entity.GuestEntity;
import com.guest.mapper.GuestMapper;
import com.guest.model.Guest;
import com.guest.repository.GuestRepository;
import com.guest.validator.GuestValidator;

@SpringBootTest
class GuestServiceTest {

	@Autowired
	private GuestService guestService;

	@MockBean
	private GuestRepository guestRepositoryMock;

	@MockBean
	private GuestMapper guestMapperMock;

	@MockBean
	private GuestValidator guestValidatorMock;

	@MockBean
	private MessageConfigValues messageConfigValuesMock;

	private MessageConfigValues.MessageConfig buildMessageConfig() {
		MessageConfigValues.MessageConfig config = new MessageConfigValues.MessageConfig();
		config.setAddUpdateGuestMessage("Guest %s %s successfully");
		return config;
	}
	
	private GuestEntity getGuestEntity() {
		return new GuestEntity(1,"Amit","Jain",LocalDate.parse("1992-10-19"),"address","9999999999","test@gmail.com");
	}
	
	private Guest getGuest() {
		return new Guest(1,"Amit","Jain",LocalDate.parse("1992-10-19"),"address","9999999999","test@gmail.com");
	}
	
	@Test
	void getGuestsTest() {
		List<GuestEntity> list = new ArrayList<>();
		list.add(getGuestEntity());
		Mockito.when(guestRepositoryMock.findAll()).thenReturn(list);
		List<Guest> guests = new ArrayList<>();
		guests.add(getGuest());
		Mockito.when(guestMapperMock.guestEntitiesToGuests(Mockito.any())).thenReturn(guests);
		guests = guestService.getGuests();
		assertNotNull(guests);
		assertEquals(1, guests.size());
		assertEquals("Amit", guests.get(0).getFirstName());
	}
	
	@Test
	void getGuestslByIdTest() {
		Guest guestdb = getGuest();
		Mockito.when(guestMapperMock.guestEntityToGuest(Mockito.any(GuestEntity.class))).thenReturn(guestdb);
		Optional<GuestEntity> entityOptional = Optional.of(getGuestEntity());
		Mockito.when(guestValidatorMock.getGuestIfExist(Mockito.any(Long.class))).thenReturn(entityOptional.get());
		guestdb = guestService.getGuestById(1);
		assertNotNull(guestdb);
		assertEquals("Amit", guestdb.getFirstName());
	}
	
	@Test
	void addGuestTest() {
		GuestEntity entity = getGuestEntity();
		Guest guest = getGuest();
		Mockito.when(guestMapperMock.guestToGuestEntity(Mockito.any(Guest.class))).thenReturn(entity);
		Mockito.when(guestRepositoryMock.save(Mockito.any(GuestEntity.class))).thenReturn(entity);
		Mockito.when(messageConfigValuesMock.getMessageConfig()).thenReturn(buildMessageConfig());
		String message = guestService.addGuest(guest);
		assertEquals("Guest Amit added successfully", message);
	}
	
	@Test
	void updateGuestTest() {
		GuestEntity entity = getGuestEntity();
		Mockito.when(guestValidatorMock.getGuestIfExist(Mockito.any(Long.class))).thenReturn(entity);
		Mockito.when(guestMapperMock.guestToGuestEntity(Mockito.any(Guest.class))).thenReturn(entity);
		Mockito.when(guestRepositoryMock.save(Mockito.any(GuestEntity.class))).thenReturn(entity);
		Mockito.when(messageConfigValuesMock.getMessageConfig()).thenReturn(buildMessageConfig());
		String message = guestService.updateGuest(getGuest(),1);
		assertEquals("Guest Amit updated successfully", message);
	}
	
	

}
