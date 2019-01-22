package com.example.tickets.job;

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

    public GlobalUpdaterJob(TicketInvalidationStage ticketInvalidationStage) {
        this.ticketInvalidationStage = ticketInvalidationStage;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("GlobalUpdaterJob started");
        log.info("Starting stage 1 - TicketInvalidationStage");
        /*
        Эта стадия используется для инвалидации тех билетов, чья дата вылета в прошедшем времени.
        Предполагается, что максимальная польза от этой стадии будет после полуночи, когда сутки сменились и билеты за "сегодняшнее"
        число становятся неактуальными,а значит подлежат удалению из базы.
         */
        StageResult ticketInvalidationStageResult = ticketInvalidationStage.call();
        log.info("{}", ticketInvalidationStageResult);

        log.info("Starting stage 2 - LatestTicketsTravelPayoutsPopulation");
    }
}
