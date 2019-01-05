package com.example.tickets.repository.constant;

import com.example.tickets.city.City;
import com.example.tickets.city.CityDTO;
import com.example.tickets.city.CityRepository;
import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest

public class CityRepositoryTest {
    Logger log = LoggerFactory.getLogger(CityRepositoryTest.class);
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ModelMapper mapper;

    @Test
    public void shouldSaveCityToDB() throws ServiceException, IOException {
        String json = "  {\n" +
                "    \"code\": \"MOW\",\n" +
                "    \"name\": \"Москва\",\n" +
                "    \"coordinates\": {\n" +
                "      \"lon\": 37.617633,\n" +
                "      \"lat\": 55.755786\n" +
                "    },\n" +
                "    \"cases\": {\n" +
                "      \"ro\": \"Москвы\",\n" +
                "      \"da\": \"Москве\",\n" +
                "      \"vi\": \"в Москву\",\n" +
                "      \"tv\": \"Москвой\",\n" +
                "      \"pr\": \"Москве\"\n" +
                "    },\n" +
                "    \"time_zone\": \"Europe/Moscow\",\n" +
                "    \"name_translations\": {\n" +
                "      \"en\": \"Moscow\"\n" +
                "    },\n" +
                "    \"country_code\": \"RU\"\n" +
                "  }";

        ObjectReader reader = new ObjectMapper().readerFor(CityDTO.class);
        CityDTO dto = reader.readValue(json);
        City city = mapper.map(dto, City.class);
        cityRepository.save(city);
        Optional<City> byId = cityRepository.findById(city.getId());
        assertTrue(byId.isPresent());
        City savedCity = byId.get();
        assertEquals(city.getCases(), savedCity.getCases());
        assertEquals(city.getCoordinates(), savedCity.getCoordinates());
        assertEquals(city.getName_translations(), savedCity.getName_translations());
        cityRepository.delete(city);
    }
}
