package com.hotel.mapper;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import com.hotel.entity.HotelEntity;
import com.hotel.entity.RoomEntity;
import com.hotel.model.Hotel;
import com.hotel.model.Room;

@Mapper(componentModel = "spring")
public interface HotelMapper {

	public Hotel hotelEntityToHotel(HotelEntity entity);

	public HotelEntity hotelToHotelEntity(Hotel hotel);

	public abstract List<Hotel> hotelEntitiesToHotels(Collection<HotelEntity> entities);

	public RoomEntity roomToRoomEntity(Room room);
}
