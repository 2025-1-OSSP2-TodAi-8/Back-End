package com.todai.BE.service;

import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import com.todai.BE.dto.request.user.TargetEmotionRequestDTO;
import com.todai.BE.dto.response.diary.EmotionsResponseDto;
import com.todai.BE.dto.response.user.TargetEmotionsResponseDTO;
import com.todai.BE.entity.Diary;
import com.todai.BE.entity.ShareState;
import com.todai.BE.entity.Sharing;
import com.todai.BE.repository.DiaryRepository;
import com.todai.BE.repository.SharingRepository;
import com.todai.BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.todai.BE.entity.User;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GuardianService {

    private final DiaryRepository diaryRepository;
    private final SharingRepository sharingRepository;
    private final UserRepository userRepository;

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


        List<Diary> diaries = diaryRepository.findAllByUser_UserIdAndDateBetween(targetId, start, end);

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


    public TargetEmotionsResponseDTO countEmotion(List<EmotionsResponseDto> emotionList) {
        Map<String, Long> countMap = emotionList.stream()
                .map(EmotionsResponseDto::emotion) // record이므로 .emotion() 사용
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        int happy    = countMap.getOrDefault("행복", 0L).intValue();
        int sadness  = countMap.getOrDefault("슬픔", 0L).intValue();
        int anger    = countMap.getOrDefault("화남", 0L).intValue();
        int surprise = countMap.getOrDefault("놀람", 0L).intValue();
        int fear     = countMap.getOrDefault("공포", 0L).intValue();
        int disgust  = countMap.getOrDefault("혐오", 0L).intValue();
        int unknown  = countMap.getOrDefault("알수없음", 0L).intValue();

        return new TargetEmotionsResponseDTO(
                emotionList, // 참조 그대로 전달
                happy, sadness, anger, surprise, fear, disgust, unknown,
                emotionList.size()
        );
    }

    public


}
