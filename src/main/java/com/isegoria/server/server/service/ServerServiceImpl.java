package com.isegoria.server.server.service;

import com.isegoria.server.channel.service.ChannelService;
import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.image.service.ImageService;
import com.isegoria.server.server.entity.MemberRole;
import com.isegoria.server.server.entity.Server;
import com.isegoria.server.server.entity.ServerMember;
import com.isegoria.server.server.repository.ServerMemberRepository;
import com.isegoria.server.server.repository.ServerRepository;
import com.isegoria.server.server.request.CreateServerRequest;
import com.isegoria.server.server.request.JoinServerRequest;
import com.isegoria.server.server.response.InviteCodeResponse;
import com.isegoria.server.server.response.ServerMemberResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;
    private final ImageService imageService;
    private final ChannelService channelService;

    // ───────────────────────────────────────────
    // 서버 생성
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public Server createServer(Long ownerId, CreateServerRequest request) {

        Server ServerEntity = CreateServerRequest.toEntity(request, ownerId, generateInviteCode());
        Server newServer = serverRepository.save(ServerEntity);
        String newIconUrl = request.getIconUrl();
        if (newIconUrl != null && !newIconUrl.isEmpty()) {
            List<String> images = imageService.createImage(
                    newServer.getId().toString(),
                    List.of(newIconUrl),
                    "server");
            newServer.setIconUrl(images.get(0));
            newServer = serverRepository.save(newServer);
        }

        channelService.createDefaultChannels(newServer.getId());

        ServerMember ownerMember = CreateServerRequest.toOwnerMember(newServer, ownerId);
        serverMemberRepository.save(ownerMember);

        return newServer;
    }

    // ───────────────────────────────────────────
    // 서버 정보 수정 (OWNER만 가능)
    // ───────────────────────────────────────────
    @Override
    public Server updateServer(Long userId, Long serverId, CreateServerRequest request) {
        Server server = this.findById(serverId);

        if (!server.getOwnerId().equals(userId)) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }
        server.setName(request.getName());
        String newIconUrl = request.getIconUrl();
        String currentIconUrl = server.getIconUrl();

        if (newIconUrl != null && !newIconUrl.trim().isEmpty()) {

            if (!Objects.equals(newIconUrl, currentIconUrl)) {

                if (currentIconUrl != null) {
                    List<String> images = imageService.updateImage(
                            serverId.toString(),
                            List.of(newIconUrl),
                            List.of(currentIconUrl),
                            "user");
                    server.setIconUrl(images.get(0));
                } else {
                    List<String> images = imageService.createImage(
                            serverId.toString(),
                            List.of(newIconUrl),
                            "user");
                    server.setIconUrl(images.get(0));
                }
            }
        }

        server = serverRepository.save(server);

        return server;
    }

    // ───────────────────────────────────────────
    // 초대 코드로 서버 입장
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public Server joinServer(Long userId, String inviteCode) {
        Server server = serverRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new ApiException(ErrorCode.INVITE_CODE_NOT_FOUND));

        if (serverMemberRepository.existsByServerAndUserId(server, userId)) {
            throw new ApiException(ErrorCode.ALREADY_JOINED);
        }

        ServerMember newMember = JoinServerRequest.toEntity(server, userId);
        serverMemberRepository.save(newMember);

        return server;
    }

    // ───────────────────────────────────────────
    // 내 서버 목록 조회
    // ───────────────────────────────────────────
    @Transactional(readOnly = true)
    @Override
    public List<Server> getServerList(Long userId) {
        return serverRepository.findServersByUserId(userId);
    }

    // ───────────────────────────────────────────
    // 서버 멤버 목록 조회
    // ───────────────────────────────────────────
    @Transactional(readOnly = true)
    @Override
    public List<ServerMemberResponse> getServerMembers(Long userId, Long serverId) {
        Server server = findById(serverId);

        serverMemberRepository.findByServerAndUserId(server, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        return serverMemberRepository.findAllWithUserByServer(server)
                .stream()
                .map(ServerMemberResponse::fromEntity)
                .toList();
    }

    // ───────────────────────────────────────────
    // 서버 나가기 (OWNER 불가)
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public void leaveServer(Long userId, Long serverId) {
        Server server = findById(serverId);

        ServerMember member = serverMemberRepository.findByServerAndUserId(server, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() == MemberRole.OWNER) {
            throw new ApiException(ErrorCode.OWNER_CANNOT_LEAVE);
        }

        serverMemberRepository.deleteByServerAndUserId(server, userId);
    }

    // ───────────────────────────────────────────
    // 멤버 추방 (OWNER만 가능)
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public void kickMember(Long ownerId, Long serverId, Long targetUserId) {
        Server server = findById(serverId);

        ServerMember requester = serverMemberRepository.findByServerAndUserId(server, ownerId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (requester.getRole() != MemberRole.OWNER) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }

        if (ownerId.equals(targetUserId)) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }

        serverMemberRepository.findByServerAndUserId(server, targetUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        serverMemberRepository.deleteByServerAndUserId(server, targetUserId);
    }

    // ───────────────────────────────────────────
    // 초대 코드 재발급 (OWNER만 가능)
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public InviteCodeResponse regenerateInviteCode(Long ownerId, Long serverId) {
        Server server = findById(serverId);

        ServerMember requester = serverMemberRepository.findByServerAndUserId(server, ownerId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (requester.getRole() != MemberRole.OWNER) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }

        String newCode = generateInviteCode();
        server.updateInviteCode(newCode);

        return new InviteCodeResponse(newCode);
    }

    // ───────────────────────────────────────────
    // 서버 삭제 (OWNER만 가능)
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public void deleteServer(Long ownerId, Long serverId) {
        Server server = findById(serverId);

        ServerMember requester = serverMemberRepository.findByServerAndUserId(server, ownerId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (requester.getRole() != MemberRole.OWNER) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }

        serverMemberRepository.deleteAllByServer(server);
        serverRepository.delete(server);

        boolean exists = serverRepository.existsById(serverId);
        if (exists) {
            throw new ApiException(ErrorCode.DELETE_SERVER_FAILED);
        } else if (server.getIconUrl() != null) {
            imageService.deleteImage(serverId.toString(), "server");
        }
    }

    // ───────────────────────────────────────────
    // 헬퍼 메서드
    // ───────────────────────────────────────────
    @Override
    public Server findById(Long serverId) {
        return serverRepository.findById(serverId)
                .orElseThrow(() -> new ApiException(ErrorCode.SERVER_NOT_FOUND));
    }

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private String generateInviteCode() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}