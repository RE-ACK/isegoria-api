package com.isegoria.server.message.response;

import java.time.LocalDateTime;

import com.isegoria.server.message.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private Long channelId;
    private Long senderId;
    private String senderName;
    private String content;
    private String senderImage;
    private LocalDateTime createdAt;

    public static MessageResponse fromEntity(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .channelId(message.getChannel().getId())
                .senderId(message.getSender().getId())
                .senderImage(message.getSender().getAvatarUrl())
                .senderName(message.getSender().getUsername())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
