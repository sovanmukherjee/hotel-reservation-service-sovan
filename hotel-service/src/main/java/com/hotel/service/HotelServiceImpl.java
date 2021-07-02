package com.hotel.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.config.MessageConfigValues;
import com.hotel.entity.ContactEntity;
import com.hotel.entity.HotelEntity;
import com.hotel.entity.RoomEntity;
import com.hotel.exception.BusinessException;
import com.hotel.mapper.HotelMapper;
import com.hotel.model.Hotel;
import com.hotel.model.Room;
import com.hotel.proxy.ReservationClient;
import com.hotel.proxy.model.Reservation;
import com.hotel.repository.HotelRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.validator.HotelValidator;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

/**
 * this HotelService implementation class is used to implementation business
 * logic of Hotel service APIs
 * 
 * @author Sovan_Mukherjee
 *
 */
@Slf4j
@Service
public class HotelServiceImpl implements HotelService {

	@Autowired
	private HotelRepository hotelRepository;
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private HotelMapper hotelMapper;
	@Autowired
	private MessageConfigValues errorConfigValues;
	@Autowired
	private HotelValidator hotelValidator;
	@Autowired
	private ReservationClient reservationClient;

	/**
	 * get all hotels
	 */
	@Override
	public List<Hotel> getHotels() {
		List<HotelEntity> entities = hotelRepository.findAll();
		return hotelMapper.hotelEntitiesToHotels(entities);
	}

	/**
	 * add new Hotel
	 */
	@Override
	public String addHotel(final Hotel hotel) {
		if (null != hotel.getName()) {
			var hotelEntity = hotelMapper.hotelToHotelEntity(hotel);
			ContactEntity contactEntiry = hotelEntity.getContact();
			if (null != contactEntiry) {
				contactEntiry.setHotel(hotelEntity);
			}
			hotelEntity = hotelRepository.save(hotelEntity);
			return String.format(errorConfigValues.getMessageConfig().getAddUpdateHotelMessage(), hotelEntity.getName(),
					"added");
		} else {
			throw new BusinessException(errorConfigValues.getErrorConfig().getHotelInfoErrorCode(),
					errorConfigValues.getErrorConfig().getHotelInfoErrorMessage());
		}

	}

	/**
	 * validate if hotel exist and update hotel
	 */
	@Override
	public String updateHotel(final Hotel hotel, final long hotelId) {
		HotelEntity entity = hotelValidator.getHotelIfExist(hotelId);
		var hotelEntityUpdate = hotelMapper.hotelToHotelEntity(hotel);
		hotelEntityUpdate.setId(hotelId);
		if (null != hotelEntityUpdate.getContact()) {
			hotelEntityUpdate.getContact().setHotel(hotelEntityUpdate);
			hotelEntityUpdate.setContact(hotelEntityUpdate.getContact());
		}
		if (null != hotelEntityUpdate.getContact() && null != entity.getContact()) {
			hotelEntityUpdate.getContact().setId(entity.getContact().getId());
		}
		hotelEntityUpdate = hotelRepository.save(hotelEntityUpdate);
		return String.format(errorConfigValues.getMessageConfig().getAddUpdateHotelMessage(),
				hotelEntityUpdate.getName(), "updated");
	}

	/**
	 * get hotel by hotel id
	 */
	@Override
	public Hotel getHotelById(final long hotelId) {
		HotelEntity entity = hotelValidator.getHotelIfExist(hotelId);
		return hotelMapper.hotelEntityToHotel(entity);
	}

	/**
	 * add new room for a hotel
	 */
	@Override
	public String addRoom(final Room room, final long hotelId) {
		HotelEntity entity = hotelValidator.getHotelIfExist(hotelId);
		hotelValidator.getRoomCategoryIfExist(room);
		var roomEntity = hotelMapper.roomToRoomEntity(room);
		roomEntity.setHotel(entity);
		roomEntity = roomRepository.save(roomEntity);
		return String.format(errorConfigValues.getMessageConfig().getAddRoomMessage(),
				roomEntity.getCategory().getType(), entity.getName());
	}

	/**
	 * 'delete a room from hotel
	 */
	@Override
	public String deleteRoom(final long hotelId, final long roomId) {
		HotelEntity entity = hotelValidator.getHotelIfExist(hotelId);
		var roomEntity = hotelValidator.getRoomIfExist(roomId);
		roomRepository.deleteById(roomId);
		return String.format(errorConfigValues.getMessageConfig().getDeleteRoomMessage(),
				roomEntity.getCategory().getType(), entity.getName());
	}

