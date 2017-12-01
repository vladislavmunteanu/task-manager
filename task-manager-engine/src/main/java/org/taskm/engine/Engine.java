package org.taskm.engine;

import org.quartz.Scheduler;
import org.taskm.engine.utils.SystemHistory;

/**
 * Created on 1/26/2017.
 */

public interface Engine {


    void startEngine() throws EngineException;

    void shutdownEngine() throws EngineException;

    Scheduler getScheduler();

    EngineConf getEngineConf();

    SystemHistory getSystemHistory();

    void executeTaskGroup(String groupName);

}
