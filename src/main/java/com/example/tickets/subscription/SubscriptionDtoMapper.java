package com.example.tickets.subscription;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SubscriptionDtoMapper {
    SubscriptionDtoMapper INSTANCE = Mappers.getMapper(SubscriptionDtoMapper.class);

    SubscriptionDto toDto(Subscription subscription);

    Subscription fromDto(SubscriptionDto dto);

    List<SubscriptionDto> toDto(List<Subscription> subscription);

    List<Subscription> fromDto(List<SubscriptionDto> dto);
}
