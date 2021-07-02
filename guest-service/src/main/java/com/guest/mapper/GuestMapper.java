package com.guest.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.guest.entity.GuestEntity;
import com.guest.model.Guest;

@Mapper(componentModel = "spring")
public interface GuestMapper {

	public List<Guest> guestEntitiesToGuests(List<GuestEntity> entities);

	public GuestEntity guestToGuestEntity(Guest guest);

	public Guest guestEntityToGuest(GuestEntity entity);

	
}
