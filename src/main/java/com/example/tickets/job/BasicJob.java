package com.example.tickets.job;

import lombok.extern.java.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Log
public class BasicJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info(String.format("Hello world, it's %s o'clock!", context.get("instant")));
    }
}
