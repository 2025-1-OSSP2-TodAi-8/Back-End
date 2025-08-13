package com.todai.BE.dto.request.user;

import com.todai.BE.entity.ShareState;

import java.util.UUID;

public record HandleSharingRequestDTO(
        UUID sharingId,
        String action
) {
}
