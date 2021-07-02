package com.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.entity.ReservationEntity;
/**
 * this Repository class is used to communicate with Reservation db
 * 
 * @author Sovan_Mukherjee
 *
 */
@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long>{
	
	List<ReservationEntity> findAllByCheckInDateGreaterThanEqualAndCheckOutDateLessThanEqualAndStatus(LocalDate checkInDate, LocalDate checkOutDate,String status);

	List<ReservationEntity> findAllByStatusIn(List<String> status);
	
	ReservationEntity findByReservationNo(String  reservationNo);
}
