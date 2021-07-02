package com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.entity.HotelEntity;
/**
 * this Repository class is used to communicate with hotel entity
 * 
 * @author Sovan_Mukherjee
 *
 */
@Repository
public interface HotelRepository  extends JpaRepository<HotelEntity, Long> {
}
