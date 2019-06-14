package com.example.tickets.resources;

/**
 * Хранит список констант сервиса Travelpayouts, представленных в JSON формате
 */
public enum JsonResource {
    AIRLINES("travelpayouts/airlines.json"),
    ALLIANCES("travelpayouts/alliances.json"),
    CASES("travelpayouts/cases.json"),
    CITIES("travelpayouts/cities.json"),
    COUNTRIES("travelpayouts/countries.json"),
    PLANES("travelpayouts/planes.json"),
    ROUTES("travelpayouts/routes.json");

    private final String filename;

    JsonResource(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
