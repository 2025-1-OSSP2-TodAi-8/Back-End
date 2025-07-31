package com.todai.BE.controller;

import com.todai.BE.dto.request.diary.CreateDiaryDto;
import com.todai.BE.service.DiaryService;
import com.todai.BE.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.todai.BE.security.CustomUserDetails;

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

    @PostMapping(value = "/record", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseDto<?> recordDiary(
            @AuthenticationPrincipal CustomUserDetails user,
            @ModelAttribute CreateDiaryDto diaryDto
    ){
        return CommonResponseDto.ok(diaryService.recordDiary(user.getUserId(), diaryDto));
    }

    @GetMapping("/record/{date}")
    public ResponseEntity<Resource> getAudio(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ){
        Resource audio = diaryService.getAudio(user.getUserId(), date);

        String filename = audio.getFilename();
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (filename != null && filename.toLowerCase().endsWith(".wav")) {
            mediaType = MediaType.parseMediaType("audio/wav");
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(audio);
    }

    @PatchMapping("/marking/{date}")
    public CommonResponseDto<?> markDiary(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return CommonResponseDto.ok(diaryService.markDiary(user.getUserId(), date));
    }

    @GetMapping("/get_emotion_month/marked/{yearMonth}")
    public CommonResponseDto<?> getMonthEmotionMarked(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy--MM") YearMonth yearMonth
    ) {
        return CommonResponseDto.ok(diaryService.getMonthEmotionYear(user.getUserId(), yearMonth));
    }

    @GetMapping("/get_emotion_year/marked/{year}")
    public CommonResponseDto<?> getYearEmotionMarked(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy") Year year
    ) {
        return CommonResponseDto.ok(diaryService.getYearEmotionMarked(user.getUserId(), year));
    }
}
