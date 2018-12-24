package com.example.tickets.repository.util;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionEntity;
import com.example.tickets.ticket.TicketEntity;
import com.example.tickets.ticket.TicketJson;

public class EntityConverter {
    public static TicketEntity toEntity(TicketJson ticket) {
        TicketEntity entity = new TicketEntity();
        entity.setShowToAffiliates(ticket.getShow_to_affiliates());
        entity.setOrigin(ticket.getOrigin());
        entity.setDestination(ticket.getDestination());
        entity.setDepartDate(ticket.getDepart_date());
        entity.setReturnDate(ticket.getReturn_date());
        entity.setNumberOfChanges(ticket.getNumber_of_changes());
        entity.setValue(ticket.getValue());
        entity.setFoundAt(ticket.getFound_at());
        entity.setDistance(ticket.getDistance());
        entity.setActual(ticket.getActual());
        entity.setGate(ticket.getGate());
        entity.setAirline(ticket.getAirline());
        entity.setTransfers(ticket.getTransfers());
        return entity;
    }

    public static SubscriptionEntity toEntity(Subscription subscription) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setOwner(subscription.getOwner());
        entity.setOrigin(subscription.getOrigin());
        entity.setDestination(subscription.getDestination());
        return entity;
    }
}
