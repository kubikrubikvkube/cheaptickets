package com.example.tickets.repository.constant;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class CityDTO {
    private String code;
    private String name;
    private JsonNode coordinates;
    private JsonNode cases;
    private String time_zone;
    private JsonNode name_translations;
    private String country_code;
}
