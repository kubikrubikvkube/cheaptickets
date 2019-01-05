package com.example.tickets.job;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {
    @Autowired
    private Scheduler scheduler;


    @Bean(name = "ticketInvalidationJob")
    void ticketInvalidationJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(TicketInvalidationJob.class)
                .withIdentity("ticketInvalidationJob")
                .withDescription("Job for invalidation tickets which expiresAt and departureDate is in past")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("ticketInvalidationTrigger", "trigger")
                .withDescription("basic trigger for test purpose")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
//                                .withIntervalInMinutes(30)
                                .withIntervalInSeconds(5)
                                .repeatForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
