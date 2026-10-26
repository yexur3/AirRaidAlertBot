package com.example.airAlertBot.messageTesting;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.chernihiv.SyspilneChernihivMessageProcessing;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SyspilneChernihivMessageProcessTest {

    private final SyspilneChernihivMessageProcessing strategy = new SyspilneChernihivMessageProcessing();

    @Test
    void startAlert() {
        String text = "\uD83D\uDFE1 Чернігівський район — повітряна тривога, жовтий рівень: Дронова загроза (жовтий рівень)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Чернігівський", AlertScope.RAION, DangerLevel.YELLOW, Reason.DRONES)
        ), result);
    }

    @Test
    void startRedAlert() {
        String text = "\uD83D\uDD34 Корюківський район — повітряна тривога, червоний рівень: Ракетна загроза (червоний рівень)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Корюківський", AlertScope.RAION, DangerLevel.RED, Reason.MISSILES)
        ), result);
    }

    @Test
    void endAlert() {
        String text = "\uD83D\uDFE2 Ніжинський район — відбій повітряної тривоги\n" +
                "\n" +
                "⚠\uFE0F Зверніть увагу, повітряна тривога досі триває у:\n" +
                "- Корюківський район\n" +
                "- Новгород-Сіверський район\n" +
                "- Чернігівський район";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_ENDED, "Ніжинський", AlertScope.RAION, null, null)
        ), result);
    }

    @Test
    void unknownMessage() {
        String text = "⚡\uFE0FЗміни у графіку погодинних відключень на 25 жовтня.\n" +
                "\n" +
                "\uD83D\uDD57 До 23:00 продовжуються відключення для черг 2/1 і 2/2.\n" +
                "\uD83D\uDD57 З 23:00 відключаються черги 2/2 і 4/1.\n" +
                "\n" +
                "За інформацією АТ «Чернігівобленерго».";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(), result);
    }


}
