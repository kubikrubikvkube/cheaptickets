package com.example.tickets.job;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicJobTest {
    public static final AtomicBoolean isBasicJobExecuted = new AtomicBoolean(false);
    Logger log = LoggerFactory.getLogger(BasicJobTest.class);

    @Autowired
    private Scheduler scheduler;

    @Before
    public void setUp() {
        if (isBasicJobExecuted.get()) isBasicJobExecuted.set(false);
    }

    @After
    public void tearDown() {
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
