package com.example.tickets.iata;

public interface IataService {
    Iata fromPlaceName(String placeName);

    Iata fromCode(String code);

    Iata save(IataDto dto);
}
