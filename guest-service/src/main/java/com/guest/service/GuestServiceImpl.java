package com.guest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guest.config.MessageConfigValues;
import com.guest.mapper.GuestMapper;
import com.guest.model.Guest;
import com.guest.repository.GuestRepository;
import com.guest.validator.GuestValidator;

import lombok.extern.slf4j.Slf4j;

/**
 * this GuestService implementation class is used to implementation business
 * logic of Guest service APIs
 * 
 * @author Sovan_Mukherjee
 *
 */
@Slf4j
@Service
public class GuestServiceImpl implements GuestService {

	@Autowired
	private GuestRepository guestRepository;
	@Autowired
	private GuestMapper guestMapper;
	@Autowired
	private MessageConfigValues messageConfigValues;
	@Autowired
	private GuestValidator guestValidator;

	@Override
	public List<Guest> getGuests() {
		return guestMapper.guestEntitiesToGuests(guestRepository.findAll());
	}

	@Override
	public Guest getGuestById(long guestId) {
		var entity = guestValidator.getGuestIfExist(guestId);
		return guestMapper.guestEntityToGuest(entity);
	}

	@Override
	public String addGuest(Guest guest) {
		var guestEntity = guestMapper.guestToGuestEntity(guest);
		guestEntity = guestRepository.save(guestEntity);
		return String.format(messageConfigValues.getMessageConfig().getAddUpdateGuestMessage(),
				guestEntity.getFirstName(), "added");
	}

	@Override
	public String updateGuest(Guest guest, long guestId) {
		guestValidator.getGuestIfExist(guestId);
		var guestEntityUpdate = guestMapper.guestToGuestEntity(guest);
		guestEntityUpdate.setId(guestId);
		guestEntityUpdate = guestRepository.save(guestEntityUpdate);
		return String.format(messageConfigValues.getMessageConfig().getAddUpdateGuestMessage(),
				guestEntityUpdate.getFirstName(), "updated");
	}

}
