package org.taskm.core.task;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Vlad Munteanu on 8/26/2016.
 */

public class TaskGroup {

    private final String name;
    private final boolean parallel;
    private final String scheduler;
    private final List<Task> taskList;
    private int executions;
    private int failures;
    private TaskGroupStatus status;
    private String fireTime;

    /**
     * @param name        - task name
     * @param parallel    - if true then all tasks from this group should run in parallel
     * @param scheduler   - time when task group should run
     */
    public TaskGroup(String name, boolean parallel, String scheduler) {
        this.name = name;
        this.parallel = parallel;
        this.scheduler = scheduler;
        this.taskList = new ArrayList<>();
        this.executions = 0;
        this.failures = 0;
        this.status = TaskGroupStatus.PENDING;

    }

    public boolean isParallel() {
        return parallel;
    }

    public String getScheduler() {
        return scheduler;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public String getName() {
        return name;
    }

    public void addTask(Task task) {
        this.getTaskList().add(task);
    }

    public void resetTasksStatus() {

        for (Task task : this.getTaskList()) {
            task.setStatus(TaskStatus.PENDING);
        }

    }

    public String toString() {
        return String.format("Task Group - [Name : %s] [Parallel : %s] [Scheduler : %s]", this.getName(), this.isParallel(), this.getScheduler());
    }

    public String getFireTime() throws TaskCoreException {

        if (Objects.equals(this.getScheduler(), "manual")) {
            return "manual";
        } else if (this.getStatus() == TaskGroupStatus.RUNNING){
            return String.valueOf(TaskGroupStatus.RUNNING);
        }
        else{
            return new SimpleDateFormat("hh:mm:ss a").format(this.getNextFireTime());
        }
    }

    public int getDays() throws TaskCoreException {
        if (Objects.equals(this.getScheduler(), "manual")) {
            return 0;
        } else {
            Date d2 = this.getNextFireTime();
            Date d1 = new Date();
            return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        }
    }

    private Date getNextFireTime() throws TaskCoreException {
        try {
            CronExpression cronExpression = new CronExpression(this.getScheduler());
            return cronExpression.getNextValidTimeAfter(new Date());
        } catch (ParseException e) {
            throw new TaskCoreException("Failed to get 'Fire Time'.", e);
        }
    }

    public int getExecutions() {
        return executions;
    }

    public void incrementExecutions() {
        this.executions++;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public TaskGroupStatus getStatus() {
        return status;
    }

    public void setStatus(TaskGroupStatus status){
        this.status = status;
    }

    public Task getTask(String taskName){

        for (Task task : this.getTaskList()){
            if (Objects.equals(task.getMethodName(), taskName)){
                return task;
            }
        }
        return null;
    }

}
