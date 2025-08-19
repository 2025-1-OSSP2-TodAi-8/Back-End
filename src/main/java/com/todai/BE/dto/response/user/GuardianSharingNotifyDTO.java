package com.todai.BE.dto.response.user;

import com.todai.BE.entity.ShareState;

public record GuardianSharingNotifyDTO(
        String targetName,
        ShareState state
) {
}
