package com.example.tickets.web;

import com.example.tickets.ticket.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class StatisticsController {
    private static final String STATISTICS_PAGE = "statistics";
    private final ObjectMapper mapper;
    private final TicketService ticketService;

    public StatisticsController(ObjectMapper mapper, TicketService ticketService) {
        this.ticketService = ticketService;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    @GetMapping("/admin/statistics")
    public String adminPage(Model model) {
        return STATISTICS_PAGE;
    }

    @ModelAttribute("ticketStatistics")
    public String ticketStatistics() throws JsonProcessingException {
        ObjectNode stat = mapper.createObjectNode();
        stat.put("1total tickets", ticketService.count());
        stat.put("2total tickets", ticketService.count());
        stat.put("3total tickets", ticketService.count());
        stat.put("4total tickets", ticketService.count());
        return mapper.writeValueAsString(stat);
    }
}
