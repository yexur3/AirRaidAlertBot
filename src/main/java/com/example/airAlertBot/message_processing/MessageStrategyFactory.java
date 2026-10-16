package com.example.airAlertBot.message_processing;

import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.message_processing.chernihiv.LoveChernihivMessageProcessing;
import com.example.airAlertBot.message_processing.kyiv.RealKyivMessageProcessing;

public class MessageStrategyFactory {
    public static MessageProcessingFromTelegramChannels getChannelFromSends(ChannelsId channelsId){
        switch (channelsId){
            case REAL_KYIV -> {
                return new RealKyivMessageProcessing();
            }
            case LOVE_CHERNIHIV -> {
                return new LoveChernihivMessageProcessing();
            }
            default -> throw new IllegalStateException("Invalid channel id!" + channelsId);
        }
    }
}
