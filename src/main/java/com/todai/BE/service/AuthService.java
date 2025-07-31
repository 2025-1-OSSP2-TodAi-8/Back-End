package com.todai.BE.service;

import com.todai.BE.dto.request.user.SignInRequestDTO;
import com.todai.BE.dto.request.user.SignUpRequestDTO;
import com.todai.BE.dto.response.user.SignInResponseDTO;
import com.todai.BE.entity.User;
import com.todai.BE.repository.UserRepository;
import com.todai.BE.global.auth.jwt.JwtProvider;
import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    public void signUp(SignUpRequestDTO requestDTO) {
        if (userRepository.findByUsername(requestDTO.username()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }

        User user = User.builder()
                .username(requestDTO.username())
                .password(passwordEncoder.encode(requestDTO.password()))
                .name(requestDTO.name())
                .birthdate(requestDTO.birthdate())
                .gender(requestDTO.gender())
                .userType(requestDTO.userType())
                .build();
        userRepository.save(user);
    }

    public SignInResponseDTO signIn(SignInRequestDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.username())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());

        return new SignInResponseDTO(accessToken, refreshToken);
    }
}
