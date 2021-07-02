package com.reservation;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.reservation.ReservationServiceApplication;
import com.reservation.controller.ReservationController;
import com.reservation.service.ReservationService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(classes = ReservationServiceApplication.class)
@RunWith(SpringRunner.class)
public class BaseClass {

	@Autowired
    ReservationController reservationController;

    @MockBean
    private ReservationService reservationService;
    
   @Autowired
    private WebApplicationContext applicationContext;


    @Before
    public void before() {
        RestAssuredMockMvc.standaloneSetup(this.reservationController);
        RestAssuredMockMvc.webAppContextSetup(applicationContext);
    }
}
