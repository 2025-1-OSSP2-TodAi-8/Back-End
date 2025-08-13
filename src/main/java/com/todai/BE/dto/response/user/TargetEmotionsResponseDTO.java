package com.todai.BE.dto.response.user;

import com.todai.BE.dto.response.diary.EmotionsResponseDto;

import java.util.List;

public record TargetEmotionsResponseDTO(
        List<EmotionsResponseDto> emotionList,
        //감정 별 개수 추가
        int happy,
        int sadness,
        int anger,
        int surprise,
        int fear,
        int disgust,
        int unknown,
        int total
) {
}
