package com.example.tickets.statistics;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketStatisticsByDayDTOMapper {
    TicketStatisticsByDayDTOMapper INSTANCE = Mappers.getMapper(TicketStatisticsByDayDTOMapper.class);

    TicketStatisticsByDayDTO toDTO(TicketStatisticsByDay statistics);

    TicketStatisticsByDay fromDTO(TicketStatisticsByDayDTO dto);
}
