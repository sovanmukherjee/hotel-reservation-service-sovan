package com.reservation.axon.projection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reservation.axon.event.PaymentCreatedEvent;
import com.reservation.axon.event.PaymentFailedEvent;
import com.reservation.axon.event.PaymentInitiatedEvent;
import com.reservation.axon.event.ReservationCreatedEvent;
import com.reservation.axon.query.GetPaymentsByReservationIdQuery;
import com.reservation.axon.query.GetReservationQuery;
import com.reservation.config.MessageConfigValues;
import com.reservation.constants.Status;
import com.reservation.entity.PaymentEntity;
import com.reservation.entity.PaymentModeEntity;
import com.reservation.entity.ReservationEntity;
import com.reservation.exception.BusinessException;
import com.reservation.mapper.ReservationMapper;
import com.reservation.producer.ReservationProducer;
import com.reservation.repository.PaymentRepository;
import com.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReservationProjection {
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private ReservationMapper reservationMapper;
	@Autowired
	private MessageConfigValues configValues;
	@Autowired
	private ReservationProducer reservationProducer;

	@EventHandler
	public void on(ReservationCreatedEvent event) {
		var entity = reservationMapper.hotelRoomBookedEventToReservationEntity(event);
		 reservationRepository.save(entity);
		 reservationProducer.sendToOrchestratorOrderCreated(event); 
	}
	
	@EventHandler
	public void on(PaymentCreatedEvent event) {
		var reservationEntity = reservationRepository.findByReservationNo(event.getReservationNo());
		if(null != reservationEntity) {
			var mode = new PaymentModeEntity(event.getPaymentModeId(),"");
			var payment = new PaymentEntity(event.getId(),event.getPaymentBy(),event.getAmount(),	mode,event.getCardStoreId(),reservationEntity.getId());
			paymentRepository.save(payment);
			reservationEntity.setTotalDepositedAmount(reservationEntity.getTotalDepositedAmount()+payment.getAmount());
			reservationEntity.setStatus(Status.PAYMENT_SUCCESS.toString());
			reservationRepository.save(reservationEntity);
			reservationProducer.sendToOrchestratorPaymentCreatedEvent(event); 
		}else {
			throw new BusinessException(configValues.getErrorConfig().getReservationNotFoundErrorCode()
					,String.format(configValues.getErrorConfig().getReservationFoundErrorMessage(),event.getReservationNo()));
		}
	}
	
	@EventHandler
	public void on(PaymentInitiatedEvent event) {
		var reservationEntity = reservationRepository.findByReservationNo(event.getReservationNo());
		reservationEntity.setStatus(event.getStatus());
		 reservationRepository.save(reservationEntity);
	}
	
	@EventHandler
	public void on(PaymentFailedEvent event) {
		var reservationEntity = reservationRepository.findByReservationNo(event.getReservationNo());
		reservationEntity.setStatus(Status.PAYMENT_FAILED.toString());
		reservationRepository.save(reservationEntity);
		 reservationProducer.sendToOrchestratorPaymentFailedEvent(event); 
	}
	
	
	
	@QueryHandler
	public List<ReservationEntity> handle(GetReservationQuery query) {
	    log.info("Handling GetReservationQuery query: {}", query);
	    List<String> status = Arrays.asList(Status.BOOKED.toString(),Status.PAYMENT_INITIATED.toString(),Status.PAYMENT_FAILED.toString(),Status.PAYMENT_SUCCESS.toString());
	    return reservationRepository.findAllByStatusIn(status);
	}
	
	@QueryHandler
	public List<PaymentEntity> handle(GetPaymentsByReservationIdQuery query) {
	    log.info("Handling GetPaymentsByReservationIdQuery query: {}", query);
	     return paymentRepository.findAllByReservatonId(query.getReservationId());
	}

}
