package com.todai.BE.controller;

import com.todai.BE.common.dto.CommonResponseDto;
import com.todai.BE.dto.request.user.HandleSharingRequestDTO;
import com.todai.BE.dto.request.user.SearchUserRequestDTO;
import com.todai.BE.dto.request.user.TargetEmotionRequestDTO;
import com.todai.BE.dto.request.user.UpdateShowRangeRequestDTO;
import com.todai.BE.dto.response.diary.EmotionsResponseDto;
import com.todai.BE.dto.response.user.*;
import com.todai.BE.security.CustomUserDetails;
import com.todai.BE.service.GuardianService;
import com.todai.BE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/people")
public class UserController {

    private final UserService userService;
    private final GuardianService guardianService;

    @GetMapping("/my")
    public CommonResponseDto<?> getMyPageInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getUserId();
        MyPageResponseDTO response = userService.getMyPageInfo(userId);
        return CommonResponseDto.ok(response);
    }

    @PostMapping("/search")
    public CommonResponseDto<?> searchUserByUsername(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SearchUserRequestDTO request
    ) {
        String userCode = request.userCode();
        SearchUserResponseDTO response = userService.searchUser(userCode);
        return CommonResponseDto.ok(response);
    }

    @PostMapping("/sharing/request")
    public CommonResponseDto<?> sendSharingRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SearchUserRequestDTO requestDTO
    ) {
        userService.sendSharingRequest(
                userDetails.getUserId(),
                requestDTO.userCode()
        );

        return CommonResponseDto.ok("연동 요청 성공");
    }

    @PostMapping("/sharing/handle")
    public CommonResponseDto<?> handleSharingAction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody HandleSharingRequestDTO requestDTO
    ) {
        HandleSharingResponseDTO response = userService.handleSharingAction(
                userDetails.getUserId(),
                requestDTO.sharingId(),
                requestDTO.action()
        );

        return CommonResponseDto.ok(response);
    }

    @PostMapping("/update/showrange")
    public CommonResponseDto<?> updateShowRange(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateShowRangeRequestDTO requestDTO
    ){
        UpdateShowRangeResponseDTO response = userService.updateShowRange(
                userDetails.getUserId(),
                requestDTO
        );
        return CommonResponseDto.ok(response);
    }

    @PostMapping("/sharing/month/{yearMonth}")
    public CommonResponseDto<?> getTargetMonthEmotion(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("yearMonth")
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
            @RequestBody TargetEmotionRequestDTO requestDTO
            ){

        //타겟 사용자 감정 리스트 조회
        List<EmotionsResponseDto> emotionList = guardianService.getTargetMonthEmotion(user.getUserId(), yearMonth, requestDTO);

        //개수 포함해서 응답 DTO 생성
        TargetEmotionsResponseDTO response = guardianService.countEmotion(emotionList);

        return CommonResponseDto.ok(response);
    }

    @GetMapping("/guardian/my")
    public CommonResponseDto<?> getGuardianMyPage(
            @AuthenticationPrincipal CustomUserDetails user){
        GuardianMyPageResponseDTO response = guardianService.getGuardianMyPage(user.getUserId());
        return CommonResponseDto.ok(response);
    }

    /*@PostMapping("/sharing/sendmessage")
    public CommonResponseDto<?> sendMessage(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody
    )*/





}
