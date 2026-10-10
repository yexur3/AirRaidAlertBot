package com.example.airAlertBot.repositories;

import com.example.airAlertBot.entities.Raion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RaionRepository extends JpaRepository<Raion, Long> {
    List<Raion> findByRegionId(long id);
    Optional<Raion> findByName(String name);
}
