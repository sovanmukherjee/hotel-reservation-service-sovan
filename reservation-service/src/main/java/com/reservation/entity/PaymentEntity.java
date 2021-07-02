package com.reservation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="payment")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	private long id;
	
	@Column(name="paymentBy", length=50)
	private String paymentBy;
	
	@Column(name="amount", length=50)
	private double amount;
	
	@OneToOne
	@JoinColumn(name="paymentModeId", nullable = false, referencedColumnName = "id")
	private PaymentModeEntity mode;
	
	@Column(name="cardStoreId", length=50)
	private long cardStoreId;
	
	@Column(name="reservatonId", length=50)
	private long reservatonId;
}
