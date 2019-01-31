package com.example.tickets.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private final String LOGIN_PAGE = "login";

    @GetMapping("/login")
    public String loginPage(Model model) {
        return LOGIN_PAGE;
    }
}