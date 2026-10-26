package com.example.airAlertBot.air_alert_processing;

import java.util.List;

public interface MessageProcessingFromTelegramChannels {
    List<AlertInfo> execute(String rawMessageText);
}
