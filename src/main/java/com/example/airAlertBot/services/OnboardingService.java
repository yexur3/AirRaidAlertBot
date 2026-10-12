package com.example.airAlertBot.services;

import com.example.airAlertBot.entities.*;
import com.example.airAlertBot.repositories.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OnboardingService {

    private final CityRepository cityRepository;
    private final CityDistrictRepository cityDistrictRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final TelegramClient telegramClient;
    private final RegionRepository regionRepository;
    private final RaionRepository raionRepository;

    public OnboardingService(CityRepository cityRepository, CityDistrictRepository cityDistrictRepository,
                             UserSettingsRepository userSettingsRepository, TelegramClient telegramClient,
                             RegionRepository regionRepository, RaionRepository raionRepository){
        this.cityRepository = cityRepository;
        this.cityDistrictRepository = cityDistrictRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.telegramClient = telegramClient;
        this.regionRepository = regionRepository;
        this.raionRepository = raionRepository;
    }

    private InlineKeyboardMarkup makeKeyboard(LinkedHashMap<String, String> data){
        List<InlineKeyboardRow> rows = new ArrayList<>();


        for(var item : data.entrySet()){
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(item.getKey())
                    .callbackData(item.getValue())
                    .build();

            InlineKeyboardRow row = new InlineKeyboardRow(button);
            rows.add(row);
        }

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();

        return markup;
    }

    private void sendMessage(long chatId, String text, InlineKeyboardMarkup markup){
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .build();

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }

    public void handleCallback(long chatId, String callback){
        final UserSettings user = userSettingsRepository.findByChatId(chatId)
                .orElse(new UserSettings());

        user.setChatId(chatId);


        if (callback.startsWith("Region_")){

            long regionId = Long.parseLong(callback.replace("Region_", ""));
            user.setRegionId(regionId);
            user.setRaionId(null);
            user.setCityId(null);
            user.setCityDistrictId(null);
            userSettingsRepository.save(user);

            Region region = regionRepository.findById(regionId).orElseThrow();

            if (region.getName().equals("м. Київ")) {
                List<Raion> raions = raionRepository.findByRegionId(regionId);
                Raion raion = raions.getFirst();

                List<City> cities = cityRepository.findByRaionId(raion.getId());
                City city = cities.getFirst();

                user.setRaionId(raion.getId());
                user.setCityId(city.getId());

                userSettingsRepository.save(user);

                showDistricts(chatId, city.getId());
                return;
            }

            showRaions(chatId, regionId);
            return;

        } else if (callback.startsWith("Raion_")) {

            long raionId = Long.parseLong(callback.replace("Raion_", ""));
            user.setRaionId(raionId);
            user.setCityId(null);
            user.setCityDistrictId(null);
            userSettingsRepository.save(user);

            showCities(chatId, raionId);
            return;

        } else if (callback.startsWith("City_")) {

            long cityId = Long.parseLong(callback.replace("City_", ""));
            user.setCityId(cityId);
            user.setCityDistrictId(null);
            userSettingsRepository.save(user);

            City city = cityRepository.findById(cityId).orElseThrow();
            if (city.isRegionalCenter()){
                showDistricts(chatId, cityId);
            } else {
                showNeighboringChoose(chatId);
            }

            return;

        } else if (callback.startsWith("District_")) {

            long districtId = Long.parseLong(callback.replace("District_", ""));
            user.setCityDistrictId(districtId);
            userSettingsRepository.save(user);

            showNeighboringChoose(chatId);
            return;

        } else if (callback.startsWith("Neighboring_")) {

            boolean chooseNeighboring = Boolean.parseBoolean(callback.replace("Neighboring_", ""));
            user.setSubscribeToNeighboring(chooseNeighboring);
            userSettingsRepository.save(user);

            showUnofficialChoose(chatId);
            return;

        } else if (callback.startsWith("Unofficial_")) {

            boolean chooseUnofficial = Boolean.parseBoolean(callback.replace("Unofficial_", ""));
            user.setUnofficialEnabled(chooseUnofficial);
            userSettingsRepository.save(user);
        }

        Region region = regionRepository.findById(user.getRegionId()).orElseThrow();
        Raion raion = raionRepository.findById(user.getRaionId()).orElseThrow();
        City city = cityRepository.findById(user.getCityId()).orElseThrow();

        String districtLine = "";
        if (user.getCityDistrictId() != null){
            districtLine = cityDistrictRepository.findById(user.getCityDistrictId())
                    .map(district -> "Район міста: " + district.getName() + "\n")
                    .orElse("");
        }


        String text = """
                            ✅ Налаштування збережено!
                            Область: %s
                            Район: %s
                            Місто: %s
                            %sСусідні райони: %s
                            Неофіційні повідомлення: %s
                            
                            ⚠️ Важливо:
                            • Цей бот не замінює офіційні сигнали тривоги — завжди дій за офіційними інструкціями.
                            • Відсутність повідомлення від бота не означає відсутність небезпеки.
                            • Неофіційні повідомлення позначаються окремо і не є підтвердженою інформацією.
                            
                            Змінити налаштування можна командою /settings.
                            """.formatted(region.getName(), raion.getName(),
                city.getName(), districtLine,
                user.isSubscribeToNeighboring() ? "Так" : "Ні",
                user.isUnofficialEnabled() ? "Так" : "Ні");

        sendMessage(chatId, text, null);
    }

    public void handleStart(long chatId){
        String text = "Привіт! Я бот для сповіщень про повітряні тривоги.";

        sendMessage(chatId, text, null);

        showRegions(chatId);
    }

    public void showRegions(long chatId) {
        List<Region> regions = regionRepository.findAllByOrderByNameAsc();
        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        for (var item : regions){
            data.put(item.getName(), "Region_" + item.getId());
        }

        InlineKeyboardMarkup markup = makeKeyboard(data);

        String text = "Виберіть вашу область";

        sendMessage(chatId, text, markup);
    }

    public void showRaions(long chatId, long regionId) {
        List<Raion> raions = raionRepository.findByRegionId(regionId);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        for (var item : raions){
            data.put(item.getName(), "Raion_" + item.getId());
        }

        InlineKeyboardMarkup markup = makeKeyboard(data);

        String text = "Виберіть ваш район";

        sendMessage(chatId, text, markup);
    }

    public void showCities(long chatId, long raionId){
        List<City> cities = cityRepository.findByRaionId(raionId);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        for (var item : cities){
            data.put(item.getName(), "City_" + item.getId());
        }

        InlineKeyboardMarkup markup = makeKeyboard(data);

        String text = "Виберіть ваше місто (або найближче до Вас місто з наведених)";

        sendMessage(chatId, text, markup);
    }

    public void showDistricts(long chatId, long cityId) {
        List<CityDistrict> districts = cityDistrictRepository.findByCityId(cityId);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        for (var item : districts){
            data.put(item.getName(), "District_" + item.getId());
        }

        InlineKeyboardMarkup markup = makeKeyboard(data);

        String text = "Виберіть район у вашому місті";

        sendMessage(chatId, text, markup);
    }

    public void showNeighboringChoose(long chatId){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        data.put("Так", "Neighboring_true");
        data.put("Ні", "Neighboring_false");

        InlineKeyboardMarkup markup = makeKeyboard(data);

        String text = "Чи Ви хочете отримувати повідомлення про загрозу в сусідніх від Вас районах? (Якщо ні, то Ви будете отримувати повідомлення про загрозу лише у своєму районі.)";

        sendMessage(chatId, text, markup);
    }

    public void showUnofficialChoose(long chatId) {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        data.put("Так", "Unofficial_true");
        data.put("Ні", "Unofficial_false");

        InlineKeyboardMarkup markup = makeKeyboard(data);

        String text = "Чи хочете ви отримувати повідомлення з неофіційних джерел?";

        sendMessage(chatId, text, markup);
    }
}
