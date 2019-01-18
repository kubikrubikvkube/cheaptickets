package com.example.tickets.subscription;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionDTOMapper {
    SubscriptionDTOMapper INSTANCE = Mappers.getMapper(SubscriptionDTOMapper.class);

    SubscriptionDTO toDTO(Subscription subscription);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "creationTimestamp", ignore = true),
    })
    Subscription fromDTO(SubscriptionDTO dto);

    List<SubscriptionDTO> toDTO(List<Subscription> subscription);

    List<Subscription> fromDTO(List<SubscriptionDTO> dto);
}
