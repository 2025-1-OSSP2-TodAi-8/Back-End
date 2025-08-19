package com.todai.BE.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record GetMessageRequestDTO(
        @JsonProperty("message_id") UUID messageId
) {
}
