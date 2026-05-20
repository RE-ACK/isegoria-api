package com.isegoria.server.channel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.isegoria.server.channel.request.CreateChannelRequest;
import com.isegoria.server.channel.request.UpdateChannelRequest;
import com.isegoria.server.channel.response.ChannelResponse;
import com.isegoria.server.channel.service.ChannelService;
import com.isegoria.server.global.annotations.ServerMember;
import com.isegoria.server.global.annotations.ServerOwner;
import com.isegoria.server.global.api.Api;
import com.isegoria.server.global.message.ResponseMessage;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  /**
   * 채널 생성
   * 
   * @param CreateChannelRequest
   *                             - Long serverId,
   *                             - String name,
   *                             - ChannelType type
   * @return ChannelResponse
   *         - Long id
   *         - String name,
   *         - ChannelType type,
   *         - Long serverId,
   *         - LocalDateTime createdAt
   */
  @ServerOwner
  @PostMapping("create")
  public Api<ChannelResponse> createChannel(@Valid @RequestBody CreateChannelRequest request) {
    ChannelResponse response = channelService.create(request);
    return Api.OK(response, ResponseMessage.CREATE_CHANNEL_SUCCESS);
  }

  /**
   * 채널 목록 조회
   * 
   * @param Long serverId
   * @return List<ChannelResponse>
   *         - Long id
   *         - String name,
   *         - ChannelType type,
   *         - Long serverId,
   *         - LocalDateTime createdAt
   */
  @ServerMember
  @GetMapping("all")
  public Api<List<ChannelResponse>> getChannels(@RequestParam Long serverId) {
    List<ChannelResponse> response = channelService.getChannels(serverId);
    return Api.OK(response);
  }

  /**
   * 채널 단건 조회
   * 
   * @param Long channelId
   * @return ChannelResponse
   *         - Long id
   *         - String name,
   *         - ChannelType type,
   *         - Long serverId,
   *         - LocalDateTime createdAt
   */
  @ServerMember
  @GetMapping("{channelId}")
  public Api<ChannelResponse> getChannel(@PathVariable Long channelId) {
    ChannelResponse response = channelService.getChannel(channelId);
    return Api.OK(response);
  }

  /**
   * 채널 수정
   * 
   * @param Long                 channelId
   * @param UpdateChannelRequest
   *                             - String name
   * @return ChannelResponse
   *         - Long id
   *         - String name,
   *         - ChannelType type,
   *         - Long serverId,
   *         - LocalDateTime createdAt
   */
  @ServerOwner
  @PutMapping("{channelId}")
  public Api<ChannelResponse> updateChannel(
      @PathVariable Long channelId,
      @Valid @RequestBody UpdateChannelRequest request) {
    ChannelResponse response = channelService.update(channelId, request);
    return Api.OK(response, ResponseMessage.UPDATE_CHANNEL_SUCCESS);
  }

  /**
   * 채널 삭제
   * 
   * @param Long channelId
   */
  @ServerOwner
  @DeleteMapping("{channelId}")
  public Api<Void> deleteChannel(@PathVariable Long channelId) {
    channelService.delete(channelId);
    return Api.OK(ResponseMessage.DELETE_CHANNEL_SUCCESS);
  }
}