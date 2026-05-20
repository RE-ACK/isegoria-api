package com.isegoria.server.message.request;

import com.isegoria.server.channel.entity.Channel;
import com.isegoria.server.message.entity.Message;
import com.isegoria.server.user.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateMessageRequest {

  @NotNull(message = "채널 ID는 필수입니다.")
  private Long channelId;

  @NotBlank(message = "메시지 내용을 입력하세요.")
  private String content;

  public static Message toEntity(CreateMessageRequest request, Long userId) {
    return Message.builder()
        .sender(User.builder().id(userId).build())
        .channel(Channel.builder().id(request.getChannelId()).build())
        .content(request.getContent())
        .build();
  }
}
