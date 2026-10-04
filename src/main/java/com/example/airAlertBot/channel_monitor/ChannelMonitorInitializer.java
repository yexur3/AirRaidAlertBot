package com.example.airAlertBot.channel_monitor;

import com.example.airAlertBot.repositories.MonitoredChannelRepository;
import it.tdlight.Init;
import it.tdlight.Log;
import it.tdlight.Slf4JLogMessageHandler;
import it.tdlight.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ChannelMonitorInitializer implements CommandLineRunner {

    @Value("${api.id}")
    private int apiId;

    @Value("${api.hash}")
    private String apiHash;

    private final MonitoredChannelRepository monitoredChannelRepository;

    public ChannelMonitorInitializer(MonitoredChannelRepository monitoredChannelRepository){
        this.monitoredChannelRepository = monitoredChannelRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Init.init();
        Log.setLogMessageHandler(1, new Slf4JLogMessageHandler());

        SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory();

        APIToken apiToken = new APIToken(apiId, apiHash);
        TDLibSettings settings = TDLibSettings.create(apiToken);

        Path sessionPath = Paths.get("tdlib-session");
        settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
        settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

        SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);

        var authenticationData = AuthenticationSupplier.consoleLogin();

        new ChannelMonitorApp(clientBuilder, authenticationData, monitoredChannelRepository);
    }
}
