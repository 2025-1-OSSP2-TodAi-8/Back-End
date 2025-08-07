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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private String generateShareCode(){
        String code;

        do{
            code = UUID.randomUUID().toString().substring(0, 8);
        }while (userRepository.existsByShareCode(code));

        return code;
    }


    public void signUp(SignUpRequestDTO requestDTO) {
        if (userRepository.findByUsername(requestDTO.username()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATION_LOGIN_ID);
        }

        // email 중복 검사
        if (userRepository.findByEmail(requestDTO.email()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATION_EMAIL);
        }

        //공유코드 생성
        String sharecode = generateShareCode();

        User user = User.builder()
                .username(requestDTO.username())
                .password(passwordEncoder.encode(requestDTO.password()))
                .name(requestDTO.name())
                .birthdate(requestDTO.birthdate())
                .gender(requestDTO.gender())
                .userType(requestDTO.userType())
                .email(requestDTO.email())
                .shareCode(sharecode)
                .build();
        userRepository.save(user);
    }

    //로그인
    public SignInResponseDTO signIn(SignInRequestDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.username())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());
        String userType = user.getUserType().toString();

        return new SignInResponseDTO(accessToken, refreshToken, userType);
    }
}
