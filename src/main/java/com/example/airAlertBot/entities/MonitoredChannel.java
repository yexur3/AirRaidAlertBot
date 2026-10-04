package com.example.airAlertBot.entities;

import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.enums.Type;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MonitoredChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long telegramChatId;

    @Enumerated(EnumType.STRING)
    private Type type;

    private long cityId;

    @Enumerated(EnumType.STRING)
    private ChannelsId channelsId;
}
