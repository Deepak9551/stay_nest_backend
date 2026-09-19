package com.staynest.staynest_backend.mapper;

import com.staynest.staynest_backend.dto.HotelDto;
import com.staynest.staynest_backend.entity.Hotel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    HotelDto toDto(Hotel hotel);

    Hotel toEntity(HotelDto hotelDto);

    // Update existing entity ( handle patch update )
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHotel(HotelDto dto, @MappingTarget Hotel hotel);
}
