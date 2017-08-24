package org.taskm.engine;

import org.taskm.core.task.TaskGroup;

import java.util.List;

/**
 * Created on 1/26/2017.
 */

public interface EngineConf {

    List<TaskGroup> getTaskGroupList();

    Object getClassInstance();

    String getTaskXml();

    String getTaskClass();

    String getType();

    Integer getTaskGroupIndex(String tgName);



}
