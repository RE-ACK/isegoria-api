package com.isegoria.server.channel.request;

import com.isegoria.server.channel.entity.Channel;

import com.isegoria.server.channel.entity.ChannelType;
import com.isegoria.server.server.entity.Server;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateChannelRequest {

  @NotNull(message = "서버 ID는 필수입니다.")
  private Long serverId;

  @NotBlank(message = "채널명을 입력하세요.")
  private String name;

  @NotNull(message = "채널 유형을 선택하세요.")
  private ChannelType type;

  public static Channel toEntity(CreateChannelRequest request) {
    return Channel.builder()
        .server(Server.builder().id(request.getServerId()).build())
        .name(request.getName())
        .type(request.getType())
        .build();
  }
}