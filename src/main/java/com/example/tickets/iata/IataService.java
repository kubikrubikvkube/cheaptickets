package com.example.tickets.iata;

public interface IataService {
    Iata resolve(String placeName);

    Iata fromCode(String code);

    Iata save(IataDto dto);
}
