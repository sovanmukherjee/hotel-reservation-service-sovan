package com.guest.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

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
	@NotBlank(message = "FirstName is mandatory")
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String address;
	@NotBlank(message = "Mobile is mandatory")
	private String mobile;
	@NotBlank(message = "Email is mandatory")
	private String email;

}
