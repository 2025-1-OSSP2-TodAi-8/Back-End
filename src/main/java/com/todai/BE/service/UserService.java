package com.todai.BE.service;

import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import com.todai.BE.dto.request.user.UpdateShowRangeRequestDTO;
import com.todai.BE.dto.response.user.*;
import com.todai.BE.entity.*;
import com.todai.BE.repository.MessageRepository;
import com.todai.BE.repository.SharingNotificationRepository;
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
    private final SharingNotificationRepository sharingNotificationRepository;
    private final MessageRepository messageRepository;

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

        // 메세지 데이터들
        List<Message> allMessages = messageRepository.findBySharing_Owner(user);
        List<MessageInfoDTO> messages = new ArrayList<>();
        for (Message message : allMessages) {
            String guardianName = message.getSharing().getSharedWith().getName();
            boolean is_read = message.getIsRead();
            messages.add(new MessageInfoDTO(message.getId(), guardianName, is_read));
        }

        return MyPageResponseDTO.of(user.getUserCode(), user.getName(), sharingInfoList, notificationList, messages);
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
            
            //연동 수락/거절 알림 테이블에 데이터 추가
            SharingNotification notification = SharingNotification.builder()
                    .sharing(sharing)
                    .receiver(sharing.getSharedWith())
                    .state(sharing.getShareState())   //accept → MATCHED, reject → REJECTED
                    .isRead(false)
                    .build();

            sharingNotificationRepository.save(notification);

            return HandleSharingResponseDTO.of("연동 요청이 " + action + "되었습니다.");
        }
    }

    //공개범위 수정
    @Transactional
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

    //메세지 단일조회
    @Transactional
    public GetMessageResponseDTO getMessage(UUID userId, UUID messageId) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Message message = messageRepository.findByIdAndSharing_Owner(messageId, receiver)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MESSAGE));

        // 메시지 읽음 처리
        if (!message.getIsRead()) {
            message.updateTrue();
        }

        return GetMessageResponseDTO.from(message);
    }



}
