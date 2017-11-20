package org.taskm.engine.utils;


import org.springframework.stereotype.Component;
import org.taskm.core.task.TaskGroup;
import org.taskm.core.task.TaskMap;

import java.util.Map;

@Component
public class History {

    private Map<String,TaskGroup> lastExecutedTaskGroup;
    private Map<String,TaskMap> lastFailedTask;
    private Integer groupExecutions;

    public History(){}


    public Map<String, TaskGroup> getLastExecutedTaskGroup() {
        return lastExecutedTaskGroup;
    }

    public void updateLastExecutedTaskGroup(Map<String, TaskGroup> lastExecutedTaskGroup) {
        this.lastExecutedTaskGroup = lastExecutedTaskGroup;
    }

    public Map<String, TaskMap> getLastFailedTask() {
        return lastFailedTask;
    }

    public void updateLastFailedTask(Map<String, TaskMap> lastFailedTask) {
        this.lastFailedTask = lastFailedTask;
    }


    public Integer getGroupExecutions() {
        return groupExecutions;
    }

    public void updateExecutions(Integer executions) {
        this.groupExecutions = executions;
    }
}