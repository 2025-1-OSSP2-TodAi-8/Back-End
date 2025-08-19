package com.todai.BE.dto.response.user;

import com.todai.BE.entity.Message;

public record GetMessageResponseDTO(
        String guardianName,
        String content
) {
    public static GetMessageResponseDTO from(Message message) {
        return new GetMessageResponseDTO(
                message.getUser().getName(),
                message.getContent()
        );
    }
}
