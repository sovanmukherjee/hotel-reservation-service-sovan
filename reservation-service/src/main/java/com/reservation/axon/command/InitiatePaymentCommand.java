package com.reservation.axon.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InitiatePaymentCommand {
	@TargetAggregateIdentifier
	private String reservationNo;
	private String status;
}
