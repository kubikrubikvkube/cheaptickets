package com.example.tickets.util;

import com.example.tickets.iata.IATA;
import com.example.tickets.iata.IATAService;
import com.example.tickets.owner.Owner;
import com.example.tickets.route.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
@PropertySource("classpath:mail.properties")
public class EmailServiceImpl implements EmailService {
    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final IATAService iataService;

    @Value("${spring.mail.from}")
    private String username;


    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine, IATAService iataService) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.iataService = iataService;
    }

    @Override
    public void sendNotification(Owner owner, Route route) {
        var departTicket = route.getDepartTicket();
        var returnTicket = route.getReturnTicket();

        Context ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("originName", departTicket.getOrigin());
        ctx.setVariable("destinationName", departTicket.getDestination());
        ctx.setVariable("tripDuration", route.getTripDurationInDays());
        ctx.setVariable("departureDate", departTicket.getDepartDate());
        ctx.setVariable("returnDate", returnTicket.getDepartDate());
        ctx.setVariable("numberOfChangesDepartTicket", departTicket.getNumberOfChanges());
        ctx.setVariable("numberOfChangesReturnTicket", returnTicket.getNumberOfChanges());
        ctx.setVariable("sumValue", route.getSumValue());

        MimeMessage mimeMessage;
        try {
            mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            IATA origin = iataService.fromCode(departTicket.getOrigin());
            IATA destination = iataService.fromCode(departTicket.getDestination());

            String subject = String.format("%s -> %s за %d рублей!", origin.getPlace(), destination.getPlace(), route.getSumValue());
            message.setSubject(subject);
            message.setFrom(username);
            message.setTo(owner.getEmail());
            String htmlContent = this.templateEngine.process("emailtemplate.html", ctx);
            message.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new ServiceException(e);
        }

        emailSender.send(mimeMessage);
    }
}
