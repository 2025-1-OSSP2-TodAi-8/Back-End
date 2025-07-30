package com.todai.todai.domain.diary.dto.response;

import java.util.List;

public record EmotionResponseDto(
        String emotion,
        List<Double> emotionRate,
        String summary
) {}
