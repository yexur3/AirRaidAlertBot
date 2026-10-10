package com.example.airAlertBot.repositories;

import com.example.airAlertBot.entities.CityDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityDistrictRepository extends JpaRepository<CityDistrict, Long> {

    List<CityDistrict> findByCityId(Long cityId);

}
