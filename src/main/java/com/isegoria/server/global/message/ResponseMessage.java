package com.isegoria.server.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage implements ResponseMessageInterface {
  REGISTER_SUCCESS("회원가입이 성공적으로 완료되었습니다."),
  LOGIN_SUCCESS("로그인이 성공적으로 완료되었습니다."),

  // 서버 관련
  CREATE_SERVER_SUCCESS("서버가 성공적으로 생성되었습니다."),
  UPDATE_SERVER_SUCCESS("서버 정보가 성공적으로 업데이트되었습니다."),
  KICK_MEMBER_SUCCESS("멤버를 쫒아냈습니다."),
  DELETE_SERVER_SUCCESS("서버가 성공적으로 삭제되었습니다."),
  LEAVE_SERVER_SUCCESS("성공적으로 서버를 나갔습니다."),

  // 유저 관련
  UPDATE_USER_SUCCESS("회원 정보가 성공적으로 업데이트되었습니다."),
  DELETE_USER_SUCCESS("회원 탈퇴가 성공적으로 완료되었습니다."),

  // 채널 관련
  CREATE_CHANNEL_SUCCESS("채널이 성공적으로 생성되었습니다."),
  UPDATE_CHANNEL_SUCCESS("채널 정보가 성공적으로 업데이트되었습니다."),
  DELETE_CHANNEL_SUCCESS("채널이 성공적으로 삭제되었습니다.");

  private final String message;
}
