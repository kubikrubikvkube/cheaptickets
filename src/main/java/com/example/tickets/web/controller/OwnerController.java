package com.example.tickets.web.controller;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.web.commandobjects.OwnerDtoCommandObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class OwnerController {
    private static final String OWNER_PAGE = "owner";
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/admin/owner")
    public String ownerPage(Model model) {
        model.addAttribute("ownerDtoCO", new OwnerDtoCommandObject());
        return OWNER_PAGE;
    }

    @PostMapping("/admin/owner/delete")
    public String ownerDelete(@ModelAttribute OwnerDtoCommandObject ownerDto) {
        Long id = ownerDto.getId();
        Optional<Owner> ownerOptional = ownerService.find(id);
        if (ownerOptional.isPresent()) {
            ownerService.delete(id);
        }

        return OWNER_PAGE;
    }

    @PostMapping("/admin/owner/add")
    public String ownerAdd(@ModelAttribute OwnerDtoCommandObject ownerDto) {
        ownerService.save(ownerDto);
        return OWNER_PAGE;
    }

}
