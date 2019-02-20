package com.example.tickets.aviasales;

import com.example.tickets.ticket.TicketDto;
import com.example.tickets.util.ServiceException;

import java.time.LocalDate;
import java.util.List;

/**
 * Aviasales API
 */
public interface AviasalesService {
    /**
     * Получение списка билетов в одну сторону
     *
     * @param originIAT      IAT код места отправления
     * @param destinationIAT IAT код места назначения
     * @param date           дата отправления
     * @param range          ближайший период, в течении которого также ищутся билеты (в днях)
     * @return список найденных билетов
     * @throws ServiceException исключение во время выполнения
     */
    List<TicketDto> getOneWayTicket(String originIAT, String destinationIAT, LocalDate date, Integer range);
}
