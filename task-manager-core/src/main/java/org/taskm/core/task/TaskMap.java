package org.taskm.core.task;

public class TaskMap {

    private Task task;
    private TaskGroup taskGroup;

    public TaskMap(Task task, TaskGroup taskGroup) {
        this.task = task;
        this.taskGroup = taskGroup;
    }

    public Task getTask() {
        return task;
    }

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }
}
