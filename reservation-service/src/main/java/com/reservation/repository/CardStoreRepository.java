package com.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.entity.CardStoreEntity;
/**
 * this Repository class is used to communicate with card store db
 * 
 * @author Sovan_Mukherjee
 *
 */
@Repository
public interface CardStoreRepository  extends JpaRepository<CardStoreEntity, Long> {
}
