package com.staynest.staynest_backend.dto;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
