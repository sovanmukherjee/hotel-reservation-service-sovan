package com.reservation.entity;

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
@Table(name="card_store")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardStoreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	private long id;
	
	@Column(name="creditCardNo", length=50)
	private String creditCardNo;
	
	@Column(name="creditCardName", length=50)
	private String creditCardName;
	
	@Column(name="creditCardExpiry", length=50)
	private String creditCardExpiry;
	
	@Column(name="guestId", length=50)
	private long guestId;
}
