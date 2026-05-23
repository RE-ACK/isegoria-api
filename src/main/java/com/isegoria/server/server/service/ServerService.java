package com.isegoria.server.server.service;

import com.isegoria.server.server.entity.Server;
import com.isegoria.server.server.request.CreateServerRequest;
import com.isegoria.server.server.response.InviteCodeResponse;
import com.isegoria.server.server.response.ServerMemberResponse;

import java.util.List;

//Controller 에서 코드 수정없이 쓰려고
//뭘 할 수 있는지만 정의했다.

public interface ServerService {

    // 서버 생성
    Server createServer(Long ownerId, CreateServerRequest request);

    // 서버 정보 수정
    Server updateServer(Long id, Long serverId, CreateServerRequest request);

    // 초대 코드로 서버 입장
    Server joinServer(Long userId, String inviteCode);

    // 내 서버 목록 조회
    List<Server> getServerList(Long userId);

    // 서버 나가기
    void leaveServer(Long userId, Long serverId);

    // 멤버 추방
    void kickMember(Long ownerId, Long serverId, Long targetUserId);

    // 초대 코드 재발급
    InviteCodeResponse regenerateInviteCode(Long ownerId, Long serverId);

    // 서버 멤버 목록 조회
    List<ServerMemberResponse> getServerMembers(Long userId, Long serverId);

    // 서버 삭제
    void deleteServer(Long ownerId, Long serverId);

    Server findById(Long serverId);
}