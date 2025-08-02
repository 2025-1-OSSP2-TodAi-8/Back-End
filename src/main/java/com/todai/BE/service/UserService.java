package com.todai.BE.service;

import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import com.todai.BE.dto.response.user.MyPageResponseDTO;
import com.todai.BE.dto.response.user.SharingInfoDTO;
import com.todai.BE.dto.response.user.SharingWaitingDTO;
import com.todai.BE.entity.ShareState;
import com.todai.BE.entity.Sharing;
import com.todai.BE.entity.User;
import com.todai.BE.repository.SharingRepository;
import com.todai.BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final SharingRepository sharingRepository;
    private final UserRepository userRepository;

    public MyPageResponseDTO getMyPageInfo(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //연동된 보호자들 정보
        List<Sharing> matchedSharings = sharingRepository.findByOwnerAndShareState(user, ShareState.MATCHED);
        List<SharingInfoDTO> sharingInfoList = new ArrayList<>();
        for (Sharing sharing : matchedSharings) {
            sharingInfoList.add(SharingInfoDTO.from(sharing));
        }

        //알림용 - unmatched 상태인 데이터들
        List<Sharing> unmatchedSharings = sharingRepository.findByOwnerAndShareState(user, ShareState.UNMATCHED);
        List<SharingWaitingDTO> notificationList = new ArrayList<>();
        for (Sharing sharing : unmatchedSharings) {
            notificationList.add(SharingWaitingDTO.from(sharing));
        }

        return MyPageResponseDTO.of(user.getUserId(), user.getName(), sharingInfoList, notificationList);
    }




}
