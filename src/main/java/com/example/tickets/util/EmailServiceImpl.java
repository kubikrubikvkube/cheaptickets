package com.example.tickets.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    TemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


    public void sendTest(String to, String subject) throws MessagingException {
// Prepare the evaluation context
        final Context ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("name", "slava");
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Sex", "Drugs", "RockNRoll"));
        ctx.setVariable("title", "Это заголовок");

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setSubject("Example HTML email with inline image");
        message.setFrom("subticket19@yandex.ru");
        message.setTo("v.raskulin@gmail.com");

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("emailtemplate.html", ctx);
        message.setText(htmlContent, true); // true = isHtml

        // Send mail
        emailSender.send(mimeMessage);
    }

}
