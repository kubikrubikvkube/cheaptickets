package com.example.tickets.subscription;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubscriptionDTOMapper {
    SubscriptionDTOMapper INSTANCE = Mappers.getMapper(SubscriptionDTOMapper.class);

    SubscriptionDTO toDTO(Subscription subscription);

    Subscription fromDTO(SubscriptionDTO dto);
}
