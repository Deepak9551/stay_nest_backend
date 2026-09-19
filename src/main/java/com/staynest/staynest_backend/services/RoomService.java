package com.staynest.staynest_backend.services;

import com.staynest.staynest_backend.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto create_room(Long hotel_id,RoomDto roomDto);

    RoomDto get_room_by_Id(Long room_Id);

    List<RoomDto> get_all_hotel_rooms(Long hotelId);

    RoomDto update_room(Long hotel_Id,RoomDto roomDto);

    void delete_room(Long hotel_Id,Long room_Id) ;
}
