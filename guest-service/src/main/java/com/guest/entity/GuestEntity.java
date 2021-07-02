package com.guest.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="guest")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	private long id;
	
	@Column(name="firstName", length=50)
	private String firstName;
	
	@Column(name="lastName", length=50)
	private String lastName;
	
	@Column(name="dob", length=50)
	private LocalDate dob;
	
	@Column(name="address", length=50)
	private String address;
	
	@Column(name="mobile", length=50)
	private String mobile;
	
	@Column(name="email", length=50)
	private String email;

}
