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

        String origin = null;
        if(rawMessageText.contains("з Брянської області") || rawMessageText.contains("з Брянської обл.") || rawMessageText.contains("з Брянщини")) {
            origin = "з Брянської області";
        } else if (rawMessageText.contains("з Чернгівщини")) {
            origin = "з Чернігівщини";
        } else if (rawMessageText.contains("з півночі")) {
            origin = "з півночі Чернігівщини";
        }

        // TODO: Доробити нормально розподілення по локації де летить
        String passingLocation = null;
        if (rawMessageText.contains("Чернігівщина:") || rawMessageText.contains("на Чернігівщині")) {
            passingLocation = "Чернігівська область";
        } else if (rawMessageText.contains("повз Сновськ")) {
            passingLocation = "Корюківський район";
        }

        String directionDetail = null;

        String destination = null;
        if(rawMessageText.contains("на північ Чернігівщини")) {
            result.add(new TargetInfo(objectType, origin, passingLocation, "Корюкіський", directionDetail));
            result.add(new TargetInfo(objectType, origin, passingLocation, "Чернігівський", directionDetail));
            result.add(new TargetInfo(objectType, origin, passingLocation, "Новгород-Сіверський", directionDetail));
        }

        return result;
    }
}
