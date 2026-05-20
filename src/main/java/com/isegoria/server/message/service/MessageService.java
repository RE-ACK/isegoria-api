package com.isegoria.server.message.service;

import java.util.List;

import com.isegoria.server.message.request.CreateMessageRequest;
import com.isegoria.server.message.response.MessageResponse;

public interface MessageService {
    MessageResponse create(CreateMessageRequest request, Long userId);

    List<MessageResponse> getMessages(Long channelId, Long lastMessageId, int size);
}
