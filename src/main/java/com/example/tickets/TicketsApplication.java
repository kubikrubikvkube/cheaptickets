package com.example.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@Controller
public class TicketsApplication {
    @RequestMapping("/")
    public String main() {
        return "main";
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketsApplication.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}

