package com.example.tickets.iata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class IATAServiceImplTest {

    @Autowired
    IATAService service;

    @Test
    void fromCode() {
        Optional<IATA> iataOptional = service.fromCode("BER");
        assertTrue(iataOptional.isPresent());
        var iata = iataOptional.get();
        assertEquals("Берлин", iata.getPlace());
        assertEquals("BER", iata.getCode());
        assertNotNull(iata.getCreationTimestamp());
        assertNotNull(iata.getId());
    }
}