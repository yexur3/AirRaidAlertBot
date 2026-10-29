package com.example.airAlertBot.air_alert_processing.chernihiv;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.ObjectType;
import com.example.airAlertBot.air_alert_processing.AlertInfo;
import com.example.airAlertBot.air_alert_processing.MessageProcessingFromTelegramChannels;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyspilneChernihivMessageProcessing implements MessageProcessingFromTelegramChannels {

    @Override
    public List<AlertInfo> execute(String rawMessageText) {
        List<AlertInfo> result = new ArrayList<>();

        AlertEventType eventType;
        if(rawMessageText.contains("— повітряна тривога")) {
            eventType = AlertEventType.ALERT_STARTED;
        } else if (rawMessageText.contains("— відбій повітряної тривоги")) {
            eventType = AlertEventType.ALERT_ENDED;
        } else {
            return List.of();
        }

        DangerLevel dangerLevel = null;
        if(rawMessageText.contains("жовтий рівень")){
            dangerLevel = DangerLevel.YELLOW;
        } else if (rawMessageText.contains("червоний рівень")) {
            dangerLevel = DangerLevel.RED;
        }

        ObjectType objectType = null;
        if(rawMessageText.contains("Дронова загроза")) {
            objectType = ObjectType.DRONES;
        } else if (rawMessageText.contains("Ракетна загроза")) {
            objectType = ObjectType.MISSILES;
        }

        AlertScope scope = AlertScope.RAION;

        Pattern pattern = Pattern.compile(
                "([\\p{L}’ʼ'-]+)\\h+район\\h*(?:\\*\\*)?\\h*[—–−-]\\h*"
                        + "(?:відбій\\h+повітряної\\h+тривоги|повітряна\\h+тривога)",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );
        Matcher matcher = pattern.matcher(rawMessageText);
        if(matcher.find()) {
            String location = matcher.group(1);

            result.add(new AlertInfo(eventType, location, scope, dangerLevel, objectType));
        }
//        result.add(new AlertInfo(eventType, "Чернігівський", scope, dangerLevel, objectType));

        return result;
    }

}
