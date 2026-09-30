package com.example.airAlertBot;

import com.example.airAlertBot.bot.Bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Component
public class BotInitializer implements CommandLineRunner {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        botsApplication.registerBot(botToken, new Bot(botToken));
    }
}
