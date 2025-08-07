package com.todai.BE.service;

import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import com.todai.BE.dto.response.user.*;
import com.todai.BE.entity.ShareRange;
import com.todai.BE.entity.ShareState;
import com.todai.BE.entity.Sharing;
import com.todai.BE.entity.User;
import com.todai.BE.repository.SharingRepository;
import com.todai.BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

        return MyPageResponseDTO.of(user.getShareCode(), user.getName(), sharingInfoList, notificationList);
    }

    //사용자 유저네임(영문 아이디) 검색 메소드
    public SearchUserResponseDTO searchUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return SearchUserResponseDTO.from(user.get());
        } else {
            return SearchUserResponseDTO.notFound();
        }

    }

    //보호자 -> 사용자 연동 요청 메소드
    public SharingResponseDTO handleSharingRequest(UUID requesterId, String targetUsername) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        User target = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (sharingRepository.existsByOwnerAndSharedWithAndShareState(target, requester, ShareState.UNMATCHED)) {
            throw new CustomException(ErrorCode.ALREADY_REQUESTED_SHARING);
        }

        if (sharingRepository.existsByOwnerAndSharedWithAndShareState(target, requester, ShareState.MATCHED)) {
            throw new CustomException(ErrorCode.ALREADY_MATCHED_SHARING);
        }

        Sharing sharing = Sharing.builder()
                .owner(target)
                .sharedWith(requester)
                .shareRange(ShareRange.PARTIAL) // 기본값
                .shareState(ShareState.UNMATCHED)
                .build();

        sharingRepository.save(sharing);

        return SharingResponseDTO.from(target.getUserId());
    }

    //연동 요청 수락/거절 메소드
    /*@Transactional
    public HandleSharingResponseDTO handleSharingAction(UUID userId, Long sharingId, String action) {
        if (!action.equals("accept") && !action.equals("reject")) {
            throw new CustomException(ErrorCode.INVALID_REQUEST_PARAMETER);
        }

        //사용자 조회
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Sharing sharing = sharingRepository.findByIdAndOwner(sharingId, owner)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHARING));


        // 상태가 UNMATCHED가 아닐 경우 -> 상태에 따라 다른 에러코드 반환
        if (sharing.getShareState() != ShareState.UNMATCHED) {
            if (sharing.getShareState() == ShareState.MATCHED) {
                throw new CustomException(ErrorCode.ALREADY_ACCEPTED_SHARING);
            } else {
                throw new CustomException(ErrorCode.ALREADY_REJECTED_SHARING);
            }
        }
        //상태가 UNMATCHED 인 경우
        else {
            if (action.equals("accept")) {
                sharing.accept();
            } else {
                sharing.reject();
            }

            return HandleSharingResponseDTO.of("연동 요청이 " + action + "되었습니다.");
        }
    }*/




}
