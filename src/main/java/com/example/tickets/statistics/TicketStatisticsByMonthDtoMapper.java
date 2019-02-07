package com.example.tickets.statistics;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TicketStatisticsByMonthDtoMapper {
    TicketStatisticsByMonthDtoMapper INSTANCE = Mappers.getMapper(TicketStatisticsByMonthDtoMapper.class);

    TicketStatisticsByMonthDto toDto(TicketStatisticsByMonth statistics);

    TicketStatisticsByMonth fromDto(TicketStatisticsByMonthDto dto);
}
