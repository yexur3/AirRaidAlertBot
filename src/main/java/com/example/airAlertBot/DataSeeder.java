package com.example.airAlertBot;

import com.example.airAlertBot.entities.*;
import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.enums.Type;
import com.example.airAlertBot.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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

            Region regionKyiv = new Region();
            regionKyiv.setName("Київська");
            regionRepository.save(regionKyiv);

            Region regionChernihiv = new Region();
            regionChernihiv.setName("Чернігівська");
            regionRepository.save(regionChernihiv);

            Region kyiv = new Region();
            kyiv.setName("Київ");
            regionRepository.save(kyiv);

            String[] chernihivRaions = {"Корюківський", "Ніжинський", "Новгород-Сіверський", "Прилуцький", "Чернігівський"};

            String[] kyivRaions = {"Білоцерківський", "Бориспільський", "Броварський","Бучанський", "Вишгородський", "Обухівський", "Фастівський"};


            Raion chernihivRaion = null;
            for (var raion : chernihivRaions){
                Raion raionOfRegion = new Raion();

                raionOfRegion.setName(raion);
                raionOfRegion.setRegionId(regionChernihiv.getId());

                raionRepository.save(raionOfRegion);

                if(raion.equals("Чернігівський")){
                    chernihivRaion = raionOfRegion;
                }
            }

            for(var raion : kyivRaions){
                Raion raionOfRegion = new Raion();

                raionOfRegion.setName(raion);
                raionOfRegion.setRegionId(regionKyiv.getId());

                raionRepository.save(raionOfRegion);
            }

            Raion kyivRaion = new Raion();
            kyivRaion.setName("Київ");
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

            City chernihivCity = new City();

            chernihivCity.setName("Чернігів");
            chernihivCity.setRegionalCenter(true);
            chernihivCity.setRaionId(chernihivRaion.getId());

            cityRepository.save(chernihivCity);

            String[] districtsChe = {"Деснянський", "Новозаводський"};

            Arrays.sort(districtsChe);

            for(var districtName : districtsChe){
                CityDistrict cityDistrict = new CityDistrict();

                cityDistrict.setName(districtName);
                cityDistrict.setCityId(chernihivCity.getId());

                cityDistrictRepository.save(cityDistrict);
            }

        }

        if (monitoredChannelRepository.count() == 0){


            MonitoredChannel monitoredChannel = new MonitoredChannel();
            monitoredChannel.setTelegramChatId(-1001181169156L);
            monitoredChannel.setType(Type.UNOFFICIAL);
            monitoredChannel.setCityId(1);
            monitoredChannel.setChannelsId(ChannelsId.REALKYIV);
            monitoredChannelRepository.save(monitoredChannel);

//            MonitoredChannel monitoredChannel1 = new MonitoredChannel();
//            monitoredChannel1.setTelegramChatId(-1001223955273L);
//            monitoredChannel1.setType(Type.OFFICIAL);
//            monitoredChannel1.setChannelsId(ChannelsId.POVITRYANISYLYZSUKRAINE);
//            monitoredChannelRepository.save(monitoredChannel1);

            MonitoredChannel channelForChernihiv = new MonitoredChannel();
            channelForChernihiv.setTelegramChatId(-1001238700656L);
            channelForChernihiv.setType(Type.OFFICIAL);
            channelForChernihiv.setCityId(2);
            channelForChernihiv.setChannelsId(ChannelsId.LOVECHERNIHIV);
            monitoredChannelRepository.save(channelForChernihiv);

        }
    }
}
