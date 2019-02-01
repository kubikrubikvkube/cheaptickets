package com.example.tickets.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final String ADMIN_PAGE = "admin";

    @GetMapping("/admin")
    public String adminPage(Model model) {
        return ADMIN_PAGE;
    }
}
