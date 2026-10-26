package com.example.airAlertBot.air_alert_processing.chernihiv;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.air_alert_processing.AlertInfo;
import com.example.airAlertBot.air_alert_processing.MessageProcessingFromTelegramChannels;

import java.util.ArrayList;
import java.util.List;

public class LoveChernihivMessageProcessing implements MessageProcessingFromTelegramChannels {
    @Override
    public List<AlertInfo> execute(String rawMessageText) {
        List<AlertInfo> result = new ArrayList<>();
        AlertEventType eventType;

        if (rawMessageText.contains("повітряна тривога")) {
            eventType = AlertEventType.ALERT_STARTED;
        } else if (rawMessageText.contains("відбій повітряної тривоги")) {
            eventType = AlertEventType.ALERT_ENDED;
        } else {
            return List.of();
        }

        DangerLevel dangerLevel = null;
        if (rawMessageText.contains("жовтий рівень")) {
            dangerLevel = DangerLevel.YELLOW;
        }  else if (rawMessageText.contains("червоний рівень")) {
            dangerLevel = DangerLevel.RED;
        }

        Reason reason = null;
        if (rawMessageText.contains("Дронова загроза")) {
            reason = Reason.DRONES;
        } else if (rawMessageText.contains("Ракетна загроза")) {
            reason = Reason.MISSILES;

        }

        result.add(new AlertInfo(eventType, "Чернігівський", AlertScope.RAION, dangerLevel, reason));
        return result;
    }
}
