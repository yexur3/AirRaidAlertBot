package com.example.airAlertBot.air_alert_processing;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.ObjectType;

public record AlertInfo(
        AlertEventType eventType,
        String location,
        AlertScope scope,
        DangerLevel dangerLevel,
        ObjectType objectType) {
}
