package com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.entity.RoomCategoryEntity;

/**
 * this Repository class is used to communicate with RoomCategory entity
 * 
 * @author Sovan_Mukherjee
 *
 */
@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategoryEntity, Long> {

}
