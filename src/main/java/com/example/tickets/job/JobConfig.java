package com.example.tickets.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class JobConfig {
    private final Scheduler scheduler;

    public JobConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Bean(name = "globalUpdaterJob")
    void globalUpdaterJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(GlobalUpdaterJob.class)
                .withIdentity("GlobalUpdaterJob")
                .storeDurably(true)
                .requestRecovery(true)
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("GlobalUpdaterTrigger", "trigger")
                .withSchedule(
                        SimpleScheduleBuilder.repeatHourlyForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
