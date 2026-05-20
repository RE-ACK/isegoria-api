package com.isegoria.server.message.repository;

import com.isegoria.server.message.entity.Message;
import com.isegoria.server.message.entity.QMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Message> findMessagesCursor(Long channelId, Long lastMessageId, int size) {
        QMessage message = QMessage.message;

        return queryFactory
                .selectFrom(message)
                .join(message.sender).fetchJoin()
                .where(
                        message.channel.id.eq(channelId),
                        ltLastMessageId(lastMessageId))
                .orderBy(message.id.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression ltLastMessageId(Long lastMessageId) {
        if (lastMessageId == null) {
            return null;
        }
        return QMessage.message.id.lt(lastMessageId);
    }
}
