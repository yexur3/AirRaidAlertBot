package com.example.airAlertBot.air_alert_processing.chernihiv;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.ObjectType;
import com.example.airAlertBot.air_alert_processing.AlertInfo;
import com.example.airAlertBot.air_alert_processing.MessageProcessingFromTelegramChannels;

import java.util.ArrayList;
import java.util.List;

public class TryvohaChernihivMessageProcessing implements MessageProcessingFromTelegramChannels {
    @Override
    public List<AlertInfo> execute(String rawMessageText) {
        List<AlertInfo> result = new ArrayList<>();

        AlertEventType eventType;
        if(rawMessageText.contains("Повітряна тривога")) {
            eventType = AlertEventType.ALERT_STARTED;
        } else if (rawMessageText.contains("Відбій повітряної тривоги")) {
            eventType = AlertEventType.ALERT_ENDED;
        } else {
            return List.of();
        }

        DangerLevel dangerLevel = null;
        if (rawMessageText.contains("жовтий рівень")){
            dangerLevel = DangerLevel.YELLOW;
        } else if (rawMessageText.contains("червоний рівень")) {
            dangerLevel = DangerLevel.RED;
        }

        ObjectType objectType = null;
        if (rawMessageText.contains("Дронова загроза")) {
            objectType = ObjectType.DRONES;
        } else if (rawMessageText.contains("Ракетна загроза")) {
            objectType = ObjectType.MISSILES;
        }

        AlertScope scope = AlertScope.RAION;
        for (String line : rawMessageText.split("\\R")) {
            line = line.strip();

            if (line.startsWith("•")) {
                    String location = line.substring(1)
                            .strip().replaceFirst("\\s+р-н$", "");
                    result.add(new AlertInfo(eventType, location, scope, dangerLevel, objectType));
            }
        }

        return result;
    }
}
