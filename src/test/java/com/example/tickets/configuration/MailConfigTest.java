package com.example.tickets.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MailConfigTest {

    @Autowired
    JavaMailSender javaMailSender;

    @Test
    void getJavaMailSender() throws MessagingException {

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setFrom("subticket19@yandex.ru");
        message.setTo("v.raskulin@gmail.com");
        message.setSubject("This is the message subject");
        message.setText("This is the message body");
        javaMailSender.send(mimeMessage);


    }
}