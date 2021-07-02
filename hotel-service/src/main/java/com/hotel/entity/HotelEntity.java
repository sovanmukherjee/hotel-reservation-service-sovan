package com.hotel.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hotel")
@Getter@Setter
public class HotelEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	private long id;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "address", length = 200)
	private String address;

	@OneToOne(fetch = FetchType.LAZY,cascade =  CascadeType.ALL,mappedBy = "hotel")
	@PrimaryKeyJoinColumn
	private ContactEntity contact;
	
	@OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RoomEntity> rooms;
}
