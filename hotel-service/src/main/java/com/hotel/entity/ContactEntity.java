package com.hotel.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="contacts")
@Getter@Setter
public class ContactEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	private long id;
	
	@Column(name="mobile1", length=50)
	private String mobile1;
	
	@Column(name="mobile2", length=50)
	private String mobile2;
	
	@Column(name="email", length=50)
	private String email;
		
	@OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotelId", nullable = false)
    private HotelEntity hotel;
}
