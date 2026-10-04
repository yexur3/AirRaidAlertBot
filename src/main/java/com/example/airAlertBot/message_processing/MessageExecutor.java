package com.example.airAlertBot.message_processing;

public class MessageExecutor {
    private MessageProcessingFromTelegramChannels channel;

    public MessageExecutor(MessageProcessingFromTelegramChannels channel){
        this.channel = channel;
    }

    public void setChannel(MessageProcessingFromTelegramChannels channel){
        this.channel = channel;
    }

    public String execute(){
        return channel.execute();
    }
}
