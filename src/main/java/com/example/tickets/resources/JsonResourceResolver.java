package com.example.tickets.resources;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Ресолвер JSON ресурсов сервиса Travelpayouts
 */
public interface JsonResourceResolver {
    JsonNode resolve(JsonResource jsonResource);
    JsonNode resolve(String filename);
}
