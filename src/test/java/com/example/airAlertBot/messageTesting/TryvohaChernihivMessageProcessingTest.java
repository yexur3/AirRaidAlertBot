package com.example.airAlertBot.messageTesting;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.air_alert_processing.AlertInfo;
import com.example.airAlertBot.air_alert_processing.chernihiv.TryvohaChernihivMessageProcessing;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TryvohaChernihivMessageProcessingTest {

    private final TryvohaChernihivMessageProcessing strategy = new TryvohaChernihivMessageProcessing();

    @Test
    void yellowDangerInManyRaions() {
        String text = "\uD83D\uDFE1 Повітряна тривога · жовтий рівень\n" +
                "\n" +
                "Дронова загроза:\n" +
                "• Ніжинський р-н\n" +
                "• Прилуцький р-н\n" +
                "\n" +
                "\uD83D\uDD04 Моніторинг Чернігів | Підписатись (https://t.me/trevoga_chernigov_novini)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Ніжинський", AlertScope.RAION, DangerLevel.YELLOW, Reason.DRONES),
                new AlertInfo(AlertEventType.ALERT_STARTED, "Прилуцький", AlertScope.RAION, DangerLevel.YELLOW, Reason.DRONES)
        ), result);
    }

    @Test
    void redDangerInManyRaions() {
        String text = "\uD83D\uDD34 Повітряна тривога · червоний рівень\n" +
                "\n" +
                "Ракетна загроза:\n" +
                "• Прилуцький р-н\n" +
                "• Ніжинський р-н\n" +
                "• Корюківський р-н\n" +
                "• Новгород-Сіверський р-н\n" +
                "• Чернігівський р-н\n" +
                "\n" +
                "\uD83D\uDD04 Моніторинг Чернігів | Підписатись (https://t.me/trevoga_chernigov_novini)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Прилуцький", AlertScope.RAION, DangerLevel.RED, Reason.MISSILES),
                new AlertInfo(AlertEventType.ALERT_STARTED, "Ніжинський", AlertScope.RAION, DangerLevel.RED, Reason.MISSILES),
                new AlertInfo(AlertEventType.ALERT_STARTED, "Корюківський", AlertScope.RAION, DangerLevel.RED, Reason.MISSILES),
                new AlertInfo(AlertEventType.ALERT_STARTED, "Новгород-Сіверський", AlertScope.RAION, DangerLevel.RED, Reason.MISSILES),
                new AlertInfo(AlertEventType.ALERT_STARTED, "Чернігівський", AlertScope.RAION, DangerLevel.RED, Reason.MISSILES)
        ), result);
    }

    @Test
    void endOfAlert() {
        String text = "\uD83D\uDFE2 Відбій повітряної тривоги\n" +
                "• Прилуцький р-н\n" +
                "• Корюківський р-н\n" +
                "• Новгород-Сіверський р-н\n" +
                "• Ніжинський р-н\n" +
                "\n" +
                "\uD83D\uDD04 Моніторинг Чернігів | Підписатись (https://t.me/trevoga_chernigov_novini)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_ENDED, "Прилуцький", AlertScope.RAION, null, null),
                new AlertInfo(AlertEventType.ALERT_ENDED, "Корюківський", AlertScope.RAION, null, null),
                new AlertInfo(AlertEventType.ALERT_ENDED, "Новгород-Сіверський", AlertScope.RAION, null, null),
                new AlertInfo(AlertEventType.ALERT_ENDED, "Ніжинський", AlertScope.RAION, null, null)
        ), result);
    }

    @Test
    void unknownMessage() {
        String text = "\uD83D\uDD34 Рівень підвищено до червоного\n" +
                "\n" +
                "Ракетна загроза:\n" +
                "• Ніжинський р-н\n" +
                "• Прилуцький р-н\n" +
                "\n" +
                "\uD83D\uDD04 Моніторинг Чернігів | Підписатись (https://t.me/trevoga_chernigov_novini)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(), result);
    }

}
