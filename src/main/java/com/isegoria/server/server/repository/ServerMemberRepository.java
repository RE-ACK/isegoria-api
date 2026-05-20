package com.isegoria.server.server.repository;

import com.isegoria.server.server.entity.Server;
import com.isegoria.server.server.entity.ServerMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServerMemberRepository extends JpaRepository<ServerMember, Long> {

    // 이미 가입됐는지 확인 (중복 입장 방지)
    // → SELECT COUNT(*) FROM server_members WHERE server_id = ? AND user_id = ?
    boolean existsByServerAndUserId(Server server, Long userId);

    // 특정 서버에서 특정 유저 멤버 정보 조회 (권한 확인 시 사용)
    // → SELECT * FROM server_members WHERE server_id = ? AND user_id = ?
    Optional<ServerMember> findByServerAndUserId(Server server, Long userId);

    // 나가기 / 추방 시 삭제
    // → DELETE FROM server_members WHERE server_id = ? AND user_id = ?
    void deleteByServerAndUserId(Server server, Long userId);

    // 서버 삭제 시 멤버 전체 삭제
    void deleteAllByServer(Server server);

    // 서버 멤버인지 확인 (권한 체크 시 사용)
    boolean existsByServerIdAndUserId(Long serverId, Long userId);
}