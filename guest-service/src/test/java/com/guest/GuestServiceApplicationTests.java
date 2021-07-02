package com.guest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.guest.config.MessageConfigValues;


@SpringBootTest
@ActiveProfiles("test")
class GuestServiceApplicationTests {
	@Autowired
	private MessageConfigValues values;

	@Test
	void contextLoads() {
		GuestServiceApplication.main(new String[] {});
		assertNotNull(values);
		assertEquals("GC_GENERIC_ERROR",values.getErrorConfig().getGenericErrorCode());
	}

}
