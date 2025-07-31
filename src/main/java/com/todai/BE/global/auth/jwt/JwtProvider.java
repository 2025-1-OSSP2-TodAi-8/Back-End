package com.todai.BE.global.auth.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    //일단은 테스트용으로 하드코딩. 나중에 외부에서 설정된 비밀키 사용 구조로 리팩토링
    private Key secretKey;

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.access-token-expire-time}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token-expire-time}")
    private long refreshTokenExpireTime;

    // 문자열 비밀키를 Key 객체로 변환
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKeyString.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    //액세스토큰 생성
    public String generateAccessToken(UUID userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //리프레쉬토큰 생성
    public String generateRefreshToken(UUID userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpireTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //토큰에서 userId 추출
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

}
