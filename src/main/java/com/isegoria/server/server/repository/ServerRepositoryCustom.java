package com.isegoria.server.server.repository;

import com.isegoria.server.server.entity.Server;

import java.util.List;

// 뭘 할지 선언만 하는 인터페이스.
// 
public interface ServerRepositoryCustom {

    // 내가 가입한 서버 목록 조회
    List<Server> findServersByUserId(Long userId);

    // JWT payload용 서버 ID 목록만 조회
    List<Long> findServerIdsByUserId(Long userId);
}