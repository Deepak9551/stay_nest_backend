package com.staynest.staynest_backend.mapper;

import com.staynest.staynest_backend.dto.BookingDto;
import com.staynest.staynest_backend.dto.RoomDto;
import com.staynest.staynest_backend.entity.Booking;
import com.staynest.staynest_backend.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toDto(Booking booking);

    Booking toEntity(BookingDto bookingDto);
}
