package com.example.tickets.travelpayouts;

import com.example.tickets.ticket.TicketDto;
import com.example.tickets.travelpayouts.request.*;

import java.util.List;

/**
 * TravelPayouts API
 */
public interface TravelPayoutsService {

    List<TicketDto> getLatest(LatestRequest request);

    List<TicketDto> getMonthMatrix(MonthMatrixRequest request);

    List<TicketDto> getNearestPlacesMatrix(NearestPlacesMatrixRequest request);

    List<TicketDto> getCheap(CheapRequest request);

    List<TicketDto> getDirect(DirectRequest request);

    List<TicketDto> getCalendar(CalendarRequest request);

    List<TicketDto> getWeekMatrix(WeekMatrixRequest request);

}
