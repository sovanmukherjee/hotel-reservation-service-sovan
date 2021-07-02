package com.hotel.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room")
@Getter@Setter
public class RoomEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	private long id;
	
	@Column(name = "roomFloorNo", nullable = false, length = 50)
	private long roomFloorNo;
	
	@Column(name = "price", length = 50)
	private double price;
	
	@Column(name = "status", length = 50)
	private String status;
	
	@OneToOne//(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="roomCategoryId", nullable = false, referencedColumnName = "id")
	private RoomCategoryEntity category;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotelId", nullable = false)
	private HotelEntity hotel;

}
