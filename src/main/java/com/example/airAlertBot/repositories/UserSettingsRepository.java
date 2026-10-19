package com.example.airAlertBot.repositories;

import com.example.airAlertBot.entities.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    Optional<UserSettings> findByChatId(long chatId);
    List<UserSettings> findByRegionId(Long regionId);
    List<UserSettings> findByRaionId(Long raionId);

}
