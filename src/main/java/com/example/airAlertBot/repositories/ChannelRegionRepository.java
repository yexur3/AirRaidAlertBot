package com.example.airAlertBot.repositories;

import com.example.airAlertBot.entities.ChannelRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRegionRepository extends JpaRepository<ChannelRegion, Long> {
    List<ChannelRegion> findByChannelId(long channelId);
}
