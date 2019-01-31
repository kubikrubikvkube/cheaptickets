package com.example.tickets.job;

import com.example.tickets.job.stage.*;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GlobalUpdaterJob implements Job {
    private final Logger log = LoggerFactory.getLogger(GlobalUpdaterJob.class);
    private final TicketInvalidationStage ticketInvalidationStage;
    private final LatestTicketsTravelPayoutsPopulationStage latestTicketsTravelPayoutsPopulationStage;
    private final OnewayTicketsForAYearAviasalesStage onewayTicketsForAYearAviasalesStage;
    private final CheapTicketFinderStage cheapTicketFinderStage;
    private final TicketStatisticsUpdaterStage ticketStatisticsUpdaterStage;
    private final SubscriptionTypeResolverStage subscriptionTypeResolverStage;
    private final RoutePlannerStage routePlannerStage;

    public GlobalUpdaterJob(TicketInvalidationStage ticketInvalidationStage, LatestTicketsTravelPayoutsPopulationStage latestTicketsTravelPayoutsPopulationStage, OnewayTicketsForAYearAviasalesStage onewayTicketsForAYearAviasalesStage, CheapTicketFinderStage cheapTicketFinderStage, TicketStatisticsUpdaterStage ticketStatisticsUpdaterStage, SubscriptionTypeResolverStage subscriptionTypeResolverStage, RoutePlannerStage routePlannerStage) {
        this.ticketInvalidationStage = ticketInvalidationStage;
        this.latestTicketsTravelPayoutsPopulationStage = latestTicketsTravelPayoutsPopulationStage;
        this.onewayTicketsForAYearAviasalesStage = onewayTicketsForAYearAviasalesStage;
        this.cheapTicketFinderStage = cheapTicketFinderStage;
        this.ticketStatisticsUpdaterStage = ticketStatisticsUpdaterStage;
        this.subscriptionTypeResolverStage = subscriptionTypeResolverStage;
        this.routePlannerStage = routePlannerStage;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("GlobalUpdaterJob started");

        log.info("Starting stage 1 - SubscriptionTypeResolverStage");
        /*
        Эта стадия используется для обновления SubscriptionType всех подписок, что были созданы и по каким-то причинам не имели
        указанного типа при сохранении в базу. Этот тип необходим для выбора корректного алгоритма планирования маршрута. В зависимости
        от соотношения указанных переменных в подписке мы ждём что будет выбран разный маршрут.
         */
        StageResult subscriptionTypeResolverStageResult = subscriptionTypeResolverStage.call();
        log.info("{}", subscriptionTypeResolverStageResult);

        log.info("Starting stage 2 - TicketInvalidation");
        /*
        Эта стадия используется для инвалидации тех билетов, чья дата вылета в прошедшем времени.
        Предполагается, что максимальная польза от этой стадии будет после полуночи, когда сутки сменились и билеты за "сегодняшнее"
        число становятся неактуальными,а значит подлежат удалению из базы.
         */
        StageResult ticketInvalidationStageResult = ticketInvalidationStage.call();
        log.info("{}", ticketInvalidationStageResult);

        /*
         * Эта стадия кладёт в базу все билеты, найденные пользователями TravelPayouts за последние 48 часов по каждой из наших подписок.
         * Эта стадия полезна при ежечасном обновлении, так как позволяет не пропустить билеты, добавленные в кэш реальными пользователями
         * за последний час. Это обеспечивает более быстрое пополнение нашей базы актуальными билетами, а значит более быстрое оповещение подписчиков.
         */
        log.info("Starting stage 3 - LatestTicketsTravelPayoutsPopulationStage");
        StageResult travelPayoutsLatestTicketsStageResult = latestTicketsTravelPayoutsPopulationStage.call();
        log.info("{}", travelPayoutsLatestTicketsStageResult);

        /*
         * Эта стадия необходима для получения билетов, находящихся в кэше сервиса AviaSales. Согласно подпискам, которые существуют у
         * нас в системе она формирует список направлений, которые будут обработаны. Потом мы спрашиваем у кэша AviaSales наличие билетов по
         * заданным направлениям на каждую из дат на ближайший год. Найденные билеты сохраняются к нам в базу. Это обеспечивает пополнение базы
         * кэшированными билетами на все доступные даты на ближайшее время, которые будут провалидированы позже.
         */
        log.info("Starting stage 4 - OnewayTicketsForAYearAviasalesStage");
        StageResult onewayTicketsForAYearAviasalesStageResult = onewayTicketsForAYearAviasalesStage.call();
        log.info("{}", onewayTicketsForAYearAviasalesStageResult);


        /*
         * Эта стадия необходима для получения пересчёта статистики по билетам, которые у нас уже имеются.
         */
        log.info("Starting stage 5 - TicketStatisticsUpdaterStage");
        StageResult ticketStatisticsUpdaterStageResult = ticketStatisticsUpdaterStage.call();
        log.info("{}", ticketStatisticsUpdaterStageResult);

        /*
         * Эта стадия необходима для поиска дешёвых билетов на основе заранее сформированной статистики. Мы фильтруем билеты,
         * которые считаем дешёвыми, и кладём их в отдельную таблицу. После этого мы будем работать уже непосредственно с этой
         * таблицей, фильтруя их и подбирая лучшие рейсы.
         */
        log.info("Starting stage 6 - CheapTicketFinderStage");
        StageResult cheapTicketFinderStageResult = cheapTicketFinderStage.call();
        log.info("{}", cheapTicketFinderStageResult);

        /*
         * Стадия необходима для планирования маршрутов, на основании требований, заданных в подписке
         */
        log.info("Starting stage 7 - RoutePlannerStage");
        StageResult routePlannerStageResult = routePlannerStage.call();
        log.info("{}", routePlannerStageResult);
    }
}
