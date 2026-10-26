package com.example.airAlertBot.air_target_processing;

import com.example.airAlertBot.air_target_processing.chernihiv.SyspilneChernihivTargetsStrategy;
import com.example.airAlertBot.enums.ChannelsId;

public class TargetStrategyFactory {
    public static TargetsProcessing getChannelFromSends(ChannelsId channelsId){
        switch (channelsId) {
            case SYSPILNE_CHERNIHIV -> {
                return new SyspilneChernihivTargetsStrategy();
            }
            default -> throw new IllegalStateException("Invalid channel id! " + channelsId);
        }
    }
}
