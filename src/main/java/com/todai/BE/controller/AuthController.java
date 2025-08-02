package com.todai.BE.controller;

import com.todai.BE.dto.request.user.SignInRequestDTO;
import com.todai.BE.dto.request.user.SignUpRequestDTO;
import com.todai.BE.dto.response.user.SignInResponseDTO;
import com.todai.BE.service.AuthService;
import com.todai.BE.common.dto.CommonResponseDto;
import com.todai.BE.common.dto.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/people/signup")
    public CommonResponseDto<?> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);
        return CommonResponseDto.ok("회원가입 성공");
    }

    @PostMapping("/api/people/signin")
    public CommonResponseDto<?> login(@RequestBody SignInRequestDTO signInRequestDTO) {
        SignInResponseDTO response = authService.signIn(signInRequestDTO);
        return CommonResponseDto.ok(response);
    }
}
