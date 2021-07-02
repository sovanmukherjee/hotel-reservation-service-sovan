package com.reservation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservation.config.MessageConfigValues;
import com.reservation.constants.Status;
import com.reservation.entity.ReservationEntity;
import com.reservation.mapper.ReservationMapper;
import com.reservation.model.CardDetails;
import com.reservation.model.Payment;
import com.reservation.model.ReservationRequest;
import com.reservation.model.ReservationResponse;
import com.reservation.producer.ReservationProducer;
import com.reservation.repository.CardStoreRepository;
import com.reservation.repository.PaymentRepository;
import com.reservation.repository.ReservationRepository;
import com.reservation.validator.ReservationValidator;

@Service
public class ReservationServiceImpl implements ReservationService {
	@Autowired
	private ReservationMapper reservationMapper;
	@Autowired
	private ReservationValidator reservationValidator;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private CardStoreRepository cardStoreRepository;
	@Autowired
	private MessageConfigValues configValues;
	@Autowired
	private ReservationProducer reservationProducer;

	@Override
	public List<ReservationResponse> getReservations(Map<String, String> headers) {
//		List<String> list = new ArrayList<>();
//		list.add(Status.BOOKED.toString());
//		list.add(Status.PAYMENT_INITIATED.toString());
	    List<String> list = Arrays.asList(Status.BOOKED.toString(),Status.PAYMENT_INITIATED.toString(),Status.PAYMENT_FAILED.toString(),Status.PAYMENT_SUCCESS.toString());

		List<ReservationEntity> entities = reservationRepository.findAllByStatusIn(list);
		return entities.stream().map(e-> ReservationResponse.builder()
				.id(e.getId()).checkInDate(e.getCheckInDate())
				.checkOutDate(e.getCheckOutDate())
				.status(e.getStatus())
				.hotel(reservationValidator.getHotelRoomIfExist(headers,e.getHotelId(), e.getRoomId()))
				.guest(reservationValidator.getGuestIfExist(headers,e.getGuestId()))
				.payment(getPaymentListByReservationId(e.getId()))
				.totalAmount(e.getTotalAmount())	
				.totalDepositedAmount(e.getTotalDepositedAmount())
				.build())
				.collect(Collectors.toList());
	}
	
	private List<Payment> getPaymentListByReservationId(long reservationId){
		return reservationMapper.paymentEntityListToPaymentList(paymentRepository.findAllByReservatonId(reservationId));
	}

	@Override
	public String allocateRoom(Map<String, String> headers,ReservationRequest reservation) {
		reservationValidator.getGuestIfExist(headers,reservation.getGuestId());
		reservationValidator.getHotelRoomIfExist(headers,reservation.getHotelId(), reservation.getRoomId());
		var entity = reservationMapper.reservationToReservationEntity(reservation);
		entity.setStatus(Status.BOOKED.toString());
		entity.setReservationNo(UUID.randomUUID().toString());
		entity = reservationRepository.save(entity);
//		reservationProducer.sendToUpdateHotelBook(reservation); 
		return String.format(configValues.getMessageConfig().getReservationSuccessMessage(),
				String.valueOf(entity.getId()));
	}

	@Override
	public List<ReservationResponse> getReservationHotelRoomsByDate(Map<String, String> headers,LocalDate startDate, LocalDate endDate) {
		List<ReservationEntity> entities = reservationRepository
				.findAllByCheckInDateGreaterThanEqualAndCheckOutDateLessThanEqualAndStatus(startDate, endDate,
						Status.BOOKED.toString());
		Map<Long, List<Long>> data = entities.stream().collect(
				Collectors.groupingBy(ReservationEntity::getHotelId, Collectors.collectingAndThen(Collectors.toList(),
						r -> r.stream().map(ReservationEntity::getRoomId).collect(Collectors.toList()))));
		List<ReservationResponse> list = null;
		if (null != data) {
			list = data.entrySet().stream()
					.map(e -> ReservationResponse.builder().hotelId(e.getKey()).roomIds(e.getValue()).build())
					.collect(Collectors.toList());
		}
		return list;
	}

	@Override
	public String payment(Map<String, String> headers,Payment payment, long reservationId) {
		ReservationEntity reservation = reservationValidator.getReservationIfExist(headers,reservationId);
		reservationValidator.getCardStoreIfExist(headers,payment.getCardStoreId());
		var entity = reservationMapper.paymentToPaymentEntity(payment);
		entity.setReservatonId(reservation.getId());
		paymentRepository.save(entity);
		return String.format("Payment Successful for reservation id %d", reservationId);
	}

	@Override
	public String addCardDetails(Map<String, String> headers,CardDetails cardDetails) {
		reservationValidator.getGuestIfExist(headers,cardDetails.getGuestId());
		var entity = reservationMapper.cardToCardStoreEntity(cardDetails);
		cardStoreRepository.save(entity);
		return String.format("Card details stored successfully for guest id %d", entity.getGuestId());
	}

}
