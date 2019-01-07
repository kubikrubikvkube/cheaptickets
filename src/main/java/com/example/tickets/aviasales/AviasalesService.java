package com.example.tickets.aviasales;

import com.example.tickets.ticket.Ticket;
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
    List<Ticket> getOneWayTicket(String originIAT, String destinationIAT, LocalDate date, int range) throws ServiceException;

    /**
     * Получение списка билетов туда-обратно
     *
     * @param originIAT      IAT код места отправления
     * @param destinationIAT IAT код места назначения
     * @param departure      дата отправления
     * @param returnDate     дата возращения
     * @param departRange    ближайший период отправления, в течении которого также ищутся билеты (в днях)
     * @param returnRange    ближайший период возвращения , в течении которого также ищутся билеты (в днях)
     * @return список найденных билетов
     * @throws ServiceException исключение во время выполнения
     */
    List<Ticket> getReturnTicket(String originIAT, String destinationIAT, LocalDate departure, LocalDate returnDate, int departRange, int returnRange) throws ServiceException;

    /**
     * Возвращает список билетов "Карта низких цен"
     *
     * @param originIAT  IAT код места отправления
     * @param departDate дата отправления
     * @param onlyDirect только билеты без пересадок
     * @return список найденных билетов
     */
    List<Ticket> getTicketsMap(String originIAT, LocalDate departDate, boolean onlyDirect);
}