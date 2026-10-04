package com.example.airAlertBot.repositories;

import com.example.airAlertBot.entities.MonitoredChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonitoredChannelRepository extends JpaRepository<MonitoredChannel, Long> {
    Optional<MonitoredChannel> findByTelegramChatId(long telegramChatId);
}
