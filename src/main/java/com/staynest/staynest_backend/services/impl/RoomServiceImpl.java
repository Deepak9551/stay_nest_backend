package com.staynest.staynest_backend.services.impl;

import com.staynest.staynest_backend.dto.RoomDto;
import com.staynest.staynest_backend.entity.Hotel;
import com.staynest.staynest_backend.entity.Room;
import com.staynest.staynest_backend.exception.ResourceNotFound;
import com.staynest.staynest_backend.mapper.RoomMapper;
import com.staynest.staynest_backend.repository.HotelRepository;
import com.staynest.staynest_backend.repository.RoomRepository;
import com.staynest.staynest_backend.services.InventoryService;
import com.staynest.staynest_backend.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.staynest.staynest_backend.advice.ErrorCode.HOTEL_NOT_FOUND;
import static com.staynest.staynest_backend.advice.ErrorCode.ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final RoomMapper roomMapper;

    @Override
    public RoomDto create_room(Long hotel_id,RoomDto roomDto) {

        log.info("Trying to create  room in hotel with Id :"+hotel_id);

        var hotel = check_hotel_exist(hotel_id);

      Room room = roomMapper.toEntity(roomDto);
        room.setHotel(hotel);

        roomRepository.save(room);
        log.info("Room Creation completed with hotel Id :"+hotel_id);

        // Create Inventory for room ( if hotel is active )
        if(hotel.getActive()){
            inventoryService.initialize_rooms_for_a_year(room);
        }
        return roomMapper.toDto(room);
    }


    @Override
    public List<RoomDto> get_all_hotel_rooms(Long hotelId) {
        log.info("Trying to Getting all  room  with Id :"+hotelId);
        Hotel hotel = check_hotel_exist(hotelId);
        List<Room> rooms = hotel.getRoom();

        return roomMapper.room_dto_list(rooms);
    }

    @Override
    public RoomDto get_room_by_Id(Long room_Id) {
        log.info("Trying to Getting  room  with Id :"+room_Id);

        Room room = roomRepository.findById(room_Id)
                .orElseThrow(() -> new ResourceNotFound("room", "room_Id", ROOM_NOT_FOUND));
        
                
        log.info("Fetching Room  Completed  with room Id :"+room_Id);
        return roomMapper.toDto(room);
    }

    @Override
    public RoomDto update_room(Long hotel_Id,RoomDto roomDto) {

        return null;
    }

    @Override
    public void delete_room(Long hotel_Id,Long room_Id) {
        log.info("Trying to Getting  hotel  with Id :"+hotel_Id);
   var hotel = check_hotel_exist(hotel_Id);
        log.info("Trying to deleting  room  with Id :"+room_Id);
        roomRepository.deleteById(room_Id);
        Room room = hotel.getRoom().stream()
                .filter(r -> r.getId().equals(room_Id)).findFirst().get();
        inventoryService.delete_inventories(room);
    }

    private Hotel check_hotel_exist(Long hotel_Id){


      return hotelRepository.findById(hotel_Id)
                .orElseThrow(()-> new ResourceNotFound("hotel","hotel_id", HOTEL_NOT_FOUND));

    }
}
