package com.example.tickets.subscription;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);

    SubscriptionDTO subscriptionToDTO(Subscription subscription);

    Subscription dtoToSubscription(SubscriptionDTO dto);
}
