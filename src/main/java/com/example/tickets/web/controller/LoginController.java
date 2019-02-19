package com.example.tickets.web.controller;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerDto;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.subscription.SubscriptionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    private final String MAIN_PAGE = "main";
    private final String LOGIN_PAGE = "login";
    private final OwnerService ownerService;

    public LoginController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("ownerDto", new OwnerDto());
        return LOGIN_PAGE;
    }

    @PostMapping("/login")
    public View loginPage(@ModelAttribute OwnerDto ownerDto, HttpSession session, Model model, HttpServletResponse response) {
        var userEmail = ownerDto.getEmail();
        if (!isValidEmailAddress(userEmail)) {
            return new RedirectView(LOGIN_PAGE);
        }

        Optional<Owner> ownerOptional = ownerService.find(userEmail);
        if (ownerOptional.isEmpty()) {
            ownerDto.setEmail(userEmail);
            ownerService.save(ownerDto);
        }

        response.addCookie(new Cookie("owner", userEmail));
        session.setAttribute("ownerDto", ownerService.find(userEmail));
        model.addAttribute("subscriptionDto", new SubscriptionDto());
        return new RedirectView(MAIN_PAGE);
    }
}