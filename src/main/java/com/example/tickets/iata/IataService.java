package com.example.tickets.iata;

public interface IataService {
    IATA fromPlaceName(String placeName);

    IATA fromCode(String code);

    IATA save(IATADTO dto);
}
