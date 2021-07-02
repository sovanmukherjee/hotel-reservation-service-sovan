package com.reservation.proxy.model.guest;

import java.time.LocalDate;
import java.util.List;

import com.reservation.model.CardDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guest {
	private long id;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String address;
	private String mobile;
	private String email;
	private List<CardDetails> cardDetails;


}
