package com.isegoria.server.server.repository;

import com.isegoria.server.server.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, Long>, ServerRepositoryCustom {
    // JpaRepository<Server, Long> 상속하면 save / findById / delete / findAll 자동생성

    // findByInviteCode 는 메서드 이름만 써도 Spring이 쿼리 자동 생성

    // 초대 코드로 서버 찾기
    // → SELECT * FROM servers WHERE invite_code = ?
    Optional<Server> findByInviteCode(String inviteCode);

    // 서버 ID와 소유자 ID로 서버 존재 여부 확인
    boolean existsByIdAndOwnerId(Long serverId, Long userId);
}