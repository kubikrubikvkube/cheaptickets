package com.example.tickets.job;

import com.example.tickets.statistics.TicketStatisticsByMonth;
import com.example.tickets.statistics.TicketStatisticsService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.*;
import com.google.common.collect.Multimap;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CheapTicketFinderJob implements Job {
    private final Logger log = LoggerFactory.getLogger(CheapTicketFinderJob.class);
    private final TicketStatisticsService ticketStatisticsService;
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final CheapTicketMapper cheapTicketMapper;
    private final CheapTicketService cheapTicketService;

    public CheapTicketFinderJob(TicketStatisticsService ticketStatisticsService, TicketService ticketService, SubscriptionService subscriptionService, CheapTicketMapper cheapTicketMapper, CheapTicketService cheapTicketService) {
        this.ticketStatisticsService = ticketStatisticsService;
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.cheapTicketMapper = cheapTicketMapper;
        this.cheapTicketService = cheapTicketService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var startTime = Instant.now().toEpochMilli();
        log.info("CheapTicketFinderJob started");
        //TODO сохраняет дважды в репозиторий. Сделать чек на exists а не удаление всех записей
//        cheapTicketService.deleteAll();
        //TODO ну конечно блять одна джоба ищет инфу, вторая джоба удаляет и бегут они одновременно. Очень удобно прям ппц.
        Multimap<String, String> distinctOriginAndDestination = subscriptionService.findDistinctOriginAndDestination();
        List<Subscription> foundSubscriptions = new ArrayList<>();
        distinctOriginAndDestination.forEach((o, d) -> foundSubscriptions.addAll(subscriptionService.get(o, d)));
        List<Ticket> bestTicketsList = new ArrayList<>();
        //TODO почему ищем по подписке, а не по билетам ? У билетов ведь есть depart_date а у подписки может не быть
        for (Subscription s : foundSubscriptions) {
            var origin = s.getOrigin();
            var destination = s.getDestination();
            var departDate = s.getDepartDate();
            if (departDate != null) {
                Month departureMonth = departDate.getMonth();
                Optional<TicketStatisticsByMonth> statisticsOpt = ticketStatisticsService.findByOriginAndDestination(origin, destination, departureMonth);
                if (statisticsOpt.isPresent()) {
                    TicketStatisticsByMonth monthStat = statisticsOpt.get();
                    var bestTickets = ticketService
                            .findBy(origin, destination)
                            .stream()
                            .filter(t -> t.getValue() <= monthStat.getPercentile10())
                            .collect(Collectors.toList());
                    bestTicketsList.addAll(bestTickets);
                }
            } else {
                log.info("DepartDate for subscription {} is not set. So what?", s);
            }
        }

        List<CheapTicket> cheapTicketList = cheapTicketMapper.toCheapTicket(bestTicketsList);
        cheapTicketService.saveAll(cheapTicketList);
        var endTime = Instant.now().toEpochMilli();
        log.info(format("CheapTicketFinderJob finished in %d ms", endTime - startTime));
    }
}
