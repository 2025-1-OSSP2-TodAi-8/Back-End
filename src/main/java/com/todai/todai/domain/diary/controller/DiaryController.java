package com.todai.todai.domain.diary.controller;

import com.todai.todai.domain.diary.dto.response.GetEmotionResponseDto;
import com.todai.todai.domain.diary.service.DiaryService;
import com.todai.todai.global.common.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.todai.todai.security.CustomUserDetails;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    @Autowired
    private final DiaryService diaryService;

    @GetMapping("/get_emotion/{yearMonth}")
    public CommonResponseDto<?> getMonthEmotion(

            @PathVariable("yearMonth")
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
            @AuthenticationPrincipal CustomUserDetails user) {
        return CommonResponseDto.ok(diaryService.getMonthEmotion(user.getUserId(), yearMonth));
    }
}
