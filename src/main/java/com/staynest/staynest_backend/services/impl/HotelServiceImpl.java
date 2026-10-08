package com.staynest.staynest_backend.services.impl;


import com.staynest.staynest_backend.dto.HotelDto;
import com.staynest.staynest_backend.dto.HotelInfo;
import com.staynest.staynest_backend.dto.RoomDto;
import com.staynest.staynest_backend.entity.Hotel;
import com.staynest.staynest_backend.entity.Room;
import com.staynest.staynest_backend.entity.User;
import com.staynest.staynest_backend.exception.ResourceNotFound;
import com.staynest.staynest_backend.exception.UnAuthorizedException;
import com.staynest.staynest_backend.mapper.HotelMapper;
import com.staynest.staynest_backend.mapper.RoomMapper;
import com.staynest.staynest_backend.repository.HotelRepository;
import com.staynest.staynest_backend.repository.RoomRepository;
import com.staynest.staynest_backend.services.HotelService;
import com.staynest.staynest_backend.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.staynest.staynest_backend.advice.ErrorCode.HOTEL_NOT_FOUND;
import static com.staynest.staynest_backend.advice.ErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {
    private final RoomRepository roomRepository;

    private final HotelRepository hotelRepository;

    private final HotelMapper hotelMapper;

    private final RoomMapper roomMapper;

    private final InventoryService inventoryService;

    @Override
    public HotelDto createNewHotel(HotelDto createHotelRequest) {

        log.info("creating hotel with name :"+createHotelRequest.name());
        Hotel hotel = hotelMapper.toEntity(createHotelRequest);
        hotel.setActive(false); // means hotel is onboarded but not add in inventory

        User owner =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(owner);
        hotel = hotelRepository.save(hotel);
        log.info("Hotel creation completed with id_ "+hotel.getId());
        return hotelMapper.toDto(hotel);
    }

    @Override
    public HotelDto getHotel(Long hotel_id) {
        log.info("Trying to find hotel with Id: "+hotel_id);
       var hotel =  hotelRepository.findById(hotel_id)
                .orElseThrow(()-> new ResourceNotFound("hotel","hotel_id", HOTEL_NOT_FOUND));
       if(!hotel.getOwner().equals(get_current_user())){
           throw new UnAuthorizedException("hotel not belong to user :"+get_current_user().getUsername() , UNAUTHORIZED);
       }
        return hotelMapper.toDto(hotel);
    }

    @Override
    public HotelDto update_hotel(Long hotel_id, HotelDto update_hotel_request) {
        log.info("Trying to update  hotel with Id: "+hotel_id);
        var hotel =  hotelRepository.findById(hotel_id)
                .orElseThrow(()-> new ResourceNotFound("hotel","hotel_id", HOTEL_NOT_FOUND));
        if(!hotel.getOwner().equals(get_current_user())){
            throw new UnAuthorizedException("hotel not belong to user :"+get_current_user().getUsername() , UNAUTHORIZED);
        }
            hotelMapper.updateHotel(update_hotel_request,hotel);
            hotel = hotelRepository.save(hotel);
        return hotelMapper.toDto(hotel);
    }

    @Override
    @Transactional
    public void delete_hotel(Long hotel_id) {

        log.info("Trying to delete  hotel with Id :"+hotel_id);
        var hotel =  hotelRepository.findById(hotel_id)
                .orElseThrow(()-> new ResourceNotFound("hotel","hotel_id", HOTEL_NOT_FOUND));

        if(!hotel.getOwner().equals(get_current_user())){
            throw new UnAuthorizedException("hotel not belong to user :"+get_current_user().getUsername() , UNAUTHORIZED);
        }
        // TODO: Delete all room of  hotel from inventory also

        for (Room room : hotel.getRoom()){

            inventoryService.delete_inventories(room);
            roomRepository.deleteById(room.getId());

        }

        hotelRepository.deleteById(hotel_id);
        log.info("Hotel deletion completed   hotel with Id :"+hotel_id);



    }

    @Override
    public void active_hotel(Long hotel_id) {

        log.info("Trying to update the status of  hotel with Id :"+hotel_id);
        var hotel =  hotelRepository.findById(hotel_id)
                .orElseThrow(()-> new ResourceNotFound("hotel","hotel_id", HOTEL_NOT_FOUND));

        if(!hotel.getOwner().equals(get_current_user())){
            throw new UnAuthorizedException("hotel not belong to user :"+get_current_user().getUsername() , UNAUTHORIZED);
        }
        hotel.setActive(true);
        log.info("Hotel status updation complete  hotel with Id :"+hotel_id);
        hotelRepository.save(hotel);
        // TODO: Create Inventory for all room of this Hotel

        hotel.getRoom()
                .forEach(inventoryService::initialize_rooms_for_a_year);

    }

    @Override
    public HotelInfo get_hotel_info(Long hotel_id) {
        log.info("Trying to find hotel with Id: "+hotel_id);
        var hotel =  hotelRepository.findById(hotel_id)
                .orElseThrow(()-> new ResourceNotFound("hotel","hotel_id", HOTEL_NOT_FOUND));

        if(!hotel.getOwner().equals(get_current_user())){
            throw new UnAuthorizedException("hotel not belong to user :"+get_current_user().getUsername() , UNAUTHORIZED);
        }
        HotelDto hotelDto = hotelMapper.toDto(hotel);
        List<RoomDto> roomDtos = roomMapper.room_dto_list(hotel.getRoom());

        return new HotelInfo(hotelDto,roomDtos);
    }

    private User get_current_user() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
