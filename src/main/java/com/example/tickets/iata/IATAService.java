package com.example.tickets.iata;

import java.util.Optional;

public interface IATAService {
    Optional<IATA> fromCode(String code);

    Optional<IATA> fromPlaceName(String placeName);
}
