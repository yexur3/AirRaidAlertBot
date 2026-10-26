package com.example.airAlertBot.air_alert_processing.kyiv;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.air_alert_processing.AlertInfo;
import com.example.airAlertBot.air_alert_processing.MessageProcessingFromTelegramChannels;

import java.util.ArrayList;
import java.util.List;

public class RealKyivMessageProcessing implements MessageProcessingFromTelegramChannels {
    @Override
    public List<AlertInfo> execute(String rawMessageText) {
        List<AlertInfo> result = new ArrayList<>();

        AlertEventType eventType;

        if(rawMessageText.contains("ОГОЛОШЕНА ПОВІТРЯНА ТРИВОГА")) {
            eventType = AlertEventType.ALERT_STARTED;
        } else if (rawMessageText.contains("ВІДБІЙ")) {
            eventType = AlertEventType.ALERT_ENDED;
        } else {
            return List.of();
        }

        DangerLevel dangerLevel = null;
        if (rawMessageText.contains("Жовтий рівень")) {
            dangerLevel = DangerLevel.YELLOW;
        } else if (rawMessageText.contains("Червоний рівень")) {
            dangerLevel = DangerLevel.RED;
        }

        Reason reason = rawMessageText.contains("БпЛА") ? Reason.DRONES : null;

        String location = "";
        AlertScope scope = null;
        if (rawMessageText.contains("М. КИЇВ")){
            location = "м. Київ";
            scope = AlertScope.RAION;
            result.add(new AlertInfo(eventType, location, scope, dangerLevel, reason));
        } else if (rawMessageText.contains("КИЇВСЬКА ОБЛАСТЬ")) {
            location = "Київська область";
            scope = AlertScope.REGION;
            result.add(new AlertInfo(eventType, location, scope, dangerLevel, reason));
        } else if (rawMessageText.contains("КИЇВ ТА ОБЛАСТЬ")) {
            location = "м. Київ";
            scope = AlertScope.RAION;
            result.add(new AlertInfo(eventType, location, scope, dangerLevel, reason));

            location = "Київська область";
            scope = AlertScope.REGION;
            result.add(new AlertInfo(eventType, location, scope, dangerLevel, reason));
        }

        return result;
    }
}
