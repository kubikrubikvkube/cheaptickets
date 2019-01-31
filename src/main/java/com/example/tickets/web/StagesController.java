package com.example.tickets.web;

import org.quartz.*;
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


    @ModelAttribute("isGlobalJobRunning")
    public boolean isGlobalJobRunning() throws SchedulerException {
        List<JobExecutionContext> globalUpdaterJob = scheduler.getCurrentlyExecutingJobs();
        JobDetail globalUpdaterJob1 = scheduler.getJobDetail(JobKey.jobKey("GlobalUpdaterJob"));
        //globalUpdaterJob.get(0).getJobDetail().equals(globalUpdaterJob1)
        return false;
    }

}
