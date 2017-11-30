package org.taskm.engine;

import org.quartz.Scheduler;

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
