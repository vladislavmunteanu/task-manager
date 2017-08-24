package org.taskm.core.task;

/**
 * Created on 1/27/2017.
 */
public class TaskCoreException extends Exception {

    public TaskCoreException(String message){
        super(message);
    }

    public TaskCoreException(String message, Throwable cause){
        super(message,cause);
    }

}
