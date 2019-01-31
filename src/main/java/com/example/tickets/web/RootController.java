package com.example.tickets.web;

import com.example.tickets.owner.OwnerDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {
    @RequestMapping("/")
    public String login(Model model) {
        model.addAttribute(new OwnerDTO());
        return "login";
    }
}
