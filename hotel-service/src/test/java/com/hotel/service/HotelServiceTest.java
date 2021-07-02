package com.hotel.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.hotel.config.MessageConfigValues;
import com.hotel.constant.RoomStatus;
import com.hotel.entity.ContactEntity;
import com.hotel.entity.HotelEntity;
import com.hotel.entity.RoomCategoryEntity;
import com.hotel.entity.RoomEntity;
import com.hotel.mapper.HotelMapper;
import com.hotel.model.Contact;
import com.hotel.model.Hotel;
import com.hotel.model.Room;
import com.hotel.model.RoomCategory;
import com.hotel.model.response.ApiResponseData;
import com.hotel.proxy.ReservationClient;
import com.hotel.proxy.model.Reservation;
import com.hotel.repository.HotelRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.validator.HotelValidator;

@SpringBootTest
@ActiveProfiles("test")
class HotelServiceTest {

	@Autowired
	private HotelService hotelService;

	@MockBean
	private HotelRepository hotelRepositoryMock;

	@MockBean
	private HotelMapper hotelMapperMock;

	@MockBean
	private RoomRepository roomRepositoryMock;

	@MockBean
	private HotelValidator hotelValidatorMock;
	@MockBean
	private ReservationClient reservationClientMock;
	@MockBean
	private MessageConfigValues errorConfigValuesMock;

	private MessageConfigValues.MessageConfig buildMessageConfig() {
		MessageConfigValues.MessageConfig config = new MessageConfigValues.MessageConfig();
		config.setAddRoomMessage("one %s category room added successfully for %s");
		config.setAddUpdateHotelMessage("Hotel %s %s successfully");
		config.setDeleteRoomMessage("one %s category room deleted from %s");
		config.setUpdateRoomMessage("one %s category room updated successfully for %s");
		return config;
	}

	private Hotel getHotel() {
		Hotel hotel = new Hotel();
		hotel.setName("Hotel A");
		hotel.setDescription("test");
		hotel.setAddress("Pune");

		Contact contact = new Contact();
		contact.setEmail("test@gmail.com");
		contact.setMobile1("9999999999");
		contact.setMobile2("888888888");
		hotel.setContact(contact);
		return hotel;
	}

	private HotelEntity getHotelEntity() {
		HotelEntity hotel = new HotelEntity();
		hotel.setName("Hotel A");
		hotel.setDescription("test");
		hotel.setAddress("Pune");

		ContactEntity contact = new ContactEntity();
		contact.setEmail("test@gmail.com");
		contact.setMobile1("9999999999");
		contact.setMobile2("888888888");
		hotel.setContact(contact);
		return hotel;
	}

	private Room getRoom() {
		Room room = new Room();
		room.setPrice(6000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.UNPUBLISHED.toString());
		RoomCategory category = new RoomCategory();
		category.setType("test");
		room.setCategory(category);
		return room;
	}

	private RoomEntity getRoomEntity() {
		RoomEntity room = new RoomEntity();
		room.setPrice(6000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.UNPUBLISHED.toString());
		RoomCategoryEntity category = new RoomCategoryEntity();
		category.setType("test");
		room.setCategory(category);
		return room;
	}

	@Test
	void getHotelsTest() {
		List<HotelEntity> list = new ArrayList<>();
		HotelEntity entity = new HotelEntity();
		entity.setName("Hotel A");
		ContactEntity contact = new ContactEntity();
		contact.setEmail("test@gmail.com");
		entity.setContact(contact);
		list.add(entity);

		Mockito.when(hotelRepositoryMock.findAll()).thenReturn(list);
		List<Hotel> hotels = new ArrayList<>();
		hotels.add(getHotel());
		Mockito.when(hotelMapperMock.hotelEntitiesToHotels(Mockito.any())).thenReturn(hotels);
		hotels = hotelService.getHotels();
		assertNotNull(hotels);
		assertEquals(1, hotels.size());
		assertEquals("Hotel A", hotels.get(0).getName());
	}

	@Test
	void getHotelsTestNegative() {

		Mockito.when(hotelRepositoryMock.findAll()).thenReturn(null);
		Mockito.when(hotelMapperMock.hotelEntitiesToHotels(null)).thenReturn(null);
		List<Hotel> hotels = hotelService.getHotels();
		assertNull(hotels);
	}

