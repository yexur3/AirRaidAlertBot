package com.example.airAlertBot.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
    uniqueConstraints = {
            @UniqueConstraint(
                name = "uc_channel_region",
                columnNames ={"channel_id","region_id"}
        )
    }
)
public class ChannelRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "channel_id", nullable = false)
    private long channelId;

    @Column(name = "region_id", nullable = false)
    private long regionId;

}
