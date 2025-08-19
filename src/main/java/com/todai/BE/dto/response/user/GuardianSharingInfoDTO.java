package com.todai.BE.dto.response.user;

import com.todai.BE.entity.ShareRange;
import com.todai.BE.entity.Sharing;
import com.todai.BE.entity.User;

import java.util.List;
import java.util.UUID;

public record GuardianSharingInfoDTO(
        UUID targetUserId,
        String targetName,
        String showRange,
        EmotionContinueDTO negEmotion //없으면 null
) {
    public static GuardianSharingInfoDTO of(
            User target, Sharing sharing, EmotionContinueDTO neg) {
        return new GuardianSharingInfoDTO(
                target.getUserId(),
                target.getName(),
                sharing.getShareRange().name().toLowerCase(),
                neg                                   // negEmotion (없으면 null)
        );
    }
}
