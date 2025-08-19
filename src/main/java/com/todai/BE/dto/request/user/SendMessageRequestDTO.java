package com.todai.BE.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record SendMessageRequestDTO(
        @JsonProperty("target_user_id") UUID targetId,
        String message
) {
}
