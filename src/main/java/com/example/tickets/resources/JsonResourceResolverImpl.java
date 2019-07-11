package com.example.tickets.resources;

import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonResourceResolverImpl implements JsonResourceResolver {
    private static final Logger log = LoggerFactory.getLogger(JsonResourceResolverImpl.class);
    private final ObjectMapper objectMapper;

    public JsonResourceResolverImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode resolve(JsonResource jsonResource) {
        log.debug("Resolving json resource {}", jsonResource);
        return resolve(jsonResource.getFilename());
    }

    @Override
    public JsonNode resolve(String filename) {
        log.debug("Resolving filename {}", filename);
        JsonNode node;
        try {
            Resource resource = new ClassPathResource(filename);
            node = objectMapper.readTree(resource.getURL());
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        log.debug("JsonNode resolved {}", node);
        return node;
    }
}
