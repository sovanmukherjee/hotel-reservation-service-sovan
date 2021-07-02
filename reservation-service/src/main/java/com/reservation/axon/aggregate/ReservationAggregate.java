package com.reservation.axon.aggregate;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.reservation.axon.command.CreatePaymentCommand;
import com.reservation.axon.command.CreateReservationCommand;
import com.reservation.axon.command.InitiatePaymentCommand;
import com.reservation.axon.event.EventDetails;
import com.reservation.axon.event.PaymentCreatedEvent;
import com.reservation.axon.event.PaymentFailedEvent;
import com.reservation.axon.event.PaymentInitiatedEvent;
import com.reservation.axon.event.ReservationCreatedEvent;
import com.reservation.constants.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Aggregate
public class ReservationAggregate {


	@AggregateIdentifier
	private String reservationNo;
	private long id;
	private long guestId;
	private long hotelId;
	private long roomId;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private double totalAmount;
	private double totalDepositedAmount;
	private String comment;
	private String status;
	
	@CommandHandler
	public ReservationAggregate(CreateReservationCommand command) {
		AggregateLifecycle.apply(new ReservationCreatedEvent(new EventDetails(UUID.randomUUID().toString(),"ReservationCreatedEvent"),command.getReservationNo(),command.getGuestId(),command.getHotelId(),command.getRoomId(),command.getCheckInDate(),
				command.getCheckOutDate(),command.getTotalAmount(),command.getTotalDepositedAmount(),command.getComment(),command.getStatus()));
	}

	@EventSourcingHandler
	public void on(ReservationCreatedEvent event) {
		this.reservationNo =event.getReservationNo();
		this.reservationNo =event.getReservationNo();
		this.guestId=event.getGuestId();
		this.hotelId=event.getHotelId();
		this.roomId=event.getRoomId();
		this.checkInDate=event.getCheckInDate();
		this.checkOutDate=event.getCheckOutDate();
		this.totalAmount=event.getTotalAmount();
		this.totalDepositedAmount=event.getTotalDepositedAmount();
		this.comment=event.getComment();
		this.status=event.getStatus();
	}
	
	@CommandHandler
	public void handle(CreatePaymentCommand command) {
	if(!command.isFailed()) {
		AggregateLifecycle.apply(new PaymentCreatedEvent(new EventDetails(UUID.randomUUID().toString(),"PaymentCreatedEvent"),command.getReservationNo(),command.getId(),command.getPaymentBy(),command.getAmount(),command.getPaymentModeId(),
				command.getCardStoreId(),command.getHotelId(),command.getRoomId(),Status.PAYMENT_SUCCESS.toString()));
	}else {
		AggregateLifecycle.apply(new PaymentFailedEvent(new EventDetails(UUID.randomUUID().toString(),"PaymentFailedEvent"),command.getReservationNo(),command.getId(),command.getPaymentBy(),command.getAmount(),command.getPaymentModeId(),
				command.getCardStoreId(),command.getHotelId(),command.getRoomId(),Status.PAYMENT_FAILED.toString()));
	}
	}
	
	@EventSourcingHandler
	public void on(PaymentCreatedEvent event) {
		this.reservationNo =event.getReservationNo();
		
	}
	
	@EventSourcingHandler
	public void on(PaymentFailedEvent event) {
		this.reservationNo =event.getReservationNo();
		
	}
	
	@CommandHandler
	public void handle(InitiatePaymentCommand command) {
		AggregateLifecycle.apply(new PaymentInitiatedEvent(new EventDetails(UUID.randomUUID().toString(),"PaymentInitiatedEvent"),command.getReservationNo(),command.getStatus()));
	}
	
	@EventSourcingHandler
	public void on(PaymentInitiatedEvent event) {
		this.reservationNo =event.getReservationNo();
		
	}
	
	
}
