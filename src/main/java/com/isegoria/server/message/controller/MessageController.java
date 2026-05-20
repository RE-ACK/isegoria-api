package com.isegoria.server.message.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.isegoria.server.global.annotations.CurrentUser;
import com.isegoria.server.global.annotations.ServerMember;
import com.isegoria.server.global.api.Api;
import com.isegoria.server.global.jwt.JwtPayload;
import com.isegoria.server.message.request.CreateMessageRequest;
import com.isegoria.server.message.response.MessageResponse;
import com.isegoria.server.message.service.MessageService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("messages")
public class MessageController {

  private final MessageService messageService;

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
   *         - UserResponse sender,
   *         - LocalDateTime createdAt
   */
  @ServerMember
  @PostMapping("create")
  public Api<MessageResponse> createMessage(
      @Valid @RequestBody CreateMessageRequest request,
      @CurrentUser JwtPayload user) {
    MessageResponse response = messageService.create(request, user.getId());
    return Api.OK(response);
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
   *         - UserResponse sender,
   *         - LocalDateTime createdAt
   */
  @ServerMember
  @GetMapping("channels/{channelId}/messages")
  public Api<List<MessageResponse>> getMessages(
      @PathVariable Long channelId,
      @RequestParam(required = false) Long lastMessageId,
      @RequestParam(defaultValue = "50") int size) {
    List<MessageResponse> response = messageService.getMessages(channelId, lastMessageId, size);
    return Api.OK(response);
  }
}
