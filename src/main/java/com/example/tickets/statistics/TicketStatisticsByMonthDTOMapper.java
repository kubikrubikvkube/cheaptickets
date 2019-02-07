package com.example.tickets.statistics;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TicketStatisticsByMonthDTOMapper {
    TicketStatisticsByMonthDTOMapper INSTANCE = Mappers.getMapper(TicketStatisticsByMonthDTOMapper.class);

    TicketStatisticsByMonthDTO toDTO(TicketStatisticsByMonth statistics);

    TicketStatisticsByMonth fromDTO(TicketStatisticsByMonthDTO dto);
}
