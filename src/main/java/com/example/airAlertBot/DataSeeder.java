package com.example.airAlertBot;

import com.example.airAlertBot.entities.City;
import com.example.airAlertBot.entities.CityDistrict;
import com.example.airAlertBot.entities.MonitoredChannel;
import com.example.airAlertBot.enums.ChannelsId;
import com.example.airAlertBot.enums.Type;
import com.example.airAlertBot.repositories.CityRepository;
import com.example.airAlertBot.repositories.CityDistrictRepository;
import com.example.airAlertBot.repositories.MonitoredChannelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final CityDistrictRepository cityDistrictRepository;
    private final MonitoredChannelRepository monitoredChannelRepository;

    public DataSeeder(CityRepository cityRepository, CityDistrictRepository cityDistrictRepository,
                      MonitoredChannelRepository monitoredChannelRepository){
        this.cityRepository = cityRepository;
        this.cityDistrictRepository = cityDistrictRepository;
        this.monitoredChannelRepository = monitoredChannelRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(cityRepository.count() == 0) {
            City kyivCity = new City();

            kyivCity.setName("Київ");
            kyivCity.setOblastName("Київська");

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
            chernihivCity.setOblastName("Чернігівська");

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
