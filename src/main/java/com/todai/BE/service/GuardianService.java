package com.todai.BE.service;

import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import com.todai.BE.dto.request.user.TargetEmotionRequestDTO;
import com.todai.BE.dto.response.diary.EmotionsResponseDto;
import com.todai.BE.dto.response.user.*;
import com.todai.BE.entity.Diary;
import com.todai.BE.entity.ShareState;
import com.todai.BE.entity.Sharing;
import com.todai.BE.repository.DiaryRepository;
import com.todai.BE.repository.SharingNotificationRepository;
import com.todai.BE.repository.SharingRepository;
import com.todai.BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.todai.BE.entity.User;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GuardianService {

    private final DiaryRepository diaryRepository;
    private final SharingRepository sharingRepository;
    private final UserRepository userRepository;
    private final SharingNotificationRepository sharingNotificationRepository;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    //누적 부정 감정 계산용
    private static final Set<String> NEG_LABELS = Set.of("슬픔", "화남", "혐오");

    private static final int MIN_CONTINUE_DAYS = 2;   //부정 감정 연속 일수 지정
    private static final int LOOKBACK_DAYS   = 30;  //부정 감정 조회 범위 지정

    private String mapToLabel(List<Double> emotion) {
        int idx = IntStream.range(0, emotion.size())
                .reduce((i, j) -> emotion.get(i) >= emotion.get(j) ? i : j)
                .orElse(0);

        return switch (idx) {
            case 0 -> "행복";
            case 1 -> "슬픔";
            case 2 -> "화남";
            case 3 -> "놀람";
            case 4 -> "공포";
            case 5 -> "혐오";
            default -> "알수없음";
        };
    }

    private EmotionContinueDTO getContinuousNegativeEmotionInfo(List<Diary> diaries) {
        if (diaries.isEmpty()) {
            return null;
        }

        int continueDays = 0;
        LocalDate startDate = null;
        LocalDate endDate = null;

        for (int i = 0; i < diaries.size(); i++) {
            Diary currentDiary = diaries.get(i);
            String currentLabel = mapToLabel(currentDiary.getEmotion());

            if (NEG_LABELS.contains(currentLabel)) {
                continueDays++;
                if (endDate == null) {
                    endDate = currentDiary.getDate();
                }
                startDate = currentDiary.getDate();

                // 다음 일기와의 연속성 확인
                if (i + 1 < diaries.size()) {
                    LocalDate nextDate = diaries.get(i + 1).getDate();
                    if (!currentDiary.getDate().minusDays(1).isEqual(nextDate)) {
                        break; // 연속이 끊어짐(일기가 하루 이상 건너뛰고 기록된 경우)
                    }
                }
            } else {
                break; //부정 감정이 아니면 연속이 끊어짐
            }
        }

        if (continueDays >= MIN_CONTINUE_DAYS) {
            return new EmotionContinueDTO(
                    continueDays,
                    startDate,
                    endDate,
                    endDate.isEqual(LocalDate.now())
            );
        }
        return null;
    }

    //보호자 마이페이지 조회
    public GuardianMyPageResponseDTO getGuardianMyPage(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //보호자 사용자와 연동된 정보들 조회
        List<Sharing> matchedSharings = sharingRepository.findBySharedWithAndShareState(user, ShareState.MATCHED);

        List<GuardianSharingInfoDTO> sharingInfo = new ArrayList<>();

        for (Sharing sharing : matchedSharings) {
            User target = sharing.getOwner();
            // LOOKBACK_DAYS 만큼의 일기 데이터만 조회
            List<Diary> diaries = diaryRepository.findByUserAndDateBetweenOrderByDateDesc(target, LocalDate.now().minusDays(LOOKBACK_DAYS), LocalDate.now());
            EmotionContinueDTO emotionContinueDTO = getContinuousNegativeEmotionInfo(diaries);
            sharingInfo.add(GuardianSharingInfoDTO.of(target, sharing, emotionContinueDTO));
        }

        //연동 수락/거절 여부 알림 리스트
        List<GuardianSharingNotifyDTO> notifyList = sharingNotificationRepository
                .findByReceiverAndIsReadFalseOrderByCreatedAtDesc(user)
                .stream()
                .map(s -> new GuardianSharingNotifyDTO(
                        s.getSharing().getOwner().getName(),
                        s.getState()
                ))
                .toList();


        return GuardianMyPageResponseDTO.from(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getBirthdate(),
                notifyList,
                sharingInfo
        );

    }

    //월별 감정 조회
    public List<EmotionsResponseDto> getTargetMonthEmotion(UUID userId, YearMonth yearMonth, TargetEmotionRequestDTO requestDTO) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end  = yearMonth.atEndOfMonth();

        //타겟 사용자 아이디
        UUID targetId = requestDTO.targetId();

        //타겟 유저 존재 여부 검증
        User owner = userRepository.findById(targetId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TARGET));

        //연동 관계 존재 여부 검증
        Sharing sharing = sharingRepository.findByOwner_UserIdAndSharedWith_UserIdAndShareState(targetId, userId, ShareState.MATCHED)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHARING));


        List<Diary> diaries = diaryRepository.findAllByUser_UserIdAndDateBetweenOrderByDateAsc(targetId, start, end);

        return diaries.stream()
                .map(diary -> {
                    List<Double> em = diary.getEmotion();
                    String label = mapToLabel(em);
                    return new EmotionsResponseDto(
                            diary.getDate().toString(),
                            label
                    );
                })
                .collect(Collectors.toList());
    }

    //감정 비율 계산
    public TargetEmotionsResponseDTO countEmotion(List<EmotionsResponseDto> emotionList) {
        Map<String, Long> countMap = emotionList.stream()
                .map(EmotionsResponseDto::emotion)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        int happy    = countMap.getOrDefault("행복", 0L).intValue();
        int sadness  = countMap.getOrDefault("슬픔", 0L).intValue();
        int anger    = countMap.getOrDefault("화남", 0L).intValue();
        int surprise = countMap.getOrDefault("놀람", 0L).intValue();
        int fear     = countMap.getOrDefault("공포", 0L).intValue();
        int disgust  = countMap.getOrDefault("혐오", 0L).intValue();
        int unknown  = countMap.getOrDefault("알수없음", 0L).intValue();

        return new TargetEmotionsResponseDTO(
                emotionList,
                happy, sadness, anger, surprise, fear, disgust, unknown,
                emotionList.size()
        );
    }




}
