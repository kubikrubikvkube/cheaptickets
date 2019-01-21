package com.example.tickets.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;
    private final String from;


    public EmailSenderImpl(JavaMailSender mailSender, @Value("${spring.mail.smtp.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }


    @Override
    public void send(String recepientEmail, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(from);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }
}
