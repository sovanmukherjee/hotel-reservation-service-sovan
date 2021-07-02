package com.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDetails {
	private long id;
	private String creditCardNo;
	private String creditCardName;
	private String creditCardExpiry;
	private long guestId;

}
