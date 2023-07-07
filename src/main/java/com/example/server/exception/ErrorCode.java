package com.example.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "제약 조건을 위배한 요청입니다 (constraint violation)"),
    METHOD_ARG_NOT_VALID(HttpStatus.BAD_REQUEST, "제약 조건을 위배한 요청입니다 (method argument not valid)"),
    FILE_NOT_EXIST_ERROR(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    CONFLICT_NICKNAME(HttpStatus.CONFLICT, "이미 사용중인 nickname 입니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버 에러 */
    SOCIAL_LOGIN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "소셜 로그인에 실패했습니다."),

    FILE_CAN_NOT_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
