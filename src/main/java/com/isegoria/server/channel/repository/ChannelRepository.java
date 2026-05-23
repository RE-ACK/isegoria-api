package com.isegoria.server.channel.repository;

import com.isegoria.server.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findAllByServerId(Long serverId);
}