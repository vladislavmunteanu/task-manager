package org.taskm.app;

import org.quartz.SchedulerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.taskm.app.notification.Notification;
import org.taskm.engine.Engine;
import org.taskm.engine.EngineConfImpl;
import org.taskm.engine.EngineException;
import org.taskm.engine.utils.NotificationClient;

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
            throw new RuntimeException("Failed to load property 'task.manager.port'",e);
        }

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        try {
            NotificationClient notificationClient = new NotificationClient();
            context.getBean(Engine.class).getScheduler().getContext().put("notificationClient",notificationClient);
        } catch (EngineException | SchedulerException e) {
            e.printStackTrace();
        }
    }

}
