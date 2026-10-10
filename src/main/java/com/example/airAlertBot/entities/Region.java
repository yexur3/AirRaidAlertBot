package com.example.airAlertBot.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

}
