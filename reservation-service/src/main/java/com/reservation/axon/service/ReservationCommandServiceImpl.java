package com.reservation.axon.service;

import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservation.axon.command.CreateReservationCommand;
import com.reservation.axon.command.InitiatePaymentCommand;
import com.reservation.axon.event.ReservationCreatedEvent;
import com.reservation.constants.Status;
import com.reservation.entity.CardStoreEntity;
import com.reservation.axon.command.CreatePaymentCommand;
import com.reservation.model.Payment;
import com.reservation.model.ReservationRequest;
import com.reservation.repository.ReservationRepository;
import com.reservation.validator.ReservationValidator;

@Service
public class ReservationCommandServiceImpl implements ReservationCommandService {
	@Autowired
	private CommandGateway commandGateway;
	@Autowired
	private ReservationValidator reservationValidator;

	 

	@Override
	public String bookHotelRoom(Map<String, String> headers, ReservationRequest reservation) {
			reservationValidator.getGuestIfExist(headers, reservation.getGuestId());
			reservationValidator.getHotelRoomIfExist(headers, reservation.getHotelId(), reservation.getRoomId());
			var reservationNo = UUID.randomUUID().toString();
			commandGateway
					.send(new CreateReservationCommand(reservationNo,reservation.getId(),reservation.getGuestId(),reservation.getHotelId(),reservation.getRoomId(),reservation.getCheckInDate(),
							reservation.getCheckOutDate(),reservation.getTotalAmount(),reservation.getTotalDepositedAmount(),reservation.getComment(),reservation.getStatus()));
			return String.format("Reservation created successfully. Reservation No: %s ",reservationNo);
	}

	
	@Override
	public String payment(Map<String, String> headers, Payment payment, String reservationNo) {
		var reservationEntity = reservationValidator.getReservationIfExistByReservationNo(headers,reservationNo);
		CardStoreEntity cardStore = reservationValidator.getCardStoreIfExist(headers,payment.getCardStoreId());
		if(null != cardStore) {
			commandGateway.send(new CreatePaymentCommand(reservationNo,payment.getId(),payment.getPaymentBy(),payment.getAmount(),payment.getMode().getId(),payment.getCardStoreId(),false,reservationEntity.getHotelId(),reservationEntity.getRoomId(),reservationEntity.getStatus()));
			return String.format("Payment successfull. Reservation No: %s ",reservationNo);
		}else {
			commandGateway.send(new CreatePaymentCommand(reservationNo, true,reservationEntity.getHotelId(),reservationEntity.getRoomId(),reservationEntity.getStatus()));
			return String.format("Payment failed. Reservation No: %s ",reservationNo);
		}
	}


	@Override
	public void paymentInitiate(ReservationCreatedEvent reservation) {
		reservationValidator.getReservationIfExistByReservationNo(null,reservation.getReservationNo());
		commandGateway.send(new InitiatePaymentCommand(reservation.getReservationNo(),Status.PAYMENT_INITIATED.toString()));
	}
}
