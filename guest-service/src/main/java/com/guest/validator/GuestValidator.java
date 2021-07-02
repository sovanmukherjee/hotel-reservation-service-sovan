package com.guest.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guest.config.MessageConfigValues;
import com.guest.entity.GuestEntity;
import com.guest.exception.BusinessException;
import com.guest.repository.GuestRepository;


/**
 * this class is used to validate existing guest. if validation failed it will throw exception
 * 
 * @author Sovan_Mukherjee
 *
 */
@Component
public class GuestValidator {
	@Autowired
	private GuestRepository guestRepository;
	@Autowired
	private MessageConfigValues errorConfigValues;
	
	/**
	 * get Guest from repository by guest id and if guest does not exist throw exception
	 * 
	 * @param guestlId
	 * @return GuestEntity
	 */
	public GuestEntity getGuestIfExist(final long guestlId) {
		Optional<GuestEntity> entity = guestRepository.findById(guestlId);
		if (entity.isPresent()) {
			return entity.get();
		}else {
			throw  new BusinessException(errorConfigValues.getErrorConfig().getGuestNotFoundErrorCode(),
					String.format(errorConfigValues.getErrorConfig().getGuestNotFoundErrorMessage(),"id",String.valueOf(guestlId)));
		}
	}
	
	
}
