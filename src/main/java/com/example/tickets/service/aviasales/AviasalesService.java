package com.example.tickets.service.aviasales;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.service.TicketDTO;

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
    List<TicketDTO> getOneWayTicket(String originIAT, String destinationIAT, LocalDate date, int range) throws ServiceException;

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
    List<TicketDTO> getReturnTicket(String originIAT, String destinationIAT, LocalDate departure, LocalDate returnDate, int departRange, int returnRange) throws ServiceException;
}
