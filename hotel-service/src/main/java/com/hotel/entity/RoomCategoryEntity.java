package com.hotel.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room_category")
@Getter@Setter
public class RoomCategoryEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	private long id;
	@Column(name = "type", length = 50)
	private String type;
}
