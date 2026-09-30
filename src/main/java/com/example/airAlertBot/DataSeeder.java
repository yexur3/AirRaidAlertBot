package com.example.airAlertBot;

import com.example.airAlertBot.entities.City;
import com.example.airAlertBot.entities.District;
import com.example.airAlertBot.repositories.CityRepository;
import com.example.airAlertBot.repositories.DistrictRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

            kyivCity.setName("Kyiv");

            cityRepository.save(kyivCity);

            String[] districts = {"Obolon", "Podil", "Darnytsia", "Desnianskyi", "Dniprovskyi", "Holosiivskyi", "Pecherskyi", "Solom_ianskyi", "Sviatoshynskyi", "Shevchenkivskyi"};

            for(var districtName : districts){
                District district = new District();

                district.setName(districtName);
                district.setCityId(kyivCity.getId());

                districtRepository.save(district);
            }

            City chernihivCity = new City();

            chernihivCity.setName("Chernihiv");

            cityRepository.save(chernihivCity);

        }
    }
}
