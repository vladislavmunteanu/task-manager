package org.taskm.engine.task;

/**
 * Created on 1/27/2017.
 */
public class TaskRunnerException extends Exception {

    public TaskRunnerException(String message){
        super(message);
    }

    public TaskRunnerException(String message, Throwable cause){
        super(message,cause);
    }

}
