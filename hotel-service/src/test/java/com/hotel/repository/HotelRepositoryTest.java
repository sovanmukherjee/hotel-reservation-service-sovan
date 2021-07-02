package com.hotel.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.hotel.entity.ContactEntity;
import com.hotel.entity.HotelEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class HotelRepositoryTest {

	@Autowired
	private HotelRepository hotelRepository;

	@Sql(statements = "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM hotel", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void findAlltest() {
		List<HotelEntity> entity = hotelRepository.findAll();
		assertNotNull(entity);
		assertEquals(1, entity.size());
	}

	@Sql(statements = { "DELETE FROM contacts",
			"DELETE FROM hotel" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void saveHotelTest() {
		HotelEntity hotel = new HotelEntity();
		hotel.setName("Hotel A");
		hotel.setDescription("test");
		hotel.setAddress("Pune");

		ContactEntity contact = new ContactEntity();
		contact.setEmail("test@gmail.com");
		contact.setMobile1("9999999999");
		contact.setMobile2("888888888");
		contact.setHotel(hotel);
		hotel.setContact(contact);
		HotelEntity entity = hotelRepository.save(hotel);
		assertNotNull(entity);
		assertEquals("Pune", entity.getAddress());
	}

	@Sql(statements = "INSERT INTO hotel (id, name,address,description) VALUES (1, 'HotelA','Pune','')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM hotel", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	 void findOne() {
		Optional<HotelEntity> entity = hotelRepository.findById(1L);
		assertNotNull(entity);
		assertEquals(true, entity.isPresent());
	}

}
