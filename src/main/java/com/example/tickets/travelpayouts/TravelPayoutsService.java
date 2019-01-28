package com.example.tickets.travelpayouts;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.travelpayouts.request.*;

import java.util.List;

/**
 * TravelPayouts API
 */
public interface TravelPayoutsService {

    List<Ticket> getLatest(LatestRequest request);

    List<Ticket> getMonthMatrix(MonthMatrixRequest request);

    List<Ticket> getNearestPlacesMatrix(NearestPlacesMatrixRequest request);

    List<Ticket> getCheap(CheapRequest request);

    List<Ticket> getDirect(DirectRequest request);

    List<Ticket> getCalendar(CalendarRequest request);

    List<Ticket> getWeekMatrix(WeekMatrixRequest request);

}
