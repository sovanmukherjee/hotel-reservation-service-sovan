package com.guest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guest.entity.GuestEntity;
/**
 * this Repository class is used to communicate with guest entity
 * 
 * @author Sovan_Mukherjee
 *
 */
@Repository
public interface GuestRepository  extends JpaRepository<GuestEntity, Long> {
}
