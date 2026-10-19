package com.example.airAlertBot.channel_monitor;

import com.example.airAlertBot.entities.*;
import com.example.airAlertBot.enums.AlertScope;
import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.message_processing.AlertInfo;
import com.example.airAlertBot.message_processing.AlertMessageFormater;
import com.example.airAlertBot.message_processing.MessageProcessingFromTelegramChannels;
import com.example.airAlertBot.message_processing.MessageStrategyFactory;
import com.example.airAlertBot.repositories.*;
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
    private final CityRepository cityRepository;
    private final RaionRepository raionRepository;
    private final RegionRepository regionRepository;

    public ChannelMonitorApp(SimpleTelegramClientBuilder clientBuilder, ConsoleInteractiveAuthenticationData authenticationData,
                             MonitoredChannelRepository monitoredChannelRepository,
                             TelegramClient telegramClient,
                             UserSettingsRepository userSettingsRepository,
                             CityRepository cityRepository, RaionRepository raionRepository,
                             RegionRepository regionRepository){
        this.monitoredChannelRepository = monitoredChannelRepository;
        this.telegramClient = telegramClient;
        this.userSettingsRepository = userSettingsRepository;
        this.cityRepository = cityRepository;
        this.raionRepository = raionRepository;
        this.regionRepository = regionRepository;

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

        List<AlertInfo> alertInfos = extractText(update.message.content, monitoredChannel.getChannelsId());

        if(alertInfos.isEmpty()) return;

        for(var alertInfo : alertInfos) {
            if (alertInfo.scope().equals(AlertScope.RAION)) {
                Raion raion = raionRepository.findByName(alertInfo.location()).orElseThrow();

                String text = AlertMessageFormater.format(alertInfo, raion.getName());

                List<UserSettings> users = userSettingsRepository.findByRaionId(raion.getId());

                if (users.isEmpty()) continue;

                for (var user : users){
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(user.getChatId())
                            .text(text)
                            .build();

                    try {
                        telegramClient.execute(sendMessage);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }

                }
                System.out.println("Received message from monitored chat (" + monitoredChannel.getType() + "): " + text);

            } else if (alertInfo.scope().equals(AlertScope.REGION)) {
                Region region = regionRepository.findByName(alertInfo.location()).orElseThrow();

                String text = AlertMessageFormater.format(alertInfo, region.getName());

                List<UserSettings> users = userSettingsRepository.findByRegionId(region.getId());

                if (users.isEmpty()) return;

                for (var user : users) {

                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(user.getChatId())
                            .text(text)
                            .build();

                    try {
                        telegramClient.execute(sendMessage);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println("Received message from monitored chat (" + monitoredChannel.getType() + "): " + text);
            }
        }

    }

    private List<AlertInfo> extractText(TdApi.MessageContent content, ChannelsId channelsId){
        String rawText;

        if(content instanceof TdApi.MessageText messageText){
            rawText = messageText.text.text;
        } else if (content instanceof TdApi.MessagePhoto photo){
            rawText = photo.caption.text;
        } else if (content instanceof TdApi.MessageVideo video){
            rawText = video.caption.text;
        } else {
            return null;
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
