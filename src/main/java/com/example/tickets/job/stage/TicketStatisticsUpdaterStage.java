package com.example.tickets.job.stage;

import com.example.tickets.statistics.*;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.google.common.primitives.Doubles;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.String.format;

@Component
public class TicketStatisticsUpdaterStage extends AbstractStage {
    private final Logger log = LoggerFactory.getLogger(TicketStatisticsUpdaterStage.class);
    private final TicketStatisticsRepository statisticsRepository;
    private final TicketRepository ticketRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TicketStatisticsByMonthDTOMapper ticketStatisticsByMonthDTOMapper;

    public TicketStatisticsUpdaterStage(TicketStatisticsRepository statisticsRepository, TicketRepository ticketRepository, SubscriptionRepository subscriptionRepository, TicketStatisticsByMonthDTOMapper ticketStatisticsByMonthDTOMapper) {
        this.statisticsRepository = statisticsRepository;
        this.ticketRepository = ticketRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.ticketStatisticsByMonthDTOMapper = ticketStatisticsByMonthDTOMapper;
    }


    @Override
    public StageResult call() {
        var startTime = Instant.now().toEpochMilli();
        log.info("TicketStatisticsUpdaterJob started");
        statisticsRepository.deleteAll(); //TODO обновление, а не удаление и создание заново
        Iterable<Subscription> allSubscriptions = subscriptionRepository.findAll();
        for (Subscription subscription : allSubscriptions) {
            var origin = subscription.getOrigin();
            var destination = subscription.getDestination();
            Optional<TicketStatistics> subscriptionStatisticsOpt = statisticsRepository.findByOriginAndDestination(origin, destination);
            TicketStatistics statistics = subscriptionStatisticsOpt.orElseGet(TicketStatistics::new);
            statistics.setOrigin(origin);
            statistics.setDestination(destination);
            statistics.setTicketStatisticsByMonth(byMonth(subscription));
            statisticsRepository.save(statistics);
        }

        var endTime = Instant.now().toEpochMilli();
        log.info(format("TicketStatisticsUpdaterJob finished in %d ms", endTime - startTime));
        return null;
    }

    private List<TicketStatisticsByMonth> byMonth(Subscription s) {
        List<Ticket> subscriptionTickets = ticketRepository.findBySubscription(s);
        var earliestTicketForSubscriptionOpt = subscriptionTickets.stream().min(Comparator.comparing(Ticket::getDepartDate));
        var latestTicketOpt = subscriptionTickets.stream().max(Comparator.comparing(Ticket::getDepartDate));
        if (earliestTicketForSubscriptionOpt.isEmpty() || latestTicketOpt.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate earliestDepartDate = earliestTicketForSubscriptionOpt.get().getDepartDate();
        LocalDate latestDepartDate = latestTicketOpt.get().getDepartDate();

        List<TicketStatisticsByMonth> statisticsList = new ArrayList<>();
        long monthsRange = LocalDate.from(earliestDepartDate).until(latestDepartDate, ChronoUnit.MONTHS);
        //TODO здесь мы заново переписываем объекты статистики а не апдейтим старые поэтому createdAt и updatedAt один и тот же таймстамп
        for (int i = 0; i < monthsRange; i++) {
            int year = earliestDepartDate.plusMonths(i).getYear();
            Month month = earliestDepartDate.plusMonths(i).getMonth();
            DescriptiveStatistics ds = new DescriptiveStatistics();
            subscriptionTickets
                    .stream()
                    .filter(ticket -> ticket.getDepartDate().getMonth().equals(month))
                    .forEach(ticket -> ds.addValue(ticket.getValue()));

            TicketStatisticsByMonthDTO statDTO = new TicketStatisticsByMonthDTO();
            statDTO.setYear(year);
            statDTO.setMonth(month);
            statDTO.setTicketsCount((long) Doubles.asList(ds.getValues()).size());
            statDTO.setAvgTicketPrice(ds.getMean());
            statDTO.setMinTicketPrice(ds.getMin());
            statDTO.setPercentile10(ds.getPercentile(10));
            TicketStatisticsByMonth stat = ticketStatisticsByMonthDTOMapper.fromDTO(statDTO);
            statisticsList.add(stat);
        }

        return statisticsList;
    }
}
