package org.taskm.engine;

import org.quartz.Scheduler;
import org.taskm.core.task.TaskGroup;

import java.util.List;

/**
 * Created on 1/26/2017.
 */

public interface Engine {


    void startEngine() throws EngineException;

    void shutdownEngine() throws EngineException;

    Scheduler getScheduler();

    List<TaskGroup> getTaskGroups();

    EngineConf getEngineConf();

    void executeTaskGroup(String groupName);

}
