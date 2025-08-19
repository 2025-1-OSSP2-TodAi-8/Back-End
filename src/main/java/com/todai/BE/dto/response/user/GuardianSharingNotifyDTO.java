package com.todai.BE.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.todai.BE.entity.ShareState;

import java.util.UUID;

public record GuardianSharingNotifyDTO(
        @JsonProperty("sharing_notification_id") UUID sharingNotificationId,
        String targetName,
        ShareState state
) {
}
