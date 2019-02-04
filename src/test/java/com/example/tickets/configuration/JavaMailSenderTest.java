package com.example.tickets.configuration;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class JavaMailSenderTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String user;

    @Value("${spring.mail.password}")
    private String password;

    @Test
    void java_mail_sender_can_send_email_via_smtps_protocol() throws MessagingException {
        ServerSetup server = new ServerSetup(port, null, "smtps");
        server.setVerbose(true);
        GreenMail greenMail = new GreenMail(server);
        GreenMailUser greenMailUser = greenMail.setUser(user, password);
        greenMail.start();

        final String subject = GreenMailUtil.random();
        final String body = GreenMailUtil.random();
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

        message.setFrom(greenMailUser.getEmail());
        message.setTo(greenMailUser.getEmail());
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(mimeMessage);

        assertTrue(greenMail.waitForIncomingEmail(5000, 1));
        assertEquals(greenMail.getReceivedMessages().length, 1);
        greenMail.stop();
    }
}