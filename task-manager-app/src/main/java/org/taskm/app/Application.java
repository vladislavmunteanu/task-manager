package org.taskm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.taskm.engine.EngineConfImpl;
import org.taskm.engine.EngineException;

/**
 * Created on 1/27/2017.
 */
@SpringBootApplication
@ImportResource("classpath:task-manager-context.xml")
public class Application {

    public static void main(String[] args){

        try {
            System.setProperty("port", EngineConfImpl.getProperty("task.manager.port"));
        } catch (EngineException e) {
            throw new RuntimeException("Failed to load server port",e);
        }

        SpringApplication.run(Application.class, args);
    }

}
