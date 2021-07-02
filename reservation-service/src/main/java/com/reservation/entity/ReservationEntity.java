package com.reservation.entity;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="reservation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
	
	@Column(name="reservationNo", length=50)
	private String reservationNo;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	private long id;
	
	@Column(name="guestId", length=50)
	private long guestId;
	
	@Column(name="hotelId", length=50)
	private long hotelId;
	
	@Column(name="roomId", length=50)
	private long roomId;
	
	@Column(name="checkInDate", length=50)
	private LocalDate checkInDate;
	
	@Column(name="checkOutDate", length=50)
	private LocalDate checkOutDate;
	
	@Column(name="totalAmount", length=50)
	private double totalAmount;
	
	@Column(name="totalDepositedAmount", length=50)
	private double totalDepositedAmount;
	
	@Column(name="comment", length=100)
	private String comment;
	
	@Column(name="status", length=50)
	private String status;
	
	
}
