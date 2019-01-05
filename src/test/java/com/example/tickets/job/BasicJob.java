package com.example.tickets.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class BasicJob implements Job {
    private final Logger log = LoggerFactory.getLogger(BasicJob.class);
    @Override
    public void execute(JobExecutionContext context) {
        log.info("Setting isBasicJobExecuted to TRUE");
        BasicJobTest.isBasicJobExecuted.set(true);
        log.info("isBasicJobExecuted is set to " + BasicJobTest.isBasicJobExecuted.get());
    }
}
