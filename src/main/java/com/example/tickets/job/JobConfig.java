package com.example.tickets.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {
    private final Scheduler scheduler;

    public JobConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

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
//                        SimpleScheduleBuilder.repeatHourlyForever())
                        SimpleScheduleBuilder.repeatMinutelyForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Bean(name = "LatestTicketsTravelPayoutsPopulationJob")
    void latestTicketsTravelPayoutsPopulationJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(LatestTicketsTravelPayoutsPopulationJob.class)
                .withIdentity("LatestTicketsTravelPayoutsPopulationJob")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("LatestTicketsTravelPayoutsPopulationTrigger", "trigger")
                .withSchedule(
                        SimpleScheduleBuilder.repeatHourlyForever())
//                        SimpleScheduleBuilder.repeatMinutelyForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Bean(name = "OnewayTicketsForAYearAviasalesJob")
    void oneWayTicketsForAYearAviasalesJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(OnewayTicketsForAYearAviasalesJob.class)
                .withIdentity("OnewayTicketsForAYearAviasalesJob")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("newayTicketsForAYearAviasalesTrigger", "trigger")
                .withSchedule(
                        SimpleScheduleBuilder.repeatHourlyForever())
//                        SimpleScheduleBuilder.repeatMinutelyForever(15))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
