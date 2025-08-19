package com.todai.BE.dto.response.user;

import java.time.LocalDate;

public record EmotionContinueDTO(
        int days,
        LocalDate startDate, //시작일
        LocalDate endDate,   //종료일
        boolean endIsToday

) {
}