	@Test
	void addHotelTest() {
		Hotel hotel = new Hotel();
		hotel.setName("Hotel A");
		hotel.setDescription("test");
		hotel.setAddress("Pune");

		Contact contact = new Contact();
		contact.setEmail("test@gmail.com");
		contact.setMobile1("9999999999");
		contact.setMobile2("888888888");
		hotel.setContact(contact);
		Mockito.when(errorConfigValuesMock.getMessageConfig()).thenReturn(buildMessageConfig());
		HotelEntity entity = getHotelEntity();
		Mockito.when(hotelMapperMock.hotelToHotelEntity(Mockito.any(Hotel.class))).thenReturn(entity);

		Mockito.when(hotelRepositoryMock.save(Mockito.any(HotelEntity.class))).thenReturn(entity);
		assertEquals("Hotel A", entity.getName());
		String message = hotelService.addHotel(hotel);
		assertEquals("Hotel Hotel A added successfully", message);
	}

	@Test
	void updateHotelTest() {
		Hotel hoteldb = getHotel();
		HotelEntity entitydb = getHotelEntity();
		Mockito.when(hotelMapperMock.hotelToHotelEntity(Mockito.any(Hotel.class))).thenReturn(entitydb);
		entitydb.setId(1);
		Mockito.when(hotelValidatorMock.getHotelIfExist(Mockito.any(Long.class))).thenReturn(entitydb);
		Hotel hotel = getHotel();
		hotel.setId(1);
		Mockito.when(hotelMapperMock.hotelToHotelEntity(Mockito.any(Hotel.class))).thenReturn(entitydb);
		HotelEntity entity = getHotelEntity();
		Mockito.when(errorConfigValuesMock.getMessageConfig()).thenReturn(buildMessageConfig());

		Mockito.when(hotelMapperMock.hotelToHotelEntity(Mockito.any(Hotel.class))).thenReturn(entity);
		Mockito.when(hotelRepositoryMock.save(Mockito.any(HotelEntity.class))).thenReturn(entity);
		String message = hotelService.updateHotel(hotel, 1);
		assertEquals("Hotel Hotel A updated successfully", message);
	}

	@Test
	void getHotelByIdTest() {
		Hotel hoteldb = getHotel();
		HotelEntity entitydb = new HotelEntity();
		Mockito.when(hotelValidatorMock.getHotelIfExist(Mockito.any(Long.class))).thenReturn(entitydb);
		Mockito.when(hotelMapperMock.hotelEntityToHotel(Mockito.any(HotelEntity.class))).thenReturn(hoteldb);
		hoteldb = hotelService.getHotelById(1);
		assertNotNull(hoteldb);
		assertNotEquals("Hotel B", hoteldb.getName());
		assertEquals("Hotel A", hoteldb.getName());

	}

	@Test
	
	void addRoomTest() {

		Room roomdb = getRoom();
		RoomEntity entitydb = getRoomEntity();
		Mockito.when(hotelMapperMock.roomToRoomEntity(Mockito.any(Room.class))).thenReturn(entitydb);
		entitydb.setId(1);
		Hotel hoteldb = getHotel();
		hoteldb.setId(1);
		HotelEntity hotelEntitydb = getHotelEntity();
		Mockito.when(hotelMapperMock.hotelToHotelEntity(Mockito.any(Hotel.class))).thenReturn(hotelEntitydb);
		Mockito.when(hotelValidatorMock.getHotelIfExist(Mockito.any(Long.class))).thenReturn(hotelEntitydb);
		Mockito.when(roomRepositoryMock.save(Mockito.any(RoomEntity.class))).thenReturn(entitydb);
		Mockito.when(errorConfigValuesMock.getMessageConfig()).thenReturn(buildMessageConfig());
		String message = hotelService.addRoom(roomdb, 1L);
		assertNotNull(entitydb);
		assertEquals("one test category room added successfully for Hotel A", message);
	}

	@Test
	
