package com.example.tickets.web;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class StagesController {
    private static final String STAGES_PAGE = "stages";
    private final Scheduler scheduler;


    public StagesController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    @GetMapping("/admin/stages")
    public String adminPage(Model model) {
        return STAGES_PAGE;
    }


    @ModelAttribute("isGlobalUpdaterJobRunning")
    public boolean isGlobalUpdaterJobRunning() throws SchedulerException {
        List<JobExecutionContext> contextList = scheduler.getCurrentlyExecutingJobs();
        JobKey globalUpdaterJobKey = JobKey.jobKey("GlobalUpdaterJob");
        boolean isJobRunning = false;
        for (JobExecutionContext jec : contextList) {
            if (jec.getJobDetail().getKey().equals(globalUpdaterJobKey)) {
                isJobRunning = true;
            }

        }
        return isJobRunning;
    }

}
