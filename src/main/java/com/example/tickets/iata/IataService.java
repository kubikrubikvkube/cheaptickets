package com.example.tickets.iata;

public interface IataService {
    Iata fromPlaceName(String placeName);

    String resolve(String place);
    Iata fromCode(String code);

    Iata save(IataDto dto);
}
