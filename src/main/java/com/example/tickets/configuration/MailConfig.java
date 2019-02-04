package com.example.tickets.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {
//    private final String host;
//    private final int port;
//    private final String protocol;
//    private final String user;
//    private final String password;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.yandex.ru");
        mailSender.setPort(465);
        mailSender.setDefaultEncoding("UTF-8");

        mailSender.setUsername("subticket19");
        mailSender.setPassword("kjwhnvoazmpzvhsy");

        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtps.ssl.checkserveridentity", true);
        props.put("mail.smtps.ssl.trust", "*");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
