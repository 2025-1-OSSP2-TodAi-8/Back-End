package com.todai.BE.controller;

import com.todai.BE.common.dto.CommonResponseDto;
import com.todai.BE.dto.request.user.HandleSharingRequestDTO;
import com.todai.BE.dto.request.user.SearchUserRequestDTO;
import com.todai.BE.dto.response.user.HandleSharingResponseDTO;
import com.todai.BE.dto.response.user.MyPageResponseDTO;
import com.todai.BE.dto.response.user.SearchUserResponseDTO;
import com.todai.BE.dto.response.user.SharingResponseDTO;
import com.todai.BE.security.CustomUserDetails;
import com.todai.BE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/people")
public class UserController {

    private final UserService userService;

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
        String username = request.username();
        SearchUserResponseDTO response = userService.searchUser(username);
        return CommonResponseDto.ok(response);
    }

    @PostMapping("/sharing/request")
    public CommonResponseDto<?> sendSharingRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SearchUserRequestDTO requestDTO
    ) {
        SharingResponseDTO response = userService.handleSharingRequest(
                userDetails.getUserId(),
                requestDTO.username()
        );

        return CommonResponseDto.ok(response);
    }

    /*@PostMapping("/action")
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
    }*/



}
