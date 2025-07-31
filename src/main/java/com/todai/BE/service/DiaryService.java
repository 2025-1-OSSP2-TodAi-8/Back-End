package com.todai.BE.service;

import com.todai.BE.dto.response.diary.EmotionResponseDto;
import com.todai.BE.dto.response.diary.EmotionsResponseDto;
import com.todai.BE.entity.Diary;
import com.todai.BE.repository.DiaryJpaRepository;
import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DiaryService {
    @Autowired
    private final DiaryJpaRepository repo;

    public List<EmotionsResponseDto> getMonthEmotion(UUID userId, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end   = yearMonth.atEndOfMonth();

        List<Diary> diaries = repo.findAllByUser_UserIdAndDateBetween(userId, start, end).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY));

        return diaries.stream()
                .map(diary -> {
                    List<Double> em = diary.getEmotion();
                    int maxIdx = IntStream.range(0, em.size())
                            .reduce((i, j) -> em.get(i) >= em.get(j) ? i : j)
                            .orElse(0);
                    String label;
                    switch (maxIdx) {
                        case 0: label = "행복"; break;
                        case 1: label = "슬픔"; break;
                        case 2: label = "화남"; break;
                        case 3: label = "놀람"; break;
                        case 4: label = "공포"; break;
                        case 5: label = "혐오"; break;
                        default: label = "알수없음";
                    }
                    return new EmotionsResponseDto(
                            diary.getDate().toString(),
                            label
                    );
                })
                .collect(Collectors.toList());
    }

    public List<EmotionsResponseDto> getYearEmotion(UUID userId, Year year) {
        LocalDate start = year.atDay(1);
        LocalDate end = year.atMonth(12).atEndOfMonth();
        List<Diary> diaries = repo.findAllByUser_UserIdAndDateBetween(userId, start, end).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY));

        return diaries.stream()
                .map(diary -> {
                    List<Double> em = diary.getEmotion();
                    int maxIdx = IntStream.range(0, em.size())
                            .reduce((i, j) -> em.get(i) >= em.get(j) ? i : j)
                            .orElse(0);

                    String label;
                    switch (maxIdx) {
                        case 0: label = "행복"; break;
                        case 1: label = "슬픔"; break;
                        case 2: label = "화남"; break;
                        case 3: label = "놀람"; break;
                        case 4: label = "공포"; break;
                        case 5: label = "혐오"; break;
                        default: label = "알수없음";
                    }

                    return new EmotionsResponseDto(
                            diary.getDate().toString(),
                            label
                    );
                })
                .collect(Collectors.toList());
    }

    public EmotionResponseDto getDayEmotion(UUID userId, LocalDate date) {
        Diary diary = repo.findByUser_UserIdAndDate(userId, date).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY));
        List<Double> em = diary.getEmotion();
        int maxIdx = IntStream.range(0, em.size())
                .reduce((i, j) -> em.get(i) >= em.get(j) ? i : j)
                .orElse(0);

        String label;
        switch (maxIdx) {
            case 0: label = "행복"; break;
            case 1: label = "슬픔"; break;
            case 2: label = "화남"; break;
            case 3: label = "놀람"; break;
            case 4: label = "공포"; break;
            case 5: label = "혐오"; break;
            default: label = "알수없음";
        }

        return new EmotionResponseDto(label, em, diary.getSummary());
    }
}
