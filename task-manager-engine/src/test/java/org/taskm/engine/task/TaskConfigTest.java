package org.taskm.engine.task;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.taskm.core.task.Task;
import org.taskm.core.task.TaskCoreException;
import org.taskm.core.task.TaskGroup;
import org.taskm.engine.EngineConf;
import org.taskm.engine.EngineConfImpl;
import org.taskm.engine.EngineException;
import org.taskm.engine.job.TaskJob;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Vlad Munteanu on 8/26/2016.
 */
public class TaskConfigTest {

    private static TaskGroup eTaskGroup1,eTaskGroup2;
    private static EngineConf parser;
    private static TaskJob taskManager;

    @BeforeClass
    public static void setUp() throws EngineException, ParseException, TaskCoreException {

        parser = new EngineConfImpl();
        List<Object> parameters = new ArrayList<>();

        Task eTask1 = new Task(parser.getClassInstance(),"task_1");


        parameters.add("Got the message");
        Task eTask2 = new Task(parser.getClassInstance(),"task_2", parameters);

        parameters.clear();
        parameters.add(1234);
        Task eTask3 = new Task(parser.getClassInstance(),"task_3", parameters);

        eTaskGroup1 = new TaskGroup("Group_1",true,"0/5 * * * * ?");
        eTaskGroup1.addTask(eTask1);
        eTaskGroup1.addTask(eTask2);
        eTaskGroup1.addTask(eTask3);


        parameters.clear();
        parameters.add(3456.234);
        Task eTask4 = new Task(parser.getClassInstance(),"task_4", parameters);

        parameters.clear();
        parameters.add(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("12/12/2014 10:09:09 PM"));
        Task eTask5 = new Task(parser.getClassInstance(),"task_5", parameters);

        parameters.clear();
        parameters.add(true);
        Task eTask6 = new Task(parser.getClassInstance(),"task_6", parameters);

        eTaskGroup2 = new TaskGroup("Group_2",false,"0/7 * * * * ?");
        eTaskGroup2.addTask(eTask4);
        eTaskGroup2.addTask(eTask5);
        eTaskGroup2.addTask(eTask6);

    }

    @Test
    public void test_GetClass(){

        assert Objects.equals(parser.getTaskClass(), "Tasks.java") || Objects.equals(parser.getTaskClass(), "Script.groovy");

    }

    @Test
    public void test_GetTaskXML(){

        assert Objects.equals("TaskManager.xml", parser.getTaskXml());

    }

    @Test
    public void test_Group1(){

        TaskGroup taskGroup = parser.getTaskGroupList().get(0);
        assert Objects.equals(taskGroup.toString(), eTaskGroup1.toString());
        Assert.assertEquals(taskGroup.isParallel(),eTaskGroup1.isParallel());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getMethodName(),eTaskGroup1.getTaskList().get(0).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(1).getMethodName(),eTaskGroup1.getTaskList().get(1).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(2).getMethodName(),eTaskGroup1.getTaskList().get(2).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getParameters().size(),eTaskGroup1.getTaskList().get(0).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(1).getParameters().size(),eTaskGroup1.getTaskList().get(1).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(2).getParameters().size(),eTaskGroup1.getTaskList().get(2).getParameters().size());
    }

    @Test
    public void test_Group2(){

        TaskGroup taskGroup = parser.getTaskGroupList().get(1);
        Assert.assertEquals(taskGroup.toString(),eTaskGroup2.toString());
        Assert.assertEquals(taskGroup.isParallel(),eTaskGroup2.isParallel());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getMethodName(),eTaskGroup2.getTaskList().get(0).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(1).getMethodName(),eTaskGroup2.getTaskList().get(1).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(2).getMethodName(),eTaskGroup2.getTaskList().get(2).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getParameters().size(),eTaskGroup2.getTaskList().get(0).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(1).getParameters().size(),eTaskGroup2.getTaskList().get(1).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(2).getParameters().size(),eTaskGroup2.getTaskList().get(2).getParameters().size());
    }


    @Test
    public void test_RunGroup1(){

           // TaskJob.executeTaskGroup(parser.getTaskGroupList().get(0),new TaskRunner());

    }

    @Test
    public void test_RunGroup2(){

            //TaskJob.executeTaskGroup(parser.getTaskGroupList().get(1),new TaskRunner());

    }


}
