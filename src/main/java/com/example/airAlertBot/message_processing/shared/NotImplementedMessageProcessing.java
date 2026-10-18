package com.example.airAlertBot.message_processing.shared;

import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.MessageProcessingFromTelegramChannels;

public class NotImplementedMessageProcessing implements MessageProcessingFromTelegramChannels {
    @Override
    public AlertInfo execute(String rawMessageText) {
        return null;
    }
}
