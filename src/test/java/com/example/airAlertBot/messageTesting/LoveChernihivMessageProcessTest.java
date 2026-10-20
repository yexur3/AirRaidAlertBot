package com.example.airAlertBot.messageTesting;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.chernihiv.LoveChernihivMessageProcessing;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoveChernihivMessageProcessTest {

    private final LoveChernihivMessageProcessing strategy = new LoveChernihivMessageProcessing();

    @Test
    void raionEnd() {
        String text = "\uD83D\uDFE2 Чернігівський район — відбій повітряної тривоги";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_ENDED, "Чернігівський", AlertScope.RAION, null, null)
        ), result);
    }

    @Test
    void raionStartWithDrones() {
        String text = "\uD83D\uDFE1 Чернігівський район — повітряна тривога, жовтий рівень: Дронова загроза (жовтий рівень)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Чернігівський", AlertScope.RAION, DangerLevel.YELLOW, Reason.DRONES)
        ), result);
    }

    @Test
    void raionStartWithRockets() {
        String text = "\uD83D\uDD34 Чернігівський район — повітряна тривога, червоний рівень: Ракетна загроза (червоний рівень)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Чернігівський", AlertScope.RAION, DangerLevel.RED, Reason.MISSILES)
        ), result);
    }

    @Test
    void raionStartWithoutReason() {
        String text = "Чернігівський район — повітряна тривога";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Чернігівський", AlertScope.RAION, null, null)
        ), result);
    }

    @Test
    void unknownMessage() {
        String text = "Про старт тепла заздалегідь не попереджатимуть: як у Чернігові вмикатимуть опалення ♨\uFE0F\n" +
                "\n" +
                "Публічних анонсів про підключення будинків чи лікарень до тепла цього року не буде, повідомили в міській раді. Містяни дізнаються про запуск опалення безпосередньо за теплими батареями у своїх оселях.\n" +
                "Таке рішення ухвалили з міркувань безпеки.\n" +
                "\n" +
                "Опалювальний сезон розпочнуть за стандартним правилом — коли середньодобова температура повітря три дні поспіль не перевищуватиме +8°C.\n" +
                "\n" +
                "Містян також просять утриматися від повідомлень у соцмережах про те, де саме з'явилося або зникло тепло, вода чи світло, аби не коригувати дії ворога.\n" +
                "\n" +
                "Повідомили в пресслужбі міської ради";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(), result);
    }

}
