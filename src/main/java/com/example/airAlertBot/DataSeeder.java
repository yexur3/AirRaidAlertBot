package com.example.airAlertBot;

import com.example.airAlertBot.entities.*;
import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.enums.Type;
import com.example.airAlertBot.repositories.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final CityDistrictRepository cityDistrictRepository;
    private final MonitoredChannelRepository monitoredChannelRepository;
    private final RegionRepository regionRepository;
    private final RaionRepository raionRepository;
    private final ChannelRegionRepository channelRegionRepository;

    public DataSeeder(CityRepository cityRepository, CityDistrictRepository cityDistrictRepository,
                      MonitoredChannelRepository monitoredChannelRepository, RegionRepository regionRepository,
                      RaionRepository raionRepository, ChannelRegionRepository channelRegionRepository){
        this.cityRepository = cityRepository;
        this.cityDistrictRepository = cityDistrictRepository;
        this.monitoredChannelRepository = monitoredChannelRepository;
        this.regionRepository = regionRepository;
        this.raionRepository = raionRepository;
        this.channelRegionRepository = channelRegionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(regionRepository.count() == 0) {

            Region regionChernihiv = new Region();
            regionChernihiv.setName("Чернігівська область");
            regionRepository.save(regionChernihiv);

            String[] chernihivRaions = {"Корюківський", "Ніжинський", "Новгород-Сіверський", "Прилуцький", "Чернігівський"};
            Map<String, List<String>> citiesOfChernihivRaions = new HashMap<>(Map.of(
                    "Корюківський", List.of("Корюківка", "Мена"),
                    "Ніжинський", List.of("Ніжин", "Бахмач"),
                    "Новгород-Сіверський", List.of("Новгород-Сіверський", "Семенівка"),
                    "Прилуцький", List.of("Прилуки", "Ічня"),
                    "Чернігівський", List.of("Чернігів", "Городня")
            ));
            String[] chernihivDistricts = {"Деснянський", "Новозаводський"};

            for (var raion : chernihivRaions){
                Raion raionOfRegion = new Raion();

                raionOfRegion.setName(raion);
                raionOfRegion.setRegionId(regionChernihiv.getId());

                raionRepository.save(raionOfRegion);

                for (var city : citiesOfChernihivRaions.get(raion)) {
                    City cityRegion = new City();

                    cityRegion.setName(city);
                    cityRegion.setRaionId(raionOfRegion.getId());

                    cityRepository.save(cityRegion);

                    if (city.equals("Чернігів")) {
                        cityRegion.setRegionalCenter(true);

                        cityRepository.save(cityRegion);

                        for (var district : chernihivDistricts) {
                            CityDistrict cityDistrict = new CityDistrict();

                            cityDistrict.setName(district);
                            cityDistrict.setCityId(cityRegion.getId());

                            cityDistrictRepository.save(cityDistrict);
                        }
                    }
                }
            }

            // --------------------------------------------

            Region regionKyiv = new Region();
            regionKyiv.setName("Київська область");
            regionRepository.save(regionKyiv);

            String[] kyivRaions = {"Білоцерківський", "Бориспільський", "Броварський", "Бучанський", "Вишгородський", "Обухівський", "Фастівський"};
            Map<String, List<String>> citiesOfKyivRaions = new HashMap<>(Map.of(
                    "Білоцерківський", List.of("Біла Церква", "Богуслав", "Сквира", "Тараща", "Тетіїв", "Узин"),
                    "Бориспільський", List.of("Бориспіль", "Переяслав", "Яготин"),
                    "Броварський", List.of("Бровари", "Березань"),
                    "Бучанський", List.of("Буча", "Ірпінь", "Вишневе"),
                    "Вишгородський", List.of("Вишгород", "Славутич", "Прип'ять", "Чорнобиль"),
                    "Обухівський", List.of("Обухів", "Васильків", "Кагарлик", "Миронівка", "Ржищів", "Українка"),
                    "Фастівський", List.of("Фастів", "Боярка")
            ));
            for (var raion : kyivRaions){
                Raion raionOfRegion = new Raion();

                raionOfRegion.setName(raion);
                raionOfRegion.setRegionId(regionKyiv.getId());

                raionRepository.save(raionOfRegion);

                for(var cityInRaion : citiesOfKyivRaions.get(raion)){
                    City cityInRegion = new City();

                    cityInRegion.setName(cityInRaion);
                    cityInRegion.setRaionId(raionOfRegion.getId());
                    cityInRegion.setRegionalCenter(false);

                    cityRepository.save(cityInRegion);
                }
            }

            // --------------------------------------------


            Region kyiv = new Region();
            kyiv.setName("м. Київ");
            regionRepository.save(kyiv);


            Raion kyivRaion = new Raion();
            kyivRaion.setName("м. Київ");
            kyivRaion.setRegionId(kyiv.getId());
            raionRepository.save(kyivRaion);

            City kyivCity = new City();

            kyivCity.setName("Київ");
            kyivCity.setRegionalCenter(true);
            kyivCity.setRaionId(kyivRaion.getId());

            cityRepository.save(kyivCity);

            String[] districts = {"Оболонський", "Подільський", "Дарницький", "Деснянський", "Дніпровський", "Голосіївський", "Печерський", "Солом'янський", "Святошинський", "Шевченківський"};

            Arrays.sort(districts);

            for(var districtName : districts){
                CityDistrict cityDistrict = new CityDistrict();

                cityDistrict.setName(districtName);
                cityDistrict.setCityId(kyivCity.getId());

                cityDistrictRepository.save(cityDistrict);
            }

        }

        if (monitoredChannelRepository.count() == 0){

            Region region = regionRepository.findByName("м. Київ").orElseThrow();
            MonitoredChannel monitoredChannel = new MonitoredChannel();
            monitoredChannel.setTelegramChatId(-1001181169156L);
            monitoredChannel.setType(Type.UNOFFICIAL);
            monitoredChannel.setChannelsId(ChannelsId.REAL_KYIV);
            monitoredChannelRepository.save(monitoredChannel);

            ChannelRegion channelRegion = new ChannelRegion();
            channelRegion.setChannelId(monitoredChannel.getId());
            channelRegion.setRegionId(region.getId());
            channelRegionRepository.save(channelRegion);

            Region regionKyivOblast = regionRepository.findByName("Київська область").orElseThrow();
            ChannelRegion channelRegion2 = new ChannelRegion();
            channelRegion2.setChannelId(monitoredChannel.getId());
            channelRegion2.setRegionId(regionKyivOblast.getId());
            channelRegionRepository.save(channelRegion2);


//            MonitoredChannel monitoredChannel1 = new MonitoredChannel();
//            monitoredChannel1.setTelegramChatId(-1001223955273L);
//            monitoredChannel1.setType(Type.OFFICIAL);
//            monitoredChannel1.setChannelsId(ChannelsId.POVITRYANI_SYLY_ZS_UKRAINE);
//            monitoredChannelRepository.save(monitoredChannel1);

            region = regionRepository.findByName("Чернігівська область").orElseThrow();
            MonitoredChannel channelForChernihiv = new MonitoredChannel();
            channelForChernihiv.setTelegramChatId(-1001238700656L);
            channelForChernihiv.setType(Type.OFFICIAL);
            channelForChernihiv.setChannelsId(ChannelsId.LOVE_CHERNIHIV);
            monitoredChannelRepository.save(channelForChernihiv);

            ChannelRegion channelRegionChernihiv1 = new ChannelRegion();
            channelRegionChernihiv1.setRegionId(region.getId());
            channelRegionChernihiv1.setChannelId(channelForChernihiv.getId());
            channelRegionRepository.save(channelRegionChernihiv1);

            MonitoredChannel channelForChernihiv2 = new MonitoredChannel();
            channelForChernihiv2.setTelegramChatId(-1001841705974L);
            channelForChernihiv2.setType(Type.UNOFFICIAL);
            channelForChernihiv2.setChannelsId(ChannelsId.TRYVOHA_CHERNIHIV);
            monitoredChannelRepository.save(channelForChernihiv2);

            ChannelRegion channelRegionChernihiv2 = new ChannelRegion();
            channelRegionChernihiv2.setRegionId(region.getId());
            channelRegionChernihiv2.setChannelId(channelForChernihiv2.getId());
            channelRegionRepository.save(channelRegionChernihiv2);

            MonitoredChannel channelForChernihiv3 = new MonitoredChannel();
            channelForChernihiv3.setTelegramChatId(-1001386548824L);
            channelForChernihiv3.setType(Type.OFFICIAL);
            channelForChernihiv3.setChannelsId(ChannelsId.SYSPILNE_CHERNIHIV);
            monitoredChannelRepository.save(channelForChernihiv3);

            ChannelRegion channelRegionChernihiv3 = new ChannelRegion();
            channelRegionChernihiv3.setRegionId(region.getId());
            channelRegionChernihiv3.setChannelId(channelForChernihiv3.getId());
            channelRegionRepository.save(channelRegionChernihiv3);

        }
    }
}
