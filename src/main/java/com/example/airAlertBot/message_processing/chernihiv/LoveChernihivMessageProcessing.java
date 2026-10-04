package com.example.airAlertBot.message_processing.chernihiv;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.MessageProcessingFromTelegramChannels;

public class LoveChernihivMessageProcessing implements MessageProcessingFromTelegramChannels {
    @Override
    public AlertInfo execute(String rawMessageText) {
        AlertEventType eventType;

        if (rawMessageText.contains("повітряна тривога")) {
            eventType = AlertEventType.ALERT_STARTED;
        } else if (rawMessageText.contains("відбій повітряної тривоги")) {
            eventType = AlertEventType.ALERT_ENDED;
        } else {
            return null;
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

        return new AlertInfo(eventType, AlertScope.CITY_ONLY, dangerLevel, reason);
    }
}
