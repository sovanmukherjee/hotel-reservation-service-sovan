package com.hotel.service;

import java.util.List;

import com.hotel.model.Hotel;
import com.hotel.model.Room;
import com.hotel.proxy.model.Reservation;

public interface HotelService {
	List<Hotel> getHotels();

	String addHotel(final Hotel hotel);

	String updateHotel(final Hotel hotel, final long hotelId);

	Hotel getHotelById(final long hotelId);

	String addRoom(final Room room,final long hotelId);

	String deleteRoom(final long hotelId, final long roomId);

	String updateRoom(final Room room, final long hotelId, final long roomId);

	List<Hotel> getAvailableRoomHotel(final String startDate, final String endDate);

	Hotel getRoomById(long hotelId, long roomId);

	boolean updateRoomStatus(Reservation reservation);

}
