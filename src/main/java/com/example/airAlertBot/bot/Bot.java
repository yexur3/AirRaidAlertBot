package com.example.airAlertBot.bot;

import lombok.Value;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Bot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    public Bot(String token){
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void consume(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if(messageText.equals("/start")){
                SendMessage send = SendMessage.builder()
                        .chatId(chatId)
                        .text("Привіт! Я бот для сповіщень про повітряні тривоги.")
                        .build();

                try {
                    telegramClient.execute(send);
                } catch (TelegramApiException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
