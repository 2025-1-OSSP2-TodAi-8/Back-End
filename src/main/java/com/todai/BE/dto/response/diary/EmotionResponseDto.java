package com.todai.BE.dto.response.diary;

import java.util.List;

public record EmotionResponseDto(
        String emotion,
        List<Double> emotionRate,
        String summary,
        boolean isMarking
) {}
