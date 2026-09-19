package com.staynest.staynest_backend.services;


import com.staynest.staynest_backend.dto.HotelDto;
import com.staynest.staynest_backend.dto.HotelInfo;

public interface HotelService {

    HotelDto createNewHotel(HotelDto create_hotel_request);

    HotelDto getHotel(Long hotel_id);

    HotelDto update_hotel(Long hotel_id , HotelDto update_hotel_request);

    void delete_hotel(Long hotel_id);

    void active_hotel(Long hotel_id);

    HotelInfo get_hotel_info(Long hotel_id);
}
