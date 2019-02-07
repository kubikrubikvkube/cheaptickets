package com.example.tickets.iata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class IataServiceImplTest {

    @Autowired
    private IataService service;

    @Test
    void fromCode() {
        IATA iata = service.fromCode("BER");
        assertNotNull(iata);
        assertEquals("Берлин", iata.getPlace());
        assertEquals("BER", iata.getCode());
        assertNotNull(iata.getCreationTimestamp());
        assertNotNull(iata.getId());
    }
}