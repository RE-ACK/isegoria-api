package com.isegoria.server.server;

import com.isegoria.server.global.annotations.CurrentUser;
import com.isegoria.server.global.api.Api;
import com.isegoria.server.global.jwt.JwtPayload;
import com.isegoria.server.global.message.ResponseMessage;
import com.isegoria.server.server.entity.Server;
import com.isegoria.server.server.request.CreateServerRequest;
import com.isegoria.server.server.request.JoinServerRequest;
import com.isegoria.server.server.response.InviteCodeResponse;
import com.isegoria.server.server.response.ServerMemberResponse;
import com.isegoria.server.server.response.ServerResponse;
import com.isegoria.server.server.service.ServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // HTTP 요청 받는걸 명시한다
@RequestMapping("/servers") // 모든 URL 앞에 /servers 붙는다
@RequiredArgsConstructor 
public class ServerController {

    private final ServerService serverService;

    // POST /servers — 서버 생성
    @PostMapping
    public Api<ServerResponse> createServer(
            @CurrentUser JwtPayload user, // userid 추출
            @Valid @RequestBody CreateServerRequest request) {
        // 검증, JSON 바디 -> Java 객체로 변환

        // Long ownerId = (userId != null) ? userId : 1L; // TODO: auth 구현 후 제거
        Server server = serverService.createServer(user.getId(), request);
        return Api.OK(ServerResponse.fromEntity(server), ResponseMessage.CREATE_SERVER_SUCCESS);
    }

    // PUT /servers/{serverId} — 서버 정보 수정 (OWNER만 가능)
    @PutMapping("/{serverId}")
    public Api<ServerResponse> updateServer(
            @CurrentUser JwtPayload user,
            @PathVariable Long serverId,
            @Valid @RequestBody CreateServerRequest request) {

        Server updatedServer = serverService.updateServer(user.getId(), serverId, request);
        return Api.OK(ServerResponse.fromEntity(updatedServer), ResponseMessage.UPDATE_SERVER_SUCCESS);
    }

    // POST /servers/join — 초대 코드로 서버 입장
    @PostMapping("/join")
    public Api<ServerResponse> joinServer(
            @CurrentUser JwtPayload user,
            @Valid @RequestBody JoinServerRequest request) {

        // Long joinerId = (userId != null) ? userId : 2L; // TODO: auth 구현 후 제거
        Server server = serverService.joinServer(user.getId(), request.getInviteCode());
        return Api.OK(ServerResponse.fromEntity(server));
    }

    // GET /servers/{serverId} — 서버 정보 조회
    @GetMapping("/{serverId}")
    public Api<ServerResponse> getServer(
            @CurrentUser JwtPayload user,
            @PathVariable Long serverId) {

        Server server = serverService.findById(serverId);
        return Api.OK(ServerResponse.fromEntity(server));
    }

    // GET /servers/my — 내 서버 목록
    @GetMapping("/my")
    public Api<List<ServerResponse>> getServerList(
            @CurrentUser JwtPayload user) {

        // Long id = (userId != null) ? userId : 1L; // TODO: auth 구현 후 제거
        List<ServerResponse> responses = serverService.getServerList(user.getId())
                .stream()
                .map(ServerResponse::fromEntity)
                .toList();
        return Api.OK(responses);
    }

    // GET /servers/{serverId}/members — 서버 멤버 목록 조회
    @GetMapping("/{serverId}/members")
    public Api<List<ServerMemberResponse>> getServerMembers(
            @CurrentUser JwtPayload user,
            @PathVariable Long serverId) {

        return Api.OK(serverService.getServerMembers(user.getId(), serverId));
    }

    // DELETE /servers/{serverId} — 서버 삭제
    @DeleteMapping("/{serverId}")
    public Api<Void> deleteServer(
            @CurrentUser JwtPayload user,
            @PathVariable Long serverId) {

        // Long id = (userId != null) ? userId : 1L; // TODO: auth 구현 후 제거
        serverService.deleteServer(user.getId(), serverId);
        return Api.OK(ResponseMessage.DELETE_SERVER_SUCCESS);
    }

    // DELETE /servers/{serverId}/leave — 서버 나가기
    @DeleteMapping("/{serverId}/leave")
    public Api<Void> leaveServer(
            @CurrentUser JwtPayload user,
            @PathVariable Long serverId) {

        // Long id = (userId != null) ? userId : 1L; // TODO: auth 구현 후 제거
        serverService.leaveServer(user.getId(), serverId);
        return Api.OK(ResponseMessage.DELETE_SERVER_SUCCESS);
    }

    // DELETE /servers/{serverId}/members/{targetUserId} — 멤버 추방
    @DeleteMapping("/{serverId}/members/{targetUserId}")
    public Api<Void> kickMember(
            @CurrentUser JwtPayload user,
            @PathVariable("serverId") Long serverId,
            @PathVariable("targetUserId") Long targetUserId) {

        // Long id = (userId != null) ? userId : 1L; // TODO: auth 구현 후 제거
        serverService.kickMember(user.getId(), serverId, targetUserId);
        return Api.OK(ResponseMessage.KICK_MEMBER_SUCCESS);
    }

    // POST /servers/{serverId}/invite — 초대 코드 재발급
    @PostMapping("/{serverId}/invite")
    public Api<InviteCodeResponse> regenerateInviteCode(
            @CurrentUser JwtPayload user,
            @PathVariable Long serverId) {

        // Long id = (userId != null) ? userId : 1L; // TODO: auth 구현 후 제거
        InviteCodeResponse newCode = serverService.regenerateInviteCode(user.getId(), serverId);
        return Api.OK(newCode);
    }
}