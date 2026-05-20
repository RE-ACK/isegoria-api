package com.isegoria.server.channel.service;

import com.isegoria.server.channel.request.CreateChannelRequest;
import com.isegoria.server.channel.response.ChannelResponse;

import java.util.List;
import com.isegoria.server.channel.request.UpdateChannelRequest;

public interface ChannelService {

    ChannelResponse create(CreateChannelRequest request);

    List<ChannelResponse> getChannels(Long serverId);

    ChannelResponse update(Long channelId, UpdateChannelRequest request);

    void delete(Long channelId);

    ChannelResponse getChannel(Long channelId);
}