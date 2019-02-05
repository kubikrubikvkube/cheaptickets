package com.example.tickets.web.controller;

import com.example.tickets.job.stage.Stage;
import com.example.tickets.web.commandobjects.StageCommandObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class StagesController {
    private static final String STAGES_PAGE = "stages";
    private final Scheduler scheduler;
    private final Reflections stageReflections;
    private final String stagePackage = "com.example.tickets.job.stage";
    private final ApplicationContext appContext;


    public StagesController(Scheduler scheduler, ApplicationContext appContext) {
        this.scheduler = scheduler;
        this.appContext = appContext;
        this.stageReflections = new Reflections(stagePackage);
    }


    @GetMapping("/admin/stages")
    public String adminPage(Model model) {
        model.addAttribute(new StageCommandObject());
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

    @ModelAttribute("allStages")
    public List<String> allStages() {
        Set<Class<? extends Stage>> subTypes = stageReflections.getSubTypesOf(Stage.class);
        return subTypes
                .stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toList());

    }

    @PostMapping(value = "/admin/stages/start", params = "stageName")
    public String startStage(@ModelAttribute StageCommandObject stageCO) throws Exception {
        String stageName = stageCO.getStageName();
        Class<?> stageClass = Class.forName(stagePackage + "." + stageName);
        Stage bean = (Stage) appContext.getAutowireCapableBeanFactory().getBean(stageClass);
        bean.call();
        return STAGES_PAGE;
    }

}
