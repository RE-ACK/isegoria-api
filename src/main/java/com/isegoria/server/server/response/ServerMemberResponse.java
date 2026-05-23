package com.isegoria.server.server.response;

import com.isegoria.server.server.entity.MemberRole;
import com.isegoria.server.server.entity.ServerMember;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerMemberResponse {

    private Long userId;

    private String username;

    private String avatarUrl;

    private MemberRole role;

    private LocalDateTime joinedAt;

    public static ServerMemberResponse fromEntity(ServerMember member) {
        return ServerMemberResponse.builder()
                .userId(member.getUserId())
                .username(member.getUser().getUsername())
                .avatarUrl(member.getUser().getAvatarUrl())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
