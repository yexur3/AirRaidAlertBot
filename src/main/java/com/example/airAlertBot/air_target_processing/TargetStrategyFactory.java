package com.example.airAlertBot.air_target_processing;

import com.example.airAlertBot.air_target_processing.chernihiv.SyspilneChernihivTargetsStrategy;
import com.example.airAlertBot.air_target_processing.shared.NotImplementedTagretProcessing;
import com.example.airAlertBot.enums.ChannelsId;

public class TargetStrategyFactory {
    public static TargetsProcessing getChannelFromSends(ChannelsId channelsId){
        switch (channelsId) {
            case SYSPILNE_CHERNIHIV -> {
                return new SyspilneChernihivTargetsStrategy();
            } case LOVE_CHERNIHIV, TRYVOHA_CHERNIHIV, REAL_KYIV, POVITRYANI_SYLY_ZS_UKRAINE -> {
                return new NotImplementedTagretProcessing();
            }
            default -> throw new IllegalStateException("Invalid channel id! " + channelsId);
        }
    }
}
