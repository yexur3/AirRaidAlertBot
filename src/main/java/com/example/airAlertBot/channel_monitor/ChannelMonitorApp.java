package com.example.airAlertBot.channel_monitor;

import com.example.airAlertBot.entities.MonitoredChannel;
import com.example.airAlertBot.entities.UserSettings;
import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.MessageProcessingFromTelegramChannels;
import com.example.airAlertBot.message_processing.MessageStrategyFactory;
import com.example.airAlertBot.repositories.MonitoredChannelRepository;
import com.example.airAlertBot.repositories.UserSettingsRepository;
import it.tdlight.client.ConsoleInteractiveAuthenticationData;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.SimpleTelegramClientBuilder;
import it.tdlight.jni.TdApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;

public class ChannelMonitorApp implements AutoCloseable{

    private final SimpleTelegramClient client;
    private final MonitoredChannelRepository monitoredChannelRepository;
    private final TelegramClient telegramClient;
    private final UserSettingsRepository userSettingsRepository;

    public ChannelMonitorApp(SimpleTelegramClientBuilder clientBuilder, ConsoleInteractiveAuthenticationData authenticationData,
                             MonitoredChannelRepository monitoredChannelRepository,
                             TelegramClient telegramClient,
                             UserSettingsRepository userSettingsRepository){
        this.monitoredChannelRepository = monitoredChannelRepository;
        this.telegramClient = telegramClient;
        this.userSettingsRepository = userSettingsRepository;

        clientBuilder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);
        clientBuilder.addUpdateHandler(TdApi.UpdateNewMessage.class, this::onUpdateNewMessage);

        this.client = clientBuilder.build(authenticationData);
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update){
        if(update.authorizationState instanceof TdApi.AuthorizationStateReady){
            System.out.println("TdLib: Logged in");
            openMonitoredChannels();
        }
    }

    private void onUpdateNewMessage(TdApi.UpdateNewMessage update){
        long chatId = update.message.chatId;

        Optional<MonitoredChannel> monitoredChannelOpt = monitoredChannelRepository.findByTelegramChatId(chatId);

        if(monitoredChannelOpt.isEmpty()){
            return;
        }

        MonitoredChannel monitoredChannel = monitoredChannelOpt.get();

        String text = extractText(update.message.content, monitoredChannel.getChannelsId());

        List<UserSettings> users = userSettingsRepository.findAll();

        for (var user : users){

            if (user.getCityId() != null && user.getCityId() == monitoredChannel.getCityId()){
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(user.getChatId())
                        .text(text)
                        .build();

                try {
                    telegramClient.execute(sendMessage);
                } catch (TelegramApiException ex){
                    ex.printStackTrace();
                }
            }

        }
        System.out.println("Received message from monitored chat (" + monitoredChannel.getType() + "): " + text);
    }

    private AlertInfo extractText(TdApi.MessageContent content, ChannelsId channelsId){
        String rawText;

        if(content instanceof TdApi.MessageText messageText){
            rawText = messageText.text.text;
        } else if (content instanceof TdApi.MessagePhoto photo){
            rawText = photo.caption.text;
        } else if (content instanceof TdApi.MessageVideo video){
            rawText = video.caption.text;
        } else {
            return "Can't resolve type of text";
        }

        MessageProcessingFromTelegramChannels strategy = MessageStrategyFactory.getChannelFromSends(channelsId);
        return strategy.execute(rawText);
    }

    private void openMonitoredChannels() {
        client.send(new TdApi.LoadChats(new TdApi.ChatListMain(), 100), loaadResult -> {
            List<MonitoredChannel> channelList = monitoredChannelRepository.findAll();

            for(var channel : channelList){
                client.send(new TdApi.OpenChat(channel.getTelegramChatId()), result -> {
                    if  (result.get() instanceof TdApi.Ok){
                        System.out.println("Opened chat: " + channel.getTelegramChatId());
                    } else {
                        System.out.println("Failed to open channel: " + channel.getTelegramChatId());
                    }
                });
            }
        });
    }
}
