package org.taskm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created on 1/27/2017.
 */
@SpringBootApplication
@ImportResource("classpath:task-manager-context.xml")
public class Application {

    public static void main(String[] args){

        SpringApplication.run(Application.class, args);
    }

}
