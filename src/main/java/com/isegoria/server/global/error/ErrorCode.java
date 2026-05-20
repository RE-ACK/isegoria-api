package com.isegoria.server.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeInterface {
  OK(HttpStatus.OK.value(), "성공"),
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다."),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러가 발생했습니다."),
  NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), "널 포인트 에러가 발생했습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN.value(), "접근이 거부되었습니다."),
  ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST.value(), "잘못된 인자가 전달되었습니다."),
  ILLEGAL_STATE(HttpStatus.BAD_REQUEST.value(), "잘못된 상태에서 메서드가 호출되었습니다."),
  DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT.value(), "데이터 무결성 위반 오류입니다."),
  INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "입력값이 유효하지 않습니다."),

  // JWT 관련 에러
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 액세스 토큰입니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 리프레시 토큰입니다."),
  REFRESH_TOKEN_CLAIMS_INVALID(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰의 클레임이 유효하지 않습니다."),

  // Auth 관련 에러
  LOGIN_FAILED(HttpStatus.UNAUTHORIZED.value(), "이메일 혹은 비밀번호가 일치하지 않습니다."),

  // User 관련 에러
  USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."),

  // 서버 관련 에러 :: 이민하
  SERVER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 서버입니다."),
  ALREADY_JOINED(HttpStatus.BAD_REQUEST.value(), "이미 가입된 서버입니다."),
  INVITE_CODE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 초대 코드입니다."),
  OWNER_CANNOT_LEAVE(HttpStatus.BAD_REQUEST.value(), "서버 소유자는 나갈 수 없습니다."),
  NO_PERMISSION(HttpStatus.FORBIDDEN.value(), "권한이 없습니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 멤버가 서버에 없습니다."),
  DELETE_SERVER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 삭제에 실패했습니다."),
  NOT_OWNER_IN_THIS_SERVER(HttpStatus.FORBIDDEN.value(), "해당 서버의 소유자가 아닙니다"),
  NOT_MEMBER_IN_THIS_SERVER(HttpStatus.FORBIDDEN.value(), "해당 서버의 멤버가 아닙니다"),
  CANNOT_IDENTIFY_SERVER_ID(HttpStatus.BAD_REQUEST.value(), "서버 ID를 식별할 수 없습니다."),

  // Channel 관련 에러
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 채널입니다."),

  // User 관련 에러
  DELETE_USER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "사용자 삭제에 실패했습니다."),

  // 임시 추가
  EMAIL_ALREADY_EXISTS(HttpStatus.NOT_FOUND.value(), "이미 존재하는 이메일 입니다"),

  // Image 관련 에러
  SAVE_IMAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 저장에 실패했습니다.");

  private final Integer httpStatusCode;
  private final String message;
}
