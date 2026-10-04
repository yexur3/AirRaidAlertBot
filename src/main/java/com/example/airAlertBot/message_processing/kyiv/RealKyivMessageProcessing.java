package com.example.airAlertBot.message_processing.kyiv;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.MessageProcessingFromTelegramChannels;

public class RealKyivMessageProcessing implements MessageProcessingFromTelegramChannels {
    @Override
    public AlertInfo execute(String rawMessageText) {
        AlertEventType eventType;

        if(rawMessageText.contains("ОГОЛОШЕНА ПОВІТРЯНА ТРИВОГА")) {
            eventType = AlertEventType.ALERT_STARTED;
        } else if (rawMessageText.contains("ВІДБІЙ")) {
            eventType = AlertEventType.ALERT_ENDED;
        } else {
            return null;
        }

        AlertScope scope;
        if (rawMessageText.contains("М. КИЇВ") && !rawMessageText.contains("ОБЛАСТЬ")){
            scope = AlertScope.CITY_ONLY;
        } else if (!rawMessageText.contains("М. КИЇВ") && rawMessageText.contains("ОБЛАСТЬ") && !rawMessageText.contains("КИЇВ ТА")){
            scope = AlertScope.REGION_ONLY;
        } else {
            scope = AlertScope.CITY_AND_REGION;
        }

        DangerLevel dangerLevel = null;
        if (rawMessageText.contains("Жовтий рівень")) {
            dangerLevel = DangerLevel.YELLOW;
        } else if (rawMessageText.contains("Червоний рівень")) {
            dangerLevel = DangerLevel.RED;
        }

        Reason reason = rawMessageText.contains("БпЛА") ? Reason.DRONES : null;

        return new AlertInfo(eventType, scope, dangerLevel, reason);
    }
}
