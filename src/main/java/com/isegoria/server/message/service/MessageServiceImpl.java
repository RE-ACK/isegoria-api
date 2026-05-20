package com.isegoria.server.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isegoria.server.channel.repository.ChannelRepository;
import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.message.entity.Message;
import com.isegoria.server.message.repository.MessageRepository;
import com.isegoria.server.message.request.CreateMessageRequest;
import com.isegoria.server.message.response.MessageResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    /**
     * 메세지 생성
     * 
     * @param CreateMessageRequest
     *                             - Long channelId,
     *                             - String content
     * @param JwtPayload
     *                             - Long id
     * @return MessageResponse
     *         - Long id
     *         - String content,
     *         - Long channelId,
     *         - Long senderId,
     *         - String senderName,
     *         - String senderImage,
     *         - LocalDateTime createdAt
     */
    @Override
    @Transactional
    public MessageResponse create(CreateMessageRequest request, Long userId) {
        Message message = CreateMessageRequest.toEntity(request, userId);
        Message savedMessage = messageRepository.save(message);
        MessageResponse response = MessageResponse.fromEntity(savedMessage);
        return response;
    }

    /**
     * 메세지 목록 조회
     * 
     * @param Long channelId
     * @param Long lastMessageId
     * @param int  size
     * @return List<MessageResponse>
     *         - Long id
     *         - String content,
     *         - Long channelId,
     *         - Long senderId,
     *         - String senderName,
     *         - String senderImage,
     *         - LocalDateTime createdAt
     */
    @Override
    public List<MessageResponse> getMessages(Long channelId, Long lastMessageId, int size) {
        if (!channelRepository.existsById(channelId)) {
            throw new ApiException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        // 커서 기반 페이지네이션으로 메시지 조회
        List<Message> messages = messageRepository.findMessagesCursor(channelId, lastMessageId, size);
        List<MessageResponse> response = messages.stream()
                .map(MessageResponse::fromEntity)
                .collect(Collectors.toList());
        return response;
    }
}
