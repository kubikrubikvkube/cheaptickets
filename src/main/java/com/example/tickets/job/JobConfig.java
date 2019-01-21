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
                .withDescription("Invalidates ticket")
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

    @Bean(name = "cheapTicketFinderJob")
    void cheapTicketFinderJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(CheapTicketFinderJob.class)
                .withIdentity("CheapTicketFinderJob")
                .withDescription("Selecting cheap tickets from database")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("CheapTicketFinderTrigger", "trigger")
                .withSchedule(
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
                .withDescription("Population job for tickets within last 48 hours using TravelPayouts service")
                .withSchedule(
                        SimpleScheduleBuilder.repeatHourlyForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Bean(name = "routePlannerJob")
    void routePlannerJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(RoutePlannerJob.class)
                .withIdentity("RoutePlannerJob")
                .withDescription("Gathering up routes for tickets from cheap tickets")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("RoutePlannerTrigger", "trigger")
                .withSchedule(
                        SimpleScheduleBuilder.repeatMinutelyForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Bean(name = "OnewayTicketsForAYearAviasalesJob")
    void oneWayTicketsForAYearAviasalesJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(OnewayTicketsForAYearAviasalesJob.class)
                .withIdentity("OnewayTicketsForAYearAviasalesJob")
                .withDescription("Find all tickets within a year using Aviasales service")
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
                .withDescription("Marking tickets as expired if it's departure date is passed")
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

    @Bean(name = "TicketStatisticsUpdaterJob")
    void subscriptionStatisticsUpdaterJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(TicketStatisticsUpdaterJob.class)
                .withIdentity("TicketStatisticsUpdaterJob")
                .withDescription("Updating ticket statistics")
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
