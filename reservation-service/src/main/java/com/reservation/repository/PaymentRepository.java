package com.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.entity.PaymentEntity;

/**
 * this Repository class is used to communicate with Payment db
 * 
 * @author Sovan_Mukherjee
 *
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
	List<PaymentEntity> findAllByReservatonId(long reservatonId);
}
