package com.reservation.proxy.model.hotel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hotel {
	private long id;
	private String name;
	private String description;
	private String address;
	private Contact contact;
	private List<Room> rooms;
}
