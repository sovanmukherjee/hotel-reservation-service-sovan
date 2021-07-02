package com.hotel.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.hotel.constant.RoomStatus;
import com.hotel.entity.HotelEntity;
import com.hotel.entity.RoomCategoryEntity;
import com.hotel.entity.RoomEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class RoomRepositoryTest {

	@Autowired
	private RoomRepository roomRepository;

	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void addRoomTest() {
		RoomEntity room = new RoomEntity();
		room.setPrice(6000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.UNPUBLISHED.toString());
		
		HotelEntity hotel = new HotelEntity();
		hotel.setId(1);
		room.setHotel(hotel);
		RoomCategoryEntity category = new RoomCategoryEntity();
		category.setId(2);
		category.setType("Double Queen");
		room.setCategory(category);
		
		RoomEntity entity = roomRepository.save(room);
		assertNotNull(entity);
		assertEquals(6, entity.getRoomFloorNo());
	}
	
	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void deleteRoomTest() {
		roomRepository.deleteById(1L);
		Optional<RoomEntity> entity = roomRepository.findById(1L);
		assertEquals(false, entity.isPresent());
	}
	
	@Sql(statements = { "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')",
			"INSERT INTO contacts (id, mobile1,mobile2,email,hotelId) VALUES (1, '9999999999','8888888888','test@gmail.com',1)",
			"INSERT INTO ROOM_CATEGORY(id,type) values(1,'King Bed'),(2,'Double Queen')",
			"INSERT INTO ROOM(id,roomFloorNo,price,status,roomCategoryId,hotelId) values(1,6,2000,'UNPUBLISHED',1,1)"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "DELETE FROM room", "DELETE FROM contacts", "DELETE FROM hotel",
			"DELETE FROM ROOM_CATEGORY" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void updateRoomTest() {
		RoomEntity room = new RoomEntity();
		room.setId(1);
		room.setPrice(6000);
		room.setRoomFloorNo(6);
		room.setStatus(RoomStatus.AVAILABLE.toString());
		
		HotelEntity hotel = new HotelEntity();
		hotel.setId(1);
		room.setHotel(hotel);
		RoomCategoryEntity category = new RoomCategoryEntity();
		category.setId(2);
		category.setType("Double Queen");
		room.setCategory(category);
		
		RoomEntity entity = roomRepository.save(room);
		assertNotNull(entity);
		assertEquals(RoomStatus.AVAILABLE.toString(), entity.getStatus());
		assertEquals("Double Queen", entity.getCategory().getType());
	}
}
