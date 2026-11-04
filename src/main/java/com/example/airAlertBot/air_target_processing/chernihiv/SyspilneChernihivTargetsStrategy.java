package com.example.airAlertBot.air_target_processing.chernihiv;

import com.example.airAlertBot.air_target_processing.TargetInfo;
import com.example.airAlertBot.air_target_processing.TargetsProcessing;
import com.example.airAlertBot.enums.ObjectType;

import java.util.ArrayList;
import java.util.List;

public class SyspilneChernihivTargetsStrategy implements TargetsProcessing {
    @Override
    public List<TargetInfo> execute(String rawMessageText) {
        List<TargetInfo> result = new ArrayList<>();

        ObjectType objectType = null;
        if(rawMessageText.startsWith("\uD83D\uDD3A")){
            if (rawMessageText.contains("Ударні БпЛА")) {
                objectType = ObjectType.DRONES;
            } else if (rawMessageText.contains("Реактивні БпЛА")) {
                objectType = ObjectType.JET_DRONES;
            } else if (rawMessageText.contains("БпЛА")) {
                objectType = ObjectType.DRONES;
            } else if (rawMessageText.contains("«Бандероль»")) {
                objectType = ObjectType.BANDEROL;
            } else if (rawMessageText.contains("Балістична")) {
                objectType = ObjectType.BALLISTICS_MISSILES;
            }
        }

        

        return result;
    }
}
