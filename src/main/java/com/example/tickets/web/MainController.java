package com.example.tickets.web;

import com.example.tickets.owner.OwnerDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Controller
public class MainController {
    private final String MAIN_PAGE = "main";
    private final String LOGIN_PAGE = "login";


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

    @RequestMapping("/main")
    public String main(@CookieValue("JSESSIONID") String jsessionid, @ModelAttribute OwnerDTO ownerDTO, Model model, BindingResult bindingResult) {
        if (ownerDTO == null || bindingResult.hasErrors()) return LOGIN_PAGE;
        var userEmail = ownerDTO.getEmail();
        if (!isValidEmailAddress(userEmail)) return LOGIN_PAGE;
        //TODO можно запросто сделать автоматиический логин завязавшись на jsessionid!!!
        return MAIN_PAGE;
    }

}
