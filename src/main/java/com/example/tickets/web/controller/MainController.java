package com.example.tickets.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    private final String MAIN_PAGE = "main";
    private final String LOGIN_PAGE = "login";




    @RequestMapping("/main")
    public String main(Model model, BindingResult bindingResult) {

        return MAIN_PAGE;
    }

}
