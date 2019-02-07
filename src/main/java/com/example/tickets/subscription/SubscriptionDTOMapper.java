package com.example.tickets.subscription;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SubscriptionDTOMapper {
    SubscriptionDTOMapper INSTANCE = Mappers.getMapper(SubscriptionDTOMapper.class);

    SubscriptionDTO toDTO(Subscription subscription);

    Subscription fromDTO(SubscriptionDTO dto);

    List<SubscriptionDTO> toDTO(List<Subscription> subscription);

    List<Subscription> fromDTO(List<SubscriptionDTO> dto);
}
