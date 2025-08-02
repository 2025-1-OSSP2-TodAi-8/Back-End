package com.todai.BE.dto.response.user;

import com.todai.BE.entity.Sharing;

import java.util.UUID;

public record SharingInfoDTO(
        UUID protectorId,
        String protectorName,
        String showRange
) {
    public static SharingInfoDTO from(Sharing sharing) {
        return new SharingInfoDTO(
                sharing.getSharedWith().getUserId(),
                sharing.getSharedWith().getName(),
                sharing.getShareRange().name().toLowerCase() // enum을 api 명세서대로 소문자 문자열로
        );
    }

}
