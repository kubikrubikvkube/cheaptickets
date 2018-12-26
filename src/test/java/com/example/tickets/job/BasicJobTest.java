package com.example.tickets.job;

import lombok.extern.java.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class BasicJobTest {
    public static AtomicBoolean isBasicJobExecuted = new AtomicBoolean(false);
    @Autowired
    private Scheduler scheduler;

    @Before
    public void setUp() throws Exception {
        if (isBasicJobExecuted.get()) isBasicJobExecuted.set(false);
    }

    @After
    public void tearDown() throws Exception {
        if (isBasicJobExecuted.get()) isBasicJobExecuted.set(false);
    }

    @Test
    public void basicJobWorks() throws SchedulerException, InterruptedException {
        JobDetail jobDetail = JobBuilder
                .newJob(BasicJob.class)
                .withIdentity("basicJob")
                .withDescription("basic job for test purpose")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("basicTrigger", "test")
                .withDescription("basic trigger for test purpose")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(1)
                                .withRepeatCount(0))
                .build();

        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
        TimeUnit.SECONDS.sleep(1);
        assertTrue(scheduler.isStarted());
        assertTrue(isBasicJobExecuted.get());
        scheduler.shutdown(true);
        assertTrue(scheduler.isShutdown());


    }
}
