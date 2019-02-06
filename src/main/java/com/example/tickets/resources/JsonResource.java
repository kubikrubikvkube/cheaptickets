package com.example.tickets.resources;

public enum JsonResource {
    AIRLINES("airlines.json"),
    ALLIANCES("alliances.json"),
    CASES("cases.json"),
    CITIES("cities.json"),
    COUNTRIES("countries.json"),
    PLANES("planes.json"),
    ROUTES("routes.json");

    private final String filename;

    JsonResource(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
