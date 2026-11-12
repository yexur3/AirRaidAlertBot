package com.example.airAlertBot.targetsMessageTesting;

import com.example.airAlertBot.air_target_processing.TargetInfo;
import com.example.airAlertBot.air_target_processing.chernihiv.SyspilneChernihivTargetsStrategy;
import com.example.airAlertBot.enums.ObjectType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SyspislneChernihivTargetsTest {

    private final SyspilneChernihivTargetsStrategy strategy = new SyspilneChernihivTargetsStrategy();

    @Test
    void testMessage1(){
        String text = "\uD83D\uDD3AРеактивні БпЛА з Чернгівщини на північ Київщини західним курсом.\n" +
                "\n" +
                "За інформацією Повітряних сил ЗСУ.";

        List<TargetInfo> result = strategy.execute(text);

        assertEquals(List.of(
                new TargetInfo(ObjectType.JET_DRONES, "з Чернігівщини", "Чернігівська область", "Вишгородський", null),
                new TargetInfo(ObjectType.JET_DRONES, "з Чернігівщини", "Чернігівська область", "Броварський", null)
        ), result);
    }

}
