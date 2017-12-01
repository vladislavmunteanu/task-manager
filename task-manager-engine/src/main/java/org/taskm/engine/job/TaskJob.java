package org.taskm.engine.job;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskGroup;
import org.taskm.core.task.TaskGroupStatus;
import org.taskm.engine.EngineException;
import org.taskm.engine.task.TaskRunner;
import org.taskm.engine.task.TaskRunnerException;
import org.taskm.engine.utils.NotificationClient;
import org.taskm.engine.utils.SystemHistory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 1/26/2017.
 */
@DisallowConcurrentExecution
public class TaskJob implements Job {

    private final TaskRunner taskRunner;
    private static final Logger Log = Logger.getLogger(TaskJob.class);

    public TaskJob() {
        this.taskRunner = new TaskRunner();
    }

    public TaskRunner getTaskRunner() {
        return taskRunner;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        SchedulerContext schedulerContext;
        SystemHistory systemHistory;
        NotificationClient notificationClient;
        TaskGroup taskGroup;
        try {
            schedulerContext = jobExecutionContext.getScheduler().getContext();
            taskGroup = (TaskGroup) schedulerContext.get(jobExecutionContext.getJobDetail().getKey().getName());
            notificationClient = (NotificationClient) schedulerContext.get("notificationClient");
            systemHistory = (SystemHistory) schedulerContext.get("systemHistory");

        } catch (SchedulerException e) {
            Log.error("Failed to run Scheduler, check task-scheduler.log for details", e);
            throw new RuntimeException("Failed to run Scheduler", e);
        }

        notificationClient.sendQuickNotification("notice",taskGroup.getName()+" started");
        executeTaskGroup(taskGroup,this.getTaskRunner(),notificationClient, systemHistory);
        systemHistory.increaseExecutions();
        systemHistory.setLastExecutedGroup(taskGroup.getName());

        taskGroupUpdates(taskGroup, this.getTaskRunner(),notificationClient, systemHistory);
        Log.info(String.format("%s - [Executed : %s] [Failed : %s]", taskGroup.getName(), this.getTaskRunner().getExecutedTasks(), this.getTaskRunner().getFailedTasks()));
        Log.info(String.format("%s [Next Fire Time : %s]", taskGroup, jobExecutionContext.getNextFireTime()));

    }

    public static void taskGroupUpdates(TaskGroup taskGroup, TaskRunner taskRunner,NotificationClient notificationClient,SystemHistory systemHistory) {
        taskGroup.incrementExecutions();
        taskGroup.setFailures(taskGroup.getFailures() + taskRunner.getFailedTasks());
        notificationClient.sendQuickNotification("success",taskGroup.getName()+" executed");
        systemHistory.setLastExecutionTime(new SimpleDateFormat("hh:mm:ss a").format(new Date(System.currentTimeMillis())));
        taskGroup.setStatus(TaskGroupStatus.Pending);
    }

    public static void executeTaskGroup(TaskGroup taskGroup, TaskRunner taskRunner,NotificationClient notificationClient,SystemHistory systemHistory) {

        taskGroup.setStatus(TaskGroupStatus.Running);

        if (taskGroup.isParallel() && taskGroup.getTaskList().size() > 1) {
            try {
                runTaskGroupParallel(taskGroup,taskRunner,systemHistory,notificationClient);
            } catch (EngineException taskManagerException) {
                provideException(notificationClient,taskGroup,taskRunner,taskManagerException,systemHistory);
            }
        } else {
            try {
                runTaskGroup(taskGroup,taskRunner,systemHistory,notificationClient);
            } catch (EngineException taskManagerException) {
                provideException(notificationClient,taskGroup,taskRunner,taskManagerException,systemHistory);
            }
        }
    }

    private static void provideException(NotificationClient notificationClient,TaskGroup taskGroup, TaskRunner taskRunner,EngineException taskManagerException,SystemHistory systemHistory){
        taskGroupUpdates(taskGroup, taskRunner,notificationClient,systemHistory);
        Log.warn(String.format("%s [Executed : %s] [Failed : %s]", taskGroup, taskRunner.getExecutedTasks(), taskRunner.getFailedTasks()));
        Log.error(String.format("Failed to run %s", taskGroup), taskManagerException);
        throw new RuntimeException(String.format("Failed to run %s", taskGroup), taskManagerException);


    }

    private static void runTaskGroup(TaskGroup taskGroup, TaskRunner taskRunner,SystemHistory systemHistory,NotificationClient notificationClient) throws EngineException {


        taskGroup.resetTasksStatus();
        Log.info(String.format("Running Task Group %s, Tasks : %s", taskGroup.getName(), taskGroup.getTaskList()));
        taskRunner.setFailedTasks(0);
        taskRunner.setExecutedTasks(0);

        for (Task task : taskGroup.getTaskList()) {

            try {
                taskRunner.runTask(task,systemHistory,notificationClient);
            } catch (TaskRunnerException e) {
                throw new EngineException(String.format("Failed to start task '%s'.", task.getMethodName()), e);
            }

        }

        Log.info(String.format("Finished Tasks : %s", taskGroup.getTaskList()));
    }

    private static void runTaskGroupParallel(TaskGroup taskGroup,TaskRunner taskRunner,SystemHistory systemHistory,NotificationClient notificationClient) throws EngineException {

        taskGroup.resetTasksStatus();
        Log.info(String.format("Running Task Group : %s, Tasks : %s", taskGroup.getName(), taskGroup.getTaskList()));
        taskRunner.setFailedTasks(0);
        taskRunner.setExecutedTasks(0);
        try {
            taskRunner.runParallelTasks(taskGroup.getTaskList(),systemHistory,notificationClient);
        } catch (TaskRunnerException e) {
            throw new EngineException(String.format("Failed to start task group '%s'.", taskGroup.getName()), e);
        }
        Log.info(String.format("Finished Tasks : %s", taskGroup.getTaskList()));
    }

}

