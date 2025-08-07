package com.todai.BE.dto.request.user;

public record HandleSharingRequestDTO(
        Long sharingId,
        String action
) {
}
