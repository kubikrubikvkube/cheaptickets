package com.example.tickets.web;

import com.example.tickets.owner.OwnerDTO;
import com.example.tickets.owner.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OwnerController {
    private final Logger log = LoggerFactory.getLogger(OwnerController.class);
    private final OwnerService service;

    public OwnerController(OwnerService ownerService) {
        this.service = ownerService;
    }

    @GetMapping("/admin/owner")
    public String ownerPage(Model model) {
        model.addAttribute("ownerDTO", new OwnerDTO());
        return "owner";
    }

    @PostMapping("/admin/owner/delete")
    public String ownerDelete(@ModelAttribute OwnerDTO ownerDTO) {
        Long id = ownerDTO.getId();
        service.delete(id);
        return "owner";
    }

    @PostMapping("/admin/owner/add")
    public String ownerAdd(@ModelAttribute OwnerDTO ownerDTO) {
        service.save(ownerDTO);
        return "owner";
    }

}
