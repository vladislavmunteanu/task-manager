package org.taskm.engine;

/**
 * Created by c-vladmunt on 9/29/2015.
 */
public class EngineException extends Exception {

    public EngineException(String message){
        super(message);
    }

    public EngineException(String message, Throwable cause){
        super(message,cause);
    }

}
