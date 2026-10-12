package com.example.airAlertBot;

import com.example.airAlertBot.entities.*;
import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.enums.Type;
import com.example.airAlertBot.repositories.*;
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

    public DataSeeder(CityRepository cityRepository, CityDistrictRepository cityDistrictRepository,
                      MonitoredChannelRepository monitoredChannelRepository, RegionRepository regionRepository,
                      RaionRepository raionRepository){
        this.cityRepository = cityRepository;
        this.cityDistrictRepository = cityDistrictRepository;
        this.monitoredChannelRepository = monitoredChannelRepository;
        this.regionRepository = regionRepository;
        this.raionRepository = raionRepository;
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

            City city = cityRepository.findByName("Київ").orElseThrow();
            MonitoredChannel monitoredChannel = new MonitoredChannel();
            monitoredChannel.setTelegramChatId(-1001181169156L);
            monitoredChannel.setType(Type.UNOFFICIAL);
            monitoredChannel.setCityId(city.getId());
            monitoredChannel.setChannelsId(ChannelsId.REALKYIV);
            monitoredChannelRepository.save(monitoredChannel);

//            MonitoredChannel monitoredChannel1 = new MonitoredChannel();
//            monitoredChannel1.setTelegramChatId(-1001223955273L);
//            monitoredChannel1.setType(Type.OFFICIAL);
//            monitoredChannel1.setChannelsId(ChannelsId.POVITRYANISYLYZSUKRAINE);
//            monitoredChannelRepository.save(monitoredChannel1);

            city = cityRepository.findByName("Чернігів").orElseThrow();
            MonitoredChannel channelForChernihiv = new MonitoredChannel();
            channelForChernihiv.setTelegramChatId(-1001238700656L);
            channelForChernihiv.setType(Type.OFFICIAL);
            channelForChernihiv.setCityId(city.getId());
            channelForChernihiv.setChannelsId(ChannelsId.LOVECHERNIHIV);
            monitoredChannelRepository.save(channelForChernihiv);

        }
    }
}
