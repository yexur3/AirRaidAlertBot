package com.example.airAlertBot.message_processing;

public interface MessageProcessingFromTelegramChannels {
    AlertInfo execute(String rawMessageText);
}
