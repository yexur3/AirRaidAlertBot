package com.example.airAlertBot.bot;

import com.example.airAlertBot.services.OnboardingService;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Bot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final OnboardingService onboardingService;

    public Bot(OnboardingService onboardingService, TelegramClient telegramClient){
        this.telegramClient = telegramClient;
        this.onboardingService = onboardingService;
    }

    @Override
    public void consume(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if(messageText.equals("/start")){
                onboardingService.handleStart(chatId, telegramClient);
            } else if (messageText.equals("/settings")) {
                onboardingService.citySettings(chatId, telegramClient);
            }
        } else if (update.hasCallbackQuery()){
            String callback = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            onboardingService.settings(chatId, callback, telegramClient);
        }
    }
}
