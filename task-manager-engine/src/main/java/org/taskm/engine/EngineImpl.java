package org.taskm.engine;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.taskm.core.task.TaskGroup;
import org.taskm.engine.job.TaskJob;
import org.taskm.engine.task.TaskRunner;
import org.taskm.engine.utils.SystemHistory;

import java.util.List;
import java.util.Objects;

/**
 * Created on 1/26/2017.
 */
@Component
public class EngineImpl implements Engine {

    private Scheduler scheduler;
    private EngineConf engineConf;
    private SystemHistory systemHistory;
    private static final Logger Log = Logger.getLogger(EngineImpl.class);


    @Autowired
    public EngineImpl(EngineConf engineConf,SystemHistory systemHistory) throws EngineException {
        this.engineConf = engineConf;
        this.systemHistory = systemHistory;
    }

    @Override
    public void startEngine() throws EngineException {

        System.setProperty("org.quartz.jobStore.misfireThreshold","10000");

        List<TaskGroup> taskGroupList =  this.getEngineConf().getTaskGroupList();

        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();

        } catch (SchedulerException e) {
            Log.error("Failed to run Scheduler, check task-scheduler.log for details",e);
            throw new EngineException("Failed to run Scheduler.",e);
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
                    scheduler.getContext().put("systemHistory",this.getSystemHistory());
                    scheduler.scheduleJob(job, trigger);
                } catch (SchedulerException e) {
                    Log.error("Failed to run Scheduler, check task-scheduler.log for details", e);
                    throw new EngineException("Failed to run Scheduler.", e);
                }
            }
        }
    }

    @Override
    public void shutdownEngine() throws EngineException {

        try {
            this.getScheduler().shutdown();
        } catch (SchedulerException e) {
            throw new EngineException("Failed to shutdown Task Manager Engine.",e);
        }
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public EngineConf getEngineConf() {
        return engineConf;
    }

    @Override
    public SystemHistory getSystemHistory() {
        return systemHistory;
    }

//
//    @Override
//    public void executeTaskGroup(String groupName){
//        TaskRunner taskRunner = new TaskRunner();
//        TaskGroup taskGroup = this.getEngineConf().getTaskGroupList().get(this.getEngineConf().getTaskGroupIndex(groupName));
//       // TaskJob.executeTaskGroup(taskGroup,taskRunner);
//      //  TaskJob.taskGroupUpdates(taskGroup,taskRunner);
//    }

}
