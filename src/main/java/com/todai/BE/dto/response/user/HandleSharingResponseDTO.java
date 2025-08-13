package com.todai.BE.dto.response.user;

public record HandleSharingResponseDTO(
        String message
) {
    public static HandleSharingResponseDTO of(String message) {
        return new HandleSharingResponseDTO(message);
    }
}
