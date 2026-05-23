package com.isegoria.server.server.request;

import com.isegoria.server.server.entity.MemberRole;
import com.isegoria.server.server.entity.Server;
import com.isegoria.server.server.entity.ServerMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

//데이터만 담는 class임.
//DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateServerRequest{
        @NotBlank(message = "서버 이름을 입력해주세요.")
        @Size(max = 100, message = "서버 이름은 100자 이하로 입력해주세요.")
        private String name;

        private String iconUrl;    // 선택사항, null 가능

        public static Server toEntity(CreateServerRequest request, Long ownerId, String inviteCode) {
                return Server.builder()
                        .name(request.getName())
                        .ownerId(ownerId)
                        .inviteCode(inviteCode)
                        .iconUrl(request.getIconUrl())
                        .build();
        }

        public static ServerMember toOwnerMember(Server server, Long ownerId) {
                return ServerMember.builder()
                        .server(server)
                        .userId(ownerId)
                        .role(MemberRole.OWNER)
                        .build();
        }
}