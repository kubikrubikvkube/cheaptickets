package com.example.tickets.web;

import com.example.tickets.owner.OwnerDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    private final String MAIN_PAGE = "main";

    @RequestMapping("/main")
    public String main(@ModelAttribute OwnerDTO ownerDTO, Model model) {
        //сюда приходит ownerDTO из модели логина с заполненным (или нет) emailом
        return MAIN_PAGE;
    }

}
