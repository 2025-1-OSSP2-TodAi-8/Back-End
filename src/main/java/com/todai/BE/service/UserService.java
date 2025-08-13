package com.todai.BE.service;

import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import com.todai.BE.dto.request.user.UpdateShowRangeRequestDTO;
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
@Transactional
public class UserService {

    private final SharingRepository sharingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
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

        return MyPageResponseDTO.of(user.getUserCode(), user.getName(), sharingInfoList, notificationList);
    }

    //사용자 유저네임(영문 아이디) 검색 메소드
    public SearchUserResponseDTO searchUser(String userCode) {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return SearchUserResponseDTO.from(user);
    }

    //보호자 -> 사용자 연동 요청 메소드
    @Transactional
    public void sendSharingRequest(UUID requesterId, String targetUserCode) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        User target = userRepository.findByUserCode(targetUserCode) //유저코드로 타겟 사용자 검색
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //이미 요청해서 수락 대기중인 경우
        if (sharingRepository.existsByOwnerAndSharedWithAndShareState(target, requester, ShareState.UNMATCHED)) {
            throw new CustomException(ErrorCode.ALREADY_REQUESTED_SHARING);
        }

        //이미 연동 완료된 경우
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
        //return SharingResponseDTO.from(target.getUserId());
    }

    //연동 요청 수락/거절 메소드
    @Transactional
    public HandleSharingResponseDTO handleSharingAction(UUID userId, UUID sharingId, String action) {
        if (!action.equals("accept") && !action.equals("reject")) {
            throw new CustomException(ErrorCode.INVALID_REQUEST_PARAMETER);
        }

        //사용자 조회
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        //해당 사용자가 owner이고 sharingId가 일치하는 데이터 찾기
        Sharing sharing = sharingRepository.findByOwnerAndSharingId(owner, sharingId)
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
    }

    //공개범위 수정
    public UpdateShowRangeResponseDTO updateShowRange(UUID userId, UpdateShowRangeRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        User guardian = userRepository.findById(requestDTO.guardianId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GUARDAIN));

        Sharing sharing = sharingRepository.findByOwnerAndSharedWith(user, guardian)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHARING));

        sharing.updateShareRange(requestDTO.showRange());

        return new UpdateShowRangeResponseDTO(guardian.getUserId(), sharing.getShareRange());

    }



}
