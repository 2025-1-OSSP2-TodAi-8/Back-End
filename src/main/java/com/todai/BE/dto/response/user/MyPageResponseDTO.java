package com.todai.BE.dto.response.user;

import java.util.List;
import java.util.UUID;

public record MyPageResponseDTO(
        String userCode,  //공유코드
        String name,  //유저 이름
        List<SharingInfoDTO> sharing,  //연동된 사용자 정보들 담아서 응답
        List<SharingWaitingDTO> notification, //연동 요청이 온 것들 알림 표시하기 위해서 사용
        List<MessageInfoDTO> messages
) {
    public static MyPageResponseDTO of(String shareCode, String name,
                                       List<SharingInfoDTO> sharing,
                                       List<SharingWaitingDTO> notification,
                                       List<MessageInfoDTO> messages) {
        return new MyPageResponseDTO(shareCode, name, sharing, notification, messages);
    }
}
