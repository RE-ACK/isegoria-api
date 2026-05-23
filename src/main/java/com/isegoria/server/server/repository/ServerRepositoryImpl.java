package com.isegoria.server.server.repository;

import com.isegoria.server.server.entity.QServer;
import com.isegoria.server.server.entity.QServerMember;
import com.isegoria.server.server.entity.Server;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor //QueryFactory 자동으로 넣기
public class ServerRepositoryImpl implements ServerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 내가 가입한 서버 목록
    @Override
    public List<Server> findServersByUserId(Long userId) {

        //엔티티를 쿼리에서 쓸 수 있게 변환. 빌드시에 생성됨
        QServer server = QServer.server;
        QServerMember serverMember = QServerMember.serverMember;

        //쿼리문
        return queryFactory
                .selectFrom(server)
                .join(server.members, serverMember)
                .where(serverMember.userId.eq(userId))
                .orderBy(serverMember.joinedAt.asc())
                .fetch();
    }

    // JWT payload용 서버 ID만 조회
    // 멤버에서 id만 가져옴
    @Override
    public List<Long> findServerIdsByUserId(Long userId) {
        QServerMember serverMember = QServerMember.serverMember;

        return queryFactory
                .select(serverMember.server.id)
                .from(serverMember)
                .where(serverMember.userId.eq(userId))
                .fetch();
    }
}