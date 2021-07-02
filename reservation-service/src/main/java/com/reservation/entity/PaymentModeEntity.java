package com.reservation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="payment_mode")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModeEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	
	@Column(name="type", length=50)
	private String type;
}
