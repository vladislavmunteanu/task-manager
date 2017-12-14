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

    private static TaskGroup eTaskGroup1, eTaskGroup2;
    private static EngineConf parser;
    private static TaskRunner taskRunner;

    @BeforeClass
    public static void setUp() throws EngineException, ParseException, TaskCoreException {

        taskRunner = new TaskRunner();
        parser = new EngineConfImpl();
        List<Object> parameters = new ArrayList<>();

        Task eTask1 = new Task(parser.getClassInstance(), "task_1");


        parameters.add("Got the NOTIFICATION_ERROR_MESSAGE");
        Task eTask2 = new Task(parser.getClassInstance(), "task_2", parameters);

        parameters.clear();
        parameters.add(1234);
        Task eTask3 = new Task(parser.getClassInstance(), "task_3", parameters);

        eTaskGroup1 = new TaskGroup("Group_1", true, "0/5 * * * * ?", "0001");
        eTaskGroup1.addTask(eTask1);
        eTaskGroup1.addTask(eTask2);
        eTaskGroup1.addTask(eTask3);


        parameters.clear();
        parameters.add(3456.234);
        Task eTask4 = new Task(parser.getClassInstance(), "task_4", parameters);

        parameters.clear();
        parameters.add(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("12/12/2014 10:09:09 PM"));
        Task eTask5 = new Task(parser.getClassInstance(), "task_5", parameters);

        parameters.clear();
        parameters.add(true);
        Task eTask6 = new Task(parser.getClassInstance(), "task_6", parameters);

        eTaskGroup2 = new TaskGroup("Group_2", false, "0/7 * * * * ?", "0005");
        eTaskGroup2.addTask(eTask4);
        eTaskGroup2.addTask(eTask5);
        eTaskGroup2.addTask(eTask6);

    }

    @Test
    public void test_GetClass() {

        assert Objects.equals(parser.getTaskClass(), "Tasks.java") || Objects.equals(parser.getTaskClass(), "Script.groovy");

    }

    @Test
    public void test_GetTaskXML() {

        assert Objects.equals("TaskManager.xml", parser.getTaskXml());

    }

    @Test
    public void test_Group1() {

        TaskGroup taskGroup = parser.getTaskGroupList().get(0);
        assert Objects.equals(taskGroup.toString(), eTaskGroup1.toString());
        Assert.assertEquals(taskGroup.isParallel(), eTaskGroup1.isParallel());
        Assert.assertEquals(taskGroup.getGroupId(), eTaskGroup1.getGroupId());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getMethodName(), eTaskGroup1.getTaskList().get(0).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(1).getMethodName(), eTaskGroup1.getTaskList().get(1).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(2).getMethodName(), eTaskGroup1.getTaskList().get(2).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getParameters().size(), eTaskGroup1.getTaskList().get(0).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(1).getParameters().size(), eTaskGroup1.getTaskList().get(1).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(2).getParameters().size(), eTaskGroup1.getTaskList().get(2).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getTaskId(), "0002");
        Assert.assertEquals(taskGroup.getTaskList().get(1).getTaskId(), "0003");
        Assert.assertEquals(taskGroup.getTaskList().get(2).getTaskId(), "0004");

    }

    @Test
    public void test_Group2() {

        TaskGroup taskGroup = parser.getTaskGroupList().get(1);
        Assert.assertEquals(taskGroup.toString(), eTaskGroup2.toString());
        Assert.assertEquals(taskGroup.isParallel(), eTaskGroup2.isParallel());
        Assert.assertEquals(taskGroup.getGroupId(), eTaskGroup2.getGroupId());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getMethodName(), eTaskGroup2.getTaskList().get(0).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(1).getMethodName(), eTaskGroup2.getTaskList().get(1).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(2).getMethodName(), eTaskGroup2.getTaskList().get(2).getMethodName());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getParameters().size(), eTaskGroup2.getTaskList().get(0).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(1).getParameters().size(), eTaskGroup2.getTaskList().get(1).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(2).getParameters().size(), eTaskGroup2.getTaskList().get(2).getParameters().size());
        Assert.assertEquals(taskGroup.getTaskList().get(0).getTaskId(), "0006");
        Assert.assertEquals(taskGroup.getTaskList().get(1).getTaskId(), "0007");
        Assert.assertEquals(taskGroup.getTaskList().get(2).getTaskId(), "0008");
    }

    @Test
    public void test_RunGroup1() {

        try {
            taskRunner.runParallelTasks(parser.getTaskGroupList().get(0).getTaskList());
        } catch (TaskRunnerException e) {
            Assert.fail(String.format("No error expected : '%s",e.getMessage()));
        }
    }

    @Test
    public void test_RunGroup2() {

        try {
            for (Task task : parser.getTaskGroupList().get(1).getTaskList()){
                taskRunner.runTask(task);
            }
        } catch (TaskRunnerException e) {
            Assert.fail(String.format("No error expected : '%s",e.getMessage()));
        }

    }


}
