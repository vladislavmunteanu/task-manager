package org.taskm.engine;

import org.quartz.Scheduler;
import org.taskm.engine.utils.History;
import org.taskm.engine.utils.NotificationClient;

/**
 * Created on 1/26/2017.
 */

public interface Engine {


    void startEngine() throws EngineException;

    void shutdownEngine() throws EngineException;

    Scheduler getScheduler();

    EngineConf getEngineConf();

    History getSystemHistory();

    void executeTaskGroup(String groupName);

}
