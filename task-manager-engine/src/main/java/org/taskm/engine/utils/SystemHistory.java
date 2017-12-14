package org.taskm.engine.utils;

import org.springframework.stereotype.Component;

@Component
public class SystemHistory {

    private String lastExecutedGroup;
    private String lastExecutedTask;
    private String lastExecutionTime;
    private String lastFailureTime;
    private int executions;
    private int failures;


    public SystemHistory(){
        this.executions = 0;
        this.failures = 0;
    }

    public String getLastExecutedGroup() {
        return lastExecutedGroup;
    }

    public void setLastExecutedGroup(String lastExecutedGroup) {
        this.lastExecutedGroup = lastExecutedGroup;
    }

    public String getLastExecutedTask() {
        return lastExecutedTask;
    }

    public void setLastExecutedTask(String lastExecutedTask) {
        this.lastExecutedTask = lastExecutedTask;
    }

    public String getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(String lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public String getLastFailureTime() {
        return lastFailureTime;
    }

    public void setLastFailureTime(String lastFailureTime) {
        this.lastFailureTime = lastFailureTime;
    }

    public int getExecutions() {
        return executions;
    }

    public void increaseExecutions(){
        this.executions++;
    }

    public int getFailures() {
        return failures;
    }

    public void increaseFailures(){
        this.failures++;
    }

    public String toString(){
        return String.format("Executions : %s Failures : %s",this.getExecutions(),this.getFailures());
    }
}
