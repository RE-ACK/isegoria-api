package com.isegoria.server.message.repository;

import com.isegoria.server.message.entity.Message;
import java.util.List;

public interface MessageRepositoryCustom {
    List<Message> findMessagesCursor(Long channelId, Long lastMessageId, int size);
}
