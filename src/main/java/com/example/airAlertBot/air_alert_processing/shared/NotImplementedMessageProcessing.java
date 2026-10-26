package com.example.airAlertBot.air_alert_processing.shared;

import com.example.airAlertBot.air_alert_processing.AlertInfo;
import com.example.airAlertBot.air_alert_processing.MessageProcessingFromTelegramChannels;

import java.util.List;

public class NotImplementedMessageProcessing implements MessageProcessingFromTelegramChannels {
    @Override
    public List<AlertInfo> execute(String rawMessageText) {
        return List.of();
    }
}
