package com.example.airAlertBot.message_processing.kyiv;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.MessageProcessingFromTelegramChannels;

import java.util.List;

public class RealKyivMessageProcessing implements MessageProcessingFromTelegramChannels {
    @Override
    public List<AlertInfo> execute(String rawMessageText) {
        AlertEventType eventType;

        if(rawMessageText.contains("ОГОЛОШЕНА ПОВІТРЯНА ТРИВОГА")) {
            eventType = AlertEventType.ALERT_STARTED;
        } else if (rawMessageText.contains("ВІДБІЙ")) {
            eventType = AlertEventType.ALERT_ENDED;
        } else {
            return null;
        }

        DangerLevel dangerLevel = null;
        if (rawMessageText.contains("Жовтий рівень")) {
            dangerLevel = DangerLevel.YELLOW;
        } else if (rawMessageText.contains("Червоний рівень")) {
            dangerLevel = DangerLevel.RED;
        }

        String location = "";
        AlertScope scope = null;
        if (rawMessageText.contains("М. КИЇВ")){
            location = "м. Київ";
            scope = AlertScope.RAION;
        } else if (rawMessageText.contains("КИЇВСЬКА ОБЛАСТЬ")) {
            location = "Київська область";
            scope = AlertScope.REGION;
        } else if (rawMessageText.contains("КИЇВ ТА ОБЛАСТЬ")) {

        }

        Reason reason = rawMessageText.contains("БпЛА") ? Reason.DRONES : null;

        return List.of(new AlertInfo(eventType, location, scope, dangerLevel, reason));
    }
}
