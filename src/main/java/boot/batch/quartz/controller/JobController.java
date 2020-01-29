package boot.batch.quartz.controller;

import static org.quartz.JobBuilder.newJob;

import boot.batch.quartz.job.QuartzJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class JobController {

    private Scheduler scheduler;

    /** Every minute, every 0 seconds, the Job is executed */
    @PostConstruct
    public void start() {
        JobDetail jobDetail = buildJobDetail(QuartzJob.class, "QuartzJob", "batch", new HashMap());

        try {
            scheduler.scheduleJob(jobDetail, buildJobTrigger("0 * * * * ?"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                .build();
    }

    public JobDetail buildJobDetail(Class job, String name, String group, Map params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return newJob(job)
                .withIdentity(name, group)
                .usingJobData(jobDataMap)
                .build();
    }
}
