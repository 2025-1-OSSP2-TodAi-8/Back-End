package com.todai.BE.dto.response.diary;

public record EmotionSummaryResponseDto(
        String date,
        String emotion,
        String summary
) {
}
