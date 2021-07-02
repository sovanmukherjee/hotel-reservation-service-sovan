package com.hotel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
	private long id;
	private long roomFloorNo;
	private double price;
	private String status;
	private RoomCategory category;

}
