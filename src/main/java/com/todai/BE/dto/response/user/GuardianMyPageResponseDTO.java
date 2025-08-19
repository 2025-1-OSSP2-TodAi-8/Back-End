package com.todai.BE.dto.response.user;

import java.time.LocalDate;
import java.util.List;

public record GuardianMyPageResponseDTO(
        String username, //보호자 영문 아이디
        String name,  //보호자 이름
        String email,
        LocalDate birthdate,
        List<GuardianSharingNotifyDTO> sharingNotification, //연동 수락/거절 알림용
        List<GuardianSharingInfoDTO> sharingInfo  //연동된 사용자 정보
) {
    public static GuardianMyPageResponseDTO from(
            String username,
            String name,
            String email,
            LocalDate birthdate,
            List<GuardianSharingNotifyDTO> notifyInfo,
            List<GuardianSharingInfoDTO> sharingInfo
    ) {
        return new GuardianMyPageResponseDTO(username, name, email, birthdate, notifyInfo, sharingInfo);
    }
}
