package com.example.tickets.ticketinformer;

import com.example.tickets.util.EmailSender;
import com.icegreen.greenmail.util.GreenMail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailSenderImplTest {
    @Autowired
    EmailSender emailSender;


    @Test
    public void send() throws MessagingException {
        GreenMail greenMail = new GreenMail(); //uses test ports by default
        greenMail.start();
        String expectedRecepient = "test@localhost.ru";
        String expectedSubject = "test subject";
        String expectedMsg = "We've found a best ticket for ya,pal!";
        emailSender.send(expectedRecepient, expectedSubject, expectedMsg);
        assertEquals(greenMail.getReceivedMessages().length, 1);
        greenMail.stop();
    }
}