package com.todai.todai.domain.diary.service;

import com.todai.todai.domain.diary.dto.response.GetEmotionResponseDto;
import com.todai.todai.domain.diary.entity.Diary;
import com.todai.todai.domain.diary.repository.DiaryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DiaryService {
    @Autowired
    private final DiaryJpaRepository repo;

    public List<GetEmotionResponseDto> getMonthEmotion(Long userId, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end   = yearMonth.atEndOfMonth();

        List<Diary> diaries = repo.findAllByUser_UserIdAndDateBetween(userId, start, end);

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
                    return new GetEmotionResponseDto(
                            diary.getDate().toString(),
                            label
                    );
                })
                .collect(Collectors.toList());
    }
}
