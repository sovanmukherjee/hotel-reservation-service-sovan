package com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.entity.RoomEntity;

/**
 * this Repository class is used to communicate with Room entity
 * 
 * @author Sovan_Mukherjee
 *
 */
@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
	public RoomEntity findByIdAndHotelId(long hotelId,long roomId);
}

