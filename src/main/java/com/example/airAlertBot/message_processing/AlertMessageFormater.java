package com.example.airAlertBot.message_processing;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;

public class AlertMessageFormater {

    public static String format(AlertInfo alertInfo, String cityName) {
        StringBuilder sb = new StringBuilder();

        if (alertInfo.eventType() == AlertEventType.ALERT_STARTED) {
            sb.append("🚨 Повітряна тривога: ").append(cityName).append("\n");

            if (alertInfo.dangerLevel() != null){
                String levelText = alertInfo.dangerLevel() == DangerLevel.RED ? "червоний" : "жовтий";
                sb.append("Рівень небезпеки: ").append(levelText).append("\n");
            }

            if (alertInfo.reason() != null){
                String reasonText = alertInfo.reason() == Reason.DRONES ? "загроза БпЛА" : "ракетна загроза";
                sb.append("Причина: ").append(reasonText).append("\n");
            }
        } else {
            sb.append("✅ Відбій повітряної тривоги: ").append(cityName);
        }

        return sb.toString();
    }

}
