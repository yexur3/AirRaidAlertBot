package com.example.airAlertBot.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CityDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private long cityId;

}
