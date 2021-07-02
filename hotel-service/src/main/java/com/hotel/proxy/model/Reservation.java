package com.hotel.proxy.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation {

	private long hotelId;
	private long roomId;
	private String status;
	private List<Long> roomIds;
}
