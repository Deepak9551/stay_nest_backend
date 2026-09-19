package com.staynest.staynest_backend.dto;

import java.util.List;

public record HotelInfo(
        HotelDto hotelDto,
        List<RoomDto> roomDtos
) {
}
