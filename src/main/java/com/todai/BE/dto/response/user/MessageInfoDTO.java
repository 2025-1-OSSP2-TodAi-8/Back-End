package com.todai.BE.dto.response.user;

import java.util.UUID;

public record MessageInfoDTO(
        UUID messageId,
        String guardianName,
        boolean isRead
) {

}
