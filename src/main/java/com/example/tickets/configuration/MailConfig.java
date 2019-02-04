package com.example.tickets.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port:465}")
    private int port;

    @Value("${spring.mail.defaultEncoding:UTF-8}")
    private String defaultEncoding;

    @Value("${spring.mail.username}")
    private String user;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${mail.transport.protocol:smtps}")
    private String protocol;

    @Value("${mail.smtp.auth:true}")
    private boolean smtpAuth;

    @Value("${mail.smtp.starttls.enable:true}")
    private boolean startTLS;

    @Value("${mail.smtps.ssl.checkserveridentity:true}")
    private boolean checkServerIdentity;

    @Value("${mail.smtps.ssl.trust:*}")
    private String sslTrust;

    @Value("${mail.debug:false}")
    private boolean mailDebug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setDefaultEncoding(defaultEncoding);
        mailSender.setUsername(user);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", startTLS);
        props.put("mail.smtps.ssl.checkserveridentity", checkServerIdentity);
        props.put("mail.smtps.ssl.trust", sslTrust);
        props.put("mail.debug", mailDebug);
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

}
