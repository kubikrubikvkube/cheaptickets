package com.example.tickets.web;

import com.example.tickets.owner.OwnerDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Controller
public class MainController {
    private final String MAIN_PAGE = "main";
    private final String LOGIN_PAGE = "login";


    public static boolean isValidEmailAddress(String email) {
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
    public String main(@ModelAttribute OwnerDTO ownerDTO, Model model, BindingResult bindingResult) {
        if (ownerDTO == null) return LOGIN_PAGE;
        var userEmail = ownerDTO.getEmail();
        if (!isValidEmailAddress(userEmail)) return LOGIN_PAGE;

        return MAIN_PAGE;
    }

}
