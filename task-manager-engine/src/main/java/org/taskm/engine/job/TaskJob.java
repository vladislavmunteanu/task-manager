package org.taskm.engine.job;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.taskm.core.task.SystemHistory;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskGroup;
import org.taskm.core.task.TaskGroupStatus;
import org.taskm.engine.EngineException;
import org.taskm.engine.task.TaskRunner;
import org.taskm.engine.task.TaskRunnerException;

import java.util.HashMap;

/**
 * Created on 1/26/2017.
 */
@DisallowConcurrentExecution
public class TaskJob implements Job {

    private TaskGroup taskGroup;
    private SystemHistory systemHistory;
    private final TaskRunner taskRunner;
    private static final Logger Log = Logger.getLogger(TaskJob.class);

    public TaskJob() {
        this.taskRunner = new TaskRunner();
    }

    public TaskJob(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
        this.taskRunner = new TaskRunner();
    }

    private TaskRunner getTaskRunner() {
        return taskRunner;
    }

    private TaskGroup getTaskGroup() {
        return taskGroup;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        SchedulerContext schedulerContext;
        try {
            schedulerContext = jobExecutionContext.getScheduler().getContext();
            this.taskGroup = (TaskGroup) schedulerContext.get(jobExecutionContext.getJobDetail().getKey().getName());
        } catch (SchedulerException e) {
            Log.error("Failed to run Scheduler, check task-scheduler.log for details", e);
            throw new RuntimeException("Failed to run Scheduler", e);
        }

        executeTaskGroup(this.getTaskGroup(),this.getTaskRunner());

        taskGroupUpdates(this.getTaskGroup(), this.getTaskRunner());
        Log.info(String.format("%s - [Executed : %s] [Failed : %s]", this.getTaskGroup().getName(), this.getTaskRunner().getExecutedTasks(), this.getTaskRunner().getFailedTasks()));
        Log.info(String.format("%s [Next Fire Time : %s]", this.getTaskGroup(), jobExecutionContext.getNextFireTime()));

    }

    public static void taskGroupUpdates(TaskGroup taskGroup, TaskRunner taskRunner) {
        taskGroup.incrementExecutions();
        taskGroup.setFailures(taskGroup.getFailures() + taskRunner.getFailedTasks());
        taskGroup.setStatus(TaskGroupStatus.Pending);
    }

    public static void executeTaskGroup(TaskGroup taskGroup, TaskRunner taskRunner) {

        taskGroup.setStatus(TaskGroupStatus.Running);

        if (taskGroup.isParallel() && taskGroup.getTaskList().size() > 1) {
            try {
                runTaskGroupParallel(taskGroup,taskRunner);
            } catch (EngineException taskManagerExceptions) {
                taskGroupUpdates(taskGroup, taskRunner);
                Log.warn(String.format("%s [Executed : %s] [Failed : %s]", taskGroup, taskRunner.getExecutedTasks(), taskRunner.getFailedTasks()));
                Log.error(String.format("Failed to run %s", taskGroup), taskManagerExceptions);
                throw new RuntimeException(String.format("Failed to run %s", taskGroup), taskManagerExceptions);
            }
        } else {
            try {
                runTaskGroup(taskGroup,taskRunner);
            } catch (EngineException taskManagerExceptions) {
                taskGroupUpdates(taskGroup, taskRunner);
                Log.warn(String.format("%s [Executed : %s] [Failed : %s]", taskGroup, taskRunner.getExecutedTasks(), taskRunner.getFailedTasks()));
                Log.error(String.format("Failed to run %s", taskGroup), taskManagerExceptions);
                throw new RuntimeException(String.format("Failed to run %s", taskGroup), taskManagerExceptions);

            }
        }
    }

    private static void runTaskGroup(TaskGroup taskGroup, TaskRunner taskRunner) throws EngineException {


        taskGroup.resetTasksStatus();
        Log.info(String.format("Running Task Group %s, Tasks : %s", taskGroup.getName(), taskGroup.getTaskList()));
        taskRunner.setFailedTasks(0);
        taskRunner.setExecutedTasks(0);

        for (Task task : taskGroup.getTaskList()) {

            try {
                taskRunner.runTask(task);
            } catch (TaskRunnerException e) {
                throw new EngineException(String.format("Failed to start task '%s'.", task.getMethodName()), e);
            }

        }

        Log.info(String.format("Finished Tasks : %s", taskGroup.getTaskList()));
    }

    private static void runTaskGroupParallel(TaskGroup taskGroup,TaskRunner taskRunner) throws EngineException {

        taskGroup.resetTasksStatus();
        Log.info(String.format("Running Task Group : %s, Tasks : %s", taskGroup.getName(), taskGroup.getTaskList()));
        taskRunner.setFailedTasks(0);
        taskRunner.setExecutedTasks(0);
        try {
            taskRunner.runParallelTasks(taskGroup.getTaskList());
        } catch (TaskRunnerException e) {
            throw new EngineException(String.format("Failed to start task group '%s'.", taskGroup.getName()), e);
        }
        Log.info(String.format("Finished Tasks : %s", taskGroup.getTaskList()));
    }


}

