package com.todai.BE.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ReadSharingNotifyRequestDTO(
        @JsonProperty("sharing_notification_id") UUID sharingNotificationId
) {
}
