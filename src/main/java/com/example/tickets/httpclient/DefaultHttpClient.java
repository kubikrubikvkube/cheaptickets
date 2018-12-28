package com.example.tickets.httpclient;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.java.Log;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:ticket.properties")
@Log
public class DefaultHttpClient<T> {
    @Value("${developer.token}")
    private String token;

    @Autowired
    private PoolingHttpClientConnectionManager connectionManager;


    public <T> T getWithHeaders(String getRequest, Class<T> clazz) {
        CloseableHttpClient client = HttpClients
                .custom()
                //default headers can be set in here with .setDefaultHeaders
                .setConnectionManager(connectionManager)
                .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "gzip, deflate");
        headers.add("X-Access-Token", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);
        log.info("Send request: " + getRequest);
        ResponseEntity<T> exchange = restTemplate.exchange(getRequest, HttpMethod.GET, httpEntity, clazz);
        if (!exchange.getStatusCode().is2xxSuccessful()) {
            log.severe(String.format("Request failed. Error code %s : %s", exchange.getStatusCode(), getRequest));
        }
        log.info("Got response: " + exchange);
        return exchange.getBody();
    }


    public <T> T getWithoutHeaders(String getRequest, Class<T> clazz) {
        CloseableHttpClient client = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        log.info("Send request: " + getRequest);
        ResponseEntity<T> exchange = restTemplate.exchange(getRequest, HttpMethod.GET, null, clazz);
        if (!exchange.getStatusCode().is2xxSuccessful()) {
            log.severe(String.format("Request failed. Error code %s : %s", exchange.getStatusCode(), getRequest));
        }
        log.info("Got response: " + exchange);

        return exchange.getBody();
    }

    public JsonNode getJsonResponseWithoutHeaders(String getRequest) {
        CloseableHttpClient client = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        log.info("Send request: " + getRequest);
        JsonNode node = restTemplate.getForObject(getRequest, JsonNode.class);
        log.info("Got response: " + node);
        return node;
    }

    public JsonNode getJsonResponseWithHeaders(String getRequest) {
        CloseableHttpClient client = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "gzip, deflate");
        headers.add("X-Access-Token", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);
        log.info("Send request: " + getRequest);
        ResponseEntity<JsonNode> exchange = restTemplate.exchange(getRequest, HttpMethod.GET, httpEntity, JsonNode.class);
        if (!exchange.getStatusCode().is2xxSuccessful()) {
            log.severe(String.format("Request failed. Error code %s : %s", exchange.getStatusCode(), getRequest));
        }
        log.info("Got response: " + exchange);
        return exchange.getBody();
    }
}
