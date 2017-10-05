package org.taskm.engine;

import org.quartz.Scheduler;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskGroup;

import java.util.List;
import java.util.Map;

/**
 * Created on 1/26/2017.
 */

public interface Engine {


    void startEngine() throws EngineException;

    void shutdownEngine() throws EngineException;

    Scheduler getScheduler();

    EngineConf getEngineConf();

    void executeTaskGroup(String groupName);

}
