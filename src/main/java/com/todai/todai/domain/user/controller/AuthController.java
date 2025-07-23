package com.todai.todai.domain.user.controller;

import com.todai.todai.domain.user.dto.request.SignInRequestDTO;
import com.todai.todai.domain.user.dto.request.SignUpRequestDTO;
import com.todai.todai.domain.user.dto.response.SignInResponseDTO;
import com.todai.todai.domain.user.service.AuthService;
import com.todai.todai.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/people/signup")
    public SuccessResponse<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);
        return new SuccessResponse<>("회원가입 성공");
    }

    @PostMapping("/api/people/signin")
    public ResponseEntity<SuccessResponse<SignInResponseDTO>> login(@RequestBody SignInRequestDTO signInRequestDTO) {
        SignInResponseDTO response = authService.signIn(signInRequestDTO);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
