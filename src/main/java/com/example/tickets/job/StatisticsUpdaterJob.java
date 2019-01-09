package com.example.tickets.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatisticsUpdaterJob implements Job {
    private final Logger log = LoggerFactory.getLogger(StatisticsUpdaterJob.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
