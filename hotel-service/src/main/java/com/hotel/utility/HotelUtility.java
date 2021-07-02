package com.hotel.utility;

import com.hotel.model.response.ApiResponseData;
/**
 * this is Utility class contains utility methods
 * 
 * @author Sovan_Mukherjee
 *
 * @param <T>
 */
public class HotelUtility {

	private HotelUtility() {}
	public static <T> T getResponseData(ApiResponseData<?> response,Class<T> cls) {
			return cls.cast(response.getData());
	}
	
}
