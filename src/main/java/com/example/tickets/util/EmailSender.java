package com.example.tickets.util;

import org.springframework.stereotype.Component;

@Component
public interface EmailSender {
    void send(String recepientEmail, String subject, String msg);
}
