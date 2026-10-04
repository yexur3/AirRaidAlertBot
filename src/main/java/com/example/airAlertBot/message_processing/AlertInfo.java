package com.example.airAlertBot.message_processing;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;

public record AlertInfo(AlertEventType eventType, AlertScope scope) {
}
