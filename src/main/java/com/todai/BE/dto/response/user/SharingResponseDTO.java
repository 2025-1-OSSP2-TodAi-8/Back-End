package com.todai.BE.dto.response.user;

import java.util.UUID;

public record SharingResponseDTO (
        UUID targetUserId
)
{
    public static SharingResponseDTO from(UUID targetUserId) {
        return new SharingResponseDTO(targetUserId);
    }
}
