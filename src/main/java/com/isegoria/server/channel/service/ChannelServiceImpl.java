package com.isegoria.server.channel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.isegoria.server.channel.entity.Channel;
import com.isegoria.server.channel.entity.ChannelType;
import com.isegoria.server.channel.repository.ChannelRepository;
import com.isegoria.server.channel.request.CreateChannelRequest;
import com.isegoria.server.channel.request.UpdateChannelRequest;
import com.isegoria.server.channel.response.ChannelResponse;
import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelServiceImpl implements ChannelService {

  private final ChannelRepository channelRepository;

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
  @Override
  @Transactional
  public ChannelResponse create(CreateChannelRequest request) {
    Channel newChannel = CreateChannelRequest.toEntity(request);
    Channel savedChannel = channelRepository.save(newChannel);
    ChannelResponse response = ChannelResponse.fromEntity(savedChannel);
    return response;
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
  @Override
  public ChannelResponse getChannel(Long channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ApiException(ErrorCode.CHANNEL_NOT_FOUND));
    ChannelResponse response = ChannelResponse.fromEntity(channel);
    return response;
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
  @Override
  public List<ChannelResponse> getChannels(Long serverId) {
    List<Channel> channels = channelRepository.findAllByServerId(serverId);
    List<ChannelResponse> response = channels.stream()
        .map(ChannelResponse::fromEntity)
        .collect(Collectors.toList());
    return response;
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
  @Override
  @Transactional
  public ChannelResponse update(Long channelId, UpdateChannelRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ApiException(ErrorCode.CHANNEL_NOT_FOUND));
    channel.updateName(request.getName());
    ChannelResponse response = ChannelResponse.fromEntity(channel);
    return response;
  }

  /**
   * 채널 삭제
   * 
   * @param Long channelId
   */
  @Override
  @Transactional
  public void delete(Long channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ApiException(ErrorCode.CHANNEL_NOT_FOUND));
    channelRepository.delete(channel);
  }

  /**
   * 서버 생성 시 기본 채널 생성
   * 
   * @param Long serverId
   */
  @Override
  public void createDefaultChannels(Long serverId) {
    CreateChannelRequest textChannelRequest = CreateChannelRequest.builder()
        .serverId(serverId)
        .name("채팅 채널")
        .type(ChannelType.TEXT)
        .build();
    this.create(textChannelRequest);

    CreateChannelRequest voiceChannelRequest = CreateChannelRequest.builder()
        .serverId(serverId)
        .name("음성 채널")
        .type(ChannelType.VOICE)
        .build();
    this.create(voiceChannelRequest);
  }
}