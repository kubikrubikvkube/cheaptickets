package com.example.tickets.owner;

import com.example.tickets.subscription.Subscription;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OwnerDTO {
    private Long id;
    private LocalDateTime creationTimestamp;
    private String email;
    private List<Subscription> subscriptions;
}
