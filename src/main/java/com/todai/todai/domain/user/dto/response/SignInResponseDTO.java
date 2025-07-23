package com.todai.todai.domain.user.dto.response;

public record SignInResponseDTO(
        String accessToken,
        String refreshToken
) {
}
