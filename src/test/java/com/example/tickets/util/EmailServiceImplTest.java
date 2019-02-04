package com.example.tickets.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EmailServiceImplTest {

    @Autowired
    private EmailServiceImpl emailService;

    @Test
    void sendTest() throws MessagingException {
        emailService.sendTest("v.raskulin@gmail.com", "subject");
    }
}