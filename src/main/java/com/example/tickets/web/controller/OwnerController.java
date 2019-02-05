package com.example.tickets.web.controller;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerDTO;
import com.example.tickets.owner.OwnerService;
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
        model.addAttribute("ownerDTO", new OwnerDTO());
        return OWNER_PAGE;
    }

    @PostMapping("/admin/owner/delete")
    public String ownerDelete(@ModelAttribute OwnerDTO ownerDTO) {
        Optional<Owner> ownerOptional = ownerService.find(ownerDTO.getEmail());
        if (ownerOptional.isPresent()) {
            Owner owner = ownerOptional.get();
            ownerService.delete(owner.getId());
        }

        return OWNER_PAGE;
    }

    @PostMapping("/admin/owner/add")
    public String ownerAdd(@ModelAttribute OwnerDTO ownerDTO) {
        ownerService.save(ownerDTO);
        return OWNER_PAGE;
    }

}
