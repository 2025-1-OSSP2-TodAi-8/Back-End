package com.todai.BE.dto.response.user;

import com.todai.BE.entity.Sharing;

import java.util.UUID;

public record SharingWaitingDTO(
        UUID sharingId,
        String protectorName,
        String showRange
) {
    public static SharingWaitingDTO from(Sharing sharing) {
        return new SharingWaitingDTO(
                sharing.getSharingId(), //공유관계 아이디
                sharing.getSharedWith().getName(),
                sharing.getShareRange().name().toLowerCase()
        );
    }
}
