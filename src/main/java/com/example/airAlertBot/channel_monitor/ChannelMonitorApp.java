package com.example.airAlertBot.channel_monitor;

import com.example.airAlertBot.entities.MonitoredChannel;
import com.example.airAlertBot.repositories.MonitoredChannelRepository;
import it.tdlight.client.ConsoleInteractiveAuthenticationData;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.SimpleTelegramClientBuilder;
import it.tdlight.jni.TdApi;

import java.util.List;
import java.util.Optional;

public class ChannelMonitorApp implements AutoCloseable{

    private final SimpleTelegramClient client;
    private final MonitoredChannelRepository monitoredChannelRepository;

    public ChannelMonitorApp(SimpleTelegramClientBuilder clientBuilder, ConsoleInteractiveAuthenticationData authenticationData, MonitoredChannelRepository monitoredChannelRepository){
        this.monitoredChannelRepository = monitoredChannelRepository;

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

        String text = extractText(update.message.content);

        System.out.println("Received message from monitored chat (" + monitoredChannel.getType() + "): " + text);
    }

    private String extractText(TdApi.MessageContent content){
        if(content instanceof TdApi.MessageText messageText){
            return messageText.text.text;
        } else if (content instanceof TdApi.MessagePhoto photo){
            return photo.caption.text;
        } else if (content instanceof TdApi.MessageVideo video){
            return video.caption.text;
        } else {
            return "Can't resolve type of text";
        }
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
