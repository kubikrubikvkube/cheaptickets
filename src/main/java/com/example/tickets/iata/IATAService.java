package com.example.tickets.iata;

import java.util.Optional;

public interface IATAService {
    Optional<IATA> fromPlaceName(String placeName);

    Optional<IATA> fromCode(String code);

    IATA save(IATADTO dto);
}
