package com.example.airAlertBot;

import com.example.airAlertBot.bot.Bot;
import com.example.airAlertBot.services.OnboardingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class BotInitializer implements CommandLineRunner {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final OnboardingService onboardingService;
    private final TelegramClient telegramClient;

    public BotInitializer(OnboardingService onboardingService, TelegramClient telegramClient){
        this.onboardingService = onboardingService;
        this.telegramClient = telegramClient;
    }

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        botsApplication.registerBot(botToken, new Bot(onboardingService, telegramClient));
    }
}
