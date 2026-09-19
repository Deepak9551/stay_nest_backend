package com.staynest.staynest_backend.mapper;

import com.staynest.staynest_backend.dto.HotelDto;
import com.staynest.staynest_backend.dto.RoomDto;
import com.staynest.staynest_backend.entity.Hotel;
import com.staynest.staynest_backend.entity.Room;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {


    RoomDto toDto(Room room);

    Room toEntity(RoomDto roomDto);

    // Update existing entity ( handle patch update )
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHotel(RoomDto dto, @MappingTarget Room room);

    List<RoomDto> room_dto_list(List<Room> rooms);
}