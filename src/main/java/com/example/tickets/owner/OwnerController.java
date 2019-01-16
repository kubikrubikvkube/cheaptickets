package com.example.tickets.owner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OwnerController {
    private final Logger log = LoggerFactory.getLogger(OwnerController.class);

    private final OwnerService service;

    public OwnerController(OwnerService service) {
        this.service = service;
    }

    @RequestMapping(value = "/owner/add", params = {"name", "email"})
    public Owner add(@RequestParam String name, @RequestParam String email) {
        log.info("Owner add request '{} {}'", name, email);
        return service.add(name, email);
    }

    @RequestMapping(value = "/owner/get", params = {"name"})
    public Owner get(@RequestParam String name) {
        log.info("Owner get request '{}'", name);
        return service.get(name);
    }

    @RequestMapping(value = "/owner/delete", params = {"name"})
    public void delete(@RequestParam String name) {
        log.info("Owner delete request '{}'", name);
        service.delete(name);
    }
}
