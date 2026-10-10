package com.example.airAlertBot.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private Long chatId;

    private Long regionId;

    private Long raionId;

    private Long cityId;

    private Long cityDistrictId;

    private boolean subscribeToNeighboring;

    private boolean unofficialEnabled;

}
