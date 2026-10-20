package com.example.airAlertBot.messageTesting;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.Reason;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.kyiv.RealKyivMessageProcessing;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RealKyivMessageProcessingTest {

    private final RealKyivMessageProcessing strategy = new RealKyivMessageProcessing();

    @Test
    void cityAndRegionEnd_returnTwoTargets() {
        String text = "❎ КИЇВ ТА ОБЛАСТЬ - ВІДБІЙ ТРИВОГИ";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_ENDED, "м. Київ", AlertScope.RAION, null, null),
                new AlertInfo(AlertEventType.ALERT_ENDED, "Київська область", AlertScope.REGION, null, null)
        ), result);
    }

    @Test
    void cityEnd_returnOneTarget() {
        String text  = "❎ М. КИЇВ - ВІДБІЙ ТРИВОГИ";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_ENDED, "м. Київ", AlertScope.RAION, null, null)
        ), result);
    }
    

    @Test
    void cityAndRegionStart_returnTwoTarget() {
        String text = "\uD83D\uDEA8 КИЇВ ТА ОБЛАСТЬ - ОГОЛОШЕНА ПОВІТРЯНА ТРИВОГА";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "м. Київ", AlertScope.RAION, null, null),
                new AlertInfo(AlertEventType.ALERT_STARTED, "Київська область", AlertScope.REGION, null, null)
        ), result);
    }

    @Test
    void regionStart_returnOneTarget() {
        String text = "\uD83D\uDEA8 КИЇВСЬКА ОБЛАСТЬ - ОГОЛОШЕНА ПОВІТРЯНА ТРИВОГА\n" +
                "\n" +
                "\uD83D\uDFE1Жовтий рівень небезпеки через загрозу БпЛА";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Київська область", AlertScope.REGION, DangerLevel.YELLOW, Reason.DRONES)
        ), result);
    }
}
