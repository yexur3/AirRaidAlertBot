package com.example.airAlertBot.repositories;

import com.example.airAlertBot.entities.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertStatusRepository extends JpaRepository<AlertStatus, Long> {
    Optional<AlertStatus> findByLocation(String location);
}
