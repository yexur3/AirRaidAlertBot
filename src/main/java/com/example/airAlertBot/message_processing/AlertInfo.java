package com.example.airAlertBot.message_processing;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;

public record AlertInfo(
        AlertEventType eventType,
        AlertScope scope,
        DangerLevel dangerLevel,
        Reason reason) {
}