	/**
	 * update room for a hotel if both hotel and room exists otherwise throw
	 * exception
	 */
	@Override
	public String updateRoom(final Room room, final long hotelId, final long roomId) {
		HotelEntity entity = hotelValidator.getHotelIfExist(hotelId);
		hotelValidator.getRoomIfExist(roomId);
		var updatedroomEntity = hotelMapper.roomToRoomEntity(room);
		updatedroomEntity.setId(roomId);
		updatedroomEntity.setHotel(entity);
		updatedroomEntity = roomRepository.save(updatedroomEntity);
		return String.format(errorConfigValues.getMessageConfig().getUpdateRoomMessage(),
				updatedroomEntity.getCategory().getType(), entity.getName());
	}

	/**
	 * invoke reservation service to get all reserved hotel rooms and check availability with all hotels
	 * and return available hotel rooms 
	 * 
	 * 
	 */
	@CircuitBreaker(name = "hrsClient", fallbackMethod = "getAvailableRoomHotelFallBack")
	@Override
	public List<Hotel> getAvailableRoomHotel(final String startDate, final String endDate) {
		List<Hotel> availableHotels = new ArrayList<>();
		var apiResponseData = reservationClient.getReservedRooms(startDate, endDate);
		List<Reservation> reservedHotelRooms =  apiResponseData.getData();
		List<HotelEntity> entities = hotelRepository.findAll();
		List<Hotel> hotels = hotelMapper.hotelEntitiesToHotels(entities);
		/*
		 * if reservation status is OK and there is no booking yet
		 */
		if(apiResponseData.getStatus().value()==200 && null == reservedHotelRooms||reservedHotelRooms.isEmpty()) {
			availableHotels.addAll(hotels);
		}else {
		/*
		 *	hotels are in reservation but those rooms not booked yet
		 */
		List<Hotel> availableHotelsExistInReservation = Optional.ofNullable(hotels).orElse(Collections.emptyList())
				.stream()
				.filter(allHotel->  Optional.ofNullable(reservedHotelRooms).orElse(Collections.emptyList()).stream()
						.anyMatch(rh-> rh.getHotelId() == allHotel.getId() 
						&& null != rh.getRoomIds() 
						&& allHotel.getRooms()
								.stream()
								.anyMatch(room-> !rh.getRoomIds().contains(room.getId()))))
						.collect(Collectors.toList());
		/*
		 * hotels are not present in reservation 
		 */
		List<Hotel> availableHotelsNotExistInReservation = Optional.ofNullable(hotels).orElse(Collections.emptyList())
				.stream()
				.filter(allHotel->  Optional.ofNullable(reservedHotelRooms).orElse(Collections.emptyList()).stream()
						.anyMatch(hotel-> hotel.getHotelId() != allHotel.getId()))
				.collect(Collectors.toList());
		availableHotels.addAll(availableHotelsExistInReservation);
		availableHotels.addAll(availableHotelsNotExistInReservation);
		}
		return availableHotels;
	}
	
	/**
	 * FallBack method of getAvailableRoomHotel
	 * 
	 * @param startDate
	 * @param endDate
	 * @param Throwable
	 * @return List
	 */
	public List<Hotel> getAvailableRoomHotelFallBack(final String startDate, final String endDate,Throwable t) {
		log.info("In HotelServiceImpl::getAvailableRoomHotelFallBack() method:{}",t.toString());
		log.debug("start date:end date:: {}:{}",startDate,endDate);
		return new ArrayList<>();
	}

	@Override
	public Hotel getRoomById(long hotelId, long roomId) {
		var hotelEntity = hotelValidator.getHotelIfExist(hotelId);
		var roomEntity = hotelValidator.getRoomIfExist(roomId);
		List<RoomEntity> rooms = new ArrayList<>();
		rooms.add(roomEntity);
		hotelEntity.setRooms(rooms);
		return hotelMapper.hotelEntityToHotel(hotelEntity);
	}

	@Override
	public boolean updateRoomStatus(Reservation reservation) {
		if(null != reservation) {
			log.info("hotel ID: {} ",reservation.getHotelId());
			log.info("room ID: {} ",reservation.getRoomId());
			log.info("room Status: {} ",reservation.getStatus());
			hotelValidator.getHotelIfExist(reservation.getHotelId());
			var roomEntity = hotelValidator.getRoomIfExist(reservation.getRoomId());
			roomEntity.setStatus(reservation.getStatus());
			roomEntity = roomRepository.save(roomEntity);
			log.info("Room Id {} status changed to {}",roomEntity.getId(),roomEntity.getStatus());
			return true;
		}
		return false;
	}


}
