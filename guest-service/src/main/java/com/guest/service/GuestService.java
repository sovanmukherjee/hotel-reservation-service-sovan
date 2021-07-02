package com.guest.service;

import java.util.List;

import com.guest.model.Guest;

public interface GuestService {

	public List<Guest> getGuests();
	
	public Guest getGuestById(long guestId);

	public String addGuest(Guest guest);

	public String updateGuest(Guest guest, long guestId);

	

}
