package com.todai.todai.domain.diary.controller;

import com.todai.todai.domain.diary.dto.request.CreateDiaryDto;
import com.todai.todai.domain.diary.entity.Diary;
import com.todai.todai.domain.diary.service.DiaryService;
import com.todai.todai.global.common.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.todai.todai.security.CustomUserDetails;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    @Autowired
    private final DiaryService diaryService;

    @GetMapping("/get_emotion_month/{yearMonth}")
    public CommonResponseDto<?> getMonthEmotion(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("yearMonth")
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth){
        return CommonResponseDto.ok(diaryService.getMonthEmotion(user.getUserId(), yearMonth));
    }

    @GetMapping("/get_emotion_year/{year}")
    public CommonResponseDto<?> getYearEmotion(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("year")
            @DateTimeFormat(pattern = "yyyy") Year year){
        return CommonResponseDto.ok(diaryService.getYearEmotion(user.getUserId(), year));
    }

    @GetMapping("/get_emotion_day/{date}")
    public CommonResponseDto<?> getDayEmotion(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return CommonResponseDto.ok(diaryService.getDayEmotion(user.getUserId(), date));
    }

//    @PostMapping("/diary/record")
//    public CommonResponseDto<?> recordDiary(
//            @AuthenticationPrincipal CustomUserDetails user,
//            @RequestBody CreateDiaryDto diaryDto
//    ){
//        return CommonResponseDto.ok(diaryService.recordDiary(user.getUserId(), diaryDto));
//    }
}
