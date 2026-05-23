package com.isegoria.server.channel.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateChannelRequest {

  @NotBlank(message = "채널명을 입력하세요.")
  private String name;
}
