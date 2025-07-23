package com.todai.todai.domain.user.service;

import com.todai.todai.domain.user.dto.request.SignInRequestDTO;
import com.todai.todai.domain.user.dto.request.SignUpRequestDTO;
import com.todai.todai.domain.user.dto.response.SignInResponseDTO;
import com.todai.todai.domain.user.entity.User;
import com.todai.todai.domain.user.repository.UserRepository;
import com.todai.todai.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    //회원가입 로직
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


    //로그인 로직(토큰발급)
    public SignInResponseDTO signIn(SignInRequestDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.username())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());

        return new SignInResponseDTO(accessToken, refreshToken);
    }


}
