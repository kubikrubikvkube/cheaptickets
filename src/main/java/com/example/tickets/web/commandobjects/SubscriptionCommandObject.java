package com.example.tickets.web.commandobjects;

import lombok.Data;

@Data
public class SubscriptionCommandObject implements CommandObject {
    private Long id;
    private Long maxPrice;
    private String origin;
    private String destination;
    private String originName;
    private String destinationName;
    private Integer tripDurationInDaysFrom;
    private Integer tripDurationInDaysTo;
}
