package com.example.tickets.statistics;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketStatisticsByMonthMapper {
    TicketStatisticsByMonthMapper INSTANCE = Mappers.getMapper(TicketStatisticsByMonthMapper.class);

    TicketStatisticsByMonthDTO toDTO(TicketStatisticsByMonth statistics);

    TicketStatisticsByMonth fromDTO(TicketStatisticsByMonthDTO dto);
}
