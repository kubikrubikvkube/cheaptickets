package com.example.tickets.resources;

import com.fasterxml.jackson.databind.JsonNode;

public interface ResourceResolver {
    JsonNode resolve(JsonResource jsonResource);

    JsonNode resolve(String filename);
}
