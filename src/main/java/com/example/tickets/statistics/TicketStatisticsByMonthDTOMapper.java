package com.example.tickets.statistics;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketStatisticsByMonthDTOMapper {
    TicketStatisticsByMonthDTOMapper INSTANCE = Mappers.getMapper(TicketStatisticsByMonthDTOMapper.class);

    TicketStatisticsByMonthDTO toDTO(TicketStatisticsByMonth statistics);

    TicketStatisticsByMonth fromDTO(TicketStatisticsByMonthDTO dto);
}