	void deleteRoomTest() {
		Hotel hoteldb = getHotel();
		hoteldb.setId(1);
		HotelEntity hotelEntitydb = getHotelEntity();
		Mockito.when(hotelMapperMock.hotelToHotelEntity(Mockito.any(Hotel.class))).thenReturn(hotelEntitydb);

		Mockito.when(hotelValidatorMock.getHotelIfExist(Mockito.any(Long.class))).thenReturn(hotelEntitydb);
		RoomEntity entitydb = getRoomEntity();
		Mockito.when(hotelMapperMock.roomToRoomEntity(Mockito.any(Room.class))).thenReturn(entitydb);

		entitydb.setId(1);
		Mockito.when(errorConfigValuesMock.getMessageConfig()).thenReturn(buildMessageConfig());

		Mockito.when(hotelValidatorMock.getRoomIfExist(Mockito.any(Long.class))).thenReturn(entitydb);
		Mockito.doNothing().when(roomRepositoryMock).delete(Mockito.any(RoomEntity.class));
		String message = hotelService.deleteRoom(1L, 1L);
		assertEquals("one test category room deleted from Hotel A", message);
	}

	@Test
	
	void updateRoomTest() {

		Room roomdb = getRoom();
		RoomEntity entitydb = getRoomEntity();
		Mockito.when(hotelMapperMock.roomToRoomEntity(Mockito.any(Room.class))).thenReturn(entitydb);

		entitydb.setId(1);
		Hotel hoteldb = getHotel();
		hoteldb.setId(1);
		HotelEntity hotelEntitydb = getHotelEntity();
		Mockito.when(hotelMapperMock.hotelToHotelEntity(Mockito.any(Hotel.class))).thenReturn(hotelEntitydb);

		Mockito.when(errorConfigValuesMock.getMessageConfig()).thenReturn(buildMessageConfig());

		Mockito.when(hotelValidatorMock.getHotelIfExist(Mockito.any(Long.class))).thenReturn(hotelEntitydb);
		Mockito.when(hotelValidatorMock.getRoomIfExist(Mockito.any(Long.class))).thenReturn(entitydb);
		Mockito.when(hotelMapperMock.roomToRoomEntity(roomdb)).thenReturn(entitydb);
		Mockito.when(roomRepositoryMock.save(Mockito.any(RoomEntity.class))).thenReturn(entitydb);
		String message = hotelService.updateRoom(roomdb, 1L, 1L);
		assertNotNull(entitydb);
		assertEquals("one test category room updated successfully for Hotel A", message);
	}

	
	@Test
	
	void getAvailableRoomHotelTest() {
		Reservation resrv = new Reservation();
		resrv.setHotelId(1L);
		List<Long> roomIds = new ArrayList<>();
		roomIds.add(2L);
		resrv.setRoomIds(roomIds);
		List<Reservation> reservations = new ArrayList<>();
		reservations.add(resrv);

		Room room = getRoom();
		room.setId(1);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);
		Hotel hotel = getHotel();
		hotel.setId(1);
		hotel.setRooms(rooms);
		List<Hotel> hotels = new ArrayList<>();
		hotels.add(hotel);
		List<HotelEntity> list = new ArrayList<>();
		list.add(new HotelEntity());
		Mockito.when(hotelRepositoryMock.findAll()).thenReturn(list);
		Mockito.when(hotelMapperMock.hotelEntitiesToHotels(list)).thenReturn(hotels);
		ApiResponseData responseData = new ApiResponseData(reservations, HttpStatus.OK, null);
		Mockito.when(reservationClientMock.getReservedRooms(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseData);
		hotels = hotelService.getAvailableRoomHotel("10-10-2021", "16-10-2021");
		assertEquals(1, hotels.size());

	}

	@Test
	void getAvailableRoomHotelTestWithEmptyReservedRoom() {
		Room room = getRoom();
		room.setId(1);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);
		Hotel hotel = getHotel();
		hotel.setId(1);
		hotel.setRooms(rooms);
		List<Hotel> hotels = new ArrayList<>();
		hotels.add(hotel);
		List<HotelEntity> list = new ArrayList<>();
		list.add(new HotelEntity());
		Mockito.when(hotelRepositoryMock.findAll()).thenReturn(list);
		Mockito.when(hotelMapperMock.hotelEntitiesToHotels(list)).thenReturn(hotels);
		ApiResponseData responseData = new ApiResponseData(null, HttpStatus.OK, null);
		Mockito.when(reservationClientMock.getReservedRooms(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseData);
		hotels = hotelService.getAvailableRoomHotel("10-10-2021", "16-10-2021");
		assertEquals(1, hotels.size());

	}

}
