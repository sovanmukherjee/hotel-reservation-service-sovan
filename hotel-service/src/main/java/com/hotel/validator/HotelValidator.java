package com.hotel.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hotel.config.MessageConfigValues;
import com.hotel.entity.HotelEntity;
import com.hotel.entity.RoomCategoryEntity;
import com.hotel.entity.RoomEntity;
import com.hotel.exception.BusinessException;
import com.hotel.model.Room;
import com.hotel.repository.HotelRepository;
import com.hotel.repository.RoomCategoryRepository;
import com.hotel.repository.RoomRepository;
/**
 * this class is used to validate existing hotel,room, category. if validation failed it will throw exception
 * 
 * @author Sovan_Mukherjee
 *
 */
@Component
public class HotelValidator {
	@Autowired
	private HotelRepository hotelRepository;
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private RoomCategoryRepository roomCategoryRepository;
	@Autowired
	private MessageConfigValues errorConfigValues;
	
	/**
	 * get hotel from repository by hotel id and if hotel does not exist throw exception
	 * 
	 * @param hotelId
	 * @return HotelEntity
	 */
	public HotelEntity getHotelIfExist(final long hotelId) {
		Optional<HotelEntity> entity = hotelRepository.findById(hotelId);
		if (entity.isPresent()) {
			return entity.get();
		}else {
			throw  new BusinessException(errorConfigValues.getErrorConfig().getHotelNotFoundErrorcode(),
					String.format(errorConfigValues.getErrorConfig().getHotelNotFoundErrorMessage(),"id",String.valueOf(hotelId)));
		}
	}
	
	/**
	 * get room from room repository by room id and if room does not exist throw exception
	 * 
	 * @param roomId
	 * @return RoomEntity
	 */
	public RoomEntity getRoomIfExist(final long roomId) {
		Optional<RoomEntity> roomEntity = roomRepository.findById(roomId);
		if (roomEntity.isPresent()) {
			return roomEntity.get();
		}else {
			throw  new BusinessException(errorConfigValues.getErrorConfig().getHotelNotFoundErrorcode(),
					String.format(errorConfigValues.getErrorConfig().getHotelNotFoundErrorMessage(),"room id",String.valueOf(roomId)));
		}
	}

	/**
	 * if room category exist for a room then return the room category otherwise throw exception 
	 * 
	 * @param room
	 * @return RoomCategoryEntity
	 */
	public RoomCategoryEntity getRoomCategoryIfExist(Room room) {
		if(null != room.getCategory()) {
			Optional<RoomCategoryEntity> roomCategoryEntity = roomCategoryRepository.findById(room.getCategory().getId());
			if (roomCategoryEntity.isPresent()) {
				return roomCategoryEntity.get();
			}else {
				throw  new BusinessException(errorConfigValues.getErrorConfig().getHotelNotFoundErrorcode(),
						String.format(errorConfigValues.getErrorConfig().getHotelNotFoundErrorMessage(),"room category id",String.valueOf(room.getCategory().getId())));
			}
		}else {
			throw  new BusinessException(errorConfigValues.getErrorConfig().getHotelInfoErrorCode(),
					String.format(errorConfigValues.getErrorConfig().getHotelInfoErrorMessage(),"room category"));
		}
		
	}
}
