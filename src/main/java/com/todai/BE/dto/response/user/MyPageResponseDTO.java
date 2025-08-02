package com.todai.BE.dto.response.user;

import java.util.List;
import java.util.UUID;

public record MyPageResponseDTO(
        UUID userId,  //유저아이디 정보
        String name,  //유저 이름
        List<SharingInfoDTO> sharing,  //연동된 사용자 정보들 담아서 응답
        List<SharingWaitingDTO> notification  //연동 요청이 온 것들 알림 표시하기 위해서 사용
) {
    public static MyPageResponseDTO of(UUID userId, String name,
                                       List<SharingInfoDTO> sharing,
                                       List<SharingWaitingDTO> notification) {
        return new MyPageResponseDTO(userId, name, sharing, notification);
    }
}
