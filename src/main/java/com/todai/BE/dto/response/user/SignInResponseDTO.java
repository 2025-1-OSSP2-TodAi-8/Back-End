package com.todai.BE.dto.response.user;

public record SignInResponseDTO(
        String accessToken,
        String refreshToken
) {
}
