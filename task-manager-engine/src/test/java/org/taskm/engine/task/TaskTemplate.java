package org.taskm.engine.task;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.taskm.core.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Munteanu on 8/26/2016.
 */

class TaskTemplate {

    private static final Logger LOG = Logger.getLogger(TaskTemplate.class);

    public void testNoParameter() throws InterruptedException {
        Thread.sleep(5000);
        LOG.info("Test method without parameter");}

    public void testOneParameter(String message) throws InterruptedException {
        Thread.sleep(6000);
        LOG.info("Test method with parameter " + message);}

    public void testTwoParameters(String message_0,String message_1){

        LOG.info("message_0 = " + message_0);
        LOG.info("message_1 = " + message_1);
    }

    public void testInt(int message) throws InterruptedException {
        Thread.sleep(3000);
        LOG.info(String.valueOf(message));
    }

    public void testByte(byte message) throws InterruptedException {
        Thread.sleep(2000);
        LOG.info(String.valueOf(message));
    }

    public void testShort(Short message) throws InterruptedException {
        Thread.sleep(1000);
        LOG.info(String.valueOf(message));
    }

    public String testReturnString(String message){
        LOG.info(message);
        return message;
    }

    public void theSame(String message){
        LOG.info(message);
    }

    public void theSame(int message){
        LOG.info(String.valueOf(message));
    }

    public void doError() throws Exception {

        throw new Exception("ihu");

    }


}
