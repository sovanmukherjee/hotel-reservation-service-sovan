package com.reservation.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.reservation.axon.command.CreateReservationCommand;
import com.reservation.axon.event.ReservationCreatedEvent;
import com.reservation.entity.CardStoreEntity;
import com.reservation.entity.PaymentEntity;
import com.reservation.entity.ReservationEntity;
import com.reservation.model.CardDetails;
import com.reservation.model.Payment;
import com.reservation.model.ReservationRequest;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

	ReservationEntity reservationToReservationEntity(ReservationRequest reservation);

	PaymentEntity paymentToPaymentEntity(Payment payment);

	CardStoreEntity cardToCardStoreEntity(CardDetails cardDetails);

	List<Payment> paymentEntityListToPaymentList(List<PaymentEntity> entity);

	CreateReservationCommand reservationToBookHotelRoomCommand(ReservationRequest reservation);

	ReservationCreatedEvent bookHotelRoomCommandToHotelRoomBookedEvent(CreateReservationCommand bookHotelRoomCommand);

	ReservationEntity hotelRoomBookedEventToReservationEntity(ReservationCreatedEvent event);

	 

	
}
