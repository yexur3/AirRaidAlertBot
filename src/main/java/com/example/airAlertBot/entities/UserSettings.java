package com.example.airAlertBot.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long chatId;

    private Long cityId;

    @Column(nullable = false)
    private Long districtId;

    private boolean subscribeToNeighboring;

    private boolean unofficialEnabled;

}
