package com.example.tickets.job;

import com.example.tickets.job.stage.LatestTicketsTravelPayoutsPopulationStage;
import com.example.tickets.job.stage.OnewayTicketsForAYearAviasalesStage;
import com.example.tickets.job.stage.StageResult;
import com.example.tickets.job.stage.TicketInvalidationStage;
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

    public GlobalUpdaterJob(TicketInvalidationStage ticketInvalidationStage, LatestTicketsTravelPayoutsPopulationStage latestTicketsTravelPayoutsPopulationStage, OnewayTicketsForAYearAviasalesStage onewayTicketsForAYearAviasalesStage) {
        this.ticketInvalidationStage = ticketInvalidationStage;
        this.latestTicketsTravelPayoutsPopulationStage = latestTicketsTravelPayoutsPopulationStage;
        this.onewayTicketsForAYearAviasalesStage = onewayTicketsForAYearAviasalesStage;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("GlobalUpdaterJob started");

        log.info("Starting stage 1 - TicketInvalidation");
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
        log.info("Starting stage 2 - LatestTicketsTravelPayoutsPopulationStage");
        StageResult travelPayoutsLatestTicketsStageResult = latestTicketsTravelPayoutsPopulationStage.call();
        log.info("{}", travelPayoutsLatestTicketsStageResult);

        /*
         * Эта стадия необходима для получения билетов, находящихся в кэше сервиса AviaSales. Согласно подпискам, которые существуют у
         * нас в системе она формирует список направлений, которые будут обработаны. Потом мы спрашиваем у кэша AviaSales наличие билетов по
         * заданным направлениям на каждую из дат на ближайший год. Найденные билеты сохраняются к нам в базу. Это обеспечивает пополнение базы
         * кэшированными билетами на все доступные даты на ближайшее время, которые будут провалидированы позже.
         */
        log.info("Starting stage 3 - OnewayTicketsForAYearAviasalesStage");
        StageResult onewayTicketsForAYearAviasalesStageResult = onewayTicketsForAYearAviasalesStage.call();
        log.info("{}", onewayTicketsForAYearAviasalesStageResult);


    }
}
