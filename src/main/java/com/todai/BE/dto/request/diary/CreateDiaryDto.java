package com.todai.BE.dto.request.diary;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record CreateDiaryDto(
        @NotNull(message = "date를 입력해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "emotion 값을 입력해주세요.")
        @Size(min = 6, max = 6, message = "emotion 리스트는 6개여야 합니다.")
        List<@DecimalMin(value = "0.0", inclusive = true)
                @DecimalMax(value = "1.0", inclusive = true) Double> emotion,

        @NotNull(message = "summary를 입력해주세요.")
        @Size(max = 2000, message = "summary는 최대 2000자 이하로 작성 가능합니다.")
        String summary,

        MultipartFile audioFile
) {
}
