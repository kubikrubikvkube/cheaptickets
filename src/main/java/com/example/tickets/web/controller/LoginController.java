package com.example.tickets.web.controller;

import com.example.tickets.owner.OwnerDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    private final String LOGIN_PAGE = "login";

    @RequestMapping("/")
    public String login(Model model) {
        model.addAttribute("ownerDTO", new OwnerDTO());
        return "login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("ownerDTO")) {
            model.addAttribute(new OwnerDTO());
        }

        return LOGIN_PAGE;
    }
}