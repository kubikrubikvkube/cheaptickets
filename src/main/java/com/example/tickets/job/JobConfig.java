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
                .withIdentity("TicketInvalidationJob")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("TicketInvalidationTrigger", "trigger")
                .withSchedule(
                        SimpleScheduleBuilder.repeatHourlyForever())
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
                .withIdentity("OnewayTicketsForAYearAviasalesTrigger", "trigger")
                .withSchedule(
                        SimpleScheduleBuilder.repeatHourlyForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Bean(name = "PopulateUnknownExpirationStatusJob")
    void populateUnknownExpirationStatusJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(PopulateUnknownExpirationStatusJob.class)
                .withIdentity("PopulateUnknownExpirationStatusJob")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("PopulateUnknownExpirationStatusTrigger", "trigger")
                .withSchedule(
                        SimpleScheduleBuilder.repeatMinutelyForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Bean(name = "SubscriptionStatisticsUpdaterJob")
    void subscriptionStatisticsUpdaterJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(SubscriptionStatisticsUpdaterJob.class)
                .withIdentity("SubscriptionStatisticsUpdaterJob")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("SubscriptionStatisticsUpdaterTrigger", "trigger")
                .withSchedule(
                        SimpleScheduleBuilder.repeatMinutelyForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
