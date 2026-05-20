package com.isegoria.server.channel.response;

import java.time.LocalDateTime;

import com.isegoria.server.channel.entity.Channel;
import com.isegoria.server.channel.entity.ChannelType;

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
public class ChannelResponse {
    private Long id;
    private Long serverId;
    private String name;
    private ChannelType type;
    private LocalDateTime createdAt;

    public static ChannelResponse fromEntity(Channel channel) {
        return ChannelResponse.builder()
                .id(channel.getId())
                .serverId(channel.getServer().getId())
                .name(channel.getName())
                .type(channel.getType())
                .createdAt(channel.getCreatedAt())
                .build();
    }
}