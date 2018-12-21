package com.example.tickets.httpclient;

import com.example.tickets.GetRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


public class HttpClient<T> {

    public ResponseEntity<T> get(GetRequest request, Class<T> clazz) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "gzip, deflate");
        headers.add("X-Access-Token", "a3bb9c6a5686a67be8be75805a1c4622");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String getRequestAsString = request.toString();
        return restTemplate.exchange(getRequestAsString, HttpMethod.GET, entity, clazz);
    }
}
