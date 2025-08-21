package com.todai.BE.dto.response.user;

import com.todai.BE.entity.Gender;
import com.todai.BE.entity.UserType;

public record SignInResponseDTO(
        String accessToken,
        String refreshToken,
        UserType userType,
        Gender gender
) {
}
