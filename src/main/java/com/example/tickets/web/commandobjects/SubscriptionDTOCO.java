package com.example.tickets.web.commandobjects;

import com.example.tickets.subscription.SubscriptionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubscriptionDTOCO extends SubscriptionDTO {
    private Long id;
}
