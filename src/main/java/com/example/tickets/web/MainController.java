package com.example.tickets.web;

import com.example.tickets.owner.OwnerDTO;
import com.google.common.base.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    private final String MAIN_PAGE = "main";
    private final String LOGIN_PAGE = "login";


    @RequestMapping("/main")
    public String main(@ModelAttribute OwnerDTO ownerDTO, Model model, BindingResult bindingResult) {
        if (ownerDTO == null) return LOGIN_PAGE;
        var userEmail = ownerDTO.getEmail();
        if (Strings.isNullOrEmpty(userEmail)) return LOGIN_PAGE;

        return MAIN_PAGE;
    }

}
