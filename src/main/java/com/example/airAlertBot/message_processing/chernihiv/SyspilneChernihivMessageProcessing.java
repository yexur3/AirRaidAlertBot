package com.example.airAlertBot.message_processing.chernihiv;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.MessageProcessingFromTelegramChannels;

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

        Reason reason = null;
        if(rawMessageText.contains("Дронова загроза")) {
            reason = Reason.DRONES;
        } else if (rawMessageText.contains("Ракетна загроза")) {
            reason = Reason.MISSILES;
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

            result.add(new AlertInfo(eventType, location, scope, dangerLevel, reason));
        }
//        result.add(new AlertInfo(eventType, "Чернігівський", scope, dangerLevel, reason));

        return result;
    }

}
