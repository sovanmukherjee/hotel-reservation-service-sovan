package com.reservation.axon.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservation.axon.query.GetPaymentsByReservationIdQuery;
import com.reservation.axon.query.GetReservationQuery;
import com.reservation.config.MessageConfigValues;
import com.reservation.entity.PaymentEntity;
import com.reservation.entity.ReservationEntity;
import com.reservation.exception.BusinessException;
import com.reservation.mapper.ReservationMapper;
import com.reservation.model.Payment;
import com.reservation.model.ReservationResponse;
import com.reservation.validator.ReservationValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReservationQueryServiceImpl implements ReservationQueryService {
	@Autowired
	private QueryGateway queryGateway;
	@Autowired
	private EventStore eventStore;
	@Autowired
	private ReservationValidator reservationValidator;
	@Autowired
	private MessageConfigValues configValues;
	@Autowired
	private ReservationMapper reservationMapper; 


	@Override
	public List<ReservationResponse> getReservations(Map<String, String> headers) {
		try {
			CompletableFuture<List<ReservationEntity>> queryResult = queryGateway.query(new GetReservationQuery(),
					ResponseTypes.multipleInstancesOf(ReservationEntity.class));
			
			List<ReservationEntity> entitities = queryResult.get();
			return entitities.stream()
					.map(e -> ReservationResponse.builder().id(e.getId()).checkInDate(e.getCheckInDate())
							.checkOutDate(e.getCheckOutDate()).status(e.getStatus())
							.hotel(reservationValidator.getHotelRoomIfExist(headers, e.getHotelId(), e.getRoomId()))
							.guest(reservationValidator.getGuestIfExist(headers, e.getGuestId()))
							.payment(getPaymentListByReservationId(e.getId())).totalAmount(e.getTotalAmount())
							.totalDepositedAmount(e.getTotalDepositedAmount()).build())
					.collect(Collectors.toList());

		} catch (InterruptedException | ExecutionException e) {
			log.error("ReservationCommandServiceImpl::bookHotelRoom-", e);
			Thread.currentThread().interrupt();
			throw new BusinessException(configValues.getErrorConfig().getGenericErrorCode(), e.getMessage());
		}
	}

	private List<Payment> getPaymentListByReservationId(long reservationId) {
		try {
		CompletableFuture<List<PaymentEntity>> queryResult = queryGateway.query(new GetPaymentsByReservationIdQuery(reservationId),
				ResponseTypes.multipleInstancesOf(PaymentEntity.class));
		List<PaymentEntity> entitities = queryResult.get();
		return reservationMapper.paymentEntityListToPaymentList(entitities);
		}catch (InterruptedException | ExecutionException e) {
			log.error("ReservationCommandServiceImpl::getPaymentListByReservationId-", e);
			Thread.currentThread().interrupt();
			throw new BusinessException(configValues.getErrorConfig().getGenericErrorCode(), e.getMessage());
		}
	}
	
	public List<Object> getEventsById(String id) {
		System.out.println("&&&"+id+"&&&");
		return this.eventStore
                .readEvents(UUID.fromString(id.strip()).toString())
                .asStream()
                .map(Message::getPayload)
                .collect(Collectors.toList());
    }

}
