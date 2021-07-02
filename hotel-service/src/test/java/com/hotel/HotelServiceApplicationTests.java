package com.hotel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hotel.config.MessageConfigValues;

@SpringBootTest
class HotelServiceApplicationTests {
	@Autowired
	private MessageConfigValues values;

	@Test
	void contextLoads() {
		HotelServiceApplication.main(new String[] {});
		assertNotNull(values);
		assertEquals("HC_GENERIC_ERROR",values.getErrorConfig().getGenericErrorCode());
	}

}
