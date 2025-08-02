package com.todai.BE.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 400** Bad Request
     */
    INVALID_REQUEST_PARAMETER("40000", HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터입니다."),
    INVALID_REQUEST_BODY     ("40001", HttpStatus.BAD_REQUEST, "잘못된 요청 본문입니다."),
    INVALID_METHOD_ARGUMENT  ("40002", HttpStatus.BAD_REQUEST, "잘못된 메서드 인수입니다."),
    MISSING_REQUEST_PARAMETER("40003", HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    ARGUMENT_TYPE_MISMATCH   ("40004", HttpStatus.BAD_REQUEST, "파라미터 타입이 올바르지 않습니다."),
    MISSING_REQUEST_PART     ("40005", HttpStatus.BAD_REQUEST, "요청 파트가 누락되었습니다."),
    UNSUPPORTED_MEDIA_TYPE   ("40006", HttpStatus.BAD_REQUEST, "지원되지 않는 미디어 타입입니다."),
    DUPLICATION_LOGIN_ID     ("40007", HttpStatus.BAD_REQUEST, "로그인 아이디가 중복되었습니다."),
    INVALID_MAPPING_VALUE    ("40008", HttpStatus.BAD_REQUEST, "GPT 매핑이 이루어지지 않았습니다"),

    DUPLICATION_EMAIL("40009", HttpStatus.BAD_REQUEST, "이메일이 중복되었습니다."),



    /**
     * 401** Unauthorized
     */
    FAILURE_LOGIN("40100", HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    TOKEN_EXPIRED("40101", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다,"),
    TOKEN_UNSUPPORTED("40102", HttpStatus.UNAUTHORIZED, "지원되지 않는 형식의 토큰이에요"),
    TOKEN_UNKNOWN("40103", HttpStatus.UNAUTHORIZED, "토큰을 알수 없어요"),
    TOKEN_MALFORMED("40104", HttpStatus.UNAUTHORIZED, "잘못된 토큰을 사용했어요."),
    TOKEN_TYPE("40105", HttpStatus.UNAUTHORIZED, "잘못된 토큰타입을 사용했어요."),
    INVALID_APPLE_IDENTITY_TOKEN_ERROR("40106", HttpStatus.UNAUTHORIZED, "잘못된 Apple Identity 토큰입니다."),
    EXPIRED_APPLE_IDENTITY_TOKEN_ERROR("40107", HttpStatus.UNAUTHORIZED, "만료된 Apple Identity 토큰입니다."),
    INVALID_APPLE_PUBLIC_KEY_ERROR("40108", HttpStatus.UNAUTHORIZED, "잘못된 Apple 공개키입니다."),
    INVALID_LOGIN_TYPE("40109", HttpStatus.UNAUTHORIZED, "잘못된 로그인 요청입니다."),
    INVALID_PASSWORD("40110", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다. "),
    SMS_VERIFY_FAILED("40111", HttpStatus.UNAUTHORIZED, "휴대전화 인증 실패"),

    /**
     * 403** Access Denied
     */
    ACCESS_DENIED_ERROR("40300", HttpStatus.FORBIDDEN, "액세스 권한이 없습니다."),
    EMPTY_AUTHENTICATION("40301", HttpStatus.FORBIDDEN, "인증 토큰이 비었습니다."),
    ACCESS_DENIED_LEADER("40301", HttpStatus.FORBIDDEN, "당신은 리더가 아닙니다."),
    INVALID_ROLE("40303", HttpStatus.FORBIDDEN, "권한이 맞지 않습니다."),

    /**
     * 404** Not Found
     */
    NOT_FOUND_END_POINT("40400", HttpStatus.NOT_FOUND, "존재하지 않는 엔드포인트입니다."),
    NOT_FOUND_USER("40401", HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    NOT_FOUND_DIARY("40402", HttpStatus.NOT_FOUND, "해당 조건에 해당하는 일기가 존재하지 않습니다"),
    NOT_FOUND_AUDIO("40403", HttpStatus.NOT_FOUND, "해당 일기의 음성파일이 존재하지 않습니다"),


    /**
     * 405** Method Not Allowed
     */
    METHOD_NOT_ALLOWED("40500", HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),

    /**
     * 409** Conflict
     */
    CONFLICT_TEAM_NAME("40900", HttpStatus.CONFLICT, "팀 이름이 중복입니다."),
    CONFLICT_TEAM_COUNT("40901", HttpStatus.CONFLICT, "팀 정원을 초과하였습니다."),
    CONFLICT_TEAM_BUILDING("40902", HttpStatus.CONFLICT, "이미 팀에 가입되어 있어 다른 팀을 생성하거나 가입할 수 없습니다.."),
    CONFLICT_ADMIN_ID("40903", HttpStatus.CONFLICT, "로그인 실패"),

    /**
     * 500** Server Error
     */
    SERVER_ERROR("50000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    SERVER_ERROR_INIT_FILE("50101", HttpStatus.INTERNAL_SERVER_ERROR, "초기 파일 저장 오류"),
    SERVER_ERROR_FILE_SAVE("50102", HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 오류"),
    SERVER_ERROR_DELETE_FILE("50102", HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 오류"),
    SERVER_ERROR_FILE_READ("50103", HttpStatus.INTERNAL_SERVER_ERROR, "문자열 파싱 오류");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;


}
