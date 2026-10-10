package com.example.airAlertBot.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Raion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long regionId;

}
