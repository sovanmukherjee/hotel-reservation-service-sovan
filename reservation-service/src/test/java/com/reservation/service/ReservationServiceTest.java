package com.reservation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.reservation.config.MessageConfigValues;
import com.reservation.mapper.ReservationMapper;
import com.reservation.repository.CardStoreRepository;
import com.reservation.validator.ReservationValidator;

@SpringBootTest
class ReservationServiceTest {

	@Autowired
	private ReservationService reservationService;

	@MockBean
	private CardStoreRepository cardStoreRepositoryMock;

	@MockBean
	private ReservationMapper reservationMapperMock;

	@MockBean
	private ReservationValidator reservationValidatorMock;

	@MockBean
	private MessageConfigValues messageConfigValuesMock;

	private MessageConfigValues.MessageConfig buildMessageConfig() {
		MessageConfigValues.MessageConfig config = new MessageConfigValues.MessageConfig();
		config.setReservationSuccessMessage("Successfully room allocated");
		return config;
	}
	
	@Test
	void getReservations() {
		
	}
	
	

}
