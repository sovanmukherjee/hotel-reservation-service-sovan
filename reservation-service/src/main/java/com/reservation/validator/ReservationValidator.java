package com.reservation.validator;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

import com.reservation.config.MessageConfigValues;
import com.reservation.entity.CardStoreEntity;
import com.reservation.entity.ReservationEntity;
import com.reservation.exception.BusinessException;
import com.reservation.model.response.ApiResponseData;
import com.reservation.proxy.GuestClient;
import com.reservation.proxy.HotelClient;
import com.reservation.proxy.model.guest.Guest;
import com.reservation.proxy.model.hotel.Hotel;
import com.reservation.repository.CardStoreRepository;
import com.reservation.repository.ReservationRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
/**
 * this class is used to validate related with reservation. if validation failed
 * it will throw exception
 * 
 * @author Sovan_Mukherjee
 *
 */
@Slf4j
@Component
public class ReservationValidator {
    private static final String CIRCUIT_BREAKER_ID = "circuitbreakerid";

	@Autowired
	private GuestClient guestClient;
	@Autowired
	private HotelClient hotelClient;
	@Autowired
	private MessageConfigValues errorConfigValues;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private CardStoreRepository cardStoreRepository;
	@Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

	
	private ApiResponseData<Guest> getGuestById(Map<String, String> map, long guestId){
		return circuitBreakerFactory.create(CIRCUIT_BREAKER_ID).run(() ->guestClient.getGuestById(map,guestId),
                throwable -> getGuestByIdFallBack(map,guestId));
	}
	
	private ApiResponseData<Guest> getGuestByIdFallBack(Map<String, String> map, long guestId){
		log.info("In getGuestByIdFallBack method. Guest Id: {}",guestId);
		return new ApiResponseData<>();
	}

	public Guest getGuestIfExist(Map<String, String> map, long guestId) {
		ApiResponseData<Guest> apiResponseData = getGuestById(map,guestId);
		var guest = apiResponseData.getData();
		if (null != guest) {
			return guest;
		} else {
			throw new BusinessException(errorConfigValues.getErrorConfig().getGuestNotFoundErrorCode(), String.format(
					errorConfigValues.getErrorConfig().getGuestNotFoundErrorMessasge(), String.valueOf(guestId)));

		}
	}
	
	@CircuitBreaker(name = "hrsClient",fallbackMethod = "getHotelRoomByIdFallBack")
	private ApiResponseData<Hotel> getHotelRoomById(Map<String, String> map, long hotelId, long roomId){
		return hotelClient.getHotelRoomById(map,hotelId, roomId);
	}
	
	private ApiResponseData<Hotel> getHotelRoomByIdFallBack(Map<String, String> map, long hotelId, long roomId, Throwable t){
		return new ApiResponseData<>();
	}

	public Hotel getHotelRoomIfExist(Map<String, String> map, long hotelId, long roomId) {
		ApiResponseData<Hotel> apiResponseData = hotelClient.getHotelRoomById(map,hotelId, roomId);
		var hotel = apiResponseData.getData();
		if (null != hotel) {
			return hotel;
		} else {
			throw new BusinessException(errorConfigValues.getErrorConfig().getHotelRoomNotFoundErrorCode(),
					String.format(errorConfigValues.getErrorConfig().getHotelRoomNotFoundErrorMessage(),
							String.valueOf(hotelId), String.valueOf(roomId)));

		}

	}

	public ReservationEntity getReservationIfExist(Map<String, String> headers,long reservationId) {
		Optional<ReservationEntity> reservation = reservationRepository.findById(reservationId);
		if (reservation.isPresent()) {
			return reservation.get();
		} else {
			throw new BusinessException(errorConfigValues.getErrorConfig().getReservationNotFoundErrorCode(),
					String.format(errorConfigValues.getErrorConfig().getReservationFoundErrorMessage(),
							String.valueOf(reservationId)));

		}
	}

	public CardStoreEntity getCardStoreIfExist(Map<String, String> headers,long cardStoreId) {
		Optional<CardStoreEntity> cardStore = cardStoreRepository.findById(cardStoreId);
		if (cardStore.isPresent()) {
			return cardStore.get();
		} 
//			else {
//			throw new BusinessException(errorConfigValues.getErrorConfig().getCardStoreNotFoundErrorCode(),
//					String.format(errorConfigValues.getErrorConfig().getCardStoreFoundErrorMessage(),
//							String.valueOf(cardStoreId)));
			return null;
		}

	public ReservationEntity getReservationIfExistByReservationNo(Map<String, String> headers, String reservationNo) {
		ReservationEntity reservation = reservationRepository.findByReservationNo(reservationNo);
		if (null != reservationNo) {
			return reservation;
		} else {
			throw new BusinessException(errorConfigValues.getErrorConfig().getReservationNotFoundErrorCode(),
					String.format(errorConfigValues.getErrorConfig().getReservationFoundErrorMessage(),
							String.valueOf(reservationNo)));

		}
	}
			

}
