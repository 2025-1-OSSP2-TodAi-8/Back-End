package com.todai.BE.controller;

import com.todai.BE.common.dto.CommonResponseDto;
import com.todai.BE.dto.response.user.MyPageResponseDTO;
import com.todai.BE.security.CustomUserDetails;
import com.todai.BE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/people")
public class UserController {

    private final UserService userService;

    @GetMapping("/api/test")
    public String hello() {
        return "Hello, Authenticated User!";
    }

    @GetMapping("/my")
    public CommonResponseDto<?> getMyPageInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getUserId();
        MyPageResponseDTO response = userService.getMyPageInfo(userId);
        return CommonResponseDto.ok(response);
    }


}
