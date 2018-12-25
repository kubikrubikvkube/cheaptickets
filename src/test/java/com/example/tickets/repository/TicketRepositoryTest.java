package com.example.tickets.repository;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.repository.util.EntityConverter;
import com.example.tickets.service.TicketJson;
import com.example.tickets.service.TicketService;
import com.example.tickets.service.travelpayouts.request.LatestRequest;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.tickets.service.travelpayouts.request.Sorting.DISTANCE_UNIT_PRICE;
import static com.example.tickets.service.travelpayouts.request.Sorting.PRICE;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class TicketRepositoryTest {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void shouldGetTicketsAndSaveThemToDB() throws ServiceException {
        LatestRequest priceSorting = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting(PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();
        List<TicketJson> byPrice = ticketService.getLatest(priceSorting);

        List<TicketEntity> byPriceEntity = byPrice.stream().map(EntityConverter::toEntity).collect(Collectors.toList());
        log.info("Got sorted tickets: " + byPrice);
        Iterable<TicketEntity> savedTickets = ticketRepository.saveAll(byPriceEntity);
        log.info("Saved tickets: " + savedTickets);
        ticketRepository.deleteAll(savedTickets);
    }

    @Test
    public void shouldGetTicketsByDateAndSaveThemToDB() throws ServiceException {
        LocalDate now = LocalDateTime.now().toLocalDate();
        LocalDate nextMonth = now.plusMonths(1);
        int dayOfMonth = nextMonth.getDayOfMonth();
        LocalDate firstDayOfNextMonth = nextMonth.minusDays(dayOfMonth).plusDays(1);


        LatestRequest priceSorting = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .period_type("month")
                .beginning_of_period(firstDayOfNextMonth)
                .sorting(DISTANCE_UNIT_PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();


        List<TicketJson> byPrice = ticketService.getLatest(priceSorting);
        List<TicketEntity> byPriceEntity = byPrice.stream().map(EntityConverter::toEntity).collect(Collectors.toList());
        log.info("Got sorted tickets: " + byPrice);
        Iterable<TicketEntity> savedTickets = ticketRepository.saveAll(byPriceEntity);
        log.info("Saved tickets: " + savedTickets);
        List<TicketEntity> byDepartDate = ticketRepository.findByDepartDate(byPrice.get(0).getDepart_date());
        //нужно проверить что мы сохранили больше одного билета
        //взять его дату, найти в базе, узнать что найдено большое одной записи
        //сверить что этот билет есть среди тех, которые мы сохраняли
        //удалить все сохраненные по айдишнику
        assertNotNull(byDepartDate);
        assertThat(byDepartDate, not(empty()));
        log.info("Found by date: " + byDepartDate);
        ticketRepository.deleteAll(savedTickets);
    }

    @Test
    public void shouldBeRequestByBasicData() throws ServiceException {
        LocalDate now = LocalDateTime.now().toLocalDate();
        LocalDate nextMonth = now.plusMonths(1);
        int dayOfMonth = nextMonth.getDayOfMonth();
        LocalDate firstDayOfNextMonth = nextMonth.minusDays(dayOfMonth).plusDays(1);


        LatestRequest someTicketRequest = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .period_type("month")
                .beginning_of_period(firstDayOfNextMonth)
                .sorting(DISTANCE_UNIT_PRICE)
                .show_to_affiliates(false)
                .limit(1)
                .build();


        List<TicketJson> byPrice = ticketService.getLatest(someTicketRequest);
        Optional<TicketEntity> byPriceEntityOpt = byPrice.stream().map(EntityConverter::toEntity).findFirst();
        assertTrue(byPriceEntityOpt.isPresent());
        TicketEntity ticketEntity = byPriceEntityOpt.get();
        log.info("Got ticket: " + ticketEntity);

        ticketRepository.save(ticketEntity);

        String origin = ticketEntity.getOrigin();
        String destination = ticketEntity.getDestination();
        Date departDate = ticketEntity.getDepartDate();
        Integer value = ticketEntity.getValue();

        List<TicketEntity> byBasicData = ticketRepository.findByBasicData(origin, destination, departDate, value);
        assertNotNull(byBasicData);
        assertThat(byBasicData, hasSize(1));
        TicketEntity fetchedTicketEntity = byBasicData.get(0);
        assertEquals(ticketEntity, fetchedTicketEntity);
        ticketRepository.delete(ticketEntity);
    }

    @Test
    public void equalEntitiesShouldBeEqualToEachOther() {
        LatestRequest plr = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting(PRICE)
                .show_to_affiliates(true)
                .limit(5)
                .build();
        List<TicketJson> tickets = ticketService.getLatest(plr);
        assertThat(tickets, hasSize(greaterThanOrEqualTo(1)));
        TicketJson ticketJson = tickets.get(0);
        TicketEntity ticketEntity = EntityConverter.toEntity(ticketJson);
        TicketEntity savedEntity = ticketRepository.save(ticketEntity);
        assertEquals(ticketEntity, savedEntity);
        ticketRepository.delete(savedEntity);

    }
}
