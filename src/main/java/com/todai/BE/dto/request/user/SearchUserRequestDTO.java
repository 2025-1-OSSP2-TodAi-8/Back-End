package com.todai.BE.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SearchUserRequestDTO(
        @JsonProperty("target_user_code") String userCode
) {
}
