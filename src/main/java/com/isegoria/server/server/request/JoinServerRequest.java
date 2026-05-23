package com.isegoria.server.server.request;

import com.isegoria.server.server.entity.MemberRole;
import com.isegoria.server.server.entity.Server;
import com.isegoria.server.server.entity.ServerMember;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinServerRequest{
        @NotBlank(message = "초대 코드를 입력해주세요.")
        String inviteCode;
        public static ServerMember toEntity(Server server, Long userId) {
                return ServerMember.builder()
                        .server(server)
                        .userId(userId)
                        .role(MemberRole.MEMBER)
                        .build();
        }

}

