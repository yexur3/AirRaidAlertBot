package com.example.airAlertBot.services;

import com.example.airAlertBot.entities.City;
import com.example.airAlertBot.entities.District;
import com.example.airAlertBot.entities.UserSettings;
import com.example.airAlertBot.repositories.CityRepository;
import com.example.airAlertBot.repositories.DistrictRepository;
import com.example.airAlertBot.repositories.UserSettingsRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class OnboardingService {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final UserSettingsRepository userSettingsRepository;

    public OnboardingService(CityRepository cityRepository, DistrictRepository districtRepository, UserSettingsRepository userSettingsRepository){
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.userSettingsRepository = userSettingsRepository;
    }

    public void handleStart(long chatId, TelegramClient telegramClient){
        SendMessage send = SendMessage.builder()
                .chatId(chatId)
                .text("Привіт! Я бот для сповіщень про повітряні тривоги.")
                .build();

        try {
            telegramClient.execute(send);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }

        citySettings(chatId, telegramClient);
    }

    public void settings(long chatId, String callback, TelegramClient telegramClient){
        if (callback.startsWith("City_")){
            districtSettings(chatId, callback, telegramClient);
        } else if (callback.startsWith("District_")) {
            neighboringSettings(chatId, callback, telegramClient);
        } else if (callback.startsWith("Neighboring_")) {
            unofficialSettings(chatId, callback, telegramClient);
        } else if (callback.startsWith("Unofficial_")) {
            endingOfSettings(chatId, callback, telegramClient);
        }
    }

    public void citySettings(long chatId, TelegramClient telegramClient){
        List<City> cityList = cityRepository.findAll();
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for(var city : cityList){
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(city.getName())
                    .callbackData("City_" + city.getId())
                    .build();

            InlineKeyboardRow row = new InlineKeyboardRow(button);
            rows.add(row);
        }

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();

        SendMessage citySelection = SendMessage.builder()
                .chatId(chatId)
                .text("Оберіть ваш обласний центр")
                .replyMarkup(markup)
                .build();

        try {
            telegramClient.execute(citySelection);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }

    public void districtSettings(long chatId, String callback, TelegramClient telegramClient){
        long cityId = Long.parseLong(callback.replace("City_", ""));

        UserSettings userSettings = userSettingsRepository.findByChatId(chatId)
                .orElse(new UserSettings());

        userSettings.setChatId(chatId);
        userSettings.setCityId(cityId);
        userSettings.setDistrictId(null);

        userSettingsRepository.save(userSettings);

        List<District> districts = districtRepository.findByCityId(cityId);
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (var district : districts){
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(district.getName())
                    .callbackData("District_" + district.getId())
                    .build();

            InlineKeyboardRow row = new InlineKeyboardRow(button);
            rows.add(row);
        }

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();

        SendMessage districtSelection = SendMessage.builder()
                .chatId(chatId)
                .text("Обери свій район")
                .replyMarkup(markup)
                .build();

        try {
            telegramClient.execute(districtSelection);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }

    public void neighboringSettings(long chatId, String callback, TelegramClient telegramClient){
        long districtId = Long.parseLong(callback.replace("District_", ""));

        UserSettings user = userSettingsRepository.findByChatId(chatId)
                .orElseThrow();

        user.setDistrictId(districtId);

        userSettingsRepository.save(user);

        List<InlineKeyboardRow> rows = new ArrayList<>();

        InlineKeyboardButton buttonTrue = InlineKeyboardButton.builder()
                .text("Так")
                .callbackData("Neighboring_true")
                .build();

        InlineKeyboardButton buttonFalse = InlineKeyboardButton.builder()
                .text("Ні")
                .callbackData("Neighboring_false")
                .build();

        rows.add(new InlineKeyboardRow(buttonTrue));
        rows.add(new InlineKeyboardRow(buttonFalse));

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();

        SendMessage neighboringSelection = SendMessage.builder()
                .chatId(chatId)
                .text("Чи Ви хочете отримувати повідомлення про загрозу в сусідніх від Вас районах? (Якщо ні, то Ви будете отримувати повідомлення про загрозу лише у своєму районі.)")
                .replyMarkup(markup)
                .build();

        try {
            telegramClient.execute(neighboringSelection);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }

    public void unofficialSettings(long chatId, String callback, TelegramClient telegramClient){
        boolean neighboringChoose = Boolean.parseBoolean(callback.replace("Neighboring_", ""));

        UserSettings user = userSettingsRepository.findByChatId(chatId)
                .orElseThrow();

        user.setSubscribeToNeighboring(neighboringChoose);

        userSettingsRepository.save(user);

        List<InlineKeyboardRow> rows = new ArrayList<>();

        InlineKeyboardButton buttonTrue = InlineKeyboardButton.builder()
                .text("Так")
                .callbackData("Unofficial_true")
                .build();

        InlineKeyboardButton buttonFalse = InlineKeyboardButton.builder()
                .text("Ні")
                .callbackData("Unofficial_false")
                .build();

        rows.add(new InlineKeyboardRow(buttonTrue));
        rows.add(new InlineKeyboardRow(buttonFalse));

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();

        SendMessage unofficialSelection = SendMessage.builder()
                .chatId(chatId)
                .text("Чи хочете ви отримувати повідомлення з неофіційних джерел?")
                .replyMarkup(markup)
                .build();

        try {
            telegramClient.execute(unofficialSelection);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }

    public void endingOfSettings(long chatId, String callback, TelegramClient telegramClient){
        boolean unofficialChoose = Boolean.parseBoolean(callback.replace("Unofficial_", ""));

        UserSettings user = userSettingsRepository.findByChatId(chatId)
                .orElseThrow();

        user.setUnofficialEnabled(unofficialChoose);

        userSettingsRepository.save(user);

        City city = cityRepository.findById(user.getCityId()).orElseThrow();
        District district = districtRepository.findById(user.getDistrictId()).orElseThrow();


        SendMessage lastMessage = SendMessage.builder()
                .chatId(chatId)
                .text("""
                            ✅ Налаштування збережено!
                            Місто: %s
                            Район: %s
                            Сусідні райони: %s
                            Неофіційні повідомлення: %s
                            
                            ⚠️ Важливо:
                            • Цей бот не замінює офіційні сигнали тривоги — завжди дій за офіційними інструкціями.
                            • Відсутність повідомлення від бота не означає відсутність небезпеки.
                            • Неофіційні повідомлення позначаються окремо і не є підтвердженою інформацією.
                            
                            Змінити налаштування можна командою /settings.
                            """.formatted(city.getName(), district.getName(), user.isSubscribeToNeighboring() ? "Так" : "Ні", user.isUnofficialEnabled() ? "Так" : "Ні"))
                .build();

        try {
            telegramClient.execute(lastMessage);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }
}
