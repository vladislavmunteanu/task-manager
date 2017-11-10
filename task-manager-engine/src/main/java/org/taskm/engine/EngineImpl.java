package org.taskm.engine;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.taskm.core.task.SystemHistory;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskGroup;
import org.taskm.engine.job.TaskJob;
import org.taskm.engine.task.TaskRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created on 1/26/2017.
 */
@Component
public class EngineImpl implements Engine {

    private Scheduler scheduler;
    private EngineConf engineConf;
    private SystemHistory systemHistory;
    private Map<TaskGroup, List<Task>> tasksMap;
    private static final Logger Log = Logger.getLogger(EngineImpl.class);


    @Autowired
    public EngineImpl(EngineConf engineConf) throws EngineException {
        this.engineConf = engineConf;
        this.systemHistory = new SystemHistory();
    }

    @Override
    public void startEngine() throws EngineException {

        System.setProperty("org.quartz.jobStore.misfireThreshold","10000");

        List<TaskGroup> taskGroupList =  this.getEngineConf().getTaskGroupList();
        Scheduler scheduler;
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();

        } catch (SchedulerException e) {
            Log.error("Failed to run Scheduler, check task-scheduler.log for details",e);
            throw new RuntimeException("Failed to run Scheduler.",e);
        }


        for (TaskGroup taskGroup : taskGroupList) {

            if(!Objects.equals(taskGroup.getScheduler(), "manual")) {
                JobKey jobKey = new JobKey(taskGroup.getName());
                JobDetail job = JobBuilder.newJob(TaskJob.class).withIdentity(jobKey).build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("cronTrigger_" + taskGroup.getName(), taskGroup.getName())
                        .withSchedule(CronScheduleBuilder.cronSchedule(taskGroup.getScheduler()).withMisfireHandlingInstructionDoNothing())
                        .build();

                try {
                    scheduler.getContext().put(jobKey.getName(), taskGroup);
                    scheduler.getContext().put("systemHistory",this.systemHistory);
                    scheduler.scheduleJob(job, trigger);
                } catch (SchedulerException e) {
                    Log.error("Failed to run Scheduler, check task-scheduler.log for details", e);
                    throw new RuntimeException("Failed to run Scheduler.", e);
                }
            }
        }
        //Thread schedulerMonitor = new Thread(new SchedulerMonitor(scheduler,triggerKeyList));
       // schedulerMonitor.start();
    }

    @Override
    public void shutdownEngine() throws EngineException {

        try {
            this.getScheduler().shutdown();
        } catch (SchedulerException e) {
            throw new EngineException("Failed to shutdown Task Manager Engine.",e);
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public EngineConf getEngineConf() {
        return engineConf;
    }

    @Override
    public SystemHistory getSystemHistory() {
        return systemHistory;
    }

    public void setEngineConf(EngineConf engineConf){
        this.engineConf = engineConf;
    }

    @Override
    public void executeTaskGroup(String groupName){
        TaskRunner taskRunner = new TaskRunner();
        TaskGroup taskGroup = this.getEngineConf().getTaskGroupList().get(this.getEngineConf().getTaskGroupIndex(groupName));
        TaskJob.executeTaskGroup(taskGroup,taskRunner);
        TaskJob.taskGroupUpdates(taskGroup,taskRunner);
    }

}
