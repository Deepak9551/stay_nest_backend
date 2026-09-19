package com.staynest.staynest_backend.mapper;

import com.staynest.staynest_backend.dto.GuestDto;
import com.staynest.staynest_backend.entity.Guest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    Guest toEntity(GuestDto guestDto);

    GuestDto toDto(Guest guest);

}
