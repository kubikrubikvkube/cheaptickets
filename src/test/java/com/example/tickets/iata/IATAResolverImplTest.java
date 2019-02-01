package com.example.tickets.iata;

import com.example.tickets.util.DefaultHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class IATAResolverImplTest {
    @Autowired
    private DefaultHttpClient httpClient;

    @Test
    void resolve() {
        var expectedResponse = "MOW";
        IATAResolver iataResolver = new IATAResolverImpl(httpClient);
        String result = iataResolver.resolve("масква");
        assertEquals(expectedResponse, result);
    }
}