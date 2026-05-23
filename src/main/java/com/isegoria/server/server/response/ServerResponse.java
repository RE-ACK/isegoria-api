package com.isegoria.server.server.response;

import com.isegoria.server.server.entity.Server;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerResponse {

    private Long id;

    private String name;

    private String iconUrl;

    private String inviteCode;

    private LocalDateTime createdAt;

    public static ServerResponse fromEntity(Server server) {
        return ServerResponse.builder()
                .id(server.getId())
                .name(server.getName())
                .iconUrl(server.getIconUrl())
                .inviteCode(server.getInviteCode())
                .createdAt(server.getCreatedAt())
                .build();
    }
}
