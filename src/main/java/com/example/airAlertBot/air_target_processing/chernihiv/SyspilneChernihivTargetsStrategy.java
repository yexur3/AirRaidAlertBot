package com.example.airAlertBot.air_target_processing.chernihiv;

import com.example.airAlertBot.air_target_processing.TargetInfo;
import com.example.airAlertBot.air_target_processing.TargetsProcessing;
import com.example.airAlertBot.enums.ObjectType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SyspilneChernihivTargetsStrategy implements TargetsProcessing {
    @Override
    public List<TargetInfo> execute(String rawMessageText) {
        List<TargetInfo> result = new ArrayList<>();

        ObjectType objectType = null;
        if(rawMessageText.startsWith("\uD83D\uDD3A")){
            if (rawMessageText.contains("Ударні БпЛА")) {
                objectType = ObjectType.DRONES;
            } else if (rawMessageText.contains("Реактивні БпЛА") || rawMessageText.toLowerCase().contains("реактивн")) {
                objectType = ObjectType.JET_DRONES;
            } else if (rawMessageText.contains("БпЛА")) {
                objectType = ObjectType.DRONES;
            } else if (rawMessageText.contains("«Бандероль»")) {
                objectType = ObjectType.BANDEROL;
            } else if (rawMessageText.contains("Балістична")) {
                objectType = ObjectType.BALLISTICS_MISSILES;
            } else {
                return result;
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
        Pattern pattern = Pattern.compile(
                "(?iu)\\bз\\s+(?=\\p{L}{11,12}\\b)Ч?е?р?н?і?г?і?в?щ?и?н?и?\\b"
        );
        String passingLocation = null;
        if (rawMessageText.contains("Чернігівщина:") || rawMessageText.contains("на Чернігівщині") || pattern.matcher(rawMessageText).find()) {
            passingLocation = "Чернігівська область";
        } else if (rawMessageText.contains("повз Сновськ")) {
            passingLocation = "Корюківський район";
        }

        String directionDetail = null;

        String destination = null;
        if(rawMessageText.contains("на північ Чернігівщини")) {
            result.add(new TargetInfo(objectType, origin, passingLocation, "Корюківський", directionDetail));
            result.add(new TargetInfo(objectType, origin, passingLocation, "Чернігівський", directionDetail));
            result.add(new TargetInfo(objectType, origin, passingLocation, "Новгород-Сіверський", directionDetail));
        } else if (rawMessageText.contains("на Київщину") || rawMessageText.contains("на північ Київщини")) {
            result.add(new TargetInfo(objectType, origin, passingLocation, "Вишгородський", directionDetail));
            result.add(new TargetInfo(objectType, origin, passingLocation, "Броварський", directionDetail));
        }

        return result;
    }
}
