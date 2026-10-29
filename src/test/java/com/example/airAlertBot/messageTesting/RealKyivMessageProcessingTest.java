package com.example.airAlertBot.messageTesting;

import com.example.airAlertBot.enums.AlertEventType;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.DangerLevel;
import com.example.airAlertBot.enums.ObjectType;
import com.example.airAlertBot.air_alert_processing.AlertInfo;
import com.example.airAlertBot.air_alert_processing.kyiv.RealKyivMessageProcessing;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RealKyivMessageProcessingTest {

    private final RealKyivMessageProcessing strategy = new RealKyivMessageProcessing();

    @Test
    void cityAndRegionEnd_returnTwoTargets() {
        String text = "❎ КИЇВ ТА ОБЛАСТЬ - ВІДБІЙ ТРИВОГИ\n" +
                "ㅤ  (https://t.me/+IffoCwNTQWUwNzhi)\n" +
                "Надіслати новину @novosti_kieva_bot\n" +
                "\uD83D\uDC49 (https://t.me/+IffoCwNTQWUwNzhi)ПІДПИСАТИСЯ (https://t.me/+IffoCwNTQWUwNzhi)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_ENDED, "м. Київ", AlertScope.RAION, null, null),
                new AlertInfo(AlertEventType.ALERT_ENDED, "Київська область", AlertScope.REGION, null, null)
        ), result);
    }

    @Test
    void cityEnd_returnOneTarget() {
        String text  = "❎ М. КИЇВ - ВІДБІЙ ТРИВОГИ\n" +
                "ㅤ  (https://t.me/+IffoCwNTQWUwNzhi)\n" +
                "Надіслати новину @novosti_kieva_bot\n" +
                "\uD83D\uDC49ПІДПИСАТИСЯ (https://t.me/+IffoCwNTQWUwNzhi)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_ENDED, "м. Київ", AlertScope.RAION, null, null)
        ), result);
    }

    @Test
    void regionEnd_returnOneTarget() {
        String text = "❎ КИЇВСЬКА ОБЛАСТЬ - ВІДБІЙ ТРИВОГИ\n" +
                "ㅤ  (https://t.me/+IffoCwNTQWUwNzhi)\n" +
                "Надіслати новину @novosti_kieva_bot\n" +
                "\uD83D\uDC49ПІДПИСАТИСЯ (https://t.me/+IffoCwNTQWUwNzhi)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_ENDED, "Київська область", AlertScope.REGION, null, null)
        ), result);
    }

    @Test
    void cityAndRegionStart_returnTwoTarget() {
        String text = "\uD83D\uDEA8 КИЇВ ТА ОБЛАСТЬ - ОГОЛОШЕНА ПОВІТРЯНА ТРИВОГА\n" +
                "ㅤ  (https://t.me/+IffoCwNTQWUwNzhi)\n" +
                "⏺\uFE0FЧервоний рівень небезпеки через загрозу балістики та загрозу пусків ракет з Ту-22\n" +
                "\n" +
                "Надіслати новину @novosti_kieva_bot\n" +
                "\uD83D\uDC49 (https://t.me/+IffoCwNTQWUwNzhi)ПІДПИСАТИСЯ (https://t.me/+IffoCwNTQWUwNzhi)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "м. Київ", AlertScope.RAION, DangerLevel.RED, null),
                new AlertInfo(AlertEventType.ALERT_STARTED, "Київська область", AlertScope.REGION, DangerLevel.RED, null)
        ), result);
    }

    @Test
    void regionStartWithReason_returnOneTarget() {
        String text = "\uD83D\uDEA8 КИЇВСЬКА ОБЛАСТЬ - ОГОЛОШЕНА ПОВІТРЯНА ТРИВОГА\n" +
                "\n" +
                "\uD83D\uDFE1Жовтий рівень небезпеки через загрозу БпЛА\n" +
                "ㅤ  (https://t.me/+IffoCwNTQWUwNzhi)\n" +
                "Надіслати новину @novosti_kieva_bot\n" +
                "\uD83D\uDC49ПІДПИСАТИСЯ (https://t.me/+IffoCwNTQWUwNzhi)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "Київська область", AlertScope.REGION, DangerLevel.YELLOW, ObjectType.DRONES)
        ), result);
    }

    @Test
    void cityStartWithReason_returnOneTarget() {
        String text = "\uD83D\uDEA8 М. КИЇВ - ОГОЛОШЕНА ПОВІТРЯНА ТРИВОГА\n" +
                "\n" +
                "\uD83D\uDFE1Жовтий рівень небезпеки через загрозу БпЛА\n" +
                "ㅤ  (https://t.me/+IffoCwNTQWUwNzhi)\n" +
                "Надіслати новину @novosti_kieva_bot\n" +
                "\uD83D\uDC49ПІДПИСАТИСЯ (https://t.me/+IffoCwNTQWUwNzhi)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new AlertInfo(AlertEventType.ALERT_STARTED, "м. Київ", AlertScope.RAION, DangerLevel.YELLOW, ObjectType.DRONES)
        ), result);
    }

    @Test
    void unknownMessage() {
        String text = "Троє поліцейських і троє військовослужбовців отримали поранення через стрілянину в приміщенні ТЦК. Стрілка вже затримали, – повідомили в поліції \n" +
                "\n" +
                "Всі обставини події встановлюються. Постраждалих госпіталізовано, їм надається необхідна допомога.\n" +
                "\n" +
                "Вогонь відкрив чоловік, якого «було доставлено за порушення законодавства про мобілізацію».\n" +
                "\n" +
                "✈\uFE0F Свої кадри з місця надсилайте сюди – @novosti_kieva_bot\n" +
                "ㅤ  (https://t.me/+IffoCwNTQWUwNzhi)\n" +
                "Надіслати новину @novosti_kieva_bot\n" +
                "\uD83D\uDC49 (https://t.me/+IffoCwNTQWUwNzhi)ПІДПИСАТИСЯ (https://t.me/+IffoCwNTQWUwNzhi)";

        List<AlertInfo> result = strategy.execute(text);

        assertEquals(List.of(), result);
    }
}
