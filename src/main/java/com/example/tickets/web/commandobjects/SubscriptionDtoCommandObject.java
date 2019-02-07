package com.example.tickets.web.commandobjects;

import com.example.tickets.subscription.SubscriptionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubscriptionDtoCommandObject extends SubscriptionDto implements CommandObject {
    private Long id;
}
