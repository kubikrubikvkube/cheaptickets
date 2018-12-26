package com.example.tickets.job;

import lombok.extern.java.Log;
import org.quartz.*;

@Log
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class BasicJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Setting isBasicJobExecuted to TRUE");
        BasicJobTest.isBasicJobExecuted.set(true);
        log.info("isBasicJobExecuted is set to " + BasicJobTest.isBasicJobExecuted.get());
    }
}
