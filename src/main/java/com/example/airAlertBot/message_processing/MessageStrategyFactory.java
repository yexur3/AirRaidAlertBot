package com.example.airAlertBot.message_processing;

import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.message_processing.chernihiv.LoveChernihivMessageProcessing;
import com.example.airAlertBot.message_processing.kyiv.RealKyivMessageProcessing;
import com.example.airAlertBot.message_processing.shared.NotImplementedMessageProcessing;
import org.checkerframework.checker.units.qual.N;

public class MessageStrategyFactory {
    public static MessageProcessingFromTelegramChannels getChannelFromSends(ChannelsId channelsId){
        switch (channelsId){
            case REAL_KYIV -> {
                return new RealKyivMessageProcessing();
            }
            case LOVE_CHERNIHIV -> {
                return new LoveChernihivMessageProcessing();
            }
            case POVITRYANI_SYLY_ZS_UKRAINE, TRYVOHA_CHERNIHIV, SYSPILNE_CHERNIHIV -> {
                return new NotImplementedMessageProcessing();
            }
            default -> throw new IllegalStateException("Invalid channel id!" + channelsId);
        }
    }
}
