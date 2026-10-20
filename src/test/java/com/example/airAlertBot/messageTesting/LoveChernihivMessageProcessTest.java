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
    void raionStart() {
        String text = "\uD83D\uDFE1 Чернігівський район — повітряна тривога, жовтий рівень: Дронова загроза (жовтий рівень)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Чернігівський", AlertScope.RAION, DangerLevel.YELLOW, Reason.DRONES)
        ), result);
    }

}
