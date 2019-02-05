package com.example.tickets.owner;

import com.example.tickets.subscription.Subscription;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OwnerDTO {
    private String email;
    private List<Subscription> subscriptions;
}
