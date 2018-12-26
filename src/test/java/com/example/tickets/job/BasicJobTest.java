package com.example.tickets.job;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class BasicJobTest {

    @Test
    public void basicJobWorks() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(BasicJob.class)
                .storeDurably(true)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startNow()
                .build();

        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();

        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
        assertTrue(scheduler.isStarted());
        scheduler.shutdown(true);
        assertTrue(scheduler.isShutdown());


    }
}
