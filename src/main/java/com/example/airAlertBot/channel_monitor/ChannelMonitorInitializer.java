package com.example.airAlertBot.channel_monitor;

import com.example.airAlertBot.entities.UserSettings;
import com.example.airAlertBot.repositories.CityRepository;
import com.example.airAlertBot.repositories.MonitoredChannelRepository;
import com.example.airAlertBot.repositories.UserSettingsRepository;
import it.tdlight.Init;
import it.tdlight.Log;
import it.tdlight.Slf4JLogMessageHandler;
import it.tdlight.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ChannelMonitorInitializer implements CommandLineRunner {

    @Value("${api.id}")
    private int apiId;

    @Value("${api.hash}")
    private String apiHash;

    private final MonitoredChannelRepository monitoredChannelRepository;
    private final TelegramClient telegramClient;
    private final UserSettingsRepository userSettingsRepository;
    private final CityRepository cityRepository;

    public ChannelMonitorInitializer(MonitoredChannelRepository monitoredChannelRepository,
                                     TelegramClient telegramClient,
                                     UserSettingsRepository userSettingsRepository,
                                     CityRepository cityRepository){
        this.monitoredChannelRepository = monitoredChannelRepository;
        this.telegramClient = telegramClient;
        this.userSettingsRepository = userSettingsRepository;
        this.cityRepository = cityRepository;
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

        new ChannelMonitorApp(clientBuilder, authenticationData, monitoredChannelRepository, telegramClient, userSettingsRepository, cityRepository);
    }
}
