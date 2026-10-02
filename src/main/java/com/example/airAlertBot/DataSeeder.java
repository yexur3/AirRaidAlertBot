package com.example.airAlertBot;

import com.example.airAlertBot.entities.City;
import com.example.airAlertBot.entities.District;
import com.example.airAlertBot.repositories.CityRepository;
import com.example.airAlertBot.repositories.DistrictRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;

    public DataSeeder(CityRepository cityRepository, DistrictRepository districtRepository){
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(cityRepository.count() == 0) {
            City kyivCity = new City();

            kyivCity.setName("Київ");

            cityRepository.save(kyivCity);

            String[] districts = {"Оболонський", "Подільський", "Дарницький", "Деснянський", "Дніпровський", "Голосіївський", "Печерський", "Солом'янський", "Святошинський", "Шевченківський"};

            Arrays.sort(districts);

            for(var districtName : districts){
                District district = new District();

                district.setName(districtName);
                district.setCityId(kyivCity.getId());

                districtRepository.save(district);
            }

            City chernihivCity = new City();

            chernihivCity.setName("Чернігів");

            cityRepository.save(chernihivCity);

            String[] districtsChe = {"Деснянський", "Новозаводський"};

            Arrays.sort(districtsChe);

            for(var districtName : districtsChe){
                District district = new District();

                district.setName(districtName);
                district.setCityId(chernihivCity.getId());

                districtRepository.save(district);
            }

        }
    }
}
