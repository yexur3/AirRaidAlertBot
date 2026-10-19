package com.example.airAlertBot.message_processing;

import java.util.List;

public interface MessageProcessingFromTelegramChannels {
    List<AlertInfo> execute(String rawMessageText);
}
